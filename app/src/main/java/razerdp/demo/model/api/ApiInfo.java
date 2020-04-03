package razerdp.demo.model.api;

import razerdp.demo.ui.apidemo.ApiDemoFragment;
import razerdp.demo.utils.StringUtil;

/**
 * Created by 大灯泡 on 2020/4/4.
 */
public class ApiInfo {
    private CharSequence api;
    private Class<? extends ApiDemoFragment> fragmentClass;
    private String titleText;

    public ApiInfo(CharSequence api, Class<? extends ApiDemoFragment> fragmentClass) {
        this.api = api;
        this.fragmentClass = fragmentClass;
    }


    public CharSequence getApi() {
        return api;
    }

    public Class<? extends ApiDemoFragment> getFragmentClass() {
        return fragmentClass;
    }


    public String getTitleText() {
        if (StringUtil.noEmpty(titleText)) {
            return titleText;
        }
        return String.valueOf(api);
    }
}
