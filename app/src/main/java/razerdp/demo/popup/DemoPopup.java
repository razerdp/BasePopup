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

/**
 * Created by 大灯泡 on 2019/9/20.
 */
public class DemoPopup extends BasePopupWindow {
    @BindView(R.id.tv_desc)
    TextView mTvDesc;

    public DemoPopup(Context context) {
        super(context);
        ButterKnifeUtil.bind(this, getContentView());
    }

    public DemoPopup(Fragment fragment) {
        super(fragment);
        ButterKnifeUtil.bind(this, getContentView());
    }

    public DemoPopup(Dialog dialog) {
        super(dialog);
        ButterKnifeUtil.bind(this, getContentView());
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getDefaultScaleAnimation(true);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getDefaultScaleAnimation(false);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_demo);
    }

    public DemoPopup setText(CharSequence text) {
        mTvDesc.setText(text);
        return this;
    }
}
