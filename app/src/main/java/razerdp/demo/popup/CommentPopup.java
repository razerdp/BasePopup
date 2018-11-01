package razerdp.demo.popup;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;

/**
 * Created by 大灯泡 on 2016/1/16.
 * 微信朋友圈评论弹窗
 */
public class CommentPopup extends BasePopupWindow implements View.OnClickListener {

    private ImageView mLikeAnimaView;
    private TextView mLikeText;

    private View mLikeClikcLayout;
    private View mCommentClickLayout;


    private OnCommentPopupClickListener mOnCommentPopupClickListener;

    private Handler mHandler;

    public CommentPopup(Context context) {
        super(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mHandler = new Handler();

        mLikeAnimaView = (ImageView) findViewById(R.id.iv_like);
        mLikeText = (TextView) findViewById(R.id.tv_like);

        mLikeClikcLayout =  findViewById(R.id.item_like);
        mCommentClickLayout =  findViewById(R.id.item_comment);

        mLikeClikcLayout.setOnClickListener(this);
        mCommentClickLayout.setOnClickListener(this);

        buildAnima();
        setBackgroundColor(Color.TRANSPARENT);
        setAllowDismissWhenTouchOutside(true);
        setAllowInterceptTouchEvent(false);
        setBlurBackgroundEnable(true);
    }


    private AnimationSet mAnimationSet;

    private void buildAnima() {
        ScaleAnimation mScaleAnimation = new ScaleAnimation(1f, 2f, 1f, 2f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mScaleAnimation.setDuration(200);
        mScaleAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        mScaleAnimation.setFillAfter(false);

        AlphaAnimation mAlphaAnimation = new AlphaAnimation(1, .2f);
        mAlphaAnimation.setDuration(400);
        mAlphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        mAlphaAnimation.setFillAfter(false);

        mAnimationSet = new AnimationSet(false);
        mAnimationSet.setDuration(400);
        mAnimationSet.addAnimation(mScaleAnimation);
        mAnimationSet.addAnimation(mAlphaAnimation);
        mAnimationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                }, 150);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void showPopupWindow(View v) {
        setOffsetX(-getWidth() - v.getWidth() / 2);
        setOffsetY((int) (-getHeight() / 1.5));
        super.showPopupWindow(v);
    }


    @Override
    protected Animation onCreateShowAnimation() {
        TranslateAnimation showAnima = new TranslateAnimation(dipToPx(180f), 0, 0, 0);
        showAnima.setInterpolator(new DecelerateInterpolator());
        showAnima.setDuration(350);
        return showAnima;
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        TranslateAnimation exitAnima = new TranslateAnimation(0, dipToPx(180f), 0, 0);
        exitAnima.setInterpolator(new DecelerateInterpolator());
        exitAnima.setDuration(350);
        return exitAnima;
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_comment);
    }
    //=============================================================Getter/Setter

    public OnCommentPopupClickListener getOnCommentPopupClickListener() {
        return mOnCommentPopupClickListener;
    }

    public void setOnCommentPopupClickListener(OnCommentPopupClickListener onCommentPopupClickListener) {
        mOnCommentPopupClickListener = onCommentPopupClickListener;
    }

    //=============================================================clickEvent
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_like:
                if (mOnCommentPopupClickListener != null) {
                    mOnCommentPopupClickListener.onLikeClick(v, mLikeText);
                    mLikeAnimaView.clearAnimation();
                    mLikeAnimaView.startAnimation(mAnimationSet);
                }
                break;
            case R.id.item_comment:
                if (mOnCommentPopupClickListener != null) {
                    mOnCommentPopupClickListener.onCommentClick(v);
                    dismiss();
                }
                break;
        }
    }

    //=============================================================InterFace
    public interface OnCommentPopupClickListener {
        void onLikeClick(View v, TextView likeText);

        void onCommentClick(View v);
    }
}
