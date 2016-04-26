package ninja.taskbook.business.group;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ninja.taskbook.R;

//----------------------------------------------------------------------------------------------------
public class GroupDetailFragment extends Fragment {

    //----------------------------------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.group_detail, container, false);

        // Load
        loadChart();

        return rootView;
    }

    //----------------------------------------------------------------------------------------------------
    private void loadChart() {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.chart_frame_layout, new GroupTaskLineFragment())
                .commit();
    }
}