package razerdp.demo.popup;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;

import butterknife.BindView;
import razerdp.basepopup.R;
import razerdp.demo.base.baseactivity.BaseActivity;

/**
 * Created by 大灯泡 on 2021/7/27.
 */
public class MyTestActivity extends BaseActivity {
    @BindView(R.id.layout_test)
    View test;

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public int contentViewLayoutId() {
        return R.layout.activity_my_test;
    }

    @Override
    protected void onInitView(View decorView) {
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DemoPopup(self()).setPopupGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL).showPopupWindow(view);
            }
        });
    }
}
