package razerdp.demo.ui;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;

import androidx.viewbinding.ViewBinding;
import razerdp.basepopup.databinding.ActivityUpdateLogBinding;
import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.base.baseactivity.BaseBindingActivity;
import razerdp.demo.utils.DescBuilder;

/**
 * Created by 大灯泡 on 2019/9/23.
 */
public class UpdateLogActivity extends BaseBindingActivity<ActivityUpdateLogBinding> {
    public static final String DESC = DescBuilder.get()
            .append("更新日志")
            .build();
    AgentWeb mAgentWeb;

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public ActivityUpdateLogBinding onCreateViewBinding(LayoutInflater layoutInflater) {
        return ActivityUpdateLogBinding.inflate(layoutInflater);
    }

    @Override
    protected void onInitView(View decorView) {
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(mBinding.webViewContainer,
                                   new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                                 ViewGroup.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go("https://www.yuque.com/razerdp/basepopup/uyrsxx");
        mAgentWeb.getAgentWebSettings().getWebSettings().setUseWideViewPort(true);
        mAgentWeb.getAgentWebSettings().getWebSettings().setLoadWithOverviewMode(true);
    }

    @Override
    public void onTitleLeftClick(View view) {
        if (!mAgentWeb.back()) {
            super.onTitleLeftClick(view);
        }
    }
}
