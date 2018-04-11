package net.bucssa.buassist.Ui.Webview;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.R;

import butterknife.BindView;


/**
 * Created by Shinji on 2018/4/10.
 */

public class WebViewActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.iv_back)
    ImageView iv_back;

    @BindView(R.id.webView)
    WebView webView;

    private String url;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_webpage;
    }

    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        url = getIntent().getStringExtra("url");
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initResAndListener() {
        tv_title.setText("详情");
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(url);
    }

}
