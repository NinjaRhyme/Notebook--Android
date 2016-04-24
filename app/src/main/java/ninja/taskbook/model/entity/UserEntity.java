package ninja.taskbook.model.entity;

//----------------------------------------------------------------------------------------------------
public class UserEntity {

    //----------------------------------------------------------------------------------------------------
    public int userId;
    public String userName;
    public String userNickname;

    //----------------------------------------------------------------------------------------------------
    public UserEntity() {

    }

    //----------------------------------------------------------------------------------------------------
    public UserEntity(int id, String name, String nickname) {
        userId = id;
        userName = name;
        userNickname = nickname;
    }
}
