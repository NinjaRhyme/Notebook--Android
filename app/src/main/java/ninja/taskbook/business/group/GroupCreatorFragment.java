package ninja.taskbook.business.group;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.thrift.TException;

import ninja.taskbook.R;
import ninja.taskbook.model.data.DataManager;
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
public class GroupCreatorFragment extends Fragment {

    //----------------------------------------------------------------------------------------------------
    EditText mNameEditText;

    //----------------------------------------------------------------------------------------------------
    public GroupCreatorFragment() {

    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.group_creator, container, false);

        // EditText
        mNameEditText = (EditText)rootView.findViewById(R.id.name_edit_text);

        // Create
        Button createButton = (Button)rootView.findViewById(R.id.create_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createGroup();
            }
        });

        return rootView;
    }

    //----------------------------------------------------------------------------------------------------
    private void createGroup() {
        UserEntity entity = DataManager.getInstance().getUserInfo();
        if (entity == null) {
            // Error
            return;
        }

        Observable.just(entity.userId)
                .map(new Func1<Integer, ThriftGroupInfo>() {
                    @Override
                    public ThriftGroupInfo call(Integer userId) {
                        try {
                            TaskBookService.Client client = (TaskBookService.Client) ThriftManager.createClient(ThriftManager.ClientTypeEnum.CLIENT.toString());
                            if (client != null) {
                                ThriftGroupInfo info = new ThriftGroupInfo(0, mNameEditText.getText().toString());
                                return client.createGroup(userId, info);
                            }
                        } catch (TException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ThriftGroupInfo>() {
                    @Override
                    public void call(ThriftGroupInfo result) {
                        Toast toast = Toast.makeText(getActivity(), "创建成功", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                });
    }
}
