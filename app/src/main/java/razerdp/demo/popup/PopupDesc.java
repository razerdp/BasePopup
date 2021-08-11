package razerdp.demo.popup;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.basepopup.databinding.PopupDescriptionBinding;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.ScaleConfig;

/**
 * Created by 大灯泡 on 2019/9/26.
 * 描述专用
 */
public class PopupDesc extends BasePopupWindow {
    PopupDescriptionBinding mBinding;

    public PopupDesc(Context context) {
        super(context);
        setContentView(R.layout.popup_description);
        setClipChildren(false);
        mBinding.tvOk.setOnClickListener(v -> dismiss());
    }

    @Override
    public void onViewCreated(View contentView) {
        mBinding = PopupDescriptionBinding.bind(contentView);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return AnimationHelper.asAnimation()
                .withScale(ScaleConfig.CENTER.interpolator(new OvershootInterpolator(1.5f)))
                .toShow();
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return AnimationHelper.asAnimation()
                .withScale(ScaleConfig.CENTER.interpolator(new OvershootInterpolator(-5.5f)))
                .toDismiss();
    }


    public PopupDesc setTitle(CharSequence title) {
        mBinding.tvTitle.setText(title);
        return this;
    }

    public PopupDesc setDesc(CharSequence desc) {
        mBinding.tvDesc.setText(desc);
        return this;
    }

}
