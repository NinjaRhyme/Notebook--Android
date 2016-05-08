package ninja.taskbook;

import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ninja.taskbook.model.database.DatabaseManager;
import ninja.taskbook.model.database.GroupTable;
import ninja.taskbook.model.database.NotificationTable;
import ninja.taskbook.model.database.TaskTable;
import ninja.taskbook.model.database.UserGroupTable;
import ninja.taskbook.model.database.UserTable;
import ninja.taskbook.model.database.UserTaskTable;
import ninja.taskbook.model.entity.GroupEntity;
import ninja.taskbook.model.entity.NotificationEntity;
import ninja.taskbook.model.entity.TaskEntity;
import ninja.taskbook.model.entity.UserEntity;
import ninja.taskbook.model.entity.UserGroupRelation;
import ninja.taskbook.model.entity.UserTaskRelation;
import ninja.taskbook.model.network.thrift.service.TaskBookService;
import ninja.taskbook.model.network.thrift.service.ThriftGroupInfo;
import ninja.taskbook.model.network.thrift.service.ThriftNotification;
import ninja.taskbook.model.network.thrift.service.ThriftNotificationType;
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
        userEntity = new UserEntity(0, "test1", "123456", "namenamename");
        userTable.insert(userEntity);

        GroupTable groupTable = (GroupTable)mDatabaseManager.getTable(GroupTable.class);
        groupTable.drop();
        groupTable = (GroupTable)mDatabaseManager.getTable(GroupTable.class);
        GroupEntity groupEntity = new GroupEntity(0, "Android 开发组");
        groupTable.insert(groupEntity);
        groupEntity = new GroupEntity(0, "iOS 开发组");
        groupTable.insert(groupEntity);
        groupEntity = new GroupEntity(0, "Test group");
        groupTable.insert(groupEntity);

        UserGroupTable userGroupTable = (UserGroupTable)mDatabaseManager.getTable(UserGroupTable.class);
        userGroupTable.drop();
        userGroupTable = (UserGroupTable)mDatabaseManager.getTable(UserGroupTable.class);
        UserGroupRelation userGroupRelation = new UserGroupRelation(0, 1, 1, 0);
        userGroupTable.insert(userGroupRelation);
        userGroupRelation = new UserGroupRelation(0, 1, 2, 0);
        userGroupTable.insert(userGroupRelation);

        TaskTable taskTable = (TaskTable)mDatabaseManager.getTable(TaskTable.class);
        taskTable.drop();
        taskTable = (TaskTable)mDatabaseManager.getTable(TaskTable.class);
        TaskEntity taskEntity = new TaskEntity(0, 1, "Boss1", "Task1", "嘻嘻嘻嘻嘻嘻嘻嘻嘻", "2016-04-10", "2016-04-15", 1.f);
        taskTable.insert(taskEntity);
        taskEntity = new TaskEntity(0, 1, "Boss1", "Task2", "嘻嘻嘻嘻嘻嘻嘻嘻嘻", "2016-04-13", "2016-05-1", 0.5f);
        taskTable.insert(taskEntity);
        taskEntity = new TaskEntity(0, 1, "Boss2", "Task3", "嘻嘻嘻嘻嘻嘻嘻嘻嘻", "2016-04-25", "2016-05-11",  0.3f);
        taskTable.insert(taskEntity);
        taskEntity = new TaskEntity(0, 1, "Boss2", "Task4", "嘻嘻嘻嘻嘻嘻嘻嘻嘻", "2016-05-11", "2016-05-11",  0.5f);
        taskTable.insert(taskEntity);

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
            return 0;
        }

        @Override
        public int signup(String userName, String userNickname, String userPassword) throws org.apache.thrift.TException {
            UserTable table = (UserTable)mDatabaseManager.getTable(UserTable.class);
            UserEntity entity = table.queryEntity("user_name = '" + userName + "'");
            if (entity == null) {
                entity = new UserEntity(0, userName, userPassword, userNickname);
                int userId = table.insert(entity);
                if (0 < userId) {
                    return userId;
                }
            }
            return 0;
        }

        @Override
        public ThriftUserInfo userInfo(int userId) throws org.apache.thrift.TException {
            UserTable table = (UserTable)mDatabaseManager.getTable(UserTable.class);
            UserEntity entity = table.queryEntity("user_id = '" + userId + "'");
            if (entity != null) {
                return new ThriftUserInfo(entity.userId, entity.userName, entity.userNickname);
            }
            return null;
        }

        @Override
        public ThriftGroupInfo groupInfo(int userId, int groupId) throws org.apache.thrift.TException {
            // Todo:userRole = ?;
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
                UserGroupRelation relation = new UserGroupRelation(0, userId, groupId, UserGroupRelation.UserGroupRole.USER_GROUP_ADMIN.ordinal());
                userGroupTable.insert(relation);
                return groupInfo(userId, groupId);
            }
            return null;
        }

        @Override
        public boolean join(int userId, int groupId) throws org.apache.thrift.TException {
            UserGroupTable userGroupTable = (UserGroupTable)mDatabaseManager.getTable(UserGroupTable.class);
            UserGroupRelation relation = userGroupTable.queryEntity("group_id = '" + groupId + "' and user_role = 0");
            if (relation != null) {
                JSONObject jsonData = new JSONObject();
                jsonData.put("group_id", String.valueOf(groupId));
                ThriftNotification notification = new ThriftNotification(0, userId, relation.userId, ThriftNotificationType.NOTIFICATION_JOIN, jsonData.toString());
                return sendNotification(userId, notification);
            }
            return false;
        }

        @Override
        public boolean invite(int userId, int groupId, int targetUserId) throws org.apache.thrift.TException {
            // Todo: user's right
            JSONObject jsonData = new JSONObject();
            jsonData.put("group_id", String.valueOf(groupId));
            ThriftNotification notification = new ThriftNotification(0, userId, targetUserId, ThriftNotificationType.NOTIFICATION_INVITE, jsonData.toString());
            return sendNotification(userId, notification);
        }

        @Override
        public ThriftTaskInfo taskInfo(int userId, int taskId) throws org.apache.thrift.TException {
            // Todo:userRole = ?;
            TaskTable table = (TaskTable)mDatabaseManager.getTable(TaskTable.class);
            TaskEntity entity = table.queryEntity("task_id = '" + taskId + "'");
            if (entity != null) {
                return new ThriftTaskInfo(entity.taskId, entity.taskGroupId, entity.taskAuthor, entity.taskName, entity.taskContent, entity.taskTime, entity.taskDeadline, entity.taskProgress);
            }
            return null;
        }

        @Override
        public List<ThriftTaskInfo> userTaskInfos(int userId) throws org.apache.thrift.TException {
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
                    taskInfos.add(new ThriftTaskInfo(entity.taskId, entity.taskGroupId, entity.taskAuthor, entity.taskName, entity.taskContent, entity.taskTime, entity.taskTime, entity.taskProgress)); // Todo: deadline
                }
                return taskInfos;
            }
            return null;
        }

        @Override
        public List<ThriftTaskInfo> groupTaskInfos(int groupId) throws org.apache.thrift.TException {
            TaskTable userTaskTable = (TaskTable)mDatabaseManager.getTable(TaskTable.class);
            List<TaskEntity> entities = userTaskTable.queryEntities("task_group_id = '" + groupId + "'");
            List<ThriftTaskInfo> taskInfos = new ArrayList<>();
            for (TaskEntity entity : entities) {
                taskInfos.add(new ThriftTaskInfo(entity.taskId, entity.taskGroupId, entity.taskAuthor, entity.taskName, entity.taskContent, entity.taskTime, entity.taskDeadline, entity.taskProgress));
            }
            return taskInfos;
        }

        @Override
        public ThriftTaskInfo createTask(int userId, ThriftTaskInfo taskInfo) throws org.apache.thrift.TException {
            TaskTable table = (TaskTable)mDatabaseManager.getTable(TaskTable.class);
            TaskEntity entity = new TaskEntity(0, taskInfo.groupId, taskInfo.taskAuthor, taskInfo.taskName, taskInfo.taskContent, taskInfo.taskTime, taskInfo.taskDeadline, (float)taskInfo.taskProgress);
            int taskId = table.insert(entity);
            if (0 < taskId) {
                UserTaskTable userTaskTable = (UserTaskTable)mDatabaseManager.getTable(UserTaskTable.class);
                UserTaskRelation relation = new UserTaskRelation(0, userId, taskId, UserTaskRelation.UserTaskRole.USER_TASK_ADMIN.ordinal());
                userTaskTable.insert(relation);
                return taskInfo(userId, taskId);
            }
            return null;
        }

        @Override
        public boolean sendNotification(int userId, ThriftNotification notification) throws org.apache.thrift.TException {
            // Todo: userId vs ownerId && Json
            JSONObject jsonData = new JSONObject(notification.notificationData);
            switch (notification.notificationType) {
                case NOTIFICATION_JOIN:
                case NOTIFICATION_INVITE:
                    if (notification.notificationId <= 0) {
                        NotificationTable table = (NotificationTable)mDatabaseManager.getTable(NotificationTable.class);
                        NotificationEntity entity = new NotificationEntity(notification.notificationId, notification.notificationOwnerId, notification.notificationReceiverId, notification.notificationType.getValue(), notification.notificationData);
                        int notificationId = table.insert(entity);
                        if (0 < notificationId) {
                            return true;
                        }
                    }
                    break;
                case NOTIFICATION_JOIN_ANSWER:
                    if (0 < notification.notificationId && jsonData.has("result")) {
                        if (jsonData.getString("result").equals("YES")) {
                            NotificationTable table = (NotificationTable) mDatabaseManager.getTable(NotificationTable.class);
                            int groupId = jsonData.has("group_id") ? jsonData.getInt("group_id") : 0;
                            if (0 < groupId) {
                                UserGroupTable userGroupTable = (UserGroupTable) mDatabaseManager.getTable(UserGroupTable.class);
                                UserGroupRelation relation = new UserGroupRelation(0, userId, groupId, UserGroupRelation.UserGroupRole.USER_GROUP_MEMBER.ordinal());
                                userGroupTable.insert(relation);
                                table.delete("notification_id = '" + notification.notificationId + "'");
                                return true;
                            }
                        }
                    }
                    break;
                case NOTIFICATION_INVITE_ANSWER:
                    if (0 < notification.notificationId && jsonData.has("result")) {
                        if (jsonData.getString("result").equals("YES")) {
                            NotificationTable table = (NotificationTable) mDatabaseManager.getTable(NotificationTable.class);
                            int groupId = jsonData.has("group_id") ? jsonData.getInt("group_id") : 0;
                            if (0 < groupId) {
                                UserGroupTable userGroupTable = (UserGroupTable) mDatabaseManager.getTable(UserGroupTable.class);
                                UserGroupRelation relation = new UserGroupRelation(0, notification.notificationReceiverId, groupId, UserGroupRelation.UserGroupRole.USER_GROUP_MEMBER.ordinal());
                                userGroupTable.insert(relation);
                                table.delete("notification_id = '" + notification.notificationId + "'");
                                return true;
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
            return false;
        }

        @Override
        public List<ThriftNotification> notifications(int userId) throws org.apache.thrift.TException {
            NotificationTable table = (NotificationTable)mDatabaseManager.getTable(NotificationTable.class);
            List<NotificationEntity> entities = table.queryEntities("notification_receiver_id = '" + userId + "'");
            List<ThriftNotification> notifications = new ArrayList<>();
            for (NotificationEntity entity : entities) {
                notifications.add(new ThriftNotification(entity.notificationId, entity.notificationOwnerId, entity.notificationReceiverId, ThriftNotificationType.findByValue(entity.notificationType), entity.notificationData));
            }
            return notifications;
        }
    }

    //----------------------------------------------------------------------------------------------------
    public static void main(String[] args) {
        TaskBookServer server = new TaskBookServer();
        server.startServer();
    }
}
