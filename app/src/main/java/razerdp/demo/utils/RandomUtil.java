package razerdp.demo.utils;

import android.graphics.Color;

import java.util.Random;

/**
 * Created by 大灯泡 on 2019/6/28
 * <p>
 * Description：
 */
public class RandomUtil {
    private static final String CHAR_LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String CHAR_NUMBER = "0123456789";
    private static final String CHAR_ALL = CHAR_NUMBER + CHAR_LETTERS;

    private static final Random RANDOM = new Random();

    /**
     * 产生长度为length的随机字符串（包括字母和数字）
     *
     * @param length
     * @return
     */
    public static String randomString(int length) {
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(CHAR_ALL.charAt(RANDOM.nextInt(CHAR_ALL.length())));
        }
        return sb.toString();
    }

    public static String randomNonNumberString(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(CHAR_LETTERS.charAt(random.nextInt(CHAR_LETTERS.length())));
        }
        return sb.toString();
    }

    public static String randomLowerNonNumberString(int length) {
        return randomNonNumberString(length).toLowerCase();
    }

    public static String randomUpperNonNumberString(int length) {
        return randomNonNumberString(length).toUpperCase();
    }

    public static int randomInt(int min, int max) {
        
        return RANDOM.nextInt(max - min + 1) + min;
    }

    public static float randomFloat() {
        
        return RANDOM.nextFloat();
    }

    public static boolean randomBoolean() {
        return new Random().nextBoolean();
    }

    public static String randomIntString(int min, int max) {
        return String.valueOf(randomInt(min, max));
    }

    public static int randomColor() {
        return randomColor(false);
    }

    public static int randomColor(boolean withAlpha) {
        
        return withAlpha ?
                Color.rgb(RANDOM.nextInt(256), RANDOM.nextInt(256), RANDOM.nextInt(256))
                :
                Color.argb(RANDOM.nextInt(256), RANDOM.nextInt(256), RANDOM.nextInt(256), RANDOM.nextInt(256));
    }
}
