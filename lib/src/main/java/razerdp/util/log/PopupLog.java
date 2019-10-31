package razerdp.util.log;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;


/**
 * Created by 大灯泡 on 2017/4/19.
 * <p>
 * 重构log日志类
 */
public class PopupLog {

    private enum LogMethod {
        i,
        d,
        w,
        e,
        v
    }


    private static final AtomicBoolean sLOG = new AtomicBoolean(false);
    private static final String TAG = "BasePopup";

    //logcat最大长度为4*1024，此处取4000
    private static final int MAX_LOG_MSG_LENGTH = 4000;
    //超长Log？
    private static final boolean LOG_LONG = true;

    private static void logInternal(LogMethod method, String tag, Object... content) {
        if (!isOpenLog()) return;
        if (LOG_LONG) {
            try {
                String logCat = getContent(content);
                long length = logCat.length();
                if (length <= MAX_LOG_MSG_LENGTH) {
                    logMethod(method, tag, logCat);
                } else {
                    while (logCat.length() > MAX_LOG_MSG_LENGTH) {
                        String logContent = logCat.substring(0, MAX_LOG_MSG_LENGTH);
                        logCat = logCat.replace(logContent, "");
                        logMethod(method, tag, logCat);
                    }
                    logMethod(method, tag, logCat);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            logMethod(method, tag, getContent(content));
        }
    }

    private static void logMethod(LogMethod method, String tag, String logCat) {
        if (!isOpenLog()) return;
        switch (method) {
            case d:
                Log.d(tag, logCat);
                break;
            case e:
                Log.e(tag, logCat);
                break;
            case i:
                Log.i(tag, logCat);
                break;
            case v:
                Log.v(tag, logCat);
                break;
            case w:
                Log.w(tag, logCat);
                break;
            default:
                Log.i(tag, logCat);
                break;
        }
    }


    private static String getContent(Object... content) {
        String result = LogPrinterParser.parseContent(content);
        return wrapLogWithMethodLocation(result);
    }

    //region ===============================代码定位===============================
    private static String wrapLogWithMethodLocation(String msg) {
        StackTraceElement element = getCurrentStackTrace();
        String className = "unknown";
        String methodName = "unknown";
        int lineNumber = -1;
        if (element != null) {
            className = element.getFileName();
            methodName = element.getMethodName();
            lineNumber = element.getLineNumber();
        }

        StringBuilder sb = new StringBuilder();
        msg = wrapJson(msg);
        sb.append("  (")
                .append(className)
                .append(":")
                .append(lineNumber)
                .append(") #")
                .append(methodName)
                .append("：")
                .append('\n')
                .append(msg);
        return sb.toString();
    }

    private static StackTraceElement getCurrentStackTrace() {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        int stackOffset = getStackOffset(trace, PopupLog.class);
        if (stackOffset == -1) {
            stackOffset = getStackOffset(trace, Logger.class);
            if (stackOffset == -1) {
                stackOffset = getStackOffset(trace, Log.class);
                if (stackOffset == -1) {
                    return null;
                }
            }
        }
        return trace[stackOffset];
    }

    private static int getStackOffset(StackTraceElement[] trace, Class cla) {
        int logIndex = -1;
        for (int i = 0; i < trace.length; i++) {
            StackTraceElement element = trace[i];
            String tClass = element.getClassName();
            if (TextUtils.equals(tClass, cla.getName())) {
                logIndex = i;
            } else {
                if (logIndex > -1) break;
            }
        }
        if (logIndex != -1) {
            logIndex++;
            if (logIndex >= trace.length) {
                logIndex = trace.length - 1;
            }
        }
        return logIndex;
    }

    public static String wrapLocation(Class cla, int lineNumber) {
        return ".(" + cla.getSimpleName() + ".java:" + lineNumber + ")";
    }

    public static String wrapJson(String jsonStr) {
        String message;
        if (TextUtils.isEmpty(jsonStr)) return "json为空";
        try {
            if (jsonStr.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(jsonStr);
                message = jsonObject.toString(2);
                message = "\n================JSON================\n"
                        + message + '\n'
                        + "================JSON================\n";
            } else if (jsonStr.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(jsonStr);
                message = jsonArray.toString(4);
                message = "\n================JSONARRAY================\n"
                        + message + '\n'
                        + "================JSONARRAY================\n";
            } else {
                message = jsonStr;
            }
        } catch (JSONException e) {
            message = jsonStr;
        }

        return message;
    }


    //endregion ===============================代码定位===============================

    public static void setOpenLog(boolean openLog) {
        sLOG.set(openLog);
    }

    public static boolean isOpenLog() {
        return sLOG.get();
    }

    //region ===============================log===============================
    public static void i(Object content) {
        i(TAG, content);
    }

    public static void i(String tag, Object... content) {
        logInternal(LogMethod.i, tag, content);
    }

    public static void d(Object... content) {
        d(TAG, content);
    }

    public static void d(String tag, Object... content) {
        logInternal(LogMethod.d, tag, content);
    }

    public static void e(Object... content) {
        e(TAG, content);
    }

    public static void e(String tag, Object... content) {
        logInternal(LogMethod.e, tag, content);
    }

    public static void v(Object... content) {
        v(TAG, content);
    }

    public static void v(String tag, Object... content) {
        logInternal(LogMethod.v, tag, content);
    }

    public static void w(Object... content) {
        w(TAG, content);
    }

    public static void w(String tag, Object... content) {
        logInternal(LogMethod.w, tag, content);
    }
    //endregion ===============================log===============================
}
