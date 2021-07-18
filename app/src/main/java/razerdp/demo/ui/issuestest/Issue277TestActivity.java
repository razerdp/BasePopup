package razerdp.demo.ui.issuestest;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import butterknife.BindView;
import butterknife.OnClick;
import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.widget.DPTextView;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.Direction;
import razerdp.util.animation.TranslationConfig;

/**
 * Created by 大灯泡 on 2020/7/15.
 * <p>
 * https://github.com/razerdp/BasePopup/issues/277#issuecomment-658724674
 */
public class Issue277TestActivity extends BaseActivity {
    @BindView(R.id.showPopBt)
    DPTextView mShowPopBt;

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public int contentViewLayoutId() {
        return R.layout.activity_issue_277;
    }

    @Override
    protected void onInitView(View decorView) {

    }


    @OnClick(R.id.showPopBt)
    public void onViewClicked() {
        new Issue277Popup(this).showPopupWindow();
    }

    class Issue277Popup extends BasePopupWindow {

        public Issue277Popup(Context context) {
            super(context);
            setContentView(R.layout.popup_issue_277);
            setPopupGravity(Gravity.BOTTOM);
            setBlurBackgroundEnable(false);
            setOutSideDismiss(false);
            setOutSideTouchable(true);
            setAdjustInputMode(R.id.feedbackTv, FLAG_KEYBOARD_ALIGN_TO_VIEW);
        }


        @Override
        protected Animation onCreateShowAnimation() {
            return AnimationHelper.asAnimation()
                    .withTranslation(new TranslationConfig()
                            .from(Direction.BOTTOM))
                    .toShow();
        }

        @Override
        protected Animation onCreateDismissAnimation() {
            return AnimationHelper.asAnimation()
                    .withTranslation(new TranslationConfig()
                            .to(Direction.BOTTOM))
                    .toDismiss();
        }
    }
}
