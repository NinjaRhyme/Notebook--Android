package ninja.taskbook.controller.content;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ninja.taskbook.R;

//----------------------------------------------------------------------------------------------------
public class ContentManager {

    //----------------------------------------------------------------------------------------------------
    private AppCompatActivity mActivity;
    private RecyclerView mContentView;
    private List<ContentItem> mContentItems;
    private ContentItemAdapter mContentItemAdapter;

    //----------------------------------------------------------------------------------------------------
    public ContentManager(AppCompatActivity activity, RecyclerView contentView, List<ContentItem> contentItems) {
        mActivity = activity;
        mContentView = contentView;
        mContentItems = contentItems;

        // Layout
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mActivity, 1);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mContentView.setLayoutManager(gridLayoutManager);

        // Adapter
        mContentItemAdapter = new ContentItemAdapter();
        mContentView.setAdapter(mContentItemAdapter);
    }

    // ContentItemHolder
    //----------------------------------------------------------------------------------------------------
    class ContentItemHolder extends RecyclerView.ViewHolder {
        public TextView itemTextView;

        public ContentItemHolder(View itemView) {
            super(itemView);
            itemTextView = (TextView)itemView.findViewById(R.id.item_text);
        }
    }

    // ContentItemAdapter
    //----------------------------------------------------------------------------------------------------
    class ContentItemAdapter extends RecyclerView.Adapter<ContentItemHolder> {
        public ContentItemAdapter() {

        }

        @Override
        public int getItemCount() {
            return mContentItems.size();
        }

        @Override
        public ContentItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mActivity.getLayoutInflater().inflate(R.layout.content_item, null);
            return new ContentItemHolder(view);
        }

        @Override
        public void onBindViewHolder(ContentItemHolder holder, int position) {
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

            holder.itemTextView.setText(mContentItems.get(position).getItemText());
        }
    }
}
