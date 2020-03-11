package razerdp.demo.model.common;

import android.view.View;

import razerdp.demo.model.DemoCommonUsageInfo;
import razerdp.demo.popup.PopupDesc;
import razerdp.demo.utils.RomUtils;

/**
 * Created by 大灯泡 on 2019/9/27
 * <p>
 * Description：兼容性测试 - 手势导航栏
 */
public class CommonGestureNavInfo extends DemoCommonUsageInfo {
    PopupDesc popupDesc;

    public CommonGestureNavInfo() {
        title = "各种手势导航栏测试\n" + "系统：" + RomUtils.getRomInfo().getName();
    }

    @Override
    public void toShow(View v) {
        if (popupDesc == null) {
            popupDesc = new PopupDesc(v.getContext());
            popupDesc.setTitle("手势导航栏测试");
            popupDesc.setDesc("主要测试手势导航栏情况下背景蒙层能否铺满");
        }
        popupDesc.showPopupWindow();
    }

    @Override
    public void toOption(View v) {

    }
}
