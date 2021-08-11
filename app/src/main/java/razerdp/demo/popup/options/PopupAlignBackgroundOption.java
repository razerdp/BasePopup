package razerdp.demo.popup.options;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import razerdp.basepopup.R;
import razerdp.basepopup.databinding.PopupOptionBackgroundAlignBinding;
import razerdp.demo.model.common.CommonBackgroundAlignInfo;
import razerdp.demo.utils.UIHelper;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;

/**
 * Created by 大灯泡 on 2019/9/21.
 */
public class PopupAlignBackgroundOption extends BaseOptionPopup<CommonBackgroundAlignInfo> {

    PopupOptionBackgroundAlignBinding mBinding;

    PopupSelectShowAnimate popupSelectShowAnimate;
    PopupSelectDismissAnimate popupSelectDismissAnimate;

    Animation maskShowAnimation;
    Animation maskDismissAnimation;

    long contentDuration = 500;

    public PopupAlignBackgroundOption(Context context) {
        super(context);
        setContentView(R.layout.popup_option_background_align);
        mBinding.progressOffsetx.setMax(UIHelper.getScreenWidth());
        mBinding.progressOffsety.setMax(UIHelper.getScreenHeight());
        mBinding.progressOffsetx.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mBinding.tvMaskOffsetx.setText("蒙层水平位移：" + progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mBinding.progressOffsety.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mBinding.tvMaskOffsety.setText("蒙层垂直位移：" + progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mBinding.progressContentDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mBinding.tvContentDuration.setText(String.format("ContentView动画时间：%dms",
                                                                     progress));
                    contentDuration = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mBinding.tvGo.setOnClickListener(v -> ok());
        mBinding.tvMaskAnimShow.setOnClickListener(v -> selectShow());
        mBinding.tvMaskAnimDismiss.setOnClickListener(v -> selectDismiss());
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        mBinding = PopupOptionBackgroundAlignBinding.bind(contentView);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return AnimationHelper.asAnimation()
                .withTranslation(TranslationConfig.FROM_BOTTOM)
                .toShow();
    }


    @Override
    protected Animation onCreateDismissAnimation() {
        return AnimationHelper.asAnimation()
                .withTranslation(TranslationConfig.TO_BOTTOM)
                .toDismiss();
    }


    void ok() {
        int gravity = Gravity.NO_GRAVITY;
        if (mBinding.checkLeft.isChecked()) gravity |= Gravity.LEFT;
        if (mBinding.checkTop.isChecked()) gravity |= Gravity.TOP;
        if (mBinding.checkRight.isChecked()) gravity |= Gravity.RIGHT;
        if (mBinding.checkBottom.isChecked()) gravity |= Gravity.BOTTOM;
        mInfo.alignGravity = gravity;
        mInfo.syncMaskAnimation = mBinding.checkSyncDuration.isChecked();
        mInfo.align = mBinding.checkAlignbackground.isChecked();
        mInfo.blur = mBinding.checkBlur.isChecked();
        mInfo.overlayMask = mBinding.checkOverlayMask.isChecked();
        mInfo.maskShowAnimation = maskShowAnimation;
        mInfo.maskDismissAnimation = maskDismissAnimation;
        mInfo.maskOffsetX = mBinding.progressOffsetx.getProgress();
        mInfo.maskOffsetY = mBinding.progressOffsety.getProgress();
        mInfo.contentDuration = contentDuration;
        dismiss();
    }

    void selectShow() {
        if (popupSelectShowAnimate == null) {
            popupSelectShowAnimate = new PopupSelectShowAnimate(getContext());
            popupSelectShowAnimate.setOnSelectedResultListener(new PopupSelectShowAnimate.OnSelectedResultListener() {
                @Override
                public void onSelected(@Nullable String name, @Nullable Animation animation) {
                    mBinding.tvMaskAnimShow.setText(name);
                    maskShowAnimation = animation;
                }
            });
        }
        popupSelectShowAnimate.showPopupWindow();
    }

    void selectDismiss() {
        if (popupSelectDismissAnimate == null) {
            popupSelectDismissAnimate = new PopupSelectDismissAnimate(getContext());
            popupSelectDismissAnimate.setOnSelectedResultListener(new PopupSelectDismissAnimate.OnSelectedResultListener() {
                @Override
                public void onSelected(@Nullable String name, @Nullable Animation animation) {
                    mBinding.tvMaskAnimDismiss.setText(name);
                    maskDismissAnimation = animation;
                }
            });
        }
        popupSelectDismissAnimate.showPopupWindow();
    }
}
