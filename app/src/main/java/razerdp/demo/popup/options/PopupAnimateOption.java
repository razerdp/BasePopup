package razerdp.demo.popup.options;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import razerdp.basepopup.R;
import razerdp.basepopup.databinding.PopupOptionAnimateBinding;
import razerdp.demo.model.common.CommonAnimateInfo;

/**
 * Created by 大灯泡 on 2019/9/20
 * <p>
 * Description：{@link CommonAnimateInfo}
 */
public class PopupAnimateOption extends BaseOptionPopup<CommonAnimateInfo> {
    PopupOptionAnimateBinding mBinding;


    PopupSelectShowAnimate popupSelectShowAnimate;
    PopupSelectDismissAnimate popupSelectDismissAnimate;


    Animation showAnimation;
    String showName;

    Animation dismissAnimation;
    String dissName;

    public PopupAnimateOption(Context context) {
        super(context);
        setContentView(R.layout.popup_option_animate);
        mBinding.checkClipchildren.setChecked(true);
        setAutoMirrorEnable(true);
        mBinding.tvShow.setOnClickListener(v -> selectShow());
        mBinding.tvDismiss.setOnClickListener(v -> selectDismiss());
        mBinding.tvGo.setOnClickListener(v -> ok());
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        mBinding = PopupOptionAnimateBinding.bind(contentView);
    }

    void selectShow() {
        if (popupSelectShowAnimate == null) {
            popupSelectShowAnimate = new PopupSelectShowAnimate(getContext());
            popupSelectShowAnimate.setOnSelectedResultListener(new PopupSelectShowAnimate.OnSelectedResultListener() {
                @Override
                public void onSelected(@Nullable String name, @Nullable Animation animation) {
                    showName = name;
                    showAnimation = animation;
                    mBinding.tvShow.setText(name);
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
                    dissName = name;
                    dismissAnimation = animation;
                    mBinding.tvDismiss.setText(name);
                }
            });
        }
        popupSelectDismissAnimate.showPopupWindow();
    }

    @Override
    public void showPopupWindow() {
        mBinding.tvShow.setText(showName);
        mBinding.tvDismiss.setText(dissName);
        super.showPopupWindow();
    }

    void ok() {
        if (mInfo != null) {
            mInfo.showAnimation = showAnimation;
            mInfo.dismissAnimation = dismissAnimation;
            mInfo.blur = mBinding.checkBlur.isChecked();
            mInfo.clip = mBinding.checkClipchildren.isChecked();
        }
        dismiss();
    }
}
