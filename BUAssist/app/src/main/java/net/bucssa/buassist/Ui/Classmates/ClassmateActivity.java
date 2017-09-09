package net.bucssa.buassist.Ui.Classmates;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.R;

import butterknife.BindView;

/**
 * Created by KimuraShin on 17/7/29.
 */

public class ClassmateActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView iv_back;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.ll_findClass)
    LinearLayout ll_findClass;

    @BindView(R.id.ll_myGroup)
    LinearLayout ll_myGroup;

    @BindView(R.id.ll_myCollection)
    LinearLayout ll_myCollection;

    @BindView(R.id.ll_myTopic)
    LinearLayout ll_myTopic;

    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_classmates;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initResAndListener() {
        tv_title.setText("大腿课友");
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ll_findClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FindClassActivity.class);
                startActivity(intent);
            }
        });

        ll_myGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MyGroupActivity.class);
                startActivity(intent);
            }
        });

        ll_myTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MyTopicActivity.class);
                startActivity(intent);
            }
        });

        ll_myCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MyCollectionActivity.class);
                startActivity(intent);
            }
        });



    }
}
