package ninja.taskbook.view.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ninja.taskbook.R;
import ninja.taskbook.model.data.DataManager;
import ninja.taskbook.model.entity.UserEntity;

//----------------------------------------------------------------------------------------------------
public class ProfileFragment extends Fragment {

    //----------------------------------------------------------------------------------------------------
    TextView mIdTextView;
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

        // Id
        mIdTextView = (TextView)rootView.findViewById(R.id.id_text_view);
        mIdTextView.setText("id");

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
        DataManager.getInstance().requestUserItem(
                new DataManager.RequestCallback<UserEntity>() {
                    @Override
                    public void onResult(UserEntity result) {
                        mIdTextView.setText(String.valueOf(result.userId));
                        mNameTextView.setText(result.userName);
                        mNicknameTextView.setText(result.userNickname);
                    }
                }
        );
    }
}
