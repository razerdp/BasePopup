package razerdp.demo.app;

import android.content.Context;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import androidx.multidex.MultiDexApplication;
import razerdp.basepopup.BasePopupFlag;
import razerdp.basepopup.BasePopupWindow;
import razerdp.util.log.PopupLog;

/**
 * Created by 大灯泡 on 2018/12/4.
 */
public class PopupDemoApp extends MultiDexApplication {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        BasePopupWindow.setDebugMode(true);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //检查popup flag
        new Thread(() -> {
            try {
                checkFlag();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void checkFlag() throws Exception {
        Field[] fields = BasePopupFlag.class.getFields();
        Map<Integer, CheckFlagInfo> map = new HashMap<>();
        for (Field field : fields) {
            String name = field.getName();
            int value = field.getInt(null);
            if (name.endsWith("_SHIFT")) {
                continue;
            }
            CheckFlagInfo info = map.get(value);
            if (info == null) {
                info = new CheckFlagInfo(value);
                map.put(value, info);
            }
            info.addName(name);
            PopupLog.i("checkFlag", "name = " + name +
                    "\nbinaryInt = " + Integer.toBinaryString(value) +
                    "\nbinaryLen = " + Integer.toBinaryString(value).length());
        }
        for (Map.Entry<Integer, CheckFlagInfo> entry : map.entrySet()) {
            if (entry.getValue().size() > 1) {
                PopupLog.i("checkFlag", entry.getValue());
            }
        }
    }

    static class CheckFlagInfo {
        Set<String> names = new HashSet<>();
        int value;

        public CheckFlagInfo(int value) {
            this.value = value;
        }

        void addName(String name) {
            names.add(name);
        }

        int size() {
            return names.size();
        }

        @Override
        public String toString() {
            String builder = "重复flag:" + '\n' +
                    "names = {" +
                    TextUtils.join(" , ", names) +
                    " }" +
                    '\n' +
                    "value = " +
                    value;
            return builder;
        }
    }
}
