package razerdp.demo.utils;

import java.text.DecimalFormat;

/**
 * Created by 大灯泡 on 2019/4/25
 * <p>
 * Description：
 */
public class NumberUtils {


    public static String tenThousandNumber(String money) {
        if (NumberCompareUtil.compareWithEqual(money, "10000")) {
            return NumberFormatUtil.format2(StringUtil.toDouble(money) / 10000) + "万";
        } else {
            return NumberFormatUtil.format2(StringUtil.toDouble(money));
        }
    }

    public static float range(float value, float min, float max) {
        return Math.max(min, Math.min(value, max));
    }

    public static double range(double value, double min, double max) {
        return Math.max(min, Math.min(value, max));
    }

    public static int range(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }

    public static long range(long value, long min, long max) {
        return Math.max(min, Math.min(value, max));
    }

    public static boolean inRange(double value, double min, double max) {
        return value >= min && value <= max;
    }

    public static String formatDecimal(double value, int... saveCount) {
        StringBuilder pattern;
        if (saveCount != null && saveCount.length > 0) {
            pattern = new StringBuilder("0.");
            for (int i = 0; i < saveCount[0]; i++) {
                pattern.append("0");
            }
        } else {
            pattern = new StringBuilder("0");
        }

        DecimalFormat formatter = new DecimalFormat();
        formatter.applyPattern(pattern.toString());
        return formatter.format(value);
    }

}
