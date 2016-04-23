package ninja.taskbook.controller.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.thrift.TException;

import ninja.taskbook.R;
import ninja.taskbook.model.network.thrift.manager.ThriftManager;
import ninja.taskbook.model.network.thrift.service.TaskBookService;
import ninja.taskbook.model.network.thrift.service.ThriftUserInfo;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

//----------------------------------------------------------------------------------------------------
public class ProfileFragment extends Fragment {

    //----------------------------------------------------------------------------------------------------
    TextView mNameTextView;
    TextView mNicknameTextView;

    //----------------------------------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile, container, false);

        // Name
        mNameTextView = (TextView)rootView.findViewById(R.id.name_text_view);
        mNameTextView.setText("name");

        // Nickname
        mNicknameTextView = (TextView)rootView.findViewById(R.id.nickname_text_view);
        mNicknameTextView.setText("nickname");

        // Load
        loadProfileData();

        return rootView;
    }

    //----------------------------------------------------------------------------------------------------
    private void loadProfileData() {
        Observable.just(1) // Todo: id
                .map(new Func1<Integer, ThriftUserInfo>() {
                    @Override
                    public ThriftUserInfo call(Integer userId) {
                        try {
                            TaskBookService.Client client = (TaskBookService.Client)ThriftManager.createClient(ThriftManager.ClientTypeEnum.CLIENT.toString());
                            if (client != null)
                                return client.userInfo(userId);
                        } catch (TException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ThriftUserInfo>() {
                    @Override
                    public void call(ThriftUserInfo result) {
                        mNameTextView.setText(result.getUserName());
                        mNicknameTextView.setText(result.getUserNickname());
                    }
                });
    }
}
