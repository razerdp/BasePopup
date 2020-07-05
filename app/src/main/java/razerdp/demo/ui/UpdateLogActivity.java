package razerdp.demo.ui;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;

import butterknife.BindView;
import razerdp.basepopup.R;
import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.utils.DescBuilder;

/**
 * Created by 大灯泡 on 2019/9/23.
 */
public class UpdateLogActivity extends BaseActivity {
    public static final String DESC = DescBuilder.get()
            .append("更新日志")
            .build();
    @BindView(R.id.web_view_container)
    FrameLayout mWebViewContainer;
    AgentWeb mAgentWeb;

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public int contentViewLayoutId() {
        return R.layout.activity_update_log;
    }

    @Override
    protected void onInitView(View decorView) {
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(mWebViewContainer, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go("https://www.yuque.com/razerdp/basepopup/uyrsxx");
    }

    @Override
    public void onTitleLeftClick(View view) {
        if (!mAgentWeb.back()) {
            super.onTitleLeftClick(view);
        }
    }
}
