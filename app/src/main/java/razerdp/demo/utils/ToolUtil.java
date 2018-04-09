package razerdp.demo.utils;

import java.util.List;

/**
 * Created by 大灯泡 on 2017/4/18.
 * <p>
 * 常用工具类
 */

public class ToolUtil {
    public static boolean isListEmpty(List<?> datas) {
        return datas == null || datas.size() <= 0;
    }
}
