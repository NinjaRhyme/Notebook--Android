namespace java ninja.taskbook.model.network.thrift.service

struct ThriftUserInfo
{
	1: required i32 userId
	2: required string userName
	3: required string userNickname
}

struct ThriftGroupInfo
{
	1: required i32 groupId
	2: required string groupName
	3: optional i32 userRole
}

struct ThriftTaskInfo
{
	1: required i32 taskId
	2: required i32 groupId
	3: required string taskAuthor
	4: required string taskName
	5: required string taskContent
	6: required string taskBeginning
	7: required string taskDeadline
	8: required double taskProgress
	9: optional i32 userRole
}

enum ThriftNotificationType 
{
	NOTIFICATION_JOIN = 0,
	NOTIFICATION_JOIN_ANSWER,
	NOTIFICATION_INVITE,
	NOTIFICATION_INVITE_ANSWER,
	NOTIFICATION_ALERT
}

struct ThriftNotification
{
	1: required i32 notificationId
	2: required i32 notificationOwnerId
	3: required i32 notificationReceiverId
	4: required ThriftNotificationType notificationType
	5: required string notificationData
	6: required bool notificationIsNew
}

# Memo: userId -> Token
service TaskBookService
{
	i32 login(1: required string userName, 2: required string userPassword);
	i32 signup(1: required string userName, 2: required string userNickname, 3: required string userPassword);
	ThriftUserInfo userInfo(1: required i32 userId);

	ThriftGroupInfo groupInfo(1: required i32 userId, 2: required i32 groupId);
	list<ThriftGroupInfo> groupInfos(1: required i32 userId);
	ThriftGroupInfo createGroup(1: required i32 userId, 2: required ThriftGroupInfo groupInfo);
	bool join(1: required i32 userId, 2: required i32 groupId);
	bool invite(1: required i32 userId, 2: required i32 groupId, 3: required i32 targetUserId);
	
	ThriftTaskInfo taskInfo(1: required i32 userId, 2: required i32 taskId);
	list<ThriftTaskInfo> userTaskInfos(1: required i32 userId);
	list<ThriftTaskInfo> groupTaskInfos(1: required i32 groupId);
	ThriftTaskInfo createTask(1: required i32 userId, 2: required ThriftTaskInfo taskInfo);
	bool editTask(1: required i32 userId, 2: required ThriftTaskInfo taskInfo);
	bool alertTask(1: required i32 userId, 2: required ThriftTaskInfo taskInfo);

	bool sendNotification(1: required i32 userId, 2: required ThriftNotification notification);
	list<ThriftNotification> notifications(1: required i32 userId);
	list<ThriftNotification> newNotifications(1: required i32 userId);
}
