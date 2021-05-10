package razerdp.demo.ui;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import razerdp.basepopup.R;
import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.model.common.CommonSlideInfo;
import razerdp.demo.utils.DescBuilder;

/**
 * Created by 大灯泡 on 2021/5/10
 * <p>
 * Description：
 */
public class ScreenRotateActivity extends BaseActivity {
    public static final String DESC = DescBuilder.get()
            .append("测试屏幕旋转")
            .build();
    @BindView(R.id.tv_show)
    TextView tvShow;
    @BindView(R.id.tv_setting)
    TextView tvSetting;

    CommonSlideInfo info;

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public int contentViewLayoutId() {
        return R.layout.activity_rotate;
    }

    @Override
    protected void onInitView(View decorView) {
        info = new CommonSlideInfo();
        tvShow.setOnClickListener(v -> info.toShow(v));
        tvSetting.setOnClickListener(v -> info.toOption(v));
    }

}
