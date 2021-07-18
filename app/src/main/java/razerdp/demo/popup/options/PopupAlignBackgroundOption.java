package razerdp.demo.popup.options;

import android.content.Context;
import android.view.Gravity;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatSeekBar;

import butterknife.BindView;
import butterknife.OnClick;
import razerdp.basepopup.R;
import razerdp.demo.model.common.CommonBackgroundAlignInfo;
import razerdp.demo.utils.UIHelper;
import razerdp.demo.widget.DPTextView;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;

/**
 * Created by 大灯泡 on 2019/9/21.
 */
public class PopupAlignBackgroundOption extends BaseOptionPopup<CommonBackgroundAlignInfo> {

    @BindView(R.id.check_left)
    AppCompatCheckBox mCheckLeft;
    @BindView(R.id.check_top)
    AppCompatCheckBox mCheckTop;
    @BindView(R.id.check_right)
    AppCompatCheckBox mCheckRight;
    @BindView(R.id.check_bottom)
    AppCompatCheckBox mCheckBottom;
    @BindView(R.id.check_alignbackground)
    AppCompatCheckBox mCheckAlignbackground;
    @BindView(R.id.check_blur)
    AppCompatCheckBox mCheckBlur;
    @BindView(R.id.tv_go)
    DPTextView mTvGo;
    @BindView(R.id.check_sync_duration)
    AppCompatCheckBox mCheckSyncDuration;
    @BindView(R.id.tv_mask_offsetx)
    TextView mTvMaskOffsetx;
    @BindView(R.id.progress_offsetx)
    AppCompatSeekBar mProgressOffsetx;
    @BindView(R.id.tv_mask_offsety)
    TextView mTvMaskOffsety;
    @BindView(R.id.progress_offsety)
    AppCompatSeekBar mProgressOffsety;
    @BindView(R.id.layout_select_show)
    LinearLayout mLayoutSelectShow;
    @BindView(R.id.tv_mask_anim_show)
    TextView mTvMaskAnimShow;
    @BindView(R.id.layout_select_dismiss)
    LinearLayout mLayoutSelectDismiss;
    @BindView(R.id.tv_mask_anim_dismiss)
    TextView mTvMaskAnimDismiss;
    @BindView(R.id.check_overlay_mask)
    AppCompatCheckBox mCheckOverlayMask;
    @BindView(R.id.tv_content_duration)
    TextView mTvContentDuration;
    @BindView(R.id.progress_content_duration)
    AppCompatSeekBar mProgressContentDuration;

    PopupSelectShowAnimate popupSelectShowAnimate;
    PopupSelectDismissAnimate popupSelectDismissAnimate;

    Animation maskShowAnimation;
    Animation maskDismissAnimation;

    long contentDuration = 500;

    public PopupAlignBackgroundOption(Context context) {
        super(context);
        setContentView(R.layout.popup_option_background_align);
        mProgressOffsetx.setMax(UIHelper.getScreenWidth());
        mProgressOffsety.setMax(UIHelper.getScreenHeight());
        mProgressOffsetx.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mTvMaskOffsetx.setText("蒙层水平位移：" + progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mProgressOffsety.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mTvMaskOffsety.setText("蒙层垂直位移：" + progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mProgressContentDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mTvContentDuration.setText(String.format("ContentView动画时间：%dms", progress));
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


    @OnClick(R.id.tv_go)
    void ok() {
        int gravity = Gravity.NO_GRAVITY;
        if (mCheckLeft.isChecked()) gravity |= Gravity.LEFT;
        if (mCheckTop.isChecked()) gravity |= Gravity.TOP;
        if (mCheckRight.isChecked()) gravity |= Gravity.RIGHT;
        if (mCheckBottom.isChecked()) gravity |= Gravity.BOTTOM;
        mInfo.alignGravity = gravity;
        mInfo.syncMaskAnimation = mCheckSyncDuration.isChecked();
        mInfo.align = mCheckAlignbackground.isChecked();
        mInfo.blur = mCheckBlur.isChecked();
        mInfo.overlayMask = mCheckOverlayMask.isChecked();
        mInfo.maskShowAnimation = maskShowAnimation;
        mInfo.maskDismissAnimation = maskDismissAnimation;
        mInfo.maskOffsetX = mProgressOffsetx.getProgress();
        mInfo.maskOffsetY = mProgressOffsety.getProgress();
        mInfo.contentDuration = contentDuration;
        dismiss();
    }

    @OnClick(R.id.layout_select_show)
    void selectShow() {
        if (popupSelectShowAnimate == null) {
            popupSelectShowAnimate = new PopupSelectShowAnimate(getContext());
            popupSelectShowAnimate.setOnSelectedResultListener(new PopupSelectShowAnimate.OnSelectedResultListener() {
                @Override
                public void onSelected(@Nullable String name, @Nullable Animation animation) {
                    mTvMaskAnimShow.setText(name);
                    maskShowAnimation = animation;
                }
            });
        }
        popupSelectShowAnimate.showPopupWindow();
    }

    @OnClick(R.id.layout_select_dismiss)
    void selectDismiss() {
        if (popupSelectDismissAnimate == null) {
            popupSelectDismissAnimate = new PopupSelectDismissAnimate(getContext());
            popupSelectDismissAnimate.setOnSelectedResultListener(new PopupSelectDismissAnimate.OnSelectedResultListener() {
                @Override
                public void onSelected(@Nullable String name, @Nullable Animation animation) {
                    mTvMaskAnimDismiss.setText(name);
                    maskDismissAnimation = animation;
                }
            });
        }
        popupSelectDismissAnimate.showPopupWindow();
    }
}
