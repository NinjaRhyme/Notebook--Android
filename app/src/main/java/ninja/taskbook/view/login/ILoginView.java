package ninja.taskbook.view.login;

import android.content.Context;

//----------------------------------------------------------------------------------------------------
public interface ILoginView {

    //----------------------------------------------------------------------------------------------------
    Context getContext();

    //----------------------------------------------------------------------------------------------------
    void onLoginSuccess(int userId);
    void onLoginFail();
}
