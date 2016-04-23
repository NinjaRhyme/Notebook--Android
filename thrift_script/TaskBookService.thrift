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
	7: required double taskProgress
	8: optional i32 userRole
}

# Memo: userId -> Token
service TaskBookService
{
	i32 login(1: required string userName, 2: required string userPassword);
	ThriftUserInfo userInfo(1: required i32 userId);

	ThriftGroupInfo groupInfo(1: required i32 userId, 2: required i32 groupId);
	list<ThriftGroupInfo> groupInfos(1: required i32 userId);
	ThriftGroupInfo createGroup(1: required i32 userId, 2: required ThriftGroupInfo groupInfo);
	
	ThriftTaskInfo taskInfo(1: required i32 userId, 2: required i32 taskId);
	list<ThriftTaskInfo> taskInfos(1: required i32 userId);
	ThriftTaskInfo createTask(1: required i32 userId, 2: required ThriftTaskInfo taskInfo);
}
