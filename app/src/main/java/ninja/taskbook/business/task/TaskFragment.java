package ninja.taskbook.business.task;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.thrift.TException;

import java.util.ArrayList;
import java.util.List;

import ninja.taskbook.R;
import ninja.taskbook.business.group.GroupTaskLineFragment;
import ninja.taskbook.model.data.DataManager;
import ninja.taskbook.model.entity.TaskEntity;
import ninja.taskbook.model.entity.UserEntity;
import ninja.taskbook.model.network.thrift.manager.ThriftManager;
import ninja.taskbook.model.network.thrift.service.TaskBookService;
import ninja.taskbook.model.network.thrift.service.ThriftTaskInfo;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

//----------------------------------------------------------------------------------------------------
public class TaskFragment extends Fragment {

    //----------------------------------------------------------------------------------------------------
    private RecyclerView mRecyclerView;
    private List<TaskEntity> mTaskItems = new ArrayList<>();

    //----------------------------------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.task, container, false);

        // Recycler Layout
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        // Decoration
        mRecyclerView.addItemDecoration(new TaskItemDecoration());

        // Adapter
        mRecyclerView.setAdapter(new TaskItemAdapter());

        // Load
        loadTaskItems();

        return rootView;
    }

    //----------------------------------------------------------------------------------------------------
    private void loadTaskItems() {
        UserEntity entity = DataManager.getInstance().getUserInfo();
        if (entity == null) {
            // Error
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
                        if (result != null) {
                            mTaskItems.clear();
                            for (ThriftTaskInfo info : result) {
                                mTaskItems.add(new TaskEntity(info.taskId, info.groupId, info.taskAuthor, info.taskName, info.taskContent, info.taskTime, (float) info.taskProgress));
                            }
                            DataManager.getInstance().setTaskInfos(mTaskItems);
                            mRecyclerView.getAdapter().notifyDataSetChanged();
                        }
                    }
                });
    }

    //----------------------------------------------------------------------------------------------------
    void onTaskItemClicked(int id) {
        Log.d("click", "" + id);
        TaskDetailFragment fragment = new TaskDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        fragment.setArguments(bundle);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .addToBackStack(null)
                .commit();
    }

    // TaskItemHolder
    //----------------------------------------------------------------------------------------------------
    class TaskItemHolder extends RecyclerView.ViewHolder {
        public TextView itemTitleTextView;
        public TextView itemAuthorTextView;

        public TaskItemHolder(View itemView) {
            super(itemView);
            itemTitleTextView = (TextView)itemView.findViewById(R.id.item_title);
            itemAuthorTextView = (TextView)itemView.findViewById(R.id.item_author);
        }
    }

    // TaskItemAdapter
    //----------------------------------------------------------------------------------------------------
    class TaskItemAdapter extends RecyclerView.Adapter<TaskItemHolder> {
        public TaskItemAdapter() {

        }

        @Override
        public int getItemCount() {
            return mTaskItems.size();
        }

        @Override
        public TaskItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.task_item, null);
            return new TaskItemHolder(view);
        }

        @Override
        public void onBindViewHolder(TaskItemHolder holder, final int position) {
            CardView cardView = (CardView)holder.itemView;
            switch (position % 4) {
                case 0:
                    cardView.setCardBackgroundColor(Color.parseColor("#6BC39A"));
                    break;
                case 1:
                    cardView.setCardBackgroundColor(Color.parseColor("#76BEE6"));
                    break;
                case 2:
                    cardView.setCardBackgroundColor(Color.parseColor("#E99A79"));
                    break;
                case 3:
                    cardView.setCardBackgroundColor(Color.parseColor("#657DC1"));
                    break;
            }
            holder.itemTitleTextView.setText(mTaskItems.get(position).taskName);
            holder.itemAuthorTextView.setText(mTaskItems.get(position).taskAuthor);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onTaskItemClicked(mTaskItems.get(position).taskId);
                }
            });
        }
    }
}
