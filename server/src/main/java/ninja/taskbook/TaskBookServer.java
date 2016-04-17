package ninja.taskbook;

import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;

import ninja.taskbook.model.database.DatabaseManager;
import ninja.taskbook.model.network.thrift.service.TaskBookService;
import ninja.taskbook.model.network.thrift.service.ThriftUserInfo;

//----------------------------------------------------------------------------------------------------
public class TaskBookServer {

    //----------------------------------------------------------------------------------------------------
    public static final int SERVER_PORT = 8090;
    private DatabaseManager mDatabaseManager;

    //----------------------------------------------------------------------------------------------------
    public TaskBookServer() {
        mDatabaseManager = new DatabaseManager();
    }

    //----------------------------------------------------------------------------------------------------
    public void startServer() {
        try {
            System.out.println("Server start ...");

            TProcessor processor = new TaskBookService.Processor<TaskBookService.Iface> (new ServiceImpl());
            TServerSocket transport = new TServerSocket(SERVER_PORT);
            TThreadPoolServer.Args args = new TThreadPoolServer.Args(transport);
            args.processor(processor);
            args.protocolFactory(new TBinaryProtocol.Factory());

            TServer server = new TThreadPoolServer(args);
            server.serve();

        } catch (Exception e) {
            System.out.println("Server start error ...");
            e.printStackTrace();
        }
    }

    //----------------------------------------------------------------------------------------------------
    public class ServiceImpl implements TaskBookService.Iface {

        public ServiceImpl() {
        }

        @Override
        public ThriftUserInfo userInfo(int userId) throws TException {
            ThriftUserInfo userInfo = new ThriftUserInfo();
            userInfo.setUserId(userId);
            userInfo.setUserName("TestUser");
            userInfo.setUserNickname("TestNickName");

            return userInfo;
        }
    }

    //----------------------------------------------------------------------------------------------------
    public static void main(String[] args) {
        TaskBookServer server = new TaskBookServer();
        server.startServer();
    }
}
