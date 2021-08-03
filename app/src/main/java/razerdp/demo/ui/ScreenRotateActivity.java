package razerdp.demo.ui;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.viewbinding.ViewBinding;
import razerdp.basepopup.databinding.ActivityRotateBinding;
import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.base.baseactivity.BaseBindingActivity;
import razerdp.demo.model.common.CommonSlideInfo;
import razerdp.demo.utils.DescBuilder;

/**
 * Created by 大灯泡 on 2021/5/10
 * <p>
 * Description：
 */
public class ScreenRotateActivity extends BaseBindingActivity<ActivityRotateBinding> {
    public static final String DESC = DescBuilder.get()
            .append("测试屏幕旋转")
            .build();
    CommonSlideInfo info;

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public ActivityRotateBinding onCreateViewBinding(LayoutInflater layoutInflater) {
        return ActivityRotateBinding.inflate(layoutInflater);
    }

    @Override
    protected void onInitView(View decorView) {
        info = new CommonSlideInfo();
        mBinding.tvShow.setOnClickListener(v -> info.toShow(v));
        mBinding.tvSetting.setOnClickListener(v -> info.toOption(v));
    }

}
