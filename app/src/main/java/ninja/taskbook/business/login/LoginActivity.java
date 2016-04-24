package ninja.taskbook.business.login;

import android.os.Bundle;
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
public class LoginActivity extends AppCompatActivity {

    //----------------------------------------------------------------------------------------------------
    EditText mNameEditText;
    EditText mPasswordEditText;

    //----------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);

        // EditText
        mNameEditText = (EditText)findViewById(R.id.name_edit_text);
        mPasswordEditText = (EditText)findViewById(R.id.password_edit_text);

        // Button
        Button registerButton = (Button)findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                register();
            }
        });

        Button loginButton = (Button)findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                login();
            }
        });
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {
        Log.d("Login", "back");
        System.exit(0);
    }

    //----------------------------------------------------------------------------------------------------
    private void login() {
        Observable.just(0)
                .map(new Func1<Integer, Integer>() {
                    @Override
                    public Integer call(Integer userId) {
                        try {
                            TaskBookService.Client client = (TaskBookService.Client) ThriftManager.createClient(ThriftManager.ClientTypeEnum.CLIENT.toString());
                            if (client != null)
                                return client.login(mNameEditText.getText().toString(), mPasswordEditText.getText().toString());
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
                        if (result >= 0) {
                            UserEntity entity = new UserEntity();
                            entity.userId = result;
                            DataManager.getInstance().setUserInfo(entity);

                            setResult(RESULT_OK, null);
                            finish();
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), "用户名或密码错误", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }
                });
    }

    //----------------------------------------------------------------------------------------------------
    private void register() {
        // Todo
    }
}
