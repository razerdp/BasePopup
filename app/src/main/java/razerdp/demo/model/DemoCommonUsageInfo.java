package razerdp.demo.model;

import android.view.View;

import razerdp.demo.base.baseadapter.MultiType;

/**
 * Created by 大灯泡 on 2019/9/20
 * <p>
 * Description：
 */
public abstract class DemoCommonUsageInfo implements MultiType {
    public String title;
    public String desc;
    public String option;

    public DemoCommonUsageInfo() {
    }


    @Override
    public int getItemType() {
        return 1;
    }

    public abstract void toShow(View v);

    public abstract void toOption(View v);

}
