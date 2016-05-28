package ninja.taskbook.presenter.login;

import android.content.Context;

import ninja.taskbook.model.data.DataManager;
import ninja.taskbook.model.database.DatabaseManager;
import ninja.taskbook.model.entity.UserEntity;
import ninja.taskbook.model.notification.NotificationService;
import ninja.taskbook.view.login.ILoginView;

//----------------------------------------------------------------------------------------------------
public class LoginPresenter {

    //----------------------------------------------------------------------------------------------------
    private ILoginView mLoginView;

    //----------------------------------------------------------------------------------------------------
    public LoginPresenter(ILoginView view) {
        mLoginView = view;
    }

    //----------------------------------------------------------------------------------------------------
    public void checkLogin() {
        UserEntity entity = DatabaseManager.getUserEntity();
        if (entity != null) {
            DataManager.getInstance().setUserItem(entity);
            NotificationService.actionStart(mLoginView.getContext(), entity.userId);
            mLoginView.onLoginSuccess(entity.userId);
        }
    }

    //----------------------------------------------------------------------------------------------------
    public void login(String name, String password) {
        DataManager.getInstance().requestLogin(name, password,
                new DataManager.RequestCallback<Integer>() {
                    @Override
                    public void onResult(Integer result) {
                        if (0 < result) {
                            DatabaseManager.setUserId(result);
                            NotificationService.actionStart(mLoginView.getContext(), result);
                            mLoginView.onLoginSuccess(result);
                        } else {
                            mLoginView.onLoginFail();
                        }
                    }
                }
        );
    }
}
