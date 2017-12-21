package net.bucssa.buassist.Ui.Classmates;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.Bean.Classmate.Class;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.Classmates.Adapter.ClassListAdapter;
import net.bucssa.buassist.Widget.LuluRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by KimuraShin on 17/7/29.
 */

public class FindClassActivity extends BaseActivity {

    @BindView(R.id.header)
    RelativeLayout header;

    @BindView(R.id.iv_back)
    ImageView iv_back;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.search_textView)
    LinearLayout fakeSearchBox;

    @BindView(R.id.search_editText)
    LinearLayout realSearchBox;

    @BindView(R.id.tv_cancel)
    TextView tv_cancel;

    @BindView(R.id.luluRefreshListView)
    LuluRefreshListView listView;

    private List<Class> classList = new ArrayList<>();
    private ClassListAdapter myAdapter;

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
        tv_title.setText("寻找课程");
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
        classList = new ArrayList<>();

        Class classItem = new Class("OW 210","Theory of TeamFights","OverWatch","Blizzard", 25, 4);
        classList.add(classItem);

        classItem = new Class("HS 103","Deck Formation","HearthStone","Kolento", 233, 23);
        classList.add(classItem);

        classItem = new Class("DotA 350","Professional DotA","Defense of the Ancient","LGD.BurNIng", 25, 4);
        classList.add(classItem);

        classItem = new Class("CS 101","Intro to Computer Science","Computer Science","Wayne Snyder", 25, 4);
        classList.add(classItem);

        classItem = new Class("MA 105","Intro to Mathematics","Applied Math","Tony Stark", 102, 23);
        classList.add(classItem);

        classItem = new Class("ES 105","Intro to Earth Science","Environmental Science","SpiderMan", 49, 8);
        classList.add(classItem);

        myAdapter = new ClassListAdapter(mContext, classList);
        listView.setAdapter(myAdapter);
    }
}
