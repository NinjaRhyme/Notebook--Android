package ninja.taskbook;

import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;

import java.util.ArrayList;
import java.util.List;

import ninja.taskbook.model.database.DatabaseManager;
import ninja.taskbook.model.database.UserTable;
import ninja.taskbook.model.entity.UserEntity;
import ninja.taskbook.model.network.thrift.service.TaskBookService;
import ninja.taskbook.model.network.thrift.service.ThriftGroupInfo;
import ninja.taskbook.model.network.thrift.service.ThriftTaskInfo;
import ninja.taskbook.model.network.thrift.service.ThriftUserInfo;

//----------------------------------------------------------------------------------------------------
public class TaskBookServer {

    //----------------------------------------------------------------------------------------------------
    public static final int SERVER_PORT = 8090;
    private DatabaseManager mDatabaseManager = new DatabaseManager();

    //----------------------------------------------------------------------------------------------------
    public TaskBookServer() {
        //UserTable table = (UserTable)mDatabaseManager.getTable(UserTable.class);
        //UserEntity entity = new UserEntity(-1, "test", "123456", "嘻嘻嘻嘻");
        //table.insert(entity);
    }

    //----------------------------------------------------------------------------------------------------
    public void startServer() {
        try {
            System.out.println("Server start");

            TProcessor processor = new TaskBookService.Processor<TaskBookService.Iface> (new ServiceImpl());
            TServerSocket transport = new TServerSocket(SERVER_PORT);
            TThreadPoolServer.Args args = new TThreadPoolServer.Args(transport);
            args.processor(processor);
            args.protocolFactory(new TBinaryProtocol.Factory());

            TServer server = new TThreadPoolServer(args);
            server.serve();

        } catch (Exception e) {
            System.out.println("Server start error");
            e.printStackTrace();
        }
    }

    //----------------------------------------------------------------------------------------------------
    public class ServiceImpl implements TaskBookService.Iface {

        public ServiceImpl() {
        }

        @Override
        public int login(String userName, String userPassword) throws org.apache.thrift.TException {
            UserTable table = (UserTable)mDatabaseManager.getTable(UserTable.class);
            UserEntity entity = table.queryEntity("user_name = '" + userName  + "' and user_password = '" + userPassword + "'");
            if (entity != null) {
                return entity.userId;
            }
            return -1;
        }

        @Override
        public ThriftUserInfo userInfo(int userId) throws org.apache.thrift.TException {
            UserTable table = (UserTable)mDatabaseManager.getTable(UserTable.class);
            UserEntity entity = table.queryEntity("user_id = '" + userId  + "'");
            if (entity != null) {
                return new ThriftUserInfo(entity.userId, entity.userName, entity.userNickname);
            }
            return null;
        }

        @Override
        public ThriftGroupInfo groupInfo(int groupId) throws org.apache.thrift.TException {
            return new ThriftGroupInfo(groupId, "Group0");
        }

        @Override
        public List<ThriftGroupInfo> groupInfos(int userId) throws org.apache.thrift.TException {
            List<ThriftGroupInfo> groupInfos = new ArrayList<>();
            groupInfos.add(new ThriftGroupInfo(0, "Group0"));
            groupInfos.add(new ThriftGroupInfo(1, "Group1"));
            groupInfos.add(new ThriftGroupInfo(2, "Group2"));
            groupInfos.add(new ThriftGroupInfo(3, "Group3"));
            groupInfos.add(new ThriftGroupInfo(4, "Group4"));
            return groupInfos;
        }

        @Override
        public ThriftTaskInfo taskInfo(int taskId) throws org.apache.thrift.TException {
            return new ThriftTaskInfo(taskId, 0, "Task0", 0.5);
        }

        @Override
        public List<ThriftTaskInfo> taskInfos(int userId) throws org.apache.thrift.TException {
            List<ThriftTaskInfo> taskInfos = new ArrayList<>();
            taskInfos.add(new ThriftTaskInfo(0, 0, "Task0", 0.6));
            taskInfos.add(new ThriftTaskInfo(1, 0, "Task1", 0.5));
            taskInfos.add(new ThriftTaskInfo(2, 1, "Task2", 0.2));
            taskInfos.add(new ThriftTaskInfo(3, 2, "Task3", 0.1));
            taskInfos.add(new ThriftTaskInfo(4, 2, "Task4", 0.6));
            return taskInfos;
        }
    }

    //----------------------------------------------------------------------------------------------------
    public static void main(String[] args) {
        TaskBookServer server = new TaskBookServer();
        server.startServer();
    }
}
