package razerdp.demo.popup;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.utils.ToastUtils;

/**
 * Created by 大灯泡 on 2017/12/27.
 * 从底部滑上来同时模糊的popup
 */
public class BlurSlideFromBottomPopup extends BasePopupWindow implements View.OnClickListener {

    private View popupView;

    public BlurSlideFromBottomPopup(Context context) {
        super(context);
        bindEvent();
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0f, 300);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0f, 1f, 300);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_blur_slide_from_bottom);
    }

    private void bindEvent() {
        findViewById(R.id.tx_1).setOnClickListener(this);
        findViewById(R.id.tx_2).setOnClickListener(this);
        findViewById(R.id.tx_3).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tx_1:
                ToastUtils.ToastMessage(getContext(), "click tx_1");
                break;
            case R.id.tx_2:
                ToastUtils.ToastMessage(getContext(), "click tx_2");
                break;
            case R.id.tx_3:
                ToastUtils.ToastMessage(getContext(), "click tx_3");
                break;
            default:
                break;
        }

    }
}
