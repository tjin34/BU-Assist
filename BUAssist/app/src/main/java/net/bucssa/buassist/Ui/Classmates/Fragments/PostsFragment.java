package net.bucssa.buassist.Ui.Classmates.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import net.bucssa.buassist.Base.BaseFragment;
import net.bucssa.buassist.Bean.Classmate.Post;
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

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new RecyclerViewAdapter(posts);
        recyclerView.setAdapter(adapter);
    }
}
