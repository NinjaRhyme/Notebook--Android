package ninja.taskbook.presenter;

import android.content.ContentResolver;

import ninja.taskbook.model.database.DatabaseManager;
import ninja.taskbook.view.IMainView;

//----------------------------------------------------------------------------------------------------
public class MainPresenter {

    //----------------------------------------------------------------------------------------------------
    private IMainView mMainView;

    //----------------------------------------------------------------------------------------------------
    public MainPresenter(IMainView view) {
        mMainView = view;
    }

    //----------------------------------------------------------------------------------------------------
    public void initDatabase(ContentResolver resolver) {
        DatabaseManager.init(resolver);
    }

    //----------------------------------------------------------------------------------------------------
    public void logout() {
        DatabaseManager.clear();
    }
}
