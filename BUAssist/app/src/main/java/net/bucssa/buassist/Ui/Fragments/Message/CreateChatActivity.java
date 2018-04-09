package net.bucssa.buassist.Ui.Fragments.Message;

import android.os.Bundle;

import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.R;

/**
 * Created by Shinji on 2018/4/9.
 */

public class CreateChatActivity extends BaseActivity {

    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_chat;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {

    }

    @Override
    protected void initResAndListener() {
        super.initResAndListener();
    }
}
