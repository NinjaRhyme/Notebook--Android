package ninja.taskbook.business.login;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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

//----------------------------------------------------------------------------------------------------
public class RegisterActivity extends AppCompatActivity {

    //----------------------------------------------------------------------------------------------------
    EditText mNameEditText;
    EditText mPasswordEditText;
    EditText mPasswordAgainEditText;
    EditText mNicknameEditText;

    //----------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // EditText
        mNameEditText = (EditText)findViewById(R.id.name_edit_text);
        mPasswordEditText = (EditText)findViewById(R.id.password_edit_text);
        mPasswordAgainEditText = (EditText)findViewById(R.id.password_again_edit_text);
        mNicknameEditText = (EditText)findViewById(R.id.nickname_edit_text);

        // Register
        Button registerButton = (Button)findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                register();
            }
        });

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED, null);
        finish();
    }

    //----------------------------------------------------------------------------------------------------
    private void register() {
        if (mNameEditText.getText().toString().isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(), "用户名不能为空", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }
        if (mPasswordEditText.getText().toString().isEmpty() || mPasswordAgainEditText.getText().toString().isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }
        if (mNicknameEditText.getText().toString().isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(), "昵称不能为空", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }
        if (!mPasswordEditText.getText().toString().equals(mPasswordAgainEditText.getText().toString())) {
            Toast toast = Toast.makeText(getApplicationContext(), "两次密码不相同", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }

        Observable.just(0)
                .map(new Func1<Integer, Integer>() {
                    @Override
                    public Integer call(Integer userId) {
                        try {
                            TaskBookService.Client client = (TaskBookService.Client) ThriftManager.createClient(ThriftManager.ClientTypeEnum.CLIENT.toString());
                            if (client != null) {
                                return client.signup(mNameEditText.getText().toString(), mPasswordEditText.getText().toString(), mNicknameEditText.getText().toString());
                            }
                        } catch (TException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer result) {
                        if (0 < result) {
                            UserEntity entity = new UserEntity();
                            entity.userId = result;
                            DataManager.getInstance().setUserInfo(entity);

                            setResult(RESULT_OK, null);
                            finish();
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), "用户名已被占用", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }
                });
    }
}
