package razerdp.demo.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import razerdp.basepopup.databinding.ActivitySizeLimitBinding;
import razerdp.demo.base.baseactivity.BaseBindingActivity;
import razerdp.demo.popup.PopupWidthHeightLimit;
import razerdp.demo.utils.StringUtil;

/**
 * Created by 大灯泡 on 2021/8/6
 * <p>
 * Description：
 */
public class SizeLimitActivity extends BaseBindingActivity<ActivitySizeLimitBinding> {
    PopupWidthHeightLimit mPopup;

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public ActivitySizeLimitBinding onCreateViewBinding(LayoutInflater layoutInflater) {
        return ActivitySizeLimitBinding.inflate(layoutInflater);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onInitView(View decorView) {
        mBinding.tvShow.setOnClickListener(this::showPopup);
        mBinding.tvShow.post(() -> mBinding.tvShow.setText(String.format(
                "当前AnchorView宽高信息：\nw = %s \n h = %s\n长按拖动，点击弹窗（显示在Anchor下方）",
                mBinding.tvShow.getWidth(),
                mBinding.tvShow.getHeight())));
        final ViewConfiguration configuration = ViewConfiguration.get(this);
        mBinding.tvShow.setOnTouchListener(new View.OnTouchListener() {
            float x, y;
            boolean onMove;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        onMove = false;
                        x = event.getX();
                        y = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float offsetX = event.getX() - x;
                        float offsetY = event.getY() - y;
                        if (Math.abs(offsetX) > configuration.getScaledTouchSlop() ||
                                Math.abs(offsetY) > configuration.getScaledTouchSlop()) {
                            v.offsetLeftAndRight((int) offsetX);
                            v.offsetTopAndBottom((int) offsetY);
                            onMove = true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (onMove) {
                            onMove = false;
                            return true;
                        }
                        break;
                }
                return false;
            }
        });
    }

    void showPopup(View v) {
        if (mPopup == null) {
            mPopup = new PopupWidthHeightLimit(this);
        }
        int gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        int minWidth = StringUtil.toInt(mBinding.edMinWidth.getText().toString().trim());
        int minHeight = StringUtil.toInt(mBinding.edMinHeight.getText().toString().trim());
        int maxWidth = StringUtil.toInt(mBinding.edMaxWidth.getText().toString().trim());
        int maxHeight = StringUtil.toInt(mBinding.edMaxHeight.getText().toString().trim());
        mPopup.setMinWidth(mBinding.checkMinWidth.isChecked() ? minWidth : 0);
        mPopup.setMinHeight(mBinding.checkMinHeight.isChecked() ? minHeight : 0);
        mPopup.setMaxWidth(mBinding.checkMaxWidth.isChecked() ? maxWidth : 0);
        mPopup.setMaxHeight(mBinding.checkMaxHeight.isChecked() ? maxHeight : 0);
        mPopup.setWidthAsAnchorView(mBinding.checkWidthAsAnchor.isChecked());
        mPopup.setHeightAsAnchorView(mBinding.checkHeightAsAnchor.isChecked());
        mPopup.setFitSize(mBinding.checkFitSize.isChecked());
        mPopup.setPopupGravity(gravity);
        mPopup.showPopupWindow(v);
    }
}
