package ninja.taskbook.business.group;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ninja.taskbook.R;

//----------------------------------------------------------------------------------------------------
public class GroupJoinFragment extends Fragment {

    //----------------------------------------------------------------------------------------------------
    EditText mNameEditText;

    //----------------------------------------------------------------------------------------------------
    public GroupJoinFragment() {

    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.group_join, container, false);

        // EditText
        mNameEditText = (EditText)rootView.findViewById(R.id.name_edit_text);

        // Create
        Button joinButton = (Button)rootView.findViewById(R.id.join_button);
        joinButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                joinGroup();
            }
        });

        return rootView;
    }

    //----------------------------------------------------------------------------------------------------
    private void joinGroup() {
        // Todo
    }
}
