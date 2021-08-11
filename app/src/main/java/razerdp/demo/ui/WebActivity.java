package razerdp.demo.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.WebChromeClient;

import androidx.viewbinding.ViewBinding;
import razerdp.basepopup.databinding.ActivityWebBinding;
import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.utils.StringUtil;

import static razerdp.demo.ui.WebActivity.Data;

/**
 * Created by 大灯泡 on 2019/9/23.
 */
public class WebActivity extends BaseActivity<Data, ActivityWebBinding> {
    AgentWeb mAgentWeb;

    Data data;

    @Override
    protected void onHandleIntent(Intent intent) {
        data = getActivityData();
    }

    @Override
    public ActivityWebBinding onCreateViewBinding(LayoutInflater layoutInflater) {
        return ActivityWebBinding.inflate(layoutInflater);
    }

    @Override
    protected void onInitView(View decorView) {
        if (data == null || TextUtils.isEmpty(data.url)) {
            finish();
            return;
        }
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(mBinding.webViewContainer,
                                   new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                                 ViewGroup.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator()
                .setWebChromeClient(mWebChromeClient)
                .createAgentWeb()
                .ready()
                .go(data.url);
        mAgentWeb.getAgentWebSettings().getWebSettings().setUseWideViewPort(true);
        mAgentWeb.getAgentWebSettings().getWebSettings().setLoadWithOverviewMode(true);
        if (StringUtil.noEmpty(data.title)) {
            setTitle(data.title);
        } else {
            setTitle("源码");
        }
    }

    @Override
    public void onTitleLeftClick(View view) {
        if (!mAgentWeb.back()) {
            super.onTitleLeftClick(view);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAgentWeb.getWebLifeCycle().onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mAgentWeb.getWebLifeCycle().onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAgentWeb.getWebLifeCycle().onDestroy();
    }

    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (TextUtils.isEmpty(data.title)) {
                setTitle(title);
            }
        }
    };

    public static class Data extends BaseActivity.IntentData {
        public String url;
        public String title;

        public Data setUrl(String url) {
            this.url = url;
            return this;
        }

        public Data setTitle(String title) {
            this.title = title;
            return this;
        }
    }
}
