namespace java ninja.taskbook.model.network.thrift.service

struct ThriftUserInfo
{
	1: required i32 userId
	2: required string userName
	3: required string userNickname
}

service TaskBookService
{
	ThriftUserInfo userInfo(1: required i32 userId);
}
