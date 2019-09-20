package razerdp.demo.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by 大灯泡 on 2019/4/24.
 */
public class NumberFormatUtil {

    /**
     * etc.  120,000,000,000
     */
    public static DecimalFormat format0 = new DecimalFormat(",###");
    /**
     * etc. 120000000000.15
     */
    public static DecimalFormat format2 = new DecimalFormat("#.##");

    static {
        //不允许四舍五入
        format2.setRoundingMode(RoundingMode.FLOOR);
    }

    public static String format2(double num) {
        return format2.format(num);
    }
}
