package razerdp.demo.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import razerdp.demo.app.AppContext;
import razerdp.util.log.PopupLog;

/**
 * Created by 大灯泡 on 2017/4/18.
 * <p>
 * 常用工具类
 */

public class ToolUtil {
    public static boolean isEmpty(Collection<?> datas) {
        return datas == null || datas.size() <= 0;
    }

    public static boolean isEmpty(Map map) {
        return map == null || map.isEmpty();
    }

    public static boolean isListEmptyOrEmptyElements(List<?> datas) {
        if (isEmpty(datas)) return true;
        for (Object data : datas) {
            if (data != null) {
                return false;
            }
        }
        return true;
    }

    public static boolean indexIn(Collection<?> target, int index) {
        if (target == null || isEmpty(target)) return false;
        return index >= 0 && index < target.size();
    }

    /**
     * 复制到剪切板
     *
     * @param szContent
     */
    @SuppressLint({"NewApi", "ServiceCast"})
    public static void copyToClipboard(String szContent) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) AppContext.getAppContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", szContent);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
        }
    }

    /**
     * 获取剪贴板中的第一条数据
     */
    public static String getDataFromClipboard() {
        ClipboardManager clipboard = (ClipboardManager) AppContext.getAppContext().getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard == null) {
            return "";
        }

        ClipData clipData = clipboard.getPrimaryClip();
        if (clipData != null && clipData.getItemCount() > 0) {
            return clipData.getItemAt(0).getText().toString();
        } else {
            return "";
        }
    }

    /**
     * 清空剪贴板
     */
    public static void clearClipboard() {
        ClipboardManager clipboard = (ClipboardManager) AppContext.getAppContext().getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard == null) {
            return;
        }
        clipboard.setPrimaryClip(ClipData.newPlainText(null, ""));
    }

    /**
     * 复制到剪切板
     *
     * @param szContent
     */
    @SuppressLint({"NewApi", "ServiceCast"})
    public static void copyToClipboardAndToast(String szContent) {
        Context context = AppContext.getAppContext();

        String sourceText = szContent;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(sourceText);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied Text", sourceText);
            clipboard.setPrimaryClip(clip);
        }
        UIHelper.toast("复制成功");
    }

    public static boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    public static boolean indexInList(List<?> target, int index) {
        if (target == null) return false;
        return index >= 0 && index < target.size();
    }

    public static boolean openInSystemBroswer(Context context, String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
            if (!hasPreferredApplication(context, intent)) {
                intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean hasPreferredApplication(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();
        ResolveInfo info = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return !"android".equals(info.activityInfo.packageName);
    }

    public static String getProcessName(Context context, int pid) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfoList = am.getRunningAppProcesses();
        if (processInfoList == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo processInfo : processInfoList) {
            if (processInfo.pid == pid) {
                return processInfo.processName;
            }
        }
        return null;
    }

    public static <I, O> O cast(I input, Class<O> outClass, O... defaultValue) {
        if (input != null && outClass.isAssignableFrom(input.getClass())) {
            try {
                return outClass.cast(input);
            } catch (ClassCastException e) {
                PopupLog.e(e);
            }
        }
        if (defaultValue != null && defaultValue.length > 0) {
            return defaultValue[0];
        }
        return null;

    }

    public static <T> List<T> subList(List<T> source, int max) {
        if (isEmpty(source)) return source;
        return source.size() > max ? source.subList(0, max) : source;
    }
}
