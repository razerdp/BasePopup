package razerdp.demo.popup;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.basepopup.databinding.ActivityMyTestBinding;
import razerdp.demo.base.baseactivity.BaseBindingActivity;
import razerdp.demo.utils.UIHelper;

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
                new DemoPopup(self()).showPopupWindow(view);
            }
        });
    }
}
