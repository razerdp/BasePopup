package razerdp.demo.popup;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.basepopup.databinding.ActivityMyTestBinding;
import razerdp.basepopup.databinding.PopupMyTestBinding;
import razerdp.demo.base.baseactivity.BaseBindingActivity;

/**
 * Created by 大灯泡 on 2021/7/27.
 */
public class MyTestActivity extends BaseBindingActivity<ActivityMyTestBinding> {

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public ActivityMyTestBinding onCreateViewBinding(LayoutInflater layoutInflater) {
        return ActivityMyTestBinding.inflate(layoutInflater);
    }

    @Override
    protected void onInitView(View decorView) {
        mBinding.viewTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TestPopup(self()).setMaxWidth(350).setPopupGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL).showPopupWindow(view);
            }
        });
    }


    static class TestPopup extends BasePopupWindow{
        PopupMyTestBinding mBinding;
        public TestPopup(Context context) {
            super(context);
            setContentView(R.layout.popup_my_test);
        }
        @Override
        public void onViewCreated(View contentView) {
            mBinding = PopupMyTestBinding.bind(contentView);
        }
    }
}
