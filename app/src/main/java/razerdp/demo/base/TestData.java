package razerdp.demo.base;



import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import razerdp.demo.utils.FileUtil;
import razerdp.demo.utils.ToolUtil;

/**
 * Created by 大灯泡 on 2019/7/16
 * <p>
 * Description：
 */
public class TestData {

    private static List<String> pics;
    private static List<String> avatars;


    public static void init() {
        getPicUrl();
        getAvatar();
    }

    public static String getPicUrl() {
        if (ToolUtil.isEmpty(pics)) {
            Pattern pattern = Pattern.compile("(https?|ftp|file)://(?!(\\.jpg|\\.png|\\.gif)).+?(\\.jpg|\\.png|\\.gif)");
            pics = new ArrayList<>();
            String result = FileUtil.getFromAssets("pics");
            Matcher matcher = pattern.matcher(result);
            while (matcher.find()) {
                pics.add(matcher.group(0));
            }
        }
        Random random = new Random();
        return pics.get(random.nextInt(pics.size()));
    }

    public static String getAvatar() {
        if (ToolUtil.isEmpty(avatars)) {
            Pattern pattern = Pattern.compile("(https?|ftp|file)://(?!(\\.jpg|\\.png|\\.gif)).+?(\\.jpg|\\.png|\\.gif)");
            avatars = new ArrayList<>();
            String result = FileUtil.getFromAssets("avatars");
            Matcher matcher = pattern.matcher(result);
            while (matcher.find()) {
                avatars.add(matcher.group(0));
            }
        }
        Random random = new Random();
        return avatars.get(random.nextInt(avatars.size()));
    }
}
