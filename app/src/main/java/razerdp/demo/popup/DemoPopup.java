package razerdp.demo.popup;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.basepopup.databinding.PopupDemoBinding;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;

/**
 * Created by 大灯泡 on 2019/9/20.
 */
public class DemoPopup extends BasePopupWindow {
    PopupDemoBinding mBinding;

    OnPopupLayoutListener layoutListener;

    public DemoPopup(Context context) {
        super(context);
        setContentView(R.layout.popup_demo);
    }

    public DemoPopup(Fragment fragment) {
        super(fragment);
        setContentView(R.layout.popup_demo);
    }

    public DemoPopup(Dialog dialog) {
        super(dialog);
        setContentView(R.layout.popup_demo);
    }

    @Override
    public void onViewCreated(View contentView) {
        mBinding = PopupDemoBinding.bind(contentView);
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


    public DemoPopup setText(CharSequence text) {
        mBinding.tvDesc.setText(text);
        return this;
    }

    public TextView getTextView() {
        return mBinding.tvDesc;
    }

    public DemoPopup setLayoutListener(OnPopupLayoutListener layoutListener) {
        this.layoutListener = layoutListener;
        return this;
    }

    @Override
    public void onPopupLayout(@NonNull Rect popupRect, @NonNull Rect anchorRect) {
        super.onPopupLayout(popupRect, anchorRect);
        if (layoutListener != null) {
            layoutListener.onPopupLayout(popupRect, anchorRect);
        }
    }

    public interface OnPopupLayoutListener {
        void onPopupLayout(@NonNull Rect popupRect, @NonNull Rect anchorRect);
    }
}
