package ninja.taskbook.business.task;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import org.apache.thrift.TException;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import ninja.taskbook.R;
import ninja.taskbook.model.data.DataManager;
import ninja.taskbook.model.entity.UserEntity;
import ninja.taskbook.model.network.thrift.manager.ThriftManager;
import ninja.taskbook.model.network.thrift.service.TaskBookService;
import ninja.taskbook.model.network.thrift.service.ThriftTaskInfo;
import ninja.taskbook.util.helper.Helper;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

//----------------------------------------------------------------------------------------------------
public class TaskCreatorFragment extends Fragment {

    //----------------------------------------------------------------------------------------------------
    EditText mTaskNameEditText;
    EditText mTaskBeginningDateEditText;
    EditText mTaskBeginningTimeEditText;
    EditText mTaskDeadlineDateEditText;
    EditText mTaskDeadlineTimeEditText;
    EditText mTaskContentEditText;
    int mGroupId = 0;

    //----------------------------------------------------------------------------------------------------
    public TaskCreatorFragment() {

    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.task_creator, container, false);

        // Name
        mTaskNameEditText = (EditText)rootView.findViewById(R.id.task_name_edit_text);

        // Content
        mTaskContentEditText = (EditText)rootView.findViewById(R.id.task_content_edit_text);

        // Beginning
        final Calendar calendar = Calendar.getInstance();
        mTaskBeginningDateEditText = (EditText)rootView.findViewById(R.id.task_beginning_date_edit_text);
        mTaskBeginningDateEditText.setInputType(InputType.TYPE_NULL);
        mTaskBeginningDateEditText.setText(Helper.calendarToDateString(calendar));
        mTaskBeginningDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar tmpCalendar = Calendar.getInstance();
                        tmpCalendar.set(year, monthOfYear, dayOfMonth);
                        mTaskBeginningDateEditText.setText(Helper.calendarToDateString(tmpCalendar));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });
        mTaskBeginningTimeEditText = (EditText)rootView.findViewById(R.id.task_beginning_time_edit_text);
        mTaskBeginningTimeEditText.setInputType(InputType.TYPE_NULL);
        mTaskBeginningTimeEditText.setText(Helper.calendarToTimeString(calendar));
        mTaskBeginningTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar tmpCalendar = Calendar.getInstance();
                        tmpCalendar.set(0, 0, 0, hourOfDay, minute);
                        mTaskBeginningTimeEditText.setText(Helper.calendarToTimeString(tmpCalendar));
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                dialog.show();
            }
        });

        // Deadline
        mTaskDeadlineDateEditText = (EditText)rootView.findViewById(R.id.task_deadline_date_edit_text);
        mTaskDeadlineDateEditText.setInputType(InputType.TYPE_NULL);
        mTaskDeadlineDateEditText.setText(Helper.calendarToDateString(calendar));
        mTaskDeadlineDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar tmpCalendar = Calendar.getInstance();
                        tmpCalendar.set(year, monthOfYear, dayOfMonth);
                        mTaskDeadlineDateEditText.setText(Helper.calendarToDateString(tmpCalendar));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });
        mTaskDeadlineTimeEditText = (EditText)rootView.findViewById(R.id.task_deadline_time_edit_text);
        mTaskDeadlineTimeEditText.setInputType(InputType.TYPE_NULL);
        mTaskDeadlineTimeEditText.setText(Helper.calendarToTimeString(calendar));
        mTaskDeadlineTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar tmpCalendar = Calendar.getInstance();
                        tmpCalendar.set(0, 0, 0, hourOfDay, minute);
                        mTaskDeadlineTimeEditText.setText(Helper.calendarToTimeString(tmpCalendar));
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                dialog.show();
            }
        });

        // Data
        mGroupId = getArguments().getInt("id");

        // Create
        Button createButton = (Button)rootView.findViewById(R.id.create_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createTask();
            }
        });

        return rootView;
    }

    //----------------------------------------------------------------------------------------------------
    private void createTask() {
        final UserEntity entity = DataManager.getInstance().getUserItem();
        if (entity == null) {
            return;
        }

        Observable.just(entity.userId)
                .map(new Func1<Integer, ThriftTaskInfo>() {
                    @Override
                    public ThriftTaskInfo call(Integer userId) {
                        try {
                            TaskBookService.Client client = (TaskBookService.Client) ThriftManager.createClient(ThriftManager.ClientTypeEnum.CLIENT.toString());
                            if (client != null) {
                                JSONObject timeJsonData = new JSONObject();
                                timeJsonData.put("date", mTaskBeginningDateEditText.getText().toString());
                                timeJsonData.put("time", mTaskBeginningTimeEditText.getText().toString());
                                JSONObject deadlineJsonData = new JSONObject();
                                deadlineJsonData.put("date", mTaskDeadlineDateEditText.getText().toString());
                                deadlineJsonData.put("time", mTaskDeadlineTimeEditText.getText().toString());
                                ThriftTaskInfo info = new ThriftTaskInfo(0, mGroupId, entity.userName, mTaskNameEditText.getText().toString(), mTaskContentEditText.getText().toString(), timeJsonData.toString(), deadlineJsonData.toString(), 0);
                                return client.createTask(userId, info);
                            }
                        } catch (TException | JSONException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ThriftTaskInfo>() {
                    @Override
                    public void call(ThriftTaskInfo result) {
                        Toast toast = Toast.makeText(getContext(), "创建成功", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                        getFragmentManager().popBackStack();
                    }
                });
    }
}
