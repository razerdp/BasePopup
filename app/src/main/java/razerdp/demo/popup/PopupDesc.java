package razerdp.demo.popup;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.utils.ButterKnifeUtil;
import razerdp.demo.widget.DPTextView;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.ScaleConfig;

/**
 * Created by 大灯泡 on 2019/9/26.
 * 描述专用
 */
public class PopupDesc extends BasePopupWindow {
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_desc)
    TextView mTvDesc;
    @BindView(R.id.tv_ok)
    DPTextView mTvOk;

    public PopupDesc(Context context) {
        super(context);
        setContentView(R.layout.popup_description);
        setClipChildren(false);
    }

    @Override
    public void onViewCreated(View contentView) {
        ButterKnifeUtil.bind(this, contentView);
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
        mTvTitle.setText(title);
        return this;
    }

    public PopupDesc setDesc(CharSequence desc) {
        mTvDesc.setText(desc);
        return this;
    }

    @OnClick(R.id.tv_ok)
    @Override
    public void dismiss() {
        super.dismiss();
    }
}
