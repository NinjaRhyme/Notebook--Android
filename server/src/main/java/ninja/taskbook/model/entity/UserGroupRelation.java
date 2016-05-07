package ninja.taskbook.model.entity;

//----------------------------------------------------------------------------------------------------
public class UserGroupRelation {

    //----------------------------------------------------------------------------------------------------
    public enum UserGroupRole {
        USER_GROUP_ADMIN,
        USER_GROUP_MEMBER,
    }

    //----------------------------------------------------------------------------------------------------
    public int userGroupId;
    public int userId;
    public int groupId;
    public int userRole;

    //----------------------------------------------------------------------------------------------------
    public UserGroupRelation() {

    }

    //----------------------------------------------------------------------------------------------------
    public UserGroupRelation(int _userGroupId, int _userId, int _groupId, int _userRole) {
        userGroupId = _userGroupId;
        userId = _userId;
        groupId = _groupId;
        userRole = _userRole;
    }
}
