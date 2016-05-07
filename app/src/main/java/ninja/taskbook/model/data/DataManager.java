package ninja.taskbook.model.data;

import java.util.List;

import ninja.taskbook.model.entity.GroupEntity;
import ninja.taskbook.model.entity.NotificationEntity;
import ninja.taskbook.model.entity.TaskEntity;
import ninja.taskbook.model.entity.UserEntity;

//----------------------------------------------------------------------------------------------------
public class DataManager {
    //----------------------------------------------------------------------------------------------------
    private static DataManager sInstance = null;

    //----------------------------------------------------------------------------------------------------
    private UserEntity mUserItem;
    private List<GroupEntity> mGroupItems;
    private List<TaskEntity> mTaskItems;
    private List<NotificationEntity> mNotificationItems;

    //----------------------------------------------------------------------------------------------------
    private DataManager() {

    }

    //----------------------------------------------------------------------------------------------------
    public static DataManager getInstance() {
        synchronized (DataManager.class) {
            if (sInstance == null) {
                sInstance = new DataManager();
            }
        }
        return sInstance;
    }

    //----------------------------------------------------------------------------------------------------
    public UserEntity getUserItem() {
        return mUserItem;
    }

    public void setUserItem(UserEntity userItem) {
        this.mUserItem = userItem;
    }

    //----------------------------------------------------------------------------------------------------
    public GroupEntity getGroupItem(int id) {
        if (mGroupItems != null) {
            int low = 0;
            int high = mGroupItems.size() - 1;
            while (low <= high  && high <= mGroupItems.size() - 1) {
                int middle = (high + low) >> 1;
                GroupEntity entity = mGroupItems.get(middle);
                if (entity.groupId == id) {
                    return entity;
                } else if (id < entity.groupId) {
                    high = middle - 1;
                } else {
                    low = middle + 1;
                }
            }
        }
        return null;
    }

    public List<GroupEntity> getGroupItems() {
        return mGroupItems;
    }

    public void setGroupItems(List<GroupEntity> groupItems) {
        this.mGroupItems = groupItems;
    }

    //----------------------------------------------------------------------------------------------------
    public TaskEntity getTaskItem(int id) {
        if (mTaskItems != null) {
            int low = 0;
            int high = mTaskItems.size() - 1;
            while (low <= high  && high <= mTaskItems.size() - 1) {
                int middle = (high + low) >> 1;
                TaskEntity entity = mTaskItems.get(middle);
                if (entity.taskId == id) {
                    return entity;
                } else if (id < entity.taskId) {
                    high = middle - 1;
                } else {
                    low = middle + 1;
                }
            }
        }
        return null;
    }

    public List<TaskEntity> getTaskItems() {
        return mTaskItems;
    }

    public void setTaskItems(List<TaskEntity> taskItems) {
        this.mTaskItems = taskItems;
    }

    //----------------------------------------------------------------------------------------------------
    public List<NotificationEntity> getNotificationItems() {
        return mNotificationItems;
    }

    public void setNotificationItems(List<NotificationEntity> notificationItems) {
        this.mNotificationItems = notificationItems;
    }
}
