package razerdp.demo.popup.options;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatSeekBar;
import butterknife.BindView;
import butterknife.OnClick;
import razerdp.basepopup.R;
import razerdp.demo.model.common.CommonBlurInfo;
import razerdp.demo.utils.ButterKnifeUtil;
import razerdp.demo.utils.NumberFormatUtil;
import razerdp.demo.widget.DPTextView;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;

/**
 * Created by 大灯泡 on 2020/3/12.
 * 模糊控制
 */
public class PopupBlurDemoOption extends BaseOptionPopup<CommonBlurInfo> {
    @BindView(R.id.tv_blur)
    TextView mTvBlur;
    @BindView(R.id.progress_blur)
    AppCompatSeekBar mProgressBlur;
    @BindView(R.id.tv_scale)
    TextView mTvScale;
    @BindView(R.id.progress_scale)
    AppCompatSeekBar mProgressScale;
    @BindView(R.id.check_blur)
    AppCompatCheckBox mCheckBlur;
    @BindView(R.id.check_blur_anchor)
    AppCompatCheckBox mCheckBlurAnchor;
    @BindView(R.id.tv_go)
    DPTextView mTvGo;
    @BindView(R.id.tv_blur_in)
    TextView mTvBlurIn;
    @BindView(R.id.progress_blur_in)
    AppCompatSeekBar mProgressBlurIn;
    @BindView(R.id.tv_blur_out)
    TextView mTvBlurOut;
    @BindView(R.id.progress_blur_out)
    AppCompatSeekBar mProgressBlurOut;


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
        mProgressBlur.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                blurRadius = progress;
                mTvBlur.setText(String.format("模糊度：%s%%",
                                              NumberFormatUtil.format2(((float) progress / seekBar.getMax()) * 100)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mProgressScale.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                scaled = (float) progress / seekBar.getMax();
                mTvScale.setText(String.format("预缩放系数：%s", NumberFormatUtil.format2(scaled)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mProgressBlurIn.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                blurInTime = progress;
                mTvBlurIn.setText(String.format("淡入时间：%sms", String.valueOf(progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mProgressBlurOut.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                blurOutTime = progress;
                mTvBlurOut.setText(String.format("淡出时间：%sms", String.valueOf(progress)));
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
        super.onViewCreated(contentView);
        ButterKnifeUtil.bind(this, contentView);
    }


    @OnClick(R.id.tv_go)
    void ok() {
        mInfo.scaled = scaled;
        mInfo.blurRadius = blurRadius;
        mInfo.blurInTime = blurInTime;
        mInfo.blurOutTime = blurOutTime;
        mInfo.blurEnable = mCheckBlur.isChecked();
        mInfo.blurAnchorView = mCheckBlurAnchor.isChecked();
        dismiss();
    }
}
