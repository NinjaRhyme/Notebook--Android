package ninja.taskbook.model.entity;

//----------------------------------------------------------------------------------------------------
public class TaskEntity {

    //----------------------------------------------------------------------------------------------------
    public int taskId;
    public int taskGroupId;
    public String taskAuthor;
    public String taskName;
    public String taskContent;
    public String taskBeginning;
    public String taskDeadline;
    public float taskProgress;

    //----------------------------------------------------------------------------------------------------
    public TaskEntity() {

    }

    //----------------------------------------------------------------------------------------------------
    public TaskEntity(int id, int groupId, String author, String name, String content, String beginning, String deadline, float progress) {
        taskId = id;
        taskGroupId = groupId;
        taskAuthor = author;
        taskName = name;
        taskContent = content;
        taskBeginning = beginning;
        taskDeadline = deadline;
        taskProgress = progress;
    }
}
