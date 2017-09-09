package net.bucssa.buassist.Ui.Fragments.Explore;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;

import net.bucssa.buassist.Base.BaseFragment;
import net.bucssa.buassist.Enum.Enum;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Ui.Fragments.Explore.Adapter.ExploreAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by KimuraShin on 17/8/17.
 */

public class ExploreFragment extends BaseFragment{

    @BindView(R.id.mRefreshLayout)
    MaterialRefreshLayout mRefreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<String> mDatas = new ArrayList<>();
    private ExploreAdapter myAdapter;

    private int state = Enum.STATE_NORMAL;


    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_explore;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initData();
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {

        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refreshData();
            }
        });

    }

    private void initData() {
        getExplore();
    }

    private void refreshData() {
        state = Enum.STATE_REFRESH;
        getExplore();
    }

    private void changeByState() {
        switch (state) {
            case Enum.STATE_NORMAL:
                myAdapter = new ExploreAdapter(context, mDatas);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(myAdapter);
                break;
            case Enum.STATE_REFRESH:
//                myAdapter.clearData();
//                try {
//                    Thread.sleep(3000);
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
                myAdapter.addItems(mDatas, myAdapter.getmDatas().size());
                mRefreshLayout.finishRefresh();
                break;
        }
    }

    private void getExplore(){
        mDatas = new ArrayList<>();

        mDatas.add("1");

        changeByState();
    }
}
