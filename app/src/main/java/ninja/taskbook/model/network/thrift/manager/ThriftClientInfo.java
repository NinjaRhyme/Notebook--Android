package ninja.taskbook.model.network.thrift.manager;

import org.apache.thrift.TServiceClientFactory;

//----------------------------------------------------------------------------------------------------
public class ThriftClientInfo {

    //----------------------------------------------------------------------------------------------------
    public String name;
    public String host;
    public int port;
    public TServiceClientFactory<?> factory;

    //----------------------------------------------------------------------------------------------------
    public ThriftClientInfo(String name, String host, int port, TServiceClientFactory<?> factory) {
        this.name = name;
        this.host = host;
        this.port = port;
        this.factory = factory;
    }
}
