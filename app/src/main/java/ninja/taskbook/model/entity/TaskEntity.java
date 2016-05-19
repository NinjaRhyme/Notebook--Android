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
    public int taskUserRole;

    //----------------------------------------------------------------------------------------------------
    public TaskEntity() {

    }

    //----------------------------------------------------------------------------------------------------
    public TaskEntity(int id, int groupId, String author, String name, String content, String beginning, String deadline, float progress, int userRole) {
        taskId = id;
        taskGroupId = groupId;
        taskAuthor = author;
        taskName = name;
        taskContent = content;
        taskBeginning = beginning;
        taskDeadline = deadline;
        taskProgress = progress;
        taskUserRole = userRole;
    }

    //----------------------------------------------------------------------------------------------------
    public TaskEntity(TaskEntity entity) {
        setTaskEntity(entity);
    }

    //----------------------------------------------------------------------------------------------------
    public void setTaskEntity(TaskEntity entity) {
        if (entity != null) {
            taskId = entity.taskId;
            taskGroupId = entity.taskGroupId;
            taskAuthor = entity.taskAuthor;
            taskName = entity.taskName;
            taskContent = entity.taskContent;
            taskBeginning = entity.taskBeginning;
            taskDeadline = entity.taskDeadline;
            taskProgress = entity.taskProgress;
            taskUserRole = entity.taskUserRole;
        }
    }
}
