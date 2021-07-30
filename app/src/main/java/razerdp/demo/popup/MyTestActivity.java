package razerdp.demo.popup;

import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.viewbinding.ViewBinding;
import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.databinding.ActivityMyTestBinding;
import razerdp.demo.base.baseactivity.BaseActivity;

/**
 * Created by 大灯泡 on 2021/7/27.
 */
public class MyTestActivity extends BaseActivity {
    View test;

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public ViewBinding onCreateViewBinding(LayoutInflater layoutInflater) {
        return ActivityMyTestBinding.inflate(layoutInflater);
    }

    @Override
    protected void onInitView(View decorView) {
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BasePopupWindow popup = new DemoPopup(self()).setWidth(BasePopupWindow.MATCH_PARENT).setHeight(
                        BasePopupWindow.WRAP_CONTENT).setPopupGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
                ((DemoPopup) popup).setText("本弹窗高度350dp\n显示在AnchorView下方");
                popup.setFitSize(true);
                popup.showPopupWindow(view);
            }
        });
    }
}
