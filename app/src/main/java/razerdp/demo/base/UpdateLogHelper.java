package razerdp.demo.base;

import java.util.ArrayList;
import java.util.List;

import razerdp.demo.model.updatelog.UpdateLogInfo;

/**
 * Created by 大灯泡 on 2019/9/24.
 */
public class UpdateLogHelper {

    private static List<UpdateLogInfo> sCache;

    public static List<UpdateLogInfo> getUpdateLogs() {
        if (sCache == null) {
            sCache = new ArrayList<>();
            buildUpdateLogs();
        }
        return sCache;
    }

    private static void buildUpdateLogs() {
        addRelease2_2_1();
    }

    private static void addRelease2_2_1() {
        UpdateLogInfo info = new UpdateLogInfo("【Release】2.2.1(2019/06/24)")
                .append("支持Service或者非ActivityContext里弹窗")
                .append("优化PopupUiUtils，优化获取屏幕宽高算法")
                .append("修改并优化键盘判断逻辑")
                .append("优化全屏状态下点击范围的判定，fixed #200").keyAt("#200").url("https://github.com/razerdp/BasePopup/issues/200")
                .end();
        sCache.add(info);
    }
}
