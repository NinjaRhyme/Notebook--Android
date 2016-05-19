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
import ninja.taskbook.util.pair.Pair;

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
        userGroupRelation = new UserGroupRelation(0, 1, 3, 0);
        userGroupTable.insert(userGroupRelation);

        TaskTable taskTable = (TaskTable)mDatabaseManager.getTable(TaskTable.class);
        taskTable.drop();
        taskTable = (TaskTable)mDatabaseManager.getTable(TaskTable.class);
        TaskEntity taskEntity = new TaskEntity(0, 1, "Boss1", "Task1", "嘻嘻嘻嘻嘻嘻嘻嘻嘻", "{\"calendar\":\"2016-04-10\", \"time\":\"08:00\"}", "{\"calendar\":\"2016-04-15\", \"time\":\"08:00\"}", 1.f);
        taskTable.insert(taskEntity);
        taskEntity = new TaskEntity(0, 1, "Boss1", "Task2", "嘻嘻嘻嘻嘻嘻嘻嘻嘻", "{\"calendar\":\"2016-04-13\", \"time\":\"08:00\"}", "{\"calendar\":\"2016-05-1\", \"time\":\"08:00\"}", 0.5f);
        taskTable.insert(taskEntity);
        taskEntity = new TaskEntity(0, 1, "Boss2", "Task3", "嘻嘻嘻嘻嘻嘻嘻嘻嘻", "{\"calendar\":\"2016-04-25\", \"time\":\"08:00\"}", "{\"calendar\":\"2016-05-11\", \"time\":\"08:00\"}",  0.3f);
        taskTable.insert(taskEntity);
        taskEntity = new TaskEntity(0, 1, "Boss2", "Task4", "嘻嘻嘻嘻嘻嘻嘻嘻嘻", "{\"calendar\":\"2016-05-11\", \"time\":\"08:00\"}", "{\"calendar\":\"2016-05-12\", \"time\":\"08:00\"}",  0.5f);
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

        NotificationTable notificationTable = (NotificationTable)mDatabaseManager.getTable(NotificationTable.class);
        notificationTable.drop();
        notificationTable = (NotificationTable)mDatabaseManager.getTable(NotificationTable.class);
        NotificationEntity notificationEntity = new NotificationEntity(0, 1, 1, 0, "{\"group_id\":\"1\"}", true);
        notificationTable.insert(notificationEntity);
        */

        //UserGroupTable userGroupTable = (UserGroupTable)mDatabaseManager.getTable(UserGroupTable.class);
        //List<?> tmp = userGroupTable.queryRelationEntities("user_id = '" + 1 + "' and user_group.group_id = 'group'.group_id and user_group.group_id = '" + 1 + "'");
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

        //----------------------------------------------------------------------------------------------------
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

        //----------------------------------------------------------------------------------------------------
        @Override
        public ThriftUserInfo userInfo(int userId) throws org.apache.thrift.TException {
            UserTable table = (UserTable)mDatabaseManager.getTable(UserTable.class);
            UserEntity entity = table.queryEntity("user_id = '" + userId + "'");
            if (entity != null) {
                return new ThriftUserInfo(entity.userId, entity.userName, entity.userNickname);
            }
            return null;
        }

        //----------------------------------------------------------------------------------------------------
        @Override
        public ThriftGroupInfo groupInfo(int userId, int groupId) throws org.apache.thrift.TException {
            UserGroupTable userGroupTable = (UserGroupTable)mDatabaseManager.getTable(UserGroupTable.class);
            Pair<?, ?> pair = userGroupTable.queryRelationEntity("user_id = '" + userId + "' and user_group.group_id = 'group'.group_id and user_group.group_id = '" + groupId + "'");
            if (pair != null) {
                UserGroupRelation relation = (UserGroupRelation)pair.first;
                GroupEntity entity = (GroupEntity)pair.second;
                ThriftGroupInfo groupInfo = new ThriftGroupInfo(entity.groupId, entity.groupName);
                groupInfo.userRole = relation.userRole;
                return groupInfo;
            }
            return null;
        }

        @Override
        public List<ThriftGroupInfo> groupInfos(int userId) throws org.apache.thrift.TException {
            UserGroupTable userGroupTable = (UserGroupTable)mDatabaseManager.getTable(UserGroupTable.class);
            List<Pair<?, ?>> pairs = userGroupTable.queryRelationEntities("user_id = '" + userId + "' and user_group.group_id = 'group'.group_id");
            if (pairs != null) {
                List<ThriftGroupInfo> groupInfos = new ArrayList<>();
                for (Pair<?, ?> pair : pairs) {
                    UserGroupRelation relation = (UserGroupRelation)pair.first;
                    GroupEntity entity = (GroupEntity)pair.second;
                    ThriftGroupInfo info = new ThriftGroupInfo(entity.groupId, entity.groupName);
                    info.userRole = relation.userRole;
                    groupInfos.add(info);
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
                jsonData.put("group_id", groupId);
                ThriftNotification notification = new ThriftNotification(0, userId, relation.userId, ThriftNotificationType.NOTIFICATION_JOIN, jsonData.toString(), true);
                return sendNotification(userId, notification);
            }
            return false;
        }

        @Override
        public boolean invite(int userId, int groupId, int targetUserId) throws org.apache.thrift.TException {
            // Todo: user's rights
            JSONObject jsonData = new JSONObject();
            jsonData.put("group_id", groupId);
            ThriftNotification notification = new ThriftNotification(0, userId, targetUserId, ThriftNotificationType.NOTIFICATION_INVITE, jsonData.toString(), true);
            return sendNotification(userId, notification);
        }

        //----------------------------------------------------------------------------------------------------
        @Override
        public ThriftTaskInfo taskInfo(int userId, int taskId) throws org.apache.thrift.TException {
            UserTaskTable userTaskTable = (UserTaskTable)mDatabaseManager.getTable(UserTaskTable.class);
            Pair<?, ?> pair = userTaskTable.queryRelationEntity("user_id = '" + userId + "' and user_task.task_id = task.task_id and user_task.task_id = '" + taskId + "'");
            if (pair != null) {
                UserTaskRelation relation = (UserTaskRelation)pair.first;
                TaskEntity entity = (TaskEntity)pair.second;
                ThriftTaskInfo taskInfo = new ThriftTaskInfo(entity.taskId, entity.taskGroupId, entity.taskAuthor, entity.taskName, entity.taskContent, entity.taskBeginning, entity.taskDeadline, entity.taskProgress);
                taskInfo.userRole = relation.userRole;
                return taskInfo;
            }
            return null;
        }

        @Override
        public List<ThriftTaskInfo> userTaskInfos(int userId) throws org.apache.thrift.TException {
            UserTaskTable userTaskTable = (UserTaskTable)mDatabaseManager.getTable(UserTaskTable.class);
            List<Pair<?, ?>> pairs = userTaskTable.queryRelationEntities("user_id = '" + userId + "' and user_task.task_id = task.task_id");
            if (pairs != null) {
                List<ThriftTaskInfo> taskInfos = new ArrayList<>();
                for (Pair<?, ?> pair : pairs) {
                    UserTaskRelation relation = (UserTaskRelation)pair.first;
                    TaskEntity entity = (TaskEntity)pair.second;
                    ThriftTaskInfo taskInfo = new ThriftTaskInfo(entity.taskId, entity.taskGroupId, entity.taskAuthor, entity.taskName, entity.taskContent, entity.taskBeginning, entity.taskDeadline, entity.taskProgress);
                    taskInfo.userRole = relation.userRole;
                    taskInfos.add(taskInfo);
                }
                return taskInfos;
            }
            return null;
        }

        @Override
        public List<ThriftTaskInfo> groupTaskInfos(int groupId) throws org.apache.thrift.TException {
            TaskTable taskTable = (TaskTable)mDatabaseManager.getTable(TaskTable.class);
            List<TaskEntity> entities = taskTable.queryEntities("task_group_id = '" + groupId + "'");
            List<ThriftTaskInfo> taskInfos = new ArrayList<>();
            for (TaskEntity entity : entities) {
                taskInfos.add(new ThriftTaskInfo(entity.taskId, entity.taskGroupId, entity.taskAuthor, entity.taskName, entity.taskContent, entity.taskBeginning, entity.taskDeadline, entity.taskProgress));
            }
            return taskInfos;
        }

        @Override
        public ThriftTaskInfo createTask(int userId, ThriftTaskInfo taskInfo) throws org.apache.thrift.TException {
            TaskTable table = (TaskTable)mDatabaseManager.getTable(TaskTable.class);
            TaskEntity entity = new TaskEntity(0, taskInfo.groupId, taskInfo.taskAuthor, taskInfo.taskName, taskInfo.taskContent, taskInfo.taskBeginning, taskInfo.taskDeadline, (float)taskInfo.taskProgress);
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
        public boolean editTask(int userId, ThriftTaskInfo taskInfo) throws org.apache.thrift.TException {
            // Todo: dangerous
            TaskTable table = (TaskTable)mDatabaseManager.getTable(TaskTable.class);
            return table.update(new TaskEntity(0, taskInfo.groupId, taskInfo.taskAuthor, taskInfo.taskName, taskInfo.taskContent, taskInfo.taskBeginning, taskInfo.taskDeadline, (float)taskInfo.taskProgress), "task_id = '" + taskInfo.taskId + "'");
        }

        @Override
        public boolean alertTask(int userId, ThriftTaskInfo taskInfo) throws org.apache.thrift.TException {
            UserTaskTable userTaskTable = (UserTaskTable)mDatabaseManager.getTable(UserTaskTable.class);
            List<UserTaskRelation> relations = userTaskTable.queryEntities("task_id = '" + taskInfo.taskId + "' and user_role = " + UserTaskRelation.UserTaskRole.USER_TASK_MEMBER.ordinal());
            for (UserTaskRelation relation : relations) {
                JSONObject jsonData = new JSONObject();
                jsonData.put("task_id", relation.taskId);
                ThriftNotification notification = new ThriftNotification(0, userId, relation.userId, ThriftNotificationType.NOTIFICATION_ALERT, jsonData.toString(), true);
                sendNotification(userId, notification);
            }
            return true;
        }

        //----------------------------------------------------------------------------------------------------
        @Override
        public boolean sendNotification(int userId, ThriftNotification notification) throws org.apache.thrift.TException {
            // Todo: userId vs ownerId && Json
            JSONObject jsonData = new JSONObject(notification.notificationData);
            switch (notification.notificationType) {
                case NOTIFICATION_JOIN:
                case NOTIFICATION_INVITE:
                case NOTIFICATION_ALERT:
                    if (notification.notificationId <= 0) {
                        NotificationTable table = (NotificationTable)mDatabaseManager.getTable(NotificationTable.class);
                        NotificationEntity entity = new NotificationEntity(notification.notificationId, notification.notificationOwnerId, notification.notificationReceiverId, notification.notificationType.getValue(), notification.notificationData, true);
                        int notificationId = table.insert(entity);
                        if (0 < notificationId) {
                            return true;
                        }
                    }
                    break;
                case NOTIFICATION_JOIN_ANSWER:
                    if (0 < notification.notificationId && jsonData.has("result")) {
                        if (jsonData.getString("result").equals("YES")) {
                            NotificationTable table = (NotificationTable)mDatabaseManager.getTable(NotificationTable.class);
                            int groupId = jsonData.has("group_id") ? jsonData.getInt("group_id") : 0;
                            if (0 < groupId) {
                                UserGroupTable userGroupTable = (UserGroupTable)mDatabaseManager.getTable(UserGroupTable.class);
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
                            NotificationTable table = (NotificationTable)mDatabaseManager.getTable(NotificationTable.class);
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
                notifications.add(new ThriftNotification(entity.notificationId, entity.notificationOwnerId, entity.notificationReceiverId, ThriftNotificationType.findByValue(entity.notificationType), entity.notificationData,  entity.notificationIsNew));
                table.update("notification_is_new = 0", "notification_id = '" + entity.notificationId + "'");
            }
            return notifications;
        }

        @Override
        public List<ThriftNotification> newNotifications(int userId) throws org.apache.thrift.TException {
            // Todo: update
            NotificationTable table = (NotificationTable)mDatabaseManager.getTable(NotificationTable.class);
            List<NotificationEntity> entities = table.queryEntities("notification_receiver_id = '" + userId + "' and notification_is_new = 1");
            List<ThriftNotification> notifications = new ArrayList<>();
            for (NotificationEntity entity : entities) {
                ThriftNotification notification = new ThriftNotification(entity.notificationId, entity.notificationOwnerId, entity.notificationReceiverId, ThriftNotificationType.findByValue(entity.notificationType), entity.notificationData,  entity.notificationIsNew);
                notifications.add(notification);
                table.update("notification_is_new = 0", "notification_id = '" + entity.notificationId + "'");
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
