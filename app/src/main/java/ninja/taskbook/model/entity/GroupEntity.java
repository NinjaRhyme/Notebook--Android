package ninja.taskbook.model.entity;

//----------------------------------------------------------------------------------------------------
public class GroupEntity {

    //----------------------------------------------------------------------------------------------------
    public int groupId;
    public String groupName;
    public int groupUserRole;

    //----------------------------------------------------------------------------------------------------
    public GroupEntity() {

    }

    //----------------------------------------------------------------------------------------------------
    public GroupEntity(int id, String name, int userRole) {
        groupId = id;
        groupName = name;
        groupUserRole = userRole;
    }
}
