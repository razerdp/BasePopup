package razerdp.demo.fragment.other;

import android.graphics.Color;
import android.view.Gravity;
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
    int gravity;

    @Override
    public void onInitView(View rootView) {
        this.rvGroup = (RadioGroup) findViewById(R.id.rv_group);
        this.anchor = (Button) findViewById(R.id.anchor);
        rvGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.left:
                        enterAnimation = createHorizontalAnimation(1f, 0);
                        dismissAnimation = createHorizontalAnimation(0, 1f);
                        gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
                        break;
                    case R.id.top:
                        enterAnimation = createVerticalAnimation(1f, 0);
                        dismissAnimation = createVerticalAnimation(0, 1f);
                        gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
                        break;
                    case R.id.right:
                        enterAnimation = createHorizontalAnimation(-1f, 0);
                        dismissAnimation = createHorizontalAnimation(0, -1f);
                        gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
                        break;
                    case R.id.bottom:
                        enterAnimation = createVerticalAnimation(-1f, 0);
                        dismissAnimation = createVerticalAnimation(0, -1f);
                        gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                        break;
                }
            }
        });
        anchor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuickPopupBuilder.with(getContext())
                        .contentView(R.layout.popup_menu_small)
                        .config(new QuickPopupConfig()
                                .clipChildren(true)
                                .backgroundColor(Color.parseColor("#8C617D8A"))
                                .withShowAnimation(enterAnimation)
                                .withDismissAnimation(dismissAnimation)
                                .blurBackground(true)
                                .gravity(gravity)
                                .withClick(R.id.tx_1, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ToastUtils.ToastMessage(getContext(), "tx1");
                                    }
                                }, true))
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
