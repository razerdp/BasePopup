package razerdp.demo.popup.issue;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.basepopup.databinding.PopupIssue236Binding;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;

/**
 * Created by 大灯泡 on 2020/02/11.
 */
public class PopupIssue236 extends BasePopupWindow {
    PopupIssue236Binding mBinding;

    public PopupIssue236(Context context) {
        super(context);
        setContentView(R.layout.popup_issue_236);
        setPopupGravity(Gravity.BOTTOM);
        mBinding.tvGo.setOnClickListener(v -> dismiss());
    }

    @Override
    public void onViewCreated(View contentView) {
        mBinding = PopupIssue236Binding.bind(contentView);
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

}
