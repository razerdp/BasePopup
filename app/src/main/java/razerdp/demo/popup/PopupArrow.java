package razerdp.demo.popup;

import android.content.Context;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import androidx.annotation.NonNull;
import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.basepopup.databinding.PopupArrowBinding;
import razerdp.demo.utils.ViewUtil;
import razerdp.util.animation.AlphaConfig;
import razerdp.util.animation.AnimationHelper;

/**
 * Created by 大灯泡 on 2020/5/6.
 */
public class PopupArrow extends BasePopupWindow {
    PopupArrowBinding mBinding;

    public PopupArrow(Context context) {
        super(context);
        setContentView(R.layout.popup_arrow);
        ViewUtil.setViewPivotRatio(mBinding.ivArrow, 0.5f, 0.5f);
    }


    @Override
    public void onViewCreated(@NonNull View contentView) {
        mBinding = PopupArrowBinding.bind(contentView);
    }

    @Override
    protected Animation onCreateShowAnimation(int width, int height) {
        return AnimationHelper.asAnimation()
                .withAlpha(AlphaConfig.IN)
                .toShow();
    }

    @Override
    protected Animation onCreateDismissAnimation(int width, int height) {
        return AnimationHelper.asAnimation()
                .withAlpha(AlphaConfig.OUT)
                .toDismiss();
    }

    @Override
    public void onPopupLayout(@NonNull Rect popupRect, @NonNull Rect anchorRect) {
        int gravity = computeGravity(popupRect, anchorRect);
        boolean verticalCenter = false;
        switch (gravity & Gravity.VERTICAL_GRAVITY_MASK) {
            case Gravity.TOP:
                mBinding.ivArrow.setVisibility(View.VISIBLE);
                mBinding.ivArrow.setTranslationX((popupRect.width() - mBinding.ivArrow.getWidth()) >> 1);
                mBinding.ivArrow.setTranslationY(popupRect.height() - mBinding.ivArrow.getHeight());
                mBinding.ivArrow.setRotation(0f);
                break;
            case Gravity.BOTTOM:
                mBinding.ivArrow.setVisibility(View.VISIBLE);
                mBinding.ivArrow.setTranslationX((popupRect.width() - mBinding.ivArrow.getWidth()) >> 1);
                mBinding.ivArrow.setTranslationY(0);
                mBinding.ivArrow.setRotation(180f);
                break;
            case Gravity.CENTER_VERTICAL:
                verticalCenter = true;
                break;
        }
        switch (gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
            case Gravity.LEFT:
                mBinding.ivArrow.setVisibility(View.VISIBLE);
                mBinding.ivArrow.setTranslationX(popupRect.width() - mBinding.ivArrow.getWidth());
                mBinding.ivArrow.setTranslationY((popupRect.height() - mBinding.ivArrow.getHeight()) >> 1);
                mBinding.ivArrow.setRotation(270f);
                break;
            case Gravity.RIGHT:
                mBinding.ivArrow.setVisibility(View.VISIBLE);
                mBinding.ivArrow.setTranslationX(0);
                mBinding.ivArrow.setTranslationY((popupRect.height() - mBinding.ivArrow.getHeight()) >> 1);
                mBinding.ivArrow.setRotation(90f);
                break;
            case Gravity.CENTER_HORIZONTAL:
                mBinding.ivArrow.setVisibility(verticalCenter ? View.INVISIBLE : View.VISIBLE);
                break;
        }
    }
}
