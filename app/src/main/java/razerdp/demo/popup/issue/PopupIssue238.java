package razerdp.demo.popup.issue;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;

import androidx.annotation.Nullable;
import butterknife.BindView;
import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.utils.ButterKnifeUtil;
import razerdp.demo.utils.UIHelper;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;

/**
 * Created by 大灯泡 on 2020/02/26.
 */
public class PopupIssue238 extends BasePopupWindow {
    private boolean isEdit;

    @Nullable
    @BindView(R.id.ed_input)
    EditText edInput;

    public PopupIssue238(Context context, boolean isEdit) {
        super(context);
        this.isEdit = isEdit;
        setContentView(isEdit ? R.layout.popup_issue_238_with_edittext : R.layout.popup_issue_238);
    }

    @Override
    public void onViewCreated(View contentView) {
        ButterKnifeUtil.bind(this, contentView);
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.toast("点击ContentView");
            }
        });
        if (isEdit) {
            setAutoShowInputMethod(true);
            setAdjustInputMethod(true);
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
