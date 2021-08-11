package razerdp.demo.ui.apidemo;

import androidx.annotation.NonNull;
import androidx.viewbinding.ViewBinding;
import razerdp.demo.base.baseactivity.BaseFragment;

/**
 * Created by 大灯泡 on 2020/4/4.
 */
public abstract class ApiDemoFragment<VB extends ViewBinding> extends BaseFragment<VB> {

    protected void onInitSettingPopup(@NonNull ApiDemoActivity.SimpleSelectorPopupConfig config) {

    }

    protected void onSettingPopupSelected(String selected, int index) {

    }

    protected void onSettingClick() {

    }
}
