package ninja.taskbook.view.group;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import ninja.taskbook.R;
import ninja.taskbook.view.task.TaskCreatorFragment;
import ninja.taskbook.model.data.DataManager;
import ninja.taskbook.model.entity.GroupEntity;
import ninja.taskbook.model.entity.TaskEntity;

//----------------------------------------------------------------------------------------------------
public class GroupDetailFragment extends Fragment {

    //----------------------------------------------------------------------------------------------------
    TextView mIdTextView;
    TextView mNameTextView;
    int mGroupId = 0;
    GroupEntity mGroupInfo = null;
    List<TaskEntity> mGroupTaskItems = null;

    //----------------------------------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.group_detail, container, false);

        // Id
        mIdTextView = (TextView)rootView.findViewById(R.id.id_text_view);
        mIdTextView.setText("id");

        // Name
        mNameTextView = (TextView)rootView.findViewById(R.id.name_text_view);
        mNameTextView.setText("name");

        // Invite
        Button inviteTaskButton = (Button)rootView.findViewById(R.id.invite_button);
        inviteTaskButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                invite();
            }
        });

        // Create
        Button createTaskButton = (Button)rootView.findViewById(R.id.create_task_button);
        createTaskButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createTask();
            }
        });

        // Data
        mGroupId = getArguments().getInt("id");
        mGroupInfo = DataManager.getInstance().getGroupItem(mGroupId);

        // Load
        loadGroupData();

        return rootView;
    }

    //----------------------------------------------------------------------------------------------------
    private void loadGroupData() {
        if (mGroupInfo != null) {
            mIdTextView.setText(String.valueOf(mGroupInfo.groupId));
            mNameTextView.setText(mGroupInfo.groupName);
        }

        DataManager.getInstance().requestGroupTaskItems(mGroupId,
                new DataManager.RequestCallback<List<TaskEntity>>() {
                    @Override
                    public void onResult(List<TaskEntity> result) {
                        mGroupTaskItems = result;
                        loadChart();
                    }
                }
        );
    }

    private void loadChart() {
        GroupTaskLineFragment fragment = new GroupTaskLineFragment();
        fragment.setGroupTaskItems(mGroupTaskItems);
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.chart_frame_layout, fragment)
                .commit();
    }

    //----------------------------------------------------------------------------------------------------
    private void invite() {
        GroupInviteFragment fragment = new GroupInviteFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", mGroupId);
        fragment.setArguments(bundle);
        getFragmentManager()
                .beginTransaction()
                .add(R.id.frame_layout, fragment)
                .addToBackStack(null)
                .commit();
    }

    //----------------------------------------------------------------------------------------------------
    private void createTask() {
        TaskCreatorFragment fragment = new TaskCreatorFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", mGroupId);
        fragment.setArguments(bundle);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .addToBackStack(null)
                .commit();
    }
}