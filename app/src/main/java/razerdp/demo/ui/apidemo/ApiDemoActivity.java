package razerdp.demo.ui.apidemo;

import android.content.Intent;
import android.view.View;

import razerdp.basepopup.R;
import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.model.api.ApiInfo;

import static razerdp.demo.ui.apidemo.ApiDemoActivity.Data;

/**
 * Created by 大灯泡 on 2020/4/4.
 */
public class ApiDemoActivity extends BaseActivity<Data> {

    ApiDemoFragment mApiDemoFragment;

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
        setTitle(getActivityData().mApiInfo.getTitleText());
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.layout_container, mApiDemoFragment, mApiDemoFragment.getClass().toString())
                .commitAllowingStateLoss();

    }

    @Override
    public void onTitleRightClick(View view) {
        if (mApiDemoFragment != null) {
            mApiDemoFragment.onSettingClick();
        }
    }

    public static class Data extends IntentData {
        ApiInfo mApiInfo;

        public Data(ApiInfo apiInfo) {
            mApiInfo = apiInfo;
        }
    }
}
