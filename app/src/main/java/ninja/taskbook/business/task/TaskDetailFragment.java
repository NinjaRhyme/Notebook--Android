package ninja.taskbook.business.task;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.thrift.TException;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ninja.taskbook.R;
import ninja.taskbook.model.data.DataManager;
import ninja.taskbook.model.entity.TaskEntity;

//----------------------------------------------------------------------------------------------------
public class TaskDetailFragment extends Fragment {

    //----------------------------------------------------------------------------------------------------
    TextView mIdTextView;
    EditText mNameEditText;
    EditText mTaskBeginningCalendarEditText;
    EditText mTaskBeginningTimeEditText;
    EditText mTaskDeadlineCalendarEditText;
    EditText mTaskDeadlineTimeEditText;
    EditText mContentEditText;
    EditText mProgressEditText;
    Button mEditCancelButton;
    Button mEditConfirmButton;
    Drawable mUnderlineDrawable;

    boolean mIsEditing = false;
    int mTaskId = 0;
    TaskEntity mTaskInfo = null;
    TaskEntity mTempTaskInfo = null;

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

        // Id
        mIdTextView = (TextView)rootView.findViewById(R.id.id_text_view);
        mIdTextView.setText("id");

        // Name
        mNameEditText = (EditText)rootView.findViewById(R.id.name_edit_text);
        mNameEditText.setText("name");
        mUnderlineDrawable = mNameEditText.getBackground();

        // Beginning
        mTaskBeginningCalendarEditText = (EditText)rootView.findViewById(R.id.beginning_calendar_edit_text);
        mTaskBeginningCalendarEditText.setText("beginning");
        mTaskBeginningTimeEditText = (EditText)rootView.findViewById(R.id.beginning_time_edit_text);
        mTaskBeginningTimeEditText.setText("beginning");

        // Deadline
        mTaskDeadlineCalendarEditText = (EditText)rootView.findViewById(R.id.deadline_calendar_edit_text);
        mTaskDeadlineCalendarEditText.setText("deadline");
        mTaskDeadlineTimeEditText = (EditText)rootView.findViewById(R.id.deadline_time_edit_text);
        mTaskDeadlineTimeEditText.setText("deadline");

        // Content
        mContentEditText = (EditText)rootView.findViewById(R.id.content_edit_text);
        mContentEditText.setText("content");

        // Progress
        mProgressEditText = (EditText)rootView.findViewById(R.id.progress_edit_text);
        mProgressEditText.setText("progress");

