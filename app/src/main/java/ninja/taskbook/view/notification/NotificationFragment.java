package ninja.taskbook.view.notification;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ninja.taskbook.R;
import ninja.taskbook.model.data.DataManager;
import ninja.taskbook.model.entity.NotificationEntity;
import ninja.taskbook.model.network.thrift.service.ThriftNotificationType;

//----------------------------------------------------------------------------------------------------
public class NotificationFragment  extends Fragment {

    //----------------------------------------------------------------------------------------------------
    private RecyclerView mRecyclerView;
    private List<NotificationEntity> mNotificationItems = new ArrayList<>();

    //----------------------------------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.notification, container, false);

        // Recycler Layout
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.addItemDecoration(new NotificationItemDecoration(getContext()));
        mRecyclerView.setAdapter(new NotificationItemAdapter());

        // Load
        loadNotificationData();

        return rootView;
    }

    //----------------------------------------------------------------------------------------------------
    private void loadNotificationData() {
        DataManager.getInstance().requestNotificationItems(
                new DataManager.RequestCallback<List<NotificationEntity>>() {
                    @Override
                    public void onResult(List<NotificationEntity> result) {
                        mNotificationItems = result;
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                }
        );
    }

    //----------------------------------------------------------------------------------------------------
    void onNotificationItemClicked(final int index) {
        new AlertDialog.Builder(getContext())
                .setMessage("你确定?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NotificationEntity entity = new NotificationEntity(mNotificationItems.get(index));
                        ThriftNotificationType type = ThriftNotificationType.findByValue(entity.notificationType);
                        if (type != null) {
                            switch (type) {
                                case NOTIFICATION_JOIN:
                                    entity.notificationType = ThriftNotificationType.NOTIFICATION_JOIN_ANSWER.getValue();
                                    break;
                                case NOTIFICATION_INVITE:
                                    entity.notificationType = ThriftNotificationType.NOTIFICATION_INVITE_ANSWER.getValue();
                                    break;
                            }
                        }
                        try {
                            JSONObject jsonData = new JSONObject(entity.notificationData);
                            jsonData.put("result", "YES");
                            entity.notificationData = jsonData.toString();
                            DataManager.getInstance().requestSendNotification(entity,
                                    new DataManager.RequestCallback<Boolean>() {
                                        @Override
                                        public void onResult(Boolean result) {
                                            mRecyclerView.getAdapter().notifyDataSetChanged();
                                        }
                                    }
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NotificationEntity entity = new NotificationEntity(mNotificationItems.get(index));
                        ThriftNotificationType type = ThriftNotificationType.findByValue(entity.notificationType);
                        if (type != null) {
                            switch (type) {
                                case NOTIFICATION_JOIN:
                                    entity.notificationType = ThriftNotificationType.NOTIFICATION_JOIN_ANSWER.getValue();
                                    break;
                                case NOTIFICATION_INVITE:
                                    entity.notificationType = ThriftNotificationType.NOTIFICATION_INVITE_ANSWER.getValue();
                                    break;
                            }
                        }
                        try {
                            JSONObject jsonData = new JSONObject(entity.notificationData);
                            jsonData.put("result", "NO");
                            entity.notificationData = jsonData.toString();
                            DataManager.getInstance().requestSendNotification(entity,
                                    new DataManager.RequestCallback<Boolean>() {
                                        @Override
                                        public void onResult(Boolean result) {
                                            mRecyclerView.getAdapter().notifyDataSetChanged();
                                        }
                                    }
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setCancelable(true)
                .show();
    }

    // NotificationItemHolder
    //----------------------------------------------------------------------------------------------------
    class NotificationItemHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;

        public NotificationItemHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.title_text_view);
        }
    }

    // NotificationItemAdapter
    //----------------------------------------------------------------------------------------------------
    class NotificationItemAdapter extends RecyclerView.Adapter<NotificationItemHolder> {
        public NotificationItemAdapter() {

        }

        @Override
        public int getItemCount() {
            return mNotificationItems.size();
        }

        @Override
        public NotificationItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.notification_item, null);
            return new NotificationItemHolder(view);
        }

        @Override
        public void onBindViewHolder(NotificationItemHolder holder, final int position) {
            View view = holder.itemView;
            NotificationEntity entity = mNotificationItems.get(position);
            ThriftNotificationType type = ThriftNotificationType.findByValue(entity.notificationType);
            if (type != null) {
                switch (type) {
                    case NOTIFICATION_JOIN:
                        try {
                            JSONObject jsonData = new JSONObject(entity.notificationData);
                            String text = jsonData.getString("user_name") + "申请加入群组:" + jsonData.getInt("group_id");
                            holder.titleTextView.setText(text);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case NOTIFICATION_INVITE:
                        try {
                            JSONObject jsonData = new JSONObject(entity.notificationData);
                            String text = jsonData.getString("user_name") + "邀请您加入群组:" + jsonData.getInt("group_id");
                            holder.titleTextView.setText(text);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case NOTIFICATION_JOIN_ANSWER:
                        break;
                    case NOTIFICATION_INVITE_ANSWER:
                        break;
                    default:
                        break;
                }
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNotificationItemClicked(position);
                }
            });
        }
    }
}
