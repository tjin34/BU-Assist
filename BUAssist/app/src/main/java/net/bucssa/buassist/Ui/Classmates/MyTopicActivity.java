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
import net.bucssa.buassist.Bean.Temp.Post;
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

        Post post = new Post("【求助】课上讲得例题有一处不懂","MA 101 A2",
                "张同学","有木有好兄弟给兄弟解解惑啊！麻烦啦！","几分钟前", 0);
        postList.add(post);

        post = new Post("【福利】找到了中文版教材，给大家分享一下","CS 210 A1",
                "王同学","大发现啊，MMP，这东西真的是救了我一命，给大家分享分享！","昨日",1);
        postList.add(post);

        post = new Post("【失物】课上捡到一本笔记本，失主去CAS认领哦","ES 105 C1",
                "李同学","一本笔记本，上面有名字，是某某某。我放到CAS了，去那里取吧","星期三",0);
        postList.add(post);

        post = new Post("【求助】这门课大家是否觉得值得上呢？","CS 591 A1",
                "周同学","有些纠结，不知道这课性价比如何，是不是值得去上呢，有上过的同学给点意见吗！多谢了！","6/25",1);
        postList.add(post);

        post = new Post("【求助】课上讲得例题有一处不懂","MA 101 A2",
                "张同学","有木有好兄弟给兄弟解解惑啊！麻烦啦！","几分钟前",0);
        postList.add(post);

        post = new Post("【求助】课上讲得例题有一处不懂","MA 101 A2",
                "张同学","有木有好兄弟给兄弟解解惑啊！麻烦啦！","几分钟前",0);
        postList.add(post);

        post = new Post("【求助】课上讲得例题有一处不懂","MA 101 A2",
                "张同学","有木有好兄弟给兄弟解解惑啊！麻烦啦！","几分钟前",0);
        postList.add(post);


        myAdapter = new PostListAdapter(mContext, postList);
        listView.setAdapter(myAdapter);



    }
}
