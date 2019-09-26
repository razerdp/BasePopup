package razerdp.demo.ui.issuestest;

import android.content.Intent;
import android.view.View;

import androidx.appcompat.widget.AppCompatCheckBox;

import butterknife.BindView;
import butterknife.OnClick;
import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.popup.PopupInput;
import razerdp.demo.widget.DPTextView;

/**
 * Created by 大灯泡 on 2019/9/26
 * <p>
 * Description：https://github.com/razerdp/BasePopup/issues/226
 */
public class Issue226TestActivity extends BaseActivity {
    @BindView(R.id.check_force_adjust)
    AppCompatCheckBox checkForceAdjust;
    @BindView(R.id.tv_show)
    DPTextView tvShow;

    PopupInput popupInput;

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public int contentViewLayoutId() {
        return R.layout.activity_issue_226;
    }

    @Override
    protected void onInitView(View decorView) {

    }

    @OnClick(R.id.tv_show)
    void show() {
        if (popupInput == null) {
            popupInput = new PopupInput(this);
        }
        int flag = BasePopupWindow.FLAG_KEYBOARD_ALIGN_TO_ROOT | BasePopupWindow.FLAG_KEYBOARD_ANIMATE_ALIGN;

        if (checkForceAdjust.isChecked()) {
            flag |= BasePopupWindow.FLAG_KEYBOARD_FORCE_ADJUST;
        }
        popupInput.setAdjustInputMethod(true)
                .setAdjustInputMode(flag)
                .showPopupWindow();
    }

}
