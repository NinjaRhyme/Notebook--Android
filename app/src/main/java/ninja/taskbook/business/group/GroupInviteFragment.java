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
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class GroupInviteFragment extends Fragment {

    //----------------------------------------------------------------------------------------------------
    EditText mIdEditText;
    int mGroupId = 0;

    //----------------------------------------------------------------------------------------------------
    public GroupInviteFragment() {

    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.group_invite, container, false);

        // EditText
        mIdEditText = (EditText)rootView.findViewById(R.id.id_edit_text);

        // Create
        Button inviteButton = (Button)rootView.findViewById(R.id.invite_button);
        inviteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inviteToGroup();
            }
        });

        // Data
        mGroupId = getArguments().getInt("id");

        return rootView;
    }

    //----------------------------------------------------------------------------------------------------
    private void inviteToGroup() {
        UserEntity entity = DataManager.getInstance().getUserItem();
        if (entity == null) {
            return;
        }

        Observable.just(entity.userId)
                .map(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer userId) {
                        try {
                            TaskBookService.Client client = (TaskBookService.Client) ThriftManager.createClient(ThriftManager.ClientTypeEnum.CLIENT.toString());
                            if (client != null) {
                                return client.invite(userId, mGroupId, Integer.parseInt(mIdEditText.getText().toString()));
                            }
                        } catch (TException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean result) {
                        if (result) {
                            Toast toast = Toast.makeText(getContext(), "Success", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else {
                            Toast toast = Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }
                });
    }
}
