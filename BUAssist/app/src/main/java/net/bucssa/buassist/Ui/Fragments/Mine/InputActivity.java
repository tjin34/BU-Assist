package net.bucssa.buassist.Ui.Fragments.Mine;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.R;
import net.bucssa.buassist.UserSingleton;
import net.bucssa.buassist.Widget.SuperEditText;

import butterknife.BindView;

/**
 * Created by KimuraShin on 17/8/20.
 */

public class InputActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_save)
    TextView tv_save;

    @BindView(R.id.iv_back)
    ImageView iv_back;

    @BindView(R.id.name_inputBox)
    RelativeLayout name_inputBox;

    @BindView(R.id.tv_nameCount)
    TextView tv_nameCount;

    @BindView(R.id.et_name)
    SuperEditText et_name;

    @BindView(R.id.intro_inputBox)
    RelativeLayout intro_inputBox;

    @BindView(R.id.et_intro)
    SuperEditText et_intro;

    @BindView(R.id.tv_introCount)
    TextView tv_introCount;

    private int inputType = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_input;
    }

    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        inputType = getIntent().getIntExtra("InputType", 0);
        super.onCreate(savedInstanceState);
        initView(inputType);
    }

    @Override
    protected void initResAndListener() {
        tv_save.setVisibility(View.VISIBLE);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int wordLeft = 16 - SuperEditText.SMLengthFilter.getSequenceLength(s);
                tv_nameCount.setText(String.valueOf(wordLeft));
                if (wordLeft >= 0 ) {
                    tv_nameCount.setTextColor(getResources().getColor(R.color.tv_grey));
                } else {
                    tv_nameCount.setTextColor(getResources().getColor(R.color.material_red));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_intro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int wordLeft = 100 - SuperEditText.SMLengthFilter.getSequenceLength(s);
                tv_introCount.setText(String.valueOf(wordLeft));
                if (wordLeft >= 0 ) {
                    tv_introCount.setTextColor(getResources().getColor(R.color.tv_grey));
                } else {
                    tv_introCount.setTextColor(getResources().getColor(R.color.material_red));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void initView(int type) {
        switch (type) {
            case 0:
                tv_title.setText("用户名");
                name_inputBox.setVisibility(View.VISIBLE);
                et_name.setText(UserSingleton.USERINFO.getUsername());
                break;
            case 1:
                tv_title.setText("真名");
                name_inputBox.setVisibility(View.VISIBLE);
                et_name.setText(UserSingleton.USERINFO.getRealname());
                break;
            case 2:
                tv_title.setText("个人介绍");
                intro_inputBox.setVisibility(View.VISIBLE);
                et_intro.setText(UserSingleton.USERINFO.getBio());
                break;
        }
    }
}
