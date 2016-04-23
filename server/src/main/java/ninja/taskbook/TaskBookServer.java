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
import ninja.taskbook.model.database.GroupTable;
import ninja.taskbook.model.database.TaskTable;
import ninja.taskbook.model.database.UserGroupTable;
import ninja.taskbook.model.database.UserTable;
import ninja.taskbook.model.database.UserTaskTable;
import ninja.taskbook.model.entity.GroupEntity;
import ninja.taskbook.model.entity.TaskEntity;
import ninja.taskbook.model.entity.UserEntity;
import ninja.taskbook.model.entity.UserGroupRelation;
import ninja.taskbook.model.entity.UserTaskRelation;
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

        // Demo
        /*
        UserTable userTable = (UserTable)mDatabaseManager.getTable(UserTable.class);
        userTable.drop();
        userTable = (UserTable)mDatabaseManager.getTable(UserTable.class);
        UserEntity userEntity = new UserEntity(0, "test", "123456", "嘻嘻嘻嘻");
        userTable.insert(userEntity);

        GroupTable groupTable = (GroupTable)mDatabaseManager.getTable(GroupTable.class);
        groupTable.drop();
        groupTable = (GroupTable)mDatabaseManager.getTable(GroupTable.class);
        GroupEntity groupEntity = new GroupEntity(0, "Group1");
        groupTable.insert(groupEntity);

        TaskTable taskTable = (TaskTable)mDatabaseManager.getTable(TaskTable.class);
        taskTable.drop();
        taskTable = (TaskTable)mDatabaseManager.getTable(TaskTable.class);
        TaskEntity taskEntity = new TaskEntity(0, 1, "Boss1", "Task1", "嘻嘻嘻嘻嘻嘻嘻嘻嘻", "12345", 1.f);
        taskTable.insert(taskEntity);
        taskEntity = new TaskEntity(0, 1, "Boss1", "Task2", "嘻嘻嘻嘻嘻嘻嘻嘻嘻", "12345", 0.5f);
        taskTable.insert(taskEntity);
        taskEntity = new TaskEntity(0, 1, "Boss2", "Task3", "嘻嘻嘻嘻嘻嘻嘻嘻嘻", "12345", 0.3f);
        taskTable.insert(taskEntity);
        taskEntity = new TaskEntity(0, 1, "Boss2", "Task4", "嘻嘻嘻嘻嘻嘻嘻嘻嘻", "12345", 0.5f);
        taskTable.insert(taskEntity);

        UserGroupTable userGroupTable = (UserGroupTable)mDatabaseManager.getTable(UserGroupTable.class);
        userGroupTable.drop();
        userGroupTable = (UserGroupTable)mDatabaseManager.getTable(UserGroupTable.class);
        UserGroupRelation userGroupRelation = new UserGroupRelation(0, 1, 1, 0);
        userGroupTable.insert(userGroupRelation);

        UserTaskTable userTaskTable = (UserTaskTable)mDatabaseManager.getTable(UserTaskTable.class);
        userTaskTable.drop();
        userTaskTable = (UserTaskTable)mDatabaseManager.getTable(UserTaskTable.class);
        UserTaskRelation userTaskRelation = new UserTaskRelation(0, 1, 1, 0);
        userTaskTable.insert(userTaskRelation);
        userTaskRelation = new UserTaskRelation(0, 1, 2, 0);
        userTaskTable.insert(userTaskRelation);
        userTaskRelation = new UserTaskRelation(0, 1, 3, 0);
        userTaskTable.insert(userTaskRelation);
        userTaskRelation = new UserTaskRelation(0, 1, 4, 0);
        userTaskTable.insert(userTaskRelation);
        */
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
        public ThriftGroupInfo groupInfo(int userId, int groupId) throws org.apache.thrift.TException {
            // Todo:userRole = 0;
            GroupTable table = (GroupTable)mDatabaseManager.getTable(GroupTable.class);
            GroupEntity entity = table.queryEntity("group_id = '" + groupId + "'");
            if (entity != null) {
                return new ThriftGroupInfo(entity.groupId, entity.groupName);
            }
            return null;
        }

        @Override
        public List<ThriftGroupInfo> groupInfos(int userId) throws org.apache.thrift.TException {
            UserGroupTable userGroupTable = (UserGroupTable)mDatabaseManager.getTable(UserGroupTable.class);
            List<UserGroupRelation> relations = userGroupTable.queryEntities("user_id = '" + userId + "'");
            if (relations.size() > 0) {
                String idSet = "(";
                for (UserGroupRelation relation : relations) {
                    idSet += relation.groupId + ",";
                }
                idSet = idSet.substring(0, idSet.length() - 1);
                idSet += ")";

                GroupTable groupTable = (GroupTable)mDatabaseManager.getTable(GroupTable.class);
                List<GroupEntity> entities = groupTable.queryEntities("group_id in " + idSet);
                List<ThriftGroupInfo> groupInfos = new ArrayList<>();
                for (GroupEntity entity : entities) {
                    groupInfos.add(new ThriftGroupInfo(entity.groupId, entity.groupName));
                }
                return groupInfos;
            }
            return null;
        }

        @Override
        public ThriftGroupInfo createGroup(int userId, ThriftGroupInfo groupInfo) throws org.apache.thrift.TException {
            GroupTable table = (GroupTable)mDatabaseManager.getTable(GroupTable.class);
            GroupEntity entity = new GroupEntity(0, groupInfo.groupName);
            int groupId = table.insert(entity);
            if (0 < groupId) {
                UserGroupTable userGroupTable = (UserGroupTable)mDatabaseManager.getTable(UserGroupTable.class);
                UserGroupRelation relation = new UserGroupRelation(0, userId, groupId, 0);
                userGroupTable.insert(relation);
                return groupInfo(userId, groupId);
            }
            return null;
        }

        @Override
        public ThriftTaskInfo taskInfo(int userId, int taskId) throws org.apache.thrift.TException {
            // Todo:userRole = 0;
            TaskTable table = (TaskTable)mDatabaseManager.getTable(TaskTable.class);
            TaskEntity entity = table.queryEntity("task_id = '" + taskId + "'");
            if (entity != null) {
                return new ThriftTaskInfo(entity.taskId, entity.taskGroupId, entity.taskAuthor, entity.taskName, entity.taskContent, entity.taskTime, entity.taskProgress);
            }
            return null;
        }

        @Override
        public List<ThriftTaskInfo> taskInfos(int userId) throws org.apache.thrift.TException {
            UserTaskTable userTaskTable = (UserTaskTable)mDatabaseManager.getTable(UserTaskTable.class);
            List<UserTaskRelation> relations = userTaskTable.queryEntities("user_id = '" + userId + "'");
            if (relations.size() > 0) {
                String idSet = "(";
                for (UserTaskRelation relation : relations) {
                    idSet += relation.taskId + ",";
                }
                idSet = idSet.substring(0, idSet.length() - 1);
                idSet += ")";

                TaskTable taskTable = (TaskTable)mDatabaseManager.getTable(TaskTable.class);
                List<TaskEntity> entities = taskTable.queryEntities("task_id in " + idSet);
                List<ThriftTaskInfo> taskInfos = new ArrayList<>();
                for (TaskEntity entity : entities) {
                    taskInfos.add(new ThriftTaskInfo(entity.taskId, entity.taskGroupId, entity.taskAuthor, entity.taskName, entity.taskContent, entity.taskTime, entity.taskProgress));
                }
                return taskInfos;
            }
            return null;
        }

        @Override
        public ThriftTaskInfo createTask(int userId, ThriftTaskInfo taskInfo) throws org.apache.thrift.TException {
            TaskTable table = (TaskTable)mDatabaseManager.getTable(TaskTable.class);
            TaskEntity entity = new TaskEntity(0, taskInfo.groupId, taskInfo.taskAuthor, taskInfo.taskName, taskInfo.taskContent, taskInfo.taskTime, (float)taskInfo.taskProgress);
            int taskId = table.insert(entity);
            if (0 < taskId) {
                UserTaskTable userTaskTable = (UserTaskTable)mDatabaseManager.getTable(UserTaskTable.class);
                UserTaskRelation relation = new UserTaskRelation(0, userId, taskId, 0);
                userTaskTable.insert(relation);
                return taskInfo(userId, taskId);
            }
            return null;
        }
    }

    //----------------------------------------------------------------------------------------------------
    public static void main(String[] args) {
        TaskBookServer server = new TaskBookServer();
        server.startServer();
    }
}
