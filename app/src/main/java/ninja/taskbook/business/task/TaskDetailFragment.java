package ninja.taskbook.business.task;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ninja.taskbook.R;
import ninja.taskbook.model.data.DataManager;
import ninja.taskbook.model.entity.TaskEntity;

//----------------------------------------------------------------------------------------------------
public class TaskDetailFragment extends Fragment {

    //----------------------------------------------------------------------------------------------------
    TextView mNameTextView;
    TextView mContentTextView;
    int mTaskId = 0;
    TaskEntity mTaskInfo = null;

    //----------------------------------------------------------------------------------------------------
    public TaskDetailFragment() {

    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.task_detail, container, false);

        // Name
        mNameTextView = (TextView)rootView.findViewById(R.id.name_text_view);
        mNameTextView.setText("name");

        // Content
        mContentTextView = (TextView)rootView.findViewById(R.id.content_text_view);
        mContentTextView.setText("content");

        // Data
        mTaskId = getArguments().getInt("id");
        mTaskInfo = DataManager.getInstance().getTaskItem(mTaskId);

        // Load
        loadTaskData();

        return rootView;
    }

    //----------------------------------------------------------------------------------------------------
    private void loadTaskData() {
        if (mTaskInfo != null) {
            mNameTextView.setText(mTaskInfo.taskName);
            mContentTextView.setText(mTaskInfo.taskContent);
        }
    }
}
