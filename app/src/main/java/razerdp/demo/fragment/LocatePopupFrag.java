package razerdp.demo.fragment;

import android.graphics.Color;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RadioGroup;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.QuickPopupBuilder;
import razerdp.basepopup.QuickPopupConfig;
import razerdp.basepopup.R;
import razerdp.blur.PopupBlurOption;
import razerdp.demo.utils.ToastUtils;

/**
 * Created by 大灯泡 on 2016/1/16.
 */
public class LocatePopupFrag extends SimpleBaseFrag {
    public RadioGroup rvGroup;
    public Button anchor;

    Animation enterAnimation = null;
    Animation dismissAnimation = null;
    int offsetX = 0;
    int offsetY = 0;
    //offsetWithPopupWidth和offsetWithPopupHeight
    //意思是计算offset的时候把popup的宽和高也算进去
    //意为popup宽高的百分比
    float offsetRatioOfPopupWidth = 0;
    float offsetRatioOfPopupHeight = 0;

    @Override
    public void bindEvent() {
        this.rvGroup = (RadioGroup) findViewById(R.id.rv_group);
        this.anchor = (Button) findViewById(R.id.anchor);
        rvGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                offsetX = 0;
                offsetY = 0;
                offsetRatioOfPopupWidth = 0;
                offsetRatioOfPopupHeight = 0;
                switch (checkedId) {
                    case R.id.left:
                        enterAnimation = createHorizontalAnimation(1f, 0);
                        dismissAnimation = createHorizontalAnimation(0, 1f);
                        offsetY = -anchor.getHeight() / 2;
                        offsetRatioOfPopupWidth = -1f;
                        offsetRatioOfPopupHeight = -0.5f;
                        break;
                    case R.id.top:
                        enterAnimation = createVerticalAnimation(1f, 0);
                        dismissAnimation = createVerticalAnimation(0, 1f);
                        offsetY = -anchor.getHeight();
                        offsetRatioOfPopupWidth = 0;
                        offsetRatioOfPopupHeight = -1f;
                        break;
                    case R.id.right:
                        enterAnimation = createHorizontalAnimation(-1f, 0);
                        dismissAnimation = createHorizontalAnimation(0, -1f);
                        offsetY = -anchor.getHeight() / 2;
                        offsetX = anchor.getWidth();
                        offsetRatioOfPopupWidth = 0;
                        offsetRatioOfPopupHeight = -0.5f;
                        break;
                    case R.id.bottom:
                        enterAnimation = createVerticalAnimation(-1f, 0);
                        dismissAnimation = createVerticalAnimation(0, -1f);
                        offsetRatioOfPopupWidth = 0;
                        offsetRatioOfPopupHeight = 0;
                        break;
                }
            }
        });
        anchor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuickPopupBuilder.with(getContext())
                        .contentView(R.layout.popup_menu_small)
                        .wrapContentMode()
                        .config(new QuickPopupConfig()
                                .backgroundColor(Color.parseColor("#8F617D8A"))
                                .withShowAnimation(enterAnimation)
                                .withDismissAnimation(dismissAnimation)
                                .offsetX(offsetX, offsetRatioOfPopupWidth)
                                .offsetY(offsetY, offsetRatioOfPopupHeight)
                                .blurBackground(true, new BasePopupWindow.OnBlurOptionInitListener() {
                                    @Override
                                    public void onCreateBlurOption(PopupBlurOption option) {
                                        option.setBlurRadius(6)
                                                .setBlurPreScaleRatio(0.9f);
                                    }
                                })
                                .withClick(R.id.tx_1, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ToastUtils.ToastMessage(getContext(), "tx1");
                                    }
                                },true))
                        .show(v);
            }
        });
    }

    private Animation createVerticalAnimation(float fromY, float toY) {
        Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0f,
                Animation.RELATIVE_TO_SELF,
                0f,
                Animation.RELATIVE_TO_SELF,
                fromY,
                Animation.RELATIVE_TO_SELF,
                toY);
        animation.setDuration(500);
        animation.setInterpolator(new DecelerateInterpolator());
        return animation;
    }

    private Animation createHorizontalAnimation(float fromX, float toX) {
        Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                fromX,
                Animation.RELATIVE_TO_SELF,
                toX,
                Animation.RELATIVE_TO_SELF,
                0f,
                Animation.RELATIVE_TO_SELF,
                0f);
        animation.setDuration(500);
        animation.setInterpolator(new DecelerateInterpolator());
        return animation;
    }

    @Override
    public BasePopupWindow getPopup() {
        return null;
    }

    @Override
    public Button getButton() {
        return null;
    }

    @Override
    public View getFragment() {
        return mInflater.inflate(R.layout.frag_locate_with_view, container, false);
    }
}
