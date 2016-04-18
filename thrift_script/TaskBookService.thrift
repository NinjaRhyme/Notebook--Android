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
}

struct ThriftTaskInfo
{
	1: required i32 taskId
	2: required i32 groupId
	3: required string taskName
	4: required double taskProgress
}

# Memo: userId -> Token
service TaskBookService
{
	i32 login(1: required string userName, 2: required string userPassword);
	ThriftUserInfo userInfo(1: required i32 userId);

	ThriftGroupInfo groupInfo(1: required i32 groupId);
	list<ThriftGroupInfo> groupInfos(1: required i32 userId);
	
	ThriftTaskInfo taskInfo(1: required i32 taskId);
	list<ThriftTaskInfo> taskInfos(1: required i32 userId);
}
