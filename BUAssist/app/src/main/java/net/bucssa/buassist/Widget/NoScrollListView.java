package net.bucssa.buassist.Widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by 吴文奇 on 2017/5/19.
 */
public class NoScrollListView extends ListView{

    private View footer;
    private ProgressBar progressBar;
    private TextView tv_load_more;

    public NoScrollListView(Context context) {
        super(context);
//        initView(context);
    }

    public NoScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        initView(context);
    }

    public NoScrollListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        initView(context);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

//    private void initView(Context context){
//        footer = LayoutInflater.from(context).inflate(R.layout.list_footer_more, null);
//        footer.setMinimumHeight(150);
//        footer.setMinimumWidth(WindowManager.LayoutParams.MATCH_PARENT);
//        this.addFooterView(footer);
//        progressBar = (ProgressBar) footer.findViewById(R.id.pull_to_refresh_progress);
//        tv_load_more = (TextView) footer.findViewById(R.id.load_more);
//    }
//
//    //正在加载数据，将listview底部提示文字置为"加载中。。。。"
//    public void onLoading(){
//        progressBar.setVisibility(VISIBLE);
//        tv_load_more.setText("加载中...");
//    }
//
//    //加载完毕，将listView底部提示文字改为"加载更多"
//    public void LoadingComplete(){
//        progressBar.setVisibility(GONE);
//        tv_load_more.setText("加载更多");
//    }
//
//    public void NoMoreData(){
//        progressBar.setVisibility(GONE);
//        tv_load_more.setText("没有更多了！");    }
//
//    private OnLoadMoreListener onLoadMoreListener;
//
//    public interface OnLoadMoreListener{
//        void OnLoadMore();
//    }
//
//
//    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
//        this.onLoadMoreListener = onLoadMoreListener;
//    }



}
