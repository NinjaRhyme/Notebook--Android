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
	6: required string taskTime
	7: required string taskDeadline
	8: required double taskProgress
	9: optional i32 userRole
}

struct ThriftNotification
{
	1: required i32 notiId
	2: required i32 ownerId
	3: required i32 receiverId
	4: required i32 notiType
	5: required string notiData
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
	
	ThriftTaskInfo taskInfo(1: required i32 userId, 2: required i32 taskId);
	list<ThriftTaskInfo> userTaskInfos(1: required i32 userId);
	list<ThriftTaskInfo> groupTaskInfos(1: required i32 groupId);
	ThriftTaskInfo createTask(1: required i32 userId, 2: required ThriftTaskInfo taskInfo);

	list<ThriftNotification> notifications(1: required i32 userId);
}
