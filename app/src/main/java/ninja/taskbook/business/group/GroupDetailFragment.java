package ninja.taskbook.business.group;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ninja.taskbook.R;
import ninja.taskbook.business.task.TaskCreatorFragment;
import ninja.taskbook.model.data.DataManager;
import ninja.taskbook.model.entity.GroupEntity;

//----------------------------------------------------------------------------------------------------
public class GroupDetailFragment extends Fragment {

    //----------------------------------------------------------------------------------------------------
    TextView mIdTextView;
    TextView mNameTextView;
    int mGroupId = 0;
    GroupEntity mGroupInfo = null;

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

        // Create
        Button createTaskButton = (Button)rootView.findViewById(R.id.create_task_button);
        createTaskButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createTask();
            }
        });

        // Data
        mGroupId = getArguments().getInt("id");
        mGroupInfo = DataManager.getInstance().getGroupInfo(mGroupId);

        // Load
        loadGroupData();

        return rootView;
    }

    //----------------------------------------------------------------------------------------------------
    private void loadGroupData() {
        if (mGroupInfo != null) {
            mIdTextView.setText(Integer.toString(mGroupInfo.groupId));
            mNameTextView.setText(mGroupInfo.groupName);
        }

        loadChart();
    }

    private void loadChart() {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.chart_frame_layout, new GroupTaskLineFragment())
                .commit();
    }

    //----------------------------------------------------------------------------------------------------
    private void createTask() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, new TaskCreatorFragment())
                .addToBackStack(null)
                .commit();
    }
}