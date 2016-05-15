package ninja.taskbook.model.network.thrift.manager;

import android.util.ArrayMap;

import org.apache.thrift.TServiceClient;
import org.apache.thrift.TServiceClientFactory;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.util.Map;

import ninja.taskbook.model.network.thrift.service.TaskBookService;

//----------------------------------------------------------------------------------------------------
public class ThriftManager {

    //----------------------------------------------------------------------------------------------------
    public enum ClientTypeEnum {
        CLIENT("/service");

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
    public static final String THRIFT_HOST = "10.0.2.22";
    public static final int THRIFT_PORT = 8090;

    private static ThriftManager sInstance;
    private Map<String, ThriftClientInfo> mClientMap = new ArrayMap<>();

    //----------------------------------------------------------------------------------------------------
    private static ThriftManager getInstance() {
        if (sInstance == null) {
            sInstance = new ThriftManager();
        }
        return sInstance;
    }

    private ThriftManager() {
        initialize();
    }

    private void initialize() {
        try {
            registerClient(ClientTypeEnum.CLIENT.toString(), THRIFT_HOST, THRIFT_PORT, new TaskBookService.Client.Factory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerClient(String name, String host, int port, TServiceClientFactory<?> factory) {
        mClientMap.put(name, new ThriftClientInfo(name, host, port, factory));
    }

    // APIs
    //----------------------------------------------------------------------------------------------------
    public static TServiceClient createClient(String name) {
        try {
            ThriftClientInfo clientInfo = getInstance().mClientMap.get(name);
            if (clientInfo == null) {
                throw new TTransportException("client not exists");
            }

            TTransport transport = new TSocket(clientInfo.host, clientInfo.port);
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            return clientInfo.factory.getClient(protocol);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
