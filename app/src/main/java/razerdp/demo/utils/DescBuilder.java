package razerdp.demo.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 大灯泡 on 2019/9/20
 * <p>
 * Description：
 */
public class DescBuilder {
    private static final String SEP = "• ";
    private List<String> datas;

    private DescBuilder() {
        datas = new ArrayList<>();
    }

    public static DescBuilder get() {
        return new DescBuilder();
    }

    public DescBuilder append(String desc) {
        datas.add(desc);
        return this;
    }

    public String build() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < datas.size(); i++) {
            builder.append(SEP)
                    .append(datas.get(i));
            if (i < datas.size() - 1) {
                builder.append('\n');
            }
        }
        return builder.toString();
    }
}
