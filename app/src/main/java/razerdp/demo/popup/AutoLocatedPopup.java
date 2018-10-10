package razerdp.demo.popup;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Toast;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.utils.ToastUtils;

/**
 * Created by 大灯泡 on 2016/11/23.
 * <p>
 * 自动定位的popup，空间不足显示在上面
 */
public class AutoLocatedPopup extends BasePopupWindow implements View.OnClickListener {

    public AutoLocatedPopup(Context context) {
        super(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setAutoLocatePopup(true)
                .setAlignBackground(false);
        bindEvent();
    }


    @Override
    protected Animation onCreateShowAnimation() {
        return getDefaultAlphaAnimation();
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getDefaultAlphaAnimation(false);
    }


    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_menu);
    }

    private void bindEvent() {
        findViewById(R.id.tx_1).setOnClickListener(this);
        findViewById(R.id.tx_2).setOnClickListener(this);
        findViewById(R.id.tx_3).setOnClickListener(this);

    }

    @Override
    protected void onAnchorTop(View mPopupView, View anchorView) {
        Toast.makeText(getContext(), "显示在上方（下方位置不足）", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onAnchorBottom(View mPopupView, View anchorView) {
        Toast.makeText(getContext(), "显示在下方（上位置不足）", Toast.LENGTH_SHORT).show();
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
