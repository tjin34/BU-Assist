package net.bucssa.buassist.Widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.bucssa.buassist.R;

/**
 * Created by Shinji on 2018/4/7.
 */

public class CustomListViewWithLoadMore extends ListView{

    /**
     * Footer及相关View
     */
    private View footer;
    private TextView tv_load_more;
    private ProgressBar progressBar;

    /**
     * 上拉加载更多Bool值
     */
    private boolean isLoadingMore = false;
    private boolean canLoadMore = true;
    boolean atLast;

    /**
     * 上拉加载数值
     */
    float lastDownY;

    /**
     * LoadMore Interface
     */
    public onLoadMoreListener onLoadMoreListener;

    public CustomListViewWithLoadMore(Context context) {
        super(context);
        initView(context);
    }

    public CustomListViewWithLoadMore(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CustomListViewWithLoadMore(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2
                , MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    /**
     * 初始FooterView
     * @param context
     */
    private void initView(Context context){
        footer = LayoutInflater.from(context).inflate(R.layout.list_footer_more, null);
        footer.setMinimumWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.addFooterView(footer);
        progressBar = (ProgressBar) footer.findViewById(R.id.pull_to_refresh_progress);
        tv_load_more = (TextView) footer.findViewById(R.id.load_more);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        super.onInterceptTouchEvent(ev);
        if (MotionEvent.ACTION_MOVE == ev.getAction() || MotionEvent.ACTION_DOWN == ev.getAction()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        View lastVisibleItemView;
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                /* 当用户触碰屏幕，获取当前Y值 */
//                lastDownY = event.getY();
//                atLast = !canScrollVertically(-1);
//                //保证事件可往下传递
            case MotionEvent.ACTION_MOVE:
//                /* 确认当前操作是否为上拉还是下拉，如果Y值大于之前的Y值，并且scrollY为0，则为下拉 */
//                boolean isScrollDown = event.getY() - lastDownY > 0 && this.getScrollY() == 0;
//                //允许父控件拦截，即不允许父控件拦截设为false
//                if (!isScrollDown && atLast && canLoadMore && !isLoadingMore) {
//                    /* 如果是上拉，在最底部，并且可以加载更多，并不在加载中，便开始加载 */
//                    onLoading();
//                    onLoadMoreListener.onLoadMore();
//                } else {
//                    return false;
//                }
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return false;
    }

    /**
     * LoadMore Interface
     */
    public interface onLoadMoreListener{
        void onLoadMore();
    }

    /**
     * 设定LoadMoreListener
     * @param listener
     */
    public void setOnLoadMoreListener(onLoadMoreListener listener) {
        this.onLoadMoreListener = listener;
    }

    //正在加载数据，将listview底部提示文字置为"加载中。。。。"
    public void onLoading(){
        progressBar.setVisibility(VISIBLE);
        tv_load_more.setText("加载中...");
        canLoadMore = false;
        isLoadingMore = true;
    }

    //加载完毕，将listView底部提示文字改为"加载更多"
    public void LoadingComplete(){
        progressBar.setVisibility(GONE);
        tv_load_more.setText("加载更多");
        Animation fade_out = AnimationUtils.loadAnimation(getContext(), R.anim.lulu_anim_out);
        tv_load_more.startAnimation(fade_out);
        isLoadingMore = false;
        canLoadMore = true;
    }

    //没有更多数据了
    public void NoMoreData(){
        progressBar.setVisibility(GONE);
        tv_load_more.setText("没有更多了！");
        Animation fade_out = AnimationUtils.loadAnimation(getContext(), R.anim.lulu_anim_out);
        tv_load_more.startAnimation(fade_out);
        isLoadingMore = false;
        canLoadMore = false;
    }
}
