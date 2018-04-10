package net.bucssa.buassist.Widget;

/**
 * Created by shinji on 2018/4/4.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.bucssa.buassist.R;


/**
 * 条形进度条
 * @author chenwenbiao
 * @date 2014-4-14 上午10:40:34
 * @version V1.0
 */
public class StripProgressBar extends FrameLayout{

    private String TAG = getClass().getName();

    private LinearLayout p1;
    private LinearLayout p2;
    private TextView textView;


    private float progressBarWidth = 0;//进度条的宽，使用这个宽来计算进度的
    private String progressBarTips = null;//进度条上显示的字
    private float progress = 0;

    private Context context = null;

    public StripProgressBar(Context context) {
        this(context , null , 0);
    }

    public StripProgressBar(Context context, AttributeSet attrs) {
        this(context , attrs , 0);
    }

    public StripProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context , attrs, defStyle);
        this.context = context;

//      inflate(context, R.layout.strip_progress_barl, null);
        LayoutInflater.from(context).inflate(R.layout.strip_progress_bar,this,true);

//      TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
//                R.styleable.StripProgressBar);
//
        //获取自定义属性和默认值
//      progressBarWidth = mTypedArray.getDimension(R.styleable.StripProgressBar_progressBarWidth, 200);
//      CLog.d(TAG , "====== StripProgressBar     progressBarWidth======>" + progressBarWidth);
//      progressBarHeight = mTypedArray.getDimension(R.styleable.StripProgressBar_progressBarHeight, 40);
//      progressBarTips = mTypedArray.getString(R.styleable.StripProgressBar_progressTips);
//      mTypedArray.recycle();


//      rootLayout = (RelativeLayout)findViewById(R.id.progress_bar1_root_layout);

        p1 = (LinearLayout)findViewById(R.id.strip_progress_bar1_layout);
        p2 = (LinearLayout)findViewById(R.id.strip_progress_bar2_layout);
        textView = (TextView)findViewById(R.id.strip_progress_text_tv);
        textView.setText(progressBarTips);


    }

    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom){
        super.onLayout(changed, left, top, right, bottom);
        progressBarWidth = p1.getWidth();
        setProgress(progress);
        Log.d(TAG , "====== onLayout     progressBarWidth======>" + progressBarWidth);
    }


    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        progressBarWidth = p1.getWidth();;
        setProgress(progress);
        Log.d(TAG , "====== onWindowFocusChanged     progressBarWidth======>" + progressBarWidth);
    }


    /**
     * 进度，从一到100数字来表示
     * @param progress
     */
    public void setProgress(float progress){
        this.progress = progress;
        p2.setVisibility(View.VISIBLE);
        int w = (int) (progressBarWidth*(this.progress/100.0));
        Log.d(TAG , "======W=========>" + w);

        /**
         * 宽度太小，显示的圆弧就不成圆弧，所以这里搞一个最小值给它
         */
        Log.d(TAG , "progress radius ==========> " + context.getResources().getDimension(R.dimen.progress_radius));
        int min = (int) (context.getResources().getDimension(R.dimen.progress_radius) * 2);
        Log.d(TAG , "min ==========> " + min);
        if( w < min / 2  ){//太小就不显示
            w = 0;
        }
        else if(w < min) {//小于一半，不到一个最小长度，就设置成最小长度
            w = min;
        }

        android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams)p2.getLayoutParams();
        params.width = w;
        p2.setLayoutParams(params);
    }

    /**
     * 设置进度条显示的数字
     * @param text
     */
    public void setText(String text){
        textView.setText(text);
    }
}