package ninja.taskbook.model.entity;

//----------------------------------------------------------------------------------------------------
public class TaskEntity {

    //----------------------------------------------------------------------------------------------------
    public int taskId;
    public int taskGroupId;
    public String taskAuthor;
    public String taskName;
    public String taskContent;
    public String taskTime;
    public float taskProgress;
    public int taskUserRole;

    //----------------------------------------------------------------------------------------------------
    public TaskEntity() {

    }

    //----------------------------------------------------------------------------------------------------
    public TaskEntity(int id, int groupId, String author, String name, String content, String time, float progress, int userRole) {
        taskId = id;
        taskGroupId = groupId;
        taskAuthor = author;
        taskName = name;
        taskContent = content;
        taskTime = time;
        taskProgress = progress;
        taskUserRole = userRole;
    }
}
