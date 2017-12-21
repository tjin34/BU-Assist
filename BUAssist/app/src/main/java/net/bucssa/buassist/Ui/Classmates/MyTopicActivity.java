package net.bucssa.buassist.Ui.Classmates;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.Bean.Classmate.Post;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.Classmates.Adapter.PostListAdapter;
import net.bucssa.buassist.Widget.LuluRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by KimuraShin on 17/7/29.
 */

public class MyTopicActivity extends BaseActivity {

    @BindView(R.id.header)
    RelativeLayout header;

    @BindView(R.id.iv_back)
    ImageView iv_back;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.search_textView)
    LinearLayout fakeSearchBox;

    @BindView(R.id.tv_search)
    TextView tv_search;

    @BindView(R.id.et_search)
    EditText et_search;

    @BindView(R.id.search_editText)
    LinearLayout realSearchBox;

    @BindView(R.id.tv_cancel)
    TextView tv_cancel;

    @BindView(R.id.luluRefreshListView)
    LuluRefreshListView listView;

    private List<Post> postList = new ArrayList<>();
    private PostListAdapter myAdapter;

    private final static int REFRESH_COMPLETE = 0;

    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case REFRESH_COMPLETE:
                    listView.setOnRefreshComplete();
                    myAdapter.notifyDataSetChanged();
                    listView.setSelection(0);
                    break;

                default:
                    break;
            }
        };
    };


    @Override
    protected int getLayoutId() {
        return R.layout.activity_classmate_listview;
    }

    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
    }

    @Override
    protected void initResAndListener() {
        tv_title.setText("我的话题");
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_search.setText(R.string.search_topic);
        et_search.setHint(R.string.search_topic);
        fakeSearchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                header.setVisibility(View.GONE);
                fakeSearchBox.setVisibility(View.GONE);
                realSearchBox.setVisibility(View.VISIBLE);
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                header.setVisibility(View.VISIBLE);
                fakeSearchBox.setVisibility(View.VISIBLE);
                realSearchBox.setVisibility(View.GONE);
            }
        });

        listView.setOnBaiduRefreshListener(new LuluRefreshListView.OnBaiduRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                            mHandler.sendEmptyMessage(REFRESH_COMPLETE);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

    }

    private void initData() {
        postList = new ArrayList<>();

        myAdapter = new PostListAdapter(mContext, postList);
        listView.setAdapter(myAdapter);



    }
}
