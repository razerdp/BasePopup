package razerdp.demo.model.common;

import android.view.View;

import razerdp.demo.model.DemoCommonUsageInfo;

/**
 * Created by 大灯泡 on 2019/10/15
 * <p>
 * Description：
 */
public class CommonAutoLocatedInfo extends DemoCommonUsageInfo {
    public CommonAutoLocatedInfo() {
        title = "自动移位";
        desc = "当BasePopup四周空间不足的时候自动显示在其镜像位置";
    }

    @Override
    public void toShow(View v) {

    }

    @Override
    public void toOption(View v) {

    }
}
