package ninja.taskbook.model.network.thrift.manager;

import android.content.Context;
import android.util.ArrayMap;

import org.apache.thrift.TServiceClient;
import org.apache.thrift.TServiceClientFactory;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.async.TAsyncMethodCall;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TNonblockingSocket;
import org.apache.thrift.transport.TNonblockingTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import ninja.taskbook.model.network.thrift.online.hello.HelloWorldService;

//----------------------------------------------------------------------------------------------------
public class ThriftManager {

    //----------------------------------------------------------------------------------------------------
    public enum ClientTypeEnum {
        CLIENT_HELLO("/hello");

        private String name;

        ClientTypeEnum(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    //----------------------------------------------------------------------------------------------------
    public static final String THRIFT_HOST = "http://127.0.0.1";
    private Map<String, ThriftClientInfo> mClientMap = new ArrayMap<>();

    //----------------------------------------------------------------------------------------------------
    public void initialize() {
        registerClient(ClientTypeEnum.CLIENT_HELLO.toString(), THRIFT_HOST, 8090, new HelloWorldService.Client.Factory());
    }

    //----------------------------------------------------------------------------------------------------
    public void registerClient(String name, String host, int port, TServiceClientFactory<?> factory) {
        mClientMap.put(name, new ThriftClientInfo(name, host, port, factory));
    }

    //----------------------------------------------------------------------------------------------------
    public TServiceClient createClient(String name) {
        try {
            ThriftClientInfo clientInfo = mClientMap.get(name);
            if (clientInfo == null) {
                throw new TTransportException("client not exists");
            }

            TTransport transport = new TSocket(clientInfo.host, clientInfo.port);
            transport.open();
            TProtocol protocol = new TCompactProtocol(transport);
            return clientInfo.factory.getClient(protocol);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
