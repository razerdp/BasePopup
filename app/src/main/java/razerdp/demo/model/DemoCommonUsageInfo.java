package razerdp.demo.model;

import android.os.Bundle;
import android.view.View;

import razerdp.demo.base.baseadapter.MultiType;
import razerdp.demo.utils.UnSafeUtil;

/**
 * Created by 大灯泡 on 2019/9/20
 * <p>
 * Description：
 */
public abstract class DemoCommonUsageInfo implements MultiType {
    public String title;
    public String desc;

    public DemoCommonUsageInfo() {
    }


    @Override
    public int getItemType() {
        return 1;
    }

    public abstract void toShow(View v);

    public abstract void toOption(View v);

    public void shoTips(View v){

    }


}
