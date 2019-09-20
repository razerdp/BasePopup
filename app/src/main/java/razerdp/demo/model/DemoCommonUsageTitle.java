package razerdp.demo.model;

import razerdp.demo.base.baseadapter.MultiType;

/**
 * Created by 大灯泡 on 2019/9/20
 * <p>
 * Description：
 */
public class DemoCommonUsageTitle implements MultiType {
    public String title;

    public DemoCommonUsageTitle(String title) {
        this.title = title;
    }

    @Override
    public int getItemType() {
        return 0;
    }
}
