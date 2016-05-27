package ninja.taskbook.view.notification;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

//----------------------------------------------------------------------------------------------------
public class NotificationItemDecoration extends RecyclerView.ItemDecoration {

    //----------------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------------
    public NotificationItemDecoration(Context context) {

    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.set(10, 10, 10, 10);
    }
}
