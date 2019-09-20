package razerdp.demo.popup;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;

import butterknife.BindView;
import butterknife.OnClick;
import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.model.common.CommonAnimateInfo;
import razerdp.demo.utils.ButterKnifeUtil;
import razerdp.demo.widget.DPTextView;

/**
 * Created by 大灯泡 on 2019/9/20
 * <p>
 * Description：{@link CommonAnimateInfo}
 */
public class PopupAnimateOption extends BasePopupWindow {
    @BindView(R.id.tv_show)
    TextView tvShow;
    @BindView(R.id.layout_select_show)
    LinearLayout layoutSelectShow;
    @BindView(R.id.tv_dismiss)
    TextView tvDismiss;
    @BindView(R.id.layout_select_dismiss)
    LinearLayout layoutSelectDismiss;
    @BindView(R.id.check_clipchildren)
    AppCompatCheckBox checkClipchildren;
    @BindView(R.id.check_blur)
    AppCompatCheckBox checkBlur;
    @BindView(R.id.tv_go)
    DPTextView tvGo;

    CommonAnimateInfo commonAnimateInfo;

    PopupSelectShowAnimate popupSelectShowAnimate;
    PopupSelectDismissAnimate popupSelectDismissAnimate;


    Animation showAnimation;
    String showName;

    Animation dismissAnimation;
    String dissName;


    public PopupAnimateOption(Context context) {
        super(context);
        ButterKnifeUtil.bind(this, getContentView());
        checkClipchildren.setChecked(true);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_option_animate);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(-1f, 0f, 450);
    }


    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0f, -1f, 450);
    }

    @OnClick(R.id.layout_select_show)
    void selectShow() {
        if (popupSelectShowAnimate == null) {
            popupSelectShowAnimate = new PopupSelectShowAnimate(getContext());
            popupSelectShowAnimate.setOnSelectedResultListener(new PopupSelectShowAnimate.OnSelectedResultListener() {
                @Override
                public void onSelected(@Nullable String name, @Nullable Animation animation) {
                    showName = name;
                    showAnimation = animation;
                    tvShow.setText(name);
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
                    dissName = name;
                    dismissAnimation = animation;
                    tvDismiss.setText(name);
                }
            });
        }
        popupSelectDismissAnimate.showPopupWindow();
    }

    public void attachInfo(CommonAnimateInfo info) {
        commonAnimateInfo = info;
    }

    @Override
    public void showPopupWindow() {
        tvShow.setText(showName);
        tvDismiss.setText(dissName);
        super.showPopupWindow();
    }

    @OnClick(R.id.tv_go)
    void ok() {
        if (commonAnimateInfo != null) {
            commonAnimateInfo.showAnimation = showAnimation;
            commonAnimateInfo.dismissAnimation = dismissAnimation;
            commonAnimateInfo.blur = checkBlur.isChecked();
            commonAnimateInfo.clip = checkClipchildren.isChecked();
        }
        dismiss();
    }
}
