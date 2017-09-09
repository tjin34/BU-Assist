package net.bucssa.buassist.Ui.Classmates.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import net.bucssa.buassist.Base.BaseFragment;
import net.bucssa.buassist.Bean.Temp.Post;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.Classmates.Adapter.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by KimuraShin on 17/7/24.
 */

public class PostsFragment extends BaseFragment{

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.btnBottom)
    TextView btnBottom;

    private List<Post> posts = new ArrayList<>();
    private RecyclerViewAdapter adapter;

    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_recyclerview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        btnBottom.setText("创建新话题");
    }

    private void initData() {
        posts = new ArrayList<>();

        Post post = new Post("【求助】课上讲得例题有一处不懂","MA 101 A2",
                "张同学","有木有好兄弟给兄弟解解惑啊！麻烦啦！","几分钟前", 0);
        posts.add(post);

        post = new Post("【福利】找到了中文版教材，给大家分享一下","CS 210 A1",
                "王同学","大发现啊，MMP，这东西真的是救了我一命，给大家分享分享！","昨日",1);
        posts.add(post);

        post = new Post("【失物】课上捡到一本笔记本，失主去CAS认领哦","ES 105 C1",
                "李同学","一本笔记本，上面有名字，是某某某。我放到CAS了，去那里取吧","星期三",0);
        posts.add(post);

        post = new Post("【求助】这门课大家是否觉得值得上呢？","CS 591 A1",
                "周同学","有些纠结，不知道这课性价比如何，是不是值得去上呢，有上过的同学给点意见吗！多谢了！","6/25",1);
        posts.add(post);

        post = new Post("【求助】课上讲得例题有一处不懂","MA 101 A2",
                "张同学","有木有好兄弟给兄弟解解惑啊！麻烦啦！","几分钟前",0);
        posts.add(post);

        post = new Post("【求助】课上讲得例题有一处不懂","MA 101 A2",
                "张同学","有木有好兄弟给兄弟解解惑啊！麻烦啦！","几分钟前",0);
        posts.add(post);

        post = new Post("【求助】课上讲得例题有一处不懂","MA 101 A2",
                "张同学","有木有好兄弟给兄弟解解惑啊！麻烦啦！","几分钟前",0);
        posts.add(post);


        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new RecyclerViewAdapter(posts);
        recyclerView.setAdapter(adapter);
    }
}
