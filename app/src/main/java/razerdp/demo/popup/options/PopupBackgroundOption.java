package razerdp.demo.popup.options;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Animation;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import java.util.Random;

import androidx.annotation.NonNull;
import razerdp.basepopup.R;
import razerdp.basepopup.databinding.PopupOptionBackgroundBinding;
import razerdp.demo.model.common.CommonBackgroundInfo;
import razerdp.demo.utils.ColorUtil;
import razerdp.demo.utils.ToolUtil;
import razerdp.demo.utils.UIHelper;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;

/**
 * Created by 大灯泡 on 2019/9/20.
 */
public class PopupBackgroundOption extends BaseOptionPopup<CommonBackgroundInfo> {
    PopupOptionBackgroundBinding mBinding;
    View[] colorViews;

    Drawable selectedBackground;

    public PopupBackgroundOption(Context context) {
        super(context);
        setContentView(R.layout.popup_option_background);
        colorViews = new View[]{mBinding.viewColor1, mBinding.viewColor2, mBinding.viewColor3, mBinding.viewColor4, mBinding.viewColor5, mBinding.viewColor6, mBinding.viewColor7};
        randomColors();

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedBackground = v.getBackground();
                mBinding.checkNobackground.setChecked(false);
                mBinding.checkPicBackground.setChecked(false);
                mBinding.tvGo.setNormalBackgroundColor(((ColorDrawable) v.getBackground()).getColor());
            }
        };
        for (View colorView : colorViews) {
            colorView.setOnClickListener(listener);
        }
        mBinding.tvGo.setOnClickListener(v -> ok());
        mBinding.progressAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    float alpha = (float) progress / 100;
                    mBinding.tvAlpha.setText(String.format("透明度：%s%%", String.valueOf(progress)));
                    for (View colorView : colorViews) {
                        colorView.getBackground().setAlpha((int) (alpha * 255));
                    }
                    if (selectedBackground != null) {
                        selectedBackground.setAlpha((int) (alpha * 255));
                        if (selectedBackground instanceof ColorDrawable) {
                            int color = ((ColorDrawable) selectedBackground).getColor();
                            mBinding.tvGo.setNormalBackgroundColor(ColorUtil.alphaColor(alpha,
                                                                                        color));
                        }
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mBinding.checkNobackground.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedBackground = null;
                    mBinding.checkPicBackground.setChecked(false);
                    mBinding.tvGo.setNormalBackgroundColor(UIHelper.getColor(R.color.color_blue));
                }
            }
        });
        mBinding.checkPicBackground.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedBackground = UIHelper.getDrawable(R.drawable.popup_bg_drawable);
                    selectedBackground.setAlpha((int) (((float) mBinding.progressAlpha.getProgress() / 100) * 255));
                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        mBinding = PopupOptionBackgroundBinding.bind(contentView);
    }

    void randomColors() {
        for (int i = 0; i < colorViews.length; i++) {
            ColorDrawable drawable = ToolUtil.cast(colorViews[i].getBackground(),
                                                   ColorDrawable.class);
            if (drawable == null) {
                drawable = new ColorDrawable(ColorUtil.alphaColor((float) mBinding.progressAlpha.getProgress() / 100,
                                                                  randomColor()));
                colorViews[i].setBackground(drawable);
            } else {
                drawable.setColor(randomColor());
            }
        }
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


    private int randomColor() {
        Random random = new Random();
        return 0xff000000 | random.nextInt(0x00ffffff);
    }

    void ok() {
        mInfo.background = selectedBackground;
        mInfo.blur = mBinding.checkBlur.isChecked();
        dismiss();
    }
}
