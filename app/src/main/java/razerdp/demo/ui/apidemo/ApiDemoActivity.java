package razerdp.demo.ui.apidemo;

import android.content.Intent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.R;
import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.model.api.ApiInfo;
import razerdp.demo.popup.common.SimpleSelectorPopup;
import razerdp.demo.utils.ToolUtil;

import static razerdp.demo.ui.apidemo.ApiDemoActivity.Data;

/**
 * Created by 大灯泡 on 2020/4/4.
 */
public class ApiDemoActivity extends BaseActivity<Data> {

    ApiDemoFragment mApiDemoFragment;
    SimpleSelectorPopup mSelectorPopup;

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public int contentViewLayoutId() {
        return R.layout.activity_api_demo;
    }

    @Override
    protected void onInitView(View decorView) {
        if (getActivityData() == null || getActivityData().mApiInfo == null || getActivityData().mApiInfo.getFragmentClass() == null) {
            finish();
            return;
        }
        try {
            mApiDemoFragment = getActivityData().mApiInfo.getFragmentClass().newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            finish();
            return;
        }
        SimpleSelectorPopupConfig config = new SimpleSelectorPopupConfig();
        mApiDemoFragment.onInitSettingPopup(config);
        if (!ToolUtil.isEmpty(config.selectText)) {
            mSelectorPopup = new SimpleSelectorPopup(this);
            mSelectorPopup.setTitle(config.title)
                    .append(config.selectText)
                    .setSelectedPosition(config.defaultIndex);
            mSelectorPopup.setOnSelectedListener((selected, index) -> mApiDemoFragment.onSettingPopupSelected(selected, index));
        }
        setTitle(getActivityData().mApiInfo.getTitleText());
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.layout_container, mApiDemoFragment, mApiDemoFragment.getClass().toString())
                .commitAllowingStateLoss();

    }

    @Override
    public void onTitleRightClick(View view) {
        if (mSelectorPopup != null) {
            mSelectorPopup.showPopupWindow();
            return;
        }

        if (mApiDemoFragment != null) {
            mApiDemoFragment.onSettingClick();
        }
    }

    public static class Data extends BaseActivity.IntentData {
        ApiInfo mApiInfo;

        public Data(ApiInfo apiInfo) {
            mApiInfo = apiInfo;
        }
    }

    public static class SimpleSelectorPopupConfig {
        String title;
        List<String> selectText;
        int defaultIndex = 0;

        public SimpleSelectorPopupConfig() {
        }

        public SimpleSelectorPopupConfig setTitle(String title) {
            this.title = title;
            return this;
        }

        public SimpleSelectorPopupConfig append(String tx) {
            if (selectText == null) {
                selectText = new ArrayList<>();
            }
            selectText.add(tx);
            return this;
        }

        public SimpleSelectorPopupConfig setDefaultIndex(int defaultIndex) {
            this.defaultIndex = defaultIndex;
            return this;
        }
    }
}
