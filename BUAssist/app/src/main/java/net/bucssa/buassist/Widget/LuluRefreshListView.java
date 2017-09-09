package net.bucssa.buassist.Widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import net.bucssa.buassist.R;

/**
 * Created by Hankkin on 16/4/10.
 */
public class LuluRefreshListView extends ListView implements AbsListView.OnScrollListener{
    private static final int DONE = 0;      //刷新完毕状态
    private static final int PULL_TO_REFRESH = 1;   //下拉刷新状态
    private static final int RELEASE_TO_REFRESH = 2;    //释放状态
    private static final int REFRESHING = 3;    //正在刷新状态
    private static final int RATIO = 3;
    private RelativeLayout headView;    //下拉刷新头
    private int headViewHeight; //头高度
    private float startY;   //开始Y坐标
    private float offsetY;  //Y轴偏移量
    private OnBaiduRefreshListener mOnRefreshListener;  //刷新接口
    private int state;  //状态值
    private int mFirstVisibleItem;  //第一项可见item索引
    private boolean isRecord;   //是否记录
    private boolean isEnd;  //是否结束
    private boolean isRefreable;    //是否刷新

    private ImageView ivRider;  //骑手图片组件
    private ImageView ivSun,ivBack1,ivBack2;    //太阳、背景图片1、背景图片2
    private Animation sunAnimation;  //太阳动画
    private Animation backAnimation1,backAnimation2;    //两张背景图动画

    public LuluRefreshListView(Context context) {
        super(context);
        init(context);
    }

    public LuluRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LuluRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public interface OnBaiduRefreshListener{
        void onRefresh();
    }

    /**
     * 回调接口，想实现下拉刷新的listview实现此接口
     * @param onRefreshListener
     */
    public void setOnBaiduRefreshListener(OnBaiduRefreshListener onRefreshListener){
        mOnRefreshListener = onRefreshListener;
        isRefreable = true;
    }

    /**
     * 刷新完毕，从主线程发送过来，并且改变headerView的状态和文字动画信息
     */
    public void setOnRefreshComplete(){
        //一定要将isEnd设置为true，以便于下次的下拉刷新
        isEnd = true;
        state = DONE;

        changeHeaderViewByState();
    }

    private void init(Context context) {
        //关闭view的OverScroll
        setOverScrollMode(OVER_SCROLL_NEVER);
        setOnScrollListener(this);
        //加载头布局
        headView = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.widget_lulu_headview,this,false);
        //测量头布局
        measureView(headView);
        //给ListView添加头布局
        addHeaderView(headView);
        //设置头文件隐藏在ListView的第一项
        headViewHeight = headView.getMeasuredHeight();
        headView.setPadding(0, -headViewHeight, 0, 0);

        //获取头布局图片组件
        ivRider = (ImageView) headView.findViewById(R.id.iv_rider);
        ivSun = (ImageView) headView.findViewById(R.id.ivsun);
        ivBack1 = (ImageView) headView.findViewById(R.id.iv_back1);
        ivBack2 = (ImageView) headView.findViewById(R.id.iv_back2);
        //获取动画
        sunAnimation = AnimationUtils.loadAnimation(context, R.anim.simple_rotate);
        LinearInterpolator lir = new LinearInterpolator();
        sunAnimation.setInterpolator(lir);

        backAnimation1 = AnimationUtils.loadAnimation(context, R.anim.back_move_1);
        backAnimation2 = AnimationUtils.loadAnimation(context, R.anim.back_move_2);

