package ninja.taskbook.view.group;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ninja.taskbook.R;
import ninja.taskbook.model.data.DataManager;
import ninja.taskbook.model.entity.GroupEntity;

//----------------------------------------------------------------------------------------------------
public class GroupFragment extends Fragment {

    //----------------------------------------------------------------------------------------------------
    private RecyclerView mRecyclerView;
    private List<GroupEntity> mGroupItems = new ArrayList<>();

    //----------------------------------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.group, container, false);

        // Recycler View
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.addItemDecoration(new GroupItemDecoration(getContext()));
        mRecyclerView.setAdapter(new GroupItemAdapter());

        // Join
        Button joinButton = (Button)rootView.findViewById(R.id.join_button);
        joinButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                join();
            }
        });

        // Create
        Button createButton = (Button)rootView.findViewById(R.id.create_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                create();
            }
        });

        // Load
        loadGroupData();

        return rootView;
    }

    //----------------------------------------------------------------------------------------------------
    private void join() {
        getFragmentManager()
                .beginTransaction()
                .add(R.id.frame_layout, new GroupJoinFragment())
                .addToBackStack(null)
                .commit();
    }

    //----------------------------------------------------------------------------------------------------
    private void create() {
        getFragmentManager()
                .beginTransaction()
                .add(R.id.frame_layout, new GroupCreatorFragment())
                .addToBackStack(null)
                .commit();
    }

    //----------------------------------------------------------------------------------------------------
    private void loadGroupData() {
        DataManager.getInstance().requestGroupItems(
                new DataManager.RequestCallback<List<GroupEntity>>() {
                    @Override
                    public void onResult(List<GroupEntity> result) {
                        mGroupItems = result;
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                }
        );
    }

    //----------------------------------------------------------------------------------------------------
    void onGroupItemClicked(int id) {
        GroupDetailFragment fragment = new GroupDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        fragment.setArguments(bundle);
        getFragmentManager()
                .beginTransaction()
                .add(R.id.frame_layout, fragment)
                .addToBackStack(null)
                .commit();
    }

    // GroupItemHolder
    //----------------------------------------------------------------------------------------------------
    class GroupItemHolder extends RecyclerView.ViewHolder {
        public TextView groupNameTextView;

        public GroupItemHolder(View itemView) {
            super(itemView);
            groupNameTextView = (TextView)itemView.findViewById(R.id.group_name_text_view);
        }
    }

    // GroupItemAdapter
    //----------------------------------------------------------------------------------------------------
    class GroupItemAdapter extends RecyclerView.Adapter<GroupItemHolder> {
        public GroupItemAdapter() {

        }

        @Override
        public int getItemCount() {
            return mGroupItems.size();
        }

        @Override
        public GroupItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.group_item, null);
            return new GroupItemHolder(view);
        }

        @Override
        public void onBindViewHolder(GroupItemHolder holder, final int position) {
            View view = holder.itemView;
            holder.groupNameTextView.setText(mGroupItems.get(position).groupName);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onGroupItemClicked(mGroupItems.get(position).groupId);
                }
            });
        }
    }
}
