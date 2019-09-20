package razerdp.demo.model;

import razerdp.demo.base.baseactivity.BaseActivity;

/**
 * Created by 大灯泡 on 2019/9/20
 * <p>
 * Description：
 */
public class DemoMainItem {

    public Class<? extends BaseActivity> toClass;
    public String title;
    public String desc;
    public String tag;

    public DemoMainItem(Class<? extends BaseActivity> toClass, String title, String desc, String tag) {
        this.toClass = toClass;
        this.title = title;
        this.desc = desc;
        this.tag = tag;
    }
}
