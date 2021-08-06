package razerdp.demo.popup.options;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.SeekBar;

import razerdp.basepopup.R;
import razerdp.basepopup.databinding.PopupOptionBlurBinding;
import razerdp.demo.model.common.CommonBlurInfo;
import razerdp.demo.utils.NumberFormatUtil;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;

/**
 * Created by 大灯泡 on 2020/3/12.
 * 模糊控制
 */
public class PopupBlurDemoOption extends BaseOptionPopup<CommonBlurInfo> {
    PopupOptionBlurBinding mBinding;

    float scaled = 0.15f;
    float blurRadius = 10;
    int blurInTime = 500;
    int blurOutTime = 500;

    public PopupBlurDemoOption(Context context) {
        super(context);
        setContentView(R.layout.popup_option_blur);
        init();
    }

    private void init() {
        mBinding.tvGo.setOnClickListener(v -> ok());
        mBinding.progressBlur.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                blurRadius = progress;
                mBinding.tvBlur.setText(String.format("模糊度：%s%%",
                                                      NumberFormatUtil.format2(((float) progress / seekBar.getMax()) * 100)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mBinding.progressScale.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                scaled = (float) progress / seekBar.getMax();
                mBinding.tvScale.setText(String.format("预缩放系数：%s",
                                                       NumberFormatUtil.format2(scaled)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mBinding.progressBlurIn.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                blurInTime = progress;
                mBinding.tvBlurIn.setText(String.format("淡入时间：%sms", String.valueOf(progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mBinding.progressBlurOut.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                blurOutTime = progress;
                mBinding.tvBlurOut.setText(String.format("淡出时间：%sms", String.valueOf(progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return AnimationHelper.asAnimation()
                .withTranslation(TranslationConfig.FROM_TOP)
                .toShow();
    }


    @Override
    protected Animation onCreateDismissAnimation() {
        return AnimationHelper.asAnimation()
                .withTranslation(TranslationConfig.TO_TOP)
                .toDismiss();
    }

    @Override
    public void onViewCreated(View contentView) {
        mBinding = PopupOptionBlurBinding.bind(contentView);
    }


    void ok() {
        mInfo.scaled = scaled;
        mInfo.blurRadius = blurRadius;
        mInfo.blurInTime = blurInTime;
        mInfo.blurOutTime = blurOutTime;
        mInfo.blurEnable = mBinding.checkBlur.isChecked();
        mInfo.blurAnchorView = mBinding.checkBlurAnchor.isChecked();
        dismiss();
    }
}
