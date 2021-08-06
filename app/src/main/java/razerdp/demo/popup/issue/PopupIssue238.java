package razerdp.demo.popup.issue;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.basepopup.databinding.PopupIssue238Binding;
import razerdp.basepopup.databinding.PopupIssue238WithEdittextBinding;
import razerdp.demo.utils.UIHelper;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;

/**
 * Created by 大灯泡 on 2020/02/26.
 */
public class PopupIssue238 extends BasePopupWindow {
    PopupIssue238WithEdittextBinding mWithEdittextBinding;
    PopupIssue238Binding mBinding;
    private boolean isEdit;

    public PopupIssue238(Context context, boolean isEdit) {
        super(context);
        this.isEdit = isEdit;
        setContentView(isEdit ? R.layout.popup_issue_238_with_edittext : R.layout.popup_issue_238);
    }

    @Override
    public void onViewCreated(View contentView) {
        if (isEdit) {
            mWithEdittextBinding = PopupIssue238WithEdittextBinding.bind(contentView);
        } else {
            mBinding = PopupIssue238Binding.bind(contentView);
        }
        contentView.setOnClickListener(v -> UIHelper.toast("点击ContentView"));
        if (isEdit) {
            setAutoShowKeyboard(true);
            setKeyboardAdaptive(true);
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
}
