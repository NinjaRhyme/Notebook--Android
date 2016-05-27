package ninja.taskbook.view.task;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ninja.taskbook.R;
import ninja.taskbook.model.data.DataManager;
import ninja.taskbook.model.entity.GroupEntity;
import ninja.taskbook.model.entity.TaskEntity;

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
        loadTaskData();

        return rootView;
    }

    //----------------------------------------------------------------------------------------------------
    private void loadTaskData() {
        DataManager.getInstance().requestTaskItems(
                new DataManager.RequestCallback<List<TaskEntity>>() {
                    @Override
                    public void onResult(List<TaskEntity> result) {
                        mTaskItems = result;
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                }
        );
    }

    //----------------------------------------------------------------------------------------------------
    void onTaskItemClicked(int id) {
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
        public TextView titleTextView;
        public TextView groupNameTextView;
        public TextView authorTextView;

        public TaskItemHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView)itemView.findViewById(R.id.title_text_view);
            groupNameTextView = (TextView)itemView.findViewById(R.id.group_name_text_view);
            authorTextView = (TextView)itemView.findViewById(R.id.author_text_view);
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
        public void onBindViewHolder(final TaskItemHolder holder, final int position) {
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
            final TaskEntity taskEntity = mTaskItems.get(position);
            final GroupEntity groupEntity = DataManager.getInstance().getGroupItem(taskEntity.taskGroupId);
            if (groupEntity == null) {
                DataManager.getInstance().requestGroupItem(taskEntity.taskGroupId,
                        new DataManager.RequestCallback<GroupEntity>() {
                            @Override
                            public void onResult(GroupEntity result) {
                                holder.groupNameTextView.setText(result.groupName);
                            }
                        }
                );
            } else {
                holder.groupNameTextView.setText(groupEntity.groupName);
            }
            holder.titleTextView.setText(taskEntity.taskName);
            holder.authorTextView.setText(taskEntity.taskAuthor);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onTaskItemClicked(taskEntity.taskId);
                }
            });
        }
    }
}
