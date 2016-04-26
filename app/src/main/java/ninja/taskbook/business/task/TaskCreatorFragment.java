package ninja.taskbook.business.task;

import android.app.DatePickerDialog;
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
import android.widget.Toast;

import org.apache.thrift.TException;

import java.util.Calendar;

import ninja.taskbook.R;
import ninja.taskbook.model.data.DataManager;
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
public class TaskCreatorFragment extends Fragment {

    //----------------------------------------------------------------------------------------------------
    EditText mTaskNameEditText;
    EditText mTaskContentEditText;
    EditText mTaskTimeEditText;

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

        // Time
        mTaskTimeEditText = (EditText)rootView.findViewById(R.id.task_time_edit_text);
        mTaskTimeEditText.setInputType(InputType.TYPE_NULL);
        final Calendar calendar = Calendar.getInstance();
        mTaskTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        mTaskTimeEditText.setText(DateFormat.format("yyy-MM-dd", calendar));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

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


    //----------------------------------------------------------------------------------------------------
    private void createTask() {
        final UserEntity entity = DataManager.getInstance().getUserInfo();
        if (entity == null) {
            // Error
            return;
        }

        Observable.just(entity.userId)
                .map(new Func1<Integer, ThriftTaskInfo>() {
                    @Override
                    public ThriftTaskInfo call(Integer userId) {
                        try {
                            TaskBookService.Client client = (TaskBookService.Client) ThriftManager.createClient(ThriftManager.ClientTypeEnum.CLIENT.toString());
                            if (client != null) {
                                ThriftTaskInfo info = new ThriftTaskInfo(0, 0, entity.userName, mTaskNameEditText.getText().toString(), mTaskContentEditText.getText().toString(), mTaskTimeEditText.getText().toString(), 0.5);
                                return client.createTask(userId, info);
                            }
                        } catch (TException e) {
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
