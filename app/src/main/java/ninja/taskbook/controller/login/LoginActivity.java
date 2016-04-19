package ninja.taskbook.controller.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

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
public class LoginActivity extends AppCompatActivity {

    //----------------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);

        // button
        Button registerButton = (Button)findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                login();
            }
        });

        Button loginButton = (Button)findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                register();
            }
        });
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
                                return client.login("name", "password");
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
                        setResult(RESULT_OK, null);
                        finish();
                    }
                });
    }

    //----------------------------------------------------------------------------------------------------
    private void register() {
        // Todo
        setResult(RESULT_OK, null);
        finish();
    }
}
