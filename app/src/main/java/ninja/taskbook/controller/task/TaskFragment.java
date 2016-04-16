package ninja.taskbook.controller.task;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ninja.taskbook.R;

//----------------------------------------------------------------------------------------------------
public class TaskFragment extends Fragment {

    //----------------------------------------------------------------------------------------------------
    private RecyclerView mRecyclerView;
    private List<TaskItem> mTaskItems = new ArrayList<>();

    //----------------------------------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.task, container, false);

        // Recycler Layout
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        // Decoration
        mRecyclerView.addItemDecoration(new TaskItemDecoration());

        // Adapter
        mRecyclerView.setAdapter(new TaskItemAdapter());

        // Load
        loadTaskItems();

        return rootView;
    }

    //----------------------------------------------------------------------------------------------------
    private void loadTaskItems() {
        TaskItem item0 = new TaskItem("xi xi xi", "boss");
        mTaskItems.add(item0);
        mTaskItems.add(item0);
        mTaskItems.add(item0);
        mTaskItems.add(item0);
        mTaskItems.add(item0);
        mTaskItems.add(item0);
        mTaskItems.add(item0);
        mTaskItems.add(item0);
        mTaskItems.add(item0);

        /*
        String[] words = {"Hello", "Hi", "Aloha"};
        Observable.just(words)
                .map(new Func1<String[], Integer>() {
                    @Override
                    public Integer call(String[] words) {
                        try {
                            HelloService.Client client = (HelloService.Client)ThriftManager.createClient(ThriftManager.ClientTypeEnum.CLIENT_HELLO.toString());
                            if (client != null)
                                return client.hi(words[0], words[1], words[2]);
                        } catch (TException e) {
                            e.printStackTrace();
                        }
                        return 0;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer result) {

                    }
                });
                */
    }

    // ContentItemHolder
    //----------------------------------------------------------------------------------------------------
    class TaskItemHolder extends RecyclerView.ViewHolder {
        public TextView itemTitleTextView;
        public TextView itemAuthorTextView;

        public TaskItemHolder(View itemView) {
            super(itemView);
            itemTitleTextView = (TextView)itemView.findViewById(R.id.item_title);
            itemAuthorTextView = (TextView)itemView.findViewById(R.id.item_author);
        }
    }

    // ContentItemAdapter
    //----------------------------------------------------------------------------------------------------
    class TaskItemAdapter extends RecyclerView.Adapter<TaskItemHolder> {
        public TaskItemAdapter() {

        }

        @Override
        public int getItemCount() {
            return mTaskItems.size();
        }

        @Override
        public TaskItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.task_item, null);
            return new TaskItemHolder(view);
        }

        @Override
        public void onBindViewHolder(TaskItemHolder holder, int position) {
            CardView cardView = (CardView)holder.itemView;
            switch (position % 4) {
                case 0:
                    cardView.setCardBackgroundColor(Color.parseColor("#6BC39A"));
                    break;
                case 1:
                    cardView.setCardBackgroundColor(Color.parseColor("#76BEE6"));
                    break;
                case 2:
                    cardView.setCardBackgroundColor(Color.parseColor("#E99A79"));
                    break;
                case 3:
                    cardView.setCardBackgroundColor(Color.parseColor("#657DC1"));
                    break;
            }

            holder.itemTitleTextView.setText(mTaskItems.get(position).getItemTitle());
            holder.itemAuthorTextView.setText(mTaskItems.get(position).getItemAuthor());
        }
    }
}
