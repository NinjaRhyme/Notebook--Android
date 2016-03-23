package ninja.taskbook.controller.task;

//----------------------------------------------------------------------------------------------------
public class TaskItem {

    //----------------------------------------------------------------------------------------------------
    private String mItemTitle;
    private String mItemAuthor;

    //----------------------------------------------------------------------------------------------------
    public TaskItem(String itemTitle, String itemAuthor) {
        mItemTitle = itemTitle;
        mItemAuthor = itemAuthor;
    }

    public String getItemTitle() {
        return mItemTitle;
    }

    public String getItemAuthor() {
        return mItemAuthor;
    }
}