        // Alert
        Button alertButton = (Button)rootView.findViewById(R.id.alert_task_button);
        alertButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alert();
            }
        });

        // Edit
        Button editButton = (Button)rootView.findViewById(R.id.edit_task_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                edit();
            }
        });
        mEditCancelButton = (Button)rootView.findViewById(R.id.edit_cancel_button);
        mEditCancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                editCancel();
            }
        });
        mEditConfirmButton= (Button)rootView.findViewById(R.id.edit_confirm_button);
        mEditConfirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                editConfirm();
            }
        });

        // Data
        mTaskId = getArguments().getInt("id");
        mTaskInfo = DataManager.getInstance().getTaskItem(mTaskId);
        mTempTaskInfo = new TaskEntity(mTaskInfo);
        if (mTaskInfo == null || mTaskInfo.taskUserRole != 0)
        {
            alertButton.setVisibility(View.INVISIBLE);
        }
        disableEdit();

        // Load
        loadTaskData();

        return rootView;
    }

    //----------------------------------------------------------------------------------------------------
    private void loadTaskData() {
        if (mTaskInfo != null) {
            mIdTextView.setText(String.valueOf(mTaskInfo.taskId));
            mNameEditText.setText(mTaskInfo.taskName);
            try {
                JSONObject beginningJsonData = new JSONObject(mTaskInfo.taskBeginning);
                mTaskBeginningCalendarEditText.setText(beginningJsonData.getString("calendar"));
                mTaskBeginningTimeEditText.setText(beginningJsonData.getString("time"));
                JSONObject deadlineJsonData = new JSONObject(mTaskInfo.taskDeadline);
                mTaskDeadlineCalendarEditText.setText(deadlineJsonData.getString("calendar"));
                mTaskDeadlineTimeEditText.setText(deadlineJsonData.getString("time"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mContentEditText.setText(mTaskInfo.taskContent);
            mProgressEditText.setText(String.valueOf(mTaskInfo.taskProgress));
        }
    }

    //----------------------------------------------------------------------------------------------------
    private void alert() {
        DataManager.getInstance().requestAlertTask(mTaskInfo,
                new DataManager.RequestCallback<Boolean>() {
                    @Override
                    public void onResult(Boolean result) {
                        if (result) {
                            Toast toast = Toast.makeText(getContext(), "提醒成功", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else {
                            Toast toast = Toast.makeText(getContext(), "提醒失败", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }
                }
        );
    }

    //----------------------------------------------------------------------------------------------------
    private void enableEdit() {
        switch (mTaskInfo.taskUserRole) {
            case 0:
                // Name
                mNameEditText.setEnabled(true);
                mNameEditText.setBackground(mUnderlineDrawable);
                break;
            case 1:
                break;
            default:
                break;
        }

        // Progress
        mProgressEditText.setEnabled(true);
        mProgressEditText.setBackground(mUnderlineDrawable);

        // Button
        mEditCancelButton.setVisibility(View.INVISIBLE);
        mEditConfirmButton.setVisibility(View.INVISIBLE);
    }

    //----------------------------------------------------------------------------------------------------
    private void disableEdit() {
        // Name
        mNameEditText.setEnabled(false);
        mNameEditText.setBackground(null);

        // Beginning
        mTaskBeginningCalendarEditText.setEnabled(false);
        mTaskBeginningCalendarEditText.setBackground(null);
        mTaskBeginningTimeEditText.setEnabled(false);
        mTaskBeginningTimeEditText.setBackground(null);

        // Deadline
        mTaskDeadlineCalendarEditText.setEnabled(false);
        mTaskDeadlineCalendarEditText.setBackground(null);
        mTaskDeadlineTimeEditText.setEnabled(false);
        mTaskDeadlineTimeEditText.setBackground(null);

        // Content
        mContentEditText.setEnabled(false);
        mContentEditText.setBackground(null);

        // Progress
        mProgressEditText.setEnabled(false);
        mProgressEditText.setBackground(null);

        // Button
        mEditCancelButton.setVisibility(View.INVISIBLE);
        mEditConfirmButton.setVisibility(View.INVISIBLE);
    }

    //----------------------------------------------------------------------------------------------------
    private void edit() {
        if (mIsEditing) {
            editCancel();
        } else {
            enableEdit();
        }
        mIsEditing = !mIsEditing;
    }

    //----------------------------------------------------------------------------------------------------
    private void editConfirm() {
        try {
            mTempTaskInfo.taskName = mNameEditText.getText().toString();
            JSONObject timeJsonData = new JSONObject();
            timeJsonData.put("calendar", mTaskBeginningCalendarEditText.getText().toString());
            timeJsonData.put("time", mTaskBeginningTimeEditText.getText().toString());
            mTempTaskInfo.taskBeginning = timeJsonData.toString();
            JSONObject deadlineJsonData = new JSONObject();
            deadlineJsonData.put("calendar", mTaskDeadlineCalendarEditText.getText().toString());
            deadlineJsonData.put("time", mTaskDeadlineTimeEditText.getText().toString());
            mTempTaskInfo.taskDeadline = deadlineJsonData.toString();
            mTempTaskInfo.taskContent = mContentEditText.getText().toString();
            mTempTaskInfo.taskProgress = Float.valueOf(mProgressEditText.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        DataManager.getInstance().requestEditTask(mTempTaskInfo,
                new DataManager.RequestCallback<Boolean>() {
                    @Override
                    public void onResult(Boolean result) {
                        if (result) {
                            mTaskInfo.setTaskEntity(mTempTaskInfo);
                            Toast toast = Toast.makeText(getContext(), "修改成功", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            disableEdit();
                        } else {
                            Toast toast = Toast.makeText(getContext(), "修改失败", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }
                }
        );
    }

    //----------------------------------------------------------------------------------------------------
    private void editCancel() {
        mTempTaskInfo.setTaskEntity(mTaskInfo);
        loadTaskData();
        disableEdit();
    }
}
