package net.bucssa.buassist.Ui.Classmates.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import net.bucssa.buassist.Base.BaseFragment;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.Classmates.Adapter.RecyclerGroupAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by KimuraShin on 17/7/24.
 */

public class GroupsFragment extends BaseFragment{

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.btnBottom)
    TextView btnBottom;


    private List<String> stringList = new ArrayList<>();
    private RecyclerGroupAdapter adapter;

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
        btnBottom.setText("创建新小组");
    }

    private void initData() {
        stringList = new ArrayList<>();

        stringList.add("没有拿不到的A，没有追不到的人");
        stringList.add("天霸动霸tua");
        stringList.add("普通的学习小组");
        stringList.add("霹雳无敌学习编队");
        stringList.add("欢天喜地小仙女们");
        stringList.add("啥也不说就是学");

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new RecyclerGroupAdapter(context, stringList);
        recyclerView.setAdapter(adapter);
    }
}
