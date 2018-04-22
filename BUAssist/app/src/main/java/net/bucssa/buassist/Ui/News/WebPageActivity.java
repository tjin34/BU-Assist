package net.bucssa.buassist.Ui.News;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import net.bucssa.buassist.Api.TuiSongAPI;
import net.bucssa.buassist.Base.BaseActivity;
import net.bucssa.buassist.Bean.BaseEntity;
import net.bucssa.buassist.Bean.News.NewsContent;
import net.bucssa.buassist.HttpUtils.RetrofitClient;
import net.bucssa.buassist.R;
import net.bucssa.buassist.Util.Logger;
import net.bucssa.buassist.Util.ToastUtils;

import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by KimuraShin on 17/7/6.
 */

public class WebPageActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.iv_back)
    ImageView iv_back;

    @BindView(R.id.webView)
    WebView webView;

    private String url;

    private int tid;

    private NewsContent newsContent;

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
//        tid = getIntent().getIntExtra("tid", 0);
        super.onCreate(savedInstanceState);

        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(url);

        getThreadContent();
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
    }

    private void getThreadContent(){
        Observable<BaseEntity<NewsContent>> observable = RetrofitClient.createService(TuiSongAPI.class)
                .getThreadContent(tid);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseEntity<NewsContent>>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        Logger.d();
                    }

                    @Override
                    public void onCompleted() {
                        Logger.d();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.d(e.toString());
                        ToastUtils.showToast(mContext, getString(R.string.snack_message_net_error));
                    }

                    @Override
                    public void onNext(BaseEntity<NewsContent> baseEntity) {
                        if (baseEntity.isSuccess()) {
                            newsContent = baseEntity.getDatas();
                            webView.loadDataWithBaseURL(null, newsContent.getMessage(),"text/html","utf-8", null);
                        } else {
                            ToastUtils.showToast(mContext, baseEntity.getError());
                        }
                        Logger.d();
                    }
                });
    }


}
