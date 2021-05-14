package razerdp.demo.popup.issue;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import butterknife.BindView;
import butterknife.OnClick;
import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.utils.ButterKnifeUtil;
import razerdp.demo.widget.DPTextView;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;

/**
 * Created by 大灯泡 on 2019/9/22.
 */
public class PopupIssue210 extends BasePopupWindow {
    @BindView(R.id.tv_go)
    DPTextView mTvGo;

    public PopupIssue210(Context context) {
        super(context);
        setContentView(R.layout.popup_issue_210);
        setPopupGravity(Gravity.BOTTOM);
    }

    @Override
    public void onViewCreated(View contentView) {
        ButterKnifeUtil.bind(this, contentView);
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

    @OnClick(R.id.tv_go)
    @Override
    public void dismiss() {
        super.dismiss();
    }
}
