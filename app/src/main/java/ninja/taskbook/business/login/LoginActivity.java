package ninja.taskbook.business.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ninja.taskbook.R;
import ninja.taskbook.model.data.DataManager;
import ninja.taskbook.model.database.DatabaseManager;
import ninja.taskbook.model.entity.UserEntity;
import ninja.taskbook.model.notification.NotificationService;

//----------------------------------------------------------------------------------------------------
public class LoginActivity extends AppCompatActivity {

    //----------------------------------------------------------------------------------------------------
    static final int REGISTER_ACTIVITY_CODE = 1;

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

        // Register
        Button registerButton = (Button)findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                register();
            }
        });

        // Login
        Button loginButton = (Button)findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                login();
            }
        });

        UserEntity entity = DatabaseManager.getUserEntity();
        if (entity != null) {
            DataManager.getInstance().setUserItem(entity);
            NotificationService.actionStart(getApplicationContext(), entity.userId);
            setResult(RESULT_OK, null);
            finish();
        }
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REGISTER_ACTIVITY_CODE:
                break;
            default:
                break;
        }
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {
        Log.d("Login", "back");
        System.exit(0);
    }

    //----------------------------------------------------------------------------------------------------
    private void login() {
        DataManager.getInstance().requestLogin(mNameEditText.getText().toString(), mPasswordEditText.getText().toString(),
                new DataManager.RequestCallback<Integer>() {
                    @Override
                    public void onResult(Integer result) {
                        if (0 < result) {
                            DatabaseManager.setUserId(result);
                            NotificationService.actionStart(getApplicationContext(), result);
                            setResult(RESULT_OK, null);
                            finish();
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), "用户名或密码错误", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }
                }
        );
    }

    //----------------------------------------------------------------------------------------------------
    private void register() {
        Intent register = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivityForResult(register, REGISTER_ACTIVITY_CODE);
    }
}
