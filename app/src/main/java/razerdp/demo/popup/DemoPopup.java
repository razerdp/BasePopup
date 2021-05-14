package razerdp.demo.popup;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import butterknife.BindView;
import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.utils.ButterKnifeUtil;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.ScaleConfig;
import razerdp.util.animation.TranslationConfig;

/**
 * Created by 大灯泡 on 2019/9/20.
 */
public class DemoPopup extends BasePopupWindow {
    @BindView(R.id.tv_desc)
    public TextView mTvDesc;

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
        ButterKnifeUtil.bind(this, contentView);
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
                .withScale(ScaleConfig.CENTER)
                .toDismiss();
    }


    public DemoPopup setText(CharSequence text) {
        mTvDesc.setText(text);
        return this;
    }
}
