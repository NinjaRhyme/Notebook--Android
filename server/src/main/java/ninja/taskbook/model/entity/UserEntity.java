package ninja.taskbook.model.entity;

//----------------------------------------------------------------------------------------------------
public class UserEntity {

    //----------------------------------------------------------------------------------------------------
    public int userId;
    public String userName;
    public String userPassword;
    public String userNickname;

    //----------------------------------------------------------------------------------------------------
    public UserEntity() {

    }

    //----------------------------------------------------------------------------------------------------
    public UserEntity(int id, String name, String password, String nickname) {
        userId = id;
        userName = name;
        userPassword = password;
        userNickname = nickname;
    }
}
