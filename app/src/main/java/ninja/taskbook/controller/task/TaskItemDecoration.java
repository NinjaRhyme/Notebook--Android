package ninja.taskbook.controller.task;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

//----------------------------------------------------------------------------------------------------
public class TaskItemDecoration extends RecyclerView.ItemDecoration {

    //----------------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------------
    public TaskItemDecoration() {
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(10, 10, 10, 10);
    }
}
