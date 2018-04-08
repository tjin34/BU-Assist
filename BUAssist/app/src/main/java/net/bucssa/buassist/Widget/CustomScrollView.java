package net.bucssa.buassist.Widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * Created by Shinji on 2018/4/7.
 */
public class CustomScrollView extends ScrollView {

    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        super.onInterceptTouchEvent(ev);
        if (MotionEvent.ACTION_MOVE == ev.getAction()) {
            return true;
        }
        return false;
    }

    float lastDownY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastDownY = event.getY();
                //保证事件可往下传递
                parentRequestDisallowInterceptTouchEvent(true);
                return true;
            case MotionEvent.ACTION_MOVE:
                boolean isScrollDown = event.getY() - lastDownY > 0 && this.getScrollY() == 0;
                boolean atTop = getChildAt(0) != null && getChildAt(0).getTop() == 0;
                LinearLayout lastVisibleView = (LinearLayout) getChildAt(getChildCount() - 1);
                CustomListViewWithLoadMore lastVisibleListView = (CustomListViewWithLoadMore) lastVisibleView.getChildAt(1);
                View lastVisibleItem = lastVisibleListView.getChildAt(lastVisibleListView.getChildCount() - 1);
                boolean atLast = lastVisibleItem != null && lastVisibleItem.getBottom() == lastVisibleListView.getHeight();
                //允许父控件拦截，即不允许父控件拦截设为false
                if (isScrollDown && atTop) {
                    parentRequestDisallowInterceptTouchEvent(false);
                    return false;
                } else if (!isScrollDown && atLast) {
                    /* 如果是上拉，在最底部，并且可以加载更多，并不在加载中，便开始加载 */
                    onScrollToBottomDetectedListener.onScrollToBottom();
                } else {
                    //不允许父控件拦截，即不允许父控件拦截设为true
                    parentRequestDisallowInterceptTouchEvent(true);
                    return true;
                }
            case MotionEvent.ACTION_UP:
                //保证事件可往下传递
                parentRequestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return false;
    }

    /**
     * 是否允许父控件拦截事件
     * @param disallowIntercept
     */
    private void parentRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        ViewParent viewParent = getParent();
        if (null == viewParent) {
            return;
        }
        viewParent.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    public interface onScrollToBottomDetectedListener{
        void onScrollToBottom();
    }

    private onScrollToBottomDetectedListener onScrollToBottomDetectedListener;

    public void setOnScrollToBottomDetectedListener(CustomScrollView.onScrollToBottomDetectedListener listener) {
        this.onScrollToBottomDetectedListener = listener;
    }
}
