package razerdp.demo.popup;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.utils.ToastUtils;

/**
 * Created by 大灯泡 on 2016/1/15.
 * 普通的popup
 */
public class ScalePopup extends BasePopupWindow implements View.OnClickListener {

    public ScalePopup(Activity context) {
        super(context);
        bindEvent();
    }


    @Override
    protected Animation onCreateShowAnimation() {
        return getDefaultScaleAnimation();
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getDefaultScaleAnimation(false);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_normal);
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
//                int[] location = new int[2];
//                v.getLocationOnScreen(location);
//                QuickPopupBuilder.with(getContext())
//                        .contentView(R.layout.popup_menu)
//                        .wrapContentMode()
//                        .config(new QuickPopupConfig()
//                                .offsetX((int) (location[0] + v.getWidth() / 2),-0.5f)
//                                .offsetY((int) location[1] + v.getHeight()))
//                        .show();
                new DialogPopup(getContext()).showPopupWindow();
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