        state = DONE;
        isEnd = true;
        isRefreable = false;


    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
    }
    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mFirstVisibleItem = firstVisibleItem;
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        if (isEnd) {//如果现在时结束的状态，即刷新完毕了，可以再次刷新了，在onRefreshComplete中设置
//            if (isRefreable) {//如果现在是可刷新状态   在setOnMeiTuanListener中设置为true
//                switch (ev.getAction()){
//                    //用户按下
//                    case MotionEvent.ACTION_DOWN:
//                        //如果当前是在listview顶部并且没有记录y坐标
//                        if (mFirstVisibleItem == 0 && !isRecord) {
//                            //将isRecord置为true，说明现在已记录y坐标
//                            isRecord = true;
//                            //将当前y坐标赋值给startY起始y坐标
//                            startY = ev.getY();
//                        }
//                        break;
//                    //用户滑动
//                    case MotionEvent.ACTION_MOVE:
//                        //再次得到y坐标，用来和startY相减来计算offsetY位移值
//                        float tempY = ev.getY();
//                        //再起判断一下是否为listview顶部并且没有记录y坐标
//                        if (mFirstVisibleItem == 0 && !isRecord) {
//                            isRecord = true;
//                            startY = tempY;
//                        }
//                        //如果当前状态不是正在刷新的状态，并且已经记录了y坐标
//                        if (state!=REFRESHING && isRecord ) {
//                            //计算y的偏移量
//                            offsetY = tempY - startY;
//                            //计算当前滑动的高度
//                            float currentHeight = (-headViewHeight+offsetY/3);
//                            //用当前滑动的高度和头部headerView的总高度进行比 计算出当前滑动的百分比 0到1
//                            float currentProgress = 1+currentHeight/headViewHeight;
//                            //如果当前百分比大于1了，将其设置为1，目的是让第一个状态的椭圆不再继续变大
//                            if (currentProgress>=1) {
//                                currentProgress = 1;
//                            }
//                            //如果当前的状态是放开刷新，并且已经记录y坐标
//                            if (state == RELEASE_TO_REFRESH && isRecord) {
//
//                                setSelection(0);
//                                //如果当前滑动的距离小于headerView的总高度
//                                if (-headViewHeight+offsetY/RATIO<0) {
//                                    //将状态置为下拉刷新状态
//                                    state = PULL_TO_REFRESH;
//                                    //根据状态改变headerView，主要是更新动画和文字等信息
//                                    changeHeaderByState(state);
//                                    //如果当前y的位移值小于0，即为headerView隐藏了
//                                }else if (offsetY<=0) {
//                                    //将状态变为done
//                                    state = DONE;
//                                    stopAnim();
//                                    //根据状态改变headerView，主要是更新动画和文字等信息
//                                    changeHeaderByState(state);
//                                }
//                            }
//                            //如果当前状态为下拉刷新并且已经记录y坐标
//                            if (state == PULL_TO_REFRESH && isRecord) {
//                                setSelection(0);
//                                //如果下拉距离大于等于headerView的总高度
//                                if (-headViewHeight+offsetY/RATIO>=0) {
//                                    //将状态变为放开刷新
//                                    state = RELEASE_TO_REFRESH;
//                                    //根据状态改变headerView，主要是更新动画和文字等信息
//                                    changeHeaderByState(state);
//                                    //如果当前y的位移值小于0，即为headerView隐藏了
//                                }else if (offsetY<=0) {
//                                    //将状态变为done
//                                    state = DONE;
//                                    //根据状态改变headerView，主要是更新动画和文字等信息
//                                    changeHeaderByState(state);
//                                }
//                            }
//                            //如果当前状态为done并且已经记录y坐标
//                            if (state == DONE && isRecord) {
//                                //如果位移值大于0
//                                if (offsetY>=0) {
//                                    //将状态改为下拉刷新状态
//                                    state = PULL_TO_REFRESH;
//                                    changeHeaderByState(state);
//                                }
//                            }
//                            //如果为下拉刷新状态
//                            if (state == PULL_TO_REFRESH) {
//                                //则改变headerView的padding来实现下拉的效果
//                                headView.setPadding(0,(int)(-headViewHeight+offsetY/RATIO) ,0,0);
//                            }
//                            //如果为放开刷新状态
//                            if (state == RELEASE_TO_REFRESH) {
//                                //改变headerView的padding值
//                                headView.setPadding(0,(int)(-headViewHeight+offsetY/RATIO) ,0, 0);
//                            }
//                        }
//                        break;
//                    //当用户手指抬起时
//                    case MotionEvent.ACTION_UP:
//                        //如果当前状态为下拉刷新状态
//                        if (state == PULL_TO_REFRESH) {
//                            //平滑的隐藏headerView
////                            headView.setPadding(0, -headViewHeight, 0, 0);
////                            this.smoothScrollToPosition(-headViewHeight);
//                            this.smoothScrollBy((int)(-headViewHeight+offsetY/RATIO)-headViewHeight, 200);
//                            //根据状态改变headerView
//                            changeHeaderByState(state);
//                        }
//                        //如果当前状态为放开刷新
//                        if (state == RELEASE_TO_REFRESH) {
//                            //平滑的滑到正好显示headerView
//                            this.smoothScrollBy((int)(-headViewHeight+offsetY/RATIO), 200);
////                            headView.setPadding(0, 0, 0, 0);
//                            //将当前状态设置为正在刷新
//                            state = REFRESHING;
//                            //回调接口的onRefresh方法
//                            mOnRefreshListener.onRefresh();
//                            //根据状态改变headerView
//                            changeHeaderByState(state);
//                        }
//                        //这一套手势执行完，一定别忘了将记录y坐标的isRecord改为false，以便于下一次手势的执行
//                        isRecord = false;
//                        break;
//                }
//
//            }
//        }
//        return super.onTouchEvent(ev);
//    }
//
//    /**
//     * 根据状态改变headerView的动画和文字显示
//     * @param state
//     */
//    private void changeHeaderByState(int state){
//        switch (state) {
//            case DONE://如果的隐藏的状态
//                //设置headerView的padding为隐藏
//                headView.setPadding(0, -headViewHeight, 0, 0);
//                startAnim();
//                break;
//            case RELEASE_TO_REFRESH://当前状态为放开刷新
//                break;
//            case PULL_TO_REFRESH://当前状态为下拉刷新
//                startAnim();
//                break;
//            case REFRESHING://当前状态为正在刷新
//                break;
//            default:
//                break;
//        }
//    }

    private boolean isBack;

    /**
     * 原作者的，我没改动，请读者自行优化。
     */
    public boolean onTouchEvent(MotionEvent event) {

        if (isEnd) {//如果现在时结束的状态，即刷新完毕了，可以再次刷新了，在onRefreshComplete中设置
            if (isRefreable) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        if (mFirstVisibleItem == 0 && !isRecord) {
                            isRecord = true;
                            startY = (int) event.getY();
                        }
                        break;

                    case MotionEvent.ACTION_UP:

                        if (state != REFRESHING && isRecord) {
                            if (state == DONE) {

                            }
                            if (state == PULL_TO_REFRESH) {
                                state = DONE;
                                this.smoothScrollBy((int) (-headViewHeight+offsetY/RATIO)-headViewHeight, 500);
                                changeHeaderViewByState();
                            }
                            if (state == RELEASE_TO_REFRESH) {
                                state = REFRESHING;
                                this.smoothScrollBy((int)(-headViewHeight+offsetY/RATIO), 500);
                                changeHeaderViewByState();
                                mOnRefreshListener.onRefresh();
                            }
                        }

                        isRecord = false;
                        isBack = false;

                        break;

                    case MotionEvent.ACTION_MOVE:
                        int tempY = (int) event.getY();

                        if (!isRecord && mFirstVisibleItem == 0) {
                            isRecord = true;
                            startY = tempY;
                        }

                        if (state != REFRESHING && isRecord) {
                            offsetY = tempY - (int) startY;

                            // 保证在设置padding的过程中，当前的位置一直是在head，
                            // 否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动
                            // 可以松手去刷新了
                            if (state == RELEASE_TO_REFRESH) {

                                setSelection(0);

                                // 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
                                if (((tempY - startY) / RATIO < headViewHeight)
                                        && (tempY - startY) > 0) {
                                    state = PULL_TO_REFRESH;
                                    changeHeaderViewByState();
                                }
                                // 一下子推到顶了
                                else if (tempY - startY <= 0) {
                                    state = DONE;
                                    changeHeaderViewByState();
                                }
                                // 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
                            }
                            // 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
                            if (state == PULL_TO_REFRESH) {

                                setSelection(0);

                                // 下拉到可以进入RELEASE_TO_REFRESH的状态
                                if ((tempY - startY) / RATIO >= (headViewHeight)/2) {
                                    state = RELEASE_TO_REFRESH;
                                    isBack = true;
                                    changeHeaderViewByState();
                                } else if (tempY - startY <= 0) {
                                    state = DONE;
                                    changeHeaderViewByState();
                                }
                            }

                            if (state == DONE) {
                                if (tempY - startY > 0) {
                                    state = PULL_TO_REFRESH;
                                    changeHeaderViewByState();
                                }
                            }

                            if (state == PULL_TO_REFRESH) {
                                headView.setPadding(0, -1 * headViewHeight
                                        + (tempY - (int) startY) / RATIO, 0, 0);
                            }

                            if (state == RELEASE_TO_REFRESH) {
                                headView.setPadding(0, (tempY - (int) startY) / RATIO
                                        - headViewHeight, 0, 0);
                            }
                        }
                        break;
                }
            }
        }

        return super.onTouchEvent(event);
    }

    /**
     * 当HeadView状态改变时候，调用该方法，以更新界面
     *
     * @date 2013-11-20 下午4:29:44
     * @change JohnWatson
     * @version 1.0
     */
    private void changeHeaderViewByState() {
        switch (state) {
            case RELEASE_TO_REFRESH:
//                mArrowImageView.setVisibility(View.VISIBLE);
//                mProgressBar.setVisibility(View.GONE);
//                mTipsTextView.setVisibility(View.VISIBLE);
//                mLastUpdatedTextView.setVisibility(View.VISIBLE);
//
//                mArrowImageView.clearAnimation();
//                mArrowImageView.startAnimation(mArrowAnim);
//                // 松开刷新
//                mTipsTextView.setText(R.string.p2refresh_release_refresh);

                break;
            case PULL_TO_REFRESH:
                startAnim();
//                mProgressBar.setVisibility(View.GONE);
//                mTipsTextView.setVisibility(View.VISIBLE);
//                mLastUpdatedTextView.setVisibility(View.VISIBLE);
//                mArrowImageView.clearAnimation();
//                mArrowImageView.setVisibility(View.VISIBLE);
                // 是由RELEASE_To_REFRESH状态转变来的
                if (isBack) {
                    isBack = false;
//                    mArrowImageView.clearAnimation();
//                    mArrowImageView.startAnimation(mArrowReverseAnim);
//                    // 下拉刷新
//                    mTipsTextView.setText(R.string.p2refresh_pull_to_refresh);
//                } else {
                    // 下拉刷新
//                    mTipsTextView.setText(R.string.p2refresh_pull_to_refresh);
                }
                break;

            case REFRESHING:
                headView.setPadding(0, 0, 0, 0);

                // 华生的建议： 实际上这个的setPadding可以用动画来代替。我没有试，但是我见过。其实有的人也用Scroller可以实现这个效果，
                // 我没时间研究了，后期再扩展，这个工作交给小伙伴你们啦~ 如果改进了记得发到我邮箱噢~
                // 本人邮箱： xxzhaofeng5412@gmail.com

//                mProgressBar.setVisibility(View.VISIBLE);
//                mArrowImageView.clearAnimation();
//                mArrowImageView.setVisibility(View.GONE);
//                // 正在刷新...
//                mTipsTextView.setText(R.string.p2refresh_doing_head_refresh);
//                mLastUpdatedTextView.setVisibility(View.VISIBLE);

                break;
            case DONE:
                headView.setPadding(0, -1 * headViewHeight, 0, 0);
                stopAnim();
                // 此处可以改进，同上所述。

//                mProgressBar.setVisibility(View.GONE);
//                mArrowImageView.clearAnimation();
//                mArrowImageView.setImageResource(R.drawable.arrow);
//                // 下拉刷新
//                mTipsTextView.setText(R.string.p2refresh_pull_to_refresh);
//                mLastUpdatedTextView.setVisibility(View.VISIBLE);

                break;
        }
    }

    /**
     * 测量View
     * @param child
     */
    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    /**
     * 开启动画
     */
    public void startAnim(){
        ivBack1.startAnimation(backAnimation1);
        ivBack2.startAnimation(backAnimation2);
        ivSun.startAnimation(sunAnimation);
    }

    /**
     * 关闭动画
     */
    public void stopAnim(){
        ivBack1.clearAnimation();
        ivBack2.clearAnimation();
        ivSun.clearAnimation();
    }
}
