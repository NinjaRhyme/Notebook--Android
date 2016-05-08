package ninja.taskbook.business.group;

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

import org.apache.thrift.TException;

import java.util.ArrayList;
import java.util.List;

import ninja.taskbook.R;
import ninja.taskbook.model.data.DataManager;
import ninja.taskbook.model.entity.GroupEntity;
import ninja.taskbook.model.entity.UserEntity;
import ninja.taskbook.model.network.thrift.manager.ThriftManager;
import ninja.taskbook.model.network.thrift.service.TaskBookService;
import ninja.taskbook.model.network.thrift.service.ThriftGroupInfo;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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
        loadProfileData();

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
    private void loadProfileData() {
        UserEntity entity = DataManager.getInstance().getUserItem();
        if (entity == null) {
            return;
        }

        Observable.just(entity.userId)
                .map(new Func1<Integer, List<ThriftGroupInfo>>() {
                    @Override
                    public List<ThriftGroupInfo> call(Integer userId) {
                        try {
                            TaskBookService.Client client = (TaskBookService.Client) ThriftManager.createClient(ThriftManager.ClientTypeEnum.CLIENT.toString());
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
                        mGroupItems.clear();
                        if (result != null) {
                            for (ThriftGroupInfo info : result) {
                                GroupEntity entity = new GroupEntity(info.groupId, info.groupName);
                                mGroupItems.add(entity);
                            }
                        }
                        DataManager.getInstance().setGroupItems(mGroupItems);
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                });
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
        public TextView itemTitleTextView;

        public GroupItemHolder(View itemView) {
            super(itemView);
            itemTitleTextView = (TextView)itemView.findViewById(R.id.item_title);
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
            holder.itemTitleTextView.setText(mGroupItems.get(position).groupName);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onGroupItemClicked(mGroupItems.get(position).groupId);
                }
            });
        }
    }
}
