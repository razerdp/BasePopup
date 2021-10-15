package razerdp.demo.popup;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.QuickPopupBuilder;
import razerdp.basepopup.QuickPopupConfig;
import razerdp.basepopup.R;
import razerdp.basepopup.databinding.ActivityMyTestBinding;
import razerdp.basepopup.databinding.PopupMyTestBinding;
import razerdp.demo.base.baseactivity.BaseBindingActivity;
import razerdp.util.animation.AlphaConfig;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.ScaleConfig;

/**
 * Created by 大灯泡 on 2021/7/27.
 */
public class MyTestActivity extends BaseBindingActivity<ActivityMyTestBinding> {

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public ActivityMyTestBinding onCreateViewBinding(LayoutInflater layoutInflater) {
        return ActivityMyTestBinding.inflate(layoutInflater);
    }

    @Override
    protected void onInitView(View decorView) {
        mBinding.viewTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test();
//                new TestPopup(self()).setMaxWidth(350).setPopupGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL).showPopupWindow(view);
            }
        });
    }

    void test() {
        QuickPopupBuilder.with(this)
                .contentView(R.layout.popup_demo)
                .config(new QuickPopupConfig()
                                .autoMirrorEnable(true)
                                .withShowAnimation(AnimationHelper.asAnimation()
                                                           .withScale(ScaleConfig.CENTER)
                                                           .toShow())
                                .withDismissAnimator(AnimationHelper.asAnimator()
                                                             .withAlpha(AlphaConfig.OUT)
                                                             .toDismiss())
                                .gravity(Gravity.CENTER)
                                .backgroundColor(Color.BLACK)
                                .maskOffsetX(100))
                .show();
    }

    static class TestPopup extends BasePopupWindow {
        PopupMyTestBinding mBinding;

        public TestPopup(Context context) {
            super(context);
            setContentView(R.layout.popup_my_test);
            setFitSize(false);
        }

        @Override
        public void onViewCreated(View contentView) {
            mBinding = PopupMyTestBinding.bind(contentView);
        }
    }
}
