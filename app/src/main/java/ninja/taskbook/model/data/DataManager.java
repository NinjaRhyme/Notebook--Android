package ninja.taskbook.model.data;

import org.apache.thrift.TException;

import java.util.ArrayList;
import java.util.List;

import ninja.taskbook.model.entity.GroupEntity;
import ninja.taskbook.model.entity.NotificationEntity;
import ninja.taskbook.model.entity.TaskEntity;
import ninja.taskbook.model.entity.UserEntity;
import ninja.taskbook.model.network.thrift.manager.ThriftManager;
import ninja.taskbook.model.network.thrift.service.TaskBookService;
import ninja.taskbook.model.network.thrift.service.ThriftGroupInfo;
import ninja.taskbook.model.network.thrift.service.ThriftNotification;
import ninja.taskbook.model.network.thrift.service.ThriftTaskInfo;
import ninja.taskbook.model.network.thrift.service.ThriftUserInfo;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

//----------------------------------------------------------------------------------------------------
public class DataManager {
    //----------------------------------------------------------------------------------------------------
    public interface RequestCallback<T> {
        void onResult(T result);
    }

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

    public void requestUserItem(final RequestCallback<UserEntity> callback) {
        UserEntity entity = getUserItem();
        if (entity == null || entity.userId <= 0) {
            return;
        }

        Observable.just(entity.userId)
                .map(new Func1<Integer, ThriftUserInfo>() {
                    @Override
                    public ThriftUserInfo call(Integer userId) {
                        try {
                            TaskBookService.Client client = (TaskBookService.Client) ThriftManager.createClient(ThriftManager.ClientTypeEnum.CLIENT.toString());
                            if (client != null) {
                                return client.userInfo(userId);
                            }
                        } catch (TException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ThriftUserInfo>() {
                    @Override
                    public void call(ThriftUserInfo result) {
                        setUserItem(new UserEntity(result.userId, result.userName, result.userNickname));
                        if (callback != null) {
                            callback.onResult(mUserItem);
                        }
                    }
                });
    }

    //----------------------------------------------------------------------------------------------------
    public List<GroupEntity> getGroupItems() {
        if (mGroupItems == null) {
            mGroupItems = new ArrayList<>();
        }
        return mGroupItems;
    }

    public void setGroupItems(List<GroupEntity> groupItems) {
        this.mGroupItems = groupItems;
    }

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

    public void requestGroupItems(final RequestCallback<List<GroupEntity>> callback) {
        UserEntity entity = getUserItem();
        if (entity == null || entity.userId <= 0) {
            return;
        }

        Observable.just(entity.userId)
                .map(new Func1<Integer, List<ThriftGroupInfo>>() {
                    @Override
                    public List<ThriftGroupInfo> call(Integer userId) {
                        try {
                            TaskBookService.Client client = (TaskBookService.Client)ThriftManager.createClient(ThriftManager.ClientTypeEnum.CLIENT.toString());
                            if (client != null)
                                return client.groupInfos(userId);
                        } catch (TException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<ThriftGroupInfo>>() {
                    @Override
                    public void call(List<ThriftGroupInfo> result) {
                        if (mGroupItems == null) {
                            mGroupItems = new ArrayList<>();
                        }
                        mGroupItems.clear();
                        if (result != null) {
                            for (ThriftGroupInfo info : result) {
                                GroupEntity entity = new GroupEntity(info.groupId, info.groupName, info.userRole);
                                mGroupItems.add(entity);
                            }
                        }
                        if (callback != null) {
                            callback.onResult(mGroupItems);
                        }
                    }
                });
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

    public void requestTaskItems(final RequestCallback<List<TaskEntity>> callback) {
        UserEntity entity = getUserItem();
        if (entity == null || entity.userId <= 0) {
            return;
        }

        Observable.just(entity.userId)
                .map(new Func1<Integer, List<ThriftTaskInfo>>() {
                    @Override
                    public List<ThriftTaskInfo> call(Integer userId) {
                        try {
                            TaskBookService.Client client = (TaskBookService.Client) ThriftManager.createClient(ThriftManager.ClientTypeEnum.CLIENT.toString());
                            if (client != null) {
                                return client.userTaskInfos(userId);
                            }
                        } catch (TException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<ThriftTaskInfo>>() {
                    @Override
                    public void call(List<ThriftTaskInfo> result) {
                        if (mTaskItems == null) {
                            mTaskItems = new ArrayList<>();
                        }
                        mTaskItems.clear();
                        if (result != null) {
                            for (ThriftTaskInfo info : result) {
                                mTaskItems.add(new TaskEntity(info.taskId, info.groupId, info.taskAuthor, info.taskName, info.taskContent, info.taskTime, (float)info.taskProgress, info.userRole));
                            }
                        }
                        if (callback != null) {
                            callback.onResult(mTaskItems);
                        }
                    }
                });
    }

    //----------------------------------------------------------------------------------------------------
    public List<NotificationEntity> getNotificationItems() {
        return mNotificationItems;
    }

    public void setNotificationItems(List<NotificationEntity> notificationItems) {
        this.mNotificationItems = notificationItems;
    }

    public void requestNotificationItems(final RequestCallback<List<NotificationEntity>> callback) {
        UserEntity entity = getUserItem();
        if (entity == null || entity.userId <= 0) {
            return;
        }

        Observable.just(entity.userId)
                .map(new Func1<Integer, List<ThriftNotification>>() {
                    @Override
                    public List<ThriftNotification> call(Integer userId) {
                        try {
                            TaskBookService.Client client = (TaskBookService.Client)ThriftManager.createClient(ThriftManager.ClientTypeEnum.CLIENT.toString());
                            if (client != null)
                                return client.notifications(userId);
                        } catch (TException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<ThriftNotification>>() {
                    @Override
                    public void call(List<ThriftNotification> result) {
                        if (mNotificationItems == null) {
                            mNotificationItems = new ArrayList<>();
                        }
                        mNotificationItems.clear();
                        if (result != null) {
                            for (ThriftNotification item : result) {
                                NotificationEntity entity = new NotificationEntity(item.notificationId, item.notificationOwnerId, item.notificationReceiverId, item.notificationType.getValue(), item.notificationData);
                                mNotificationItems.add(entity);
                            }
                        }
                        if (callback != null) {
                            callback.onResult(mNotificationItems);
                        }
                    }
                });
    }
}
