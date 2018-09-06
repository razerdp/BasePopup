package razerdp.util.log;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.logging.Logger;

import static razerdp.util.log.LogTag.i;


/**
 * Created by 大灯泡 on 2017/4/19.
 * <p>
 * 重构log日志类
 */
public class PopupLogUtil {
    // 获取堆栈信息会影响性能，发布应用时记得关闭DebugMode
    private static final boolean[] mIsDebugMode = {false};
    private static final String TAG = "BasePopup";
    //logcat最大长度为4*1024，此处取4000
    private static final int MAX_LOG_MSG_LENGTH = 4000;
    //超长Log？
    private static final boolean LOG_LONG = true;

    public static void trace(String msg) {
        trace(i, msg);
    }

    public static void trace(Throwable throwable) {
        trace(i, throwable);
    }

    public static void trace(LogTag method, String msg) {
        trace(method, TAG, msg);
    }

    public static void trace(LogTag method, Throwable throwable) {
        trace(method, TAG, throwable);
    }

    public static void trace(LogTag method, String tag, String msg) {
        trace(method, tag, msg, null);
    }

    public static void trace(LogTag method, String tag, Throwable throwable) {
        trace(method, tag, null, throwable);
    }

    public static void trace(LogTag method, String tag, String msg, Throwable throwable) {
        if (!checkOpenLog()) return;
        traceInternal(method, tag, getLogMsg(msg, throwable));
    }

    /**
     * 此处会将过长的logcat拆分多次log
     *
     * @param tag
     * @param content
     */
    private static void traceInternal(LogTag method, String tag, String content) {
        if (!LOG_LONG) {
            logByMethod(method, tag, content);
        } else {
            try {
                long length = content.length();
                if (length <= MAX_LOG_MSG_LENGTH) {
                    logByMethod(method, tag, content);
                } else {
                    while (content.length() > MAX_LOG_MSG_LENGTH) {
                        String logContent = content.substring(0, MAX_LOG_MSG_LENGTH);
                        content = content.replace(logContent, "");
                        logByMethod(method, tag, logContent);
                    }
                    logByMethod(method, tag, content);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private static void logByMethod(LogTag method, String tag, String outPutMsg) {
        switch (method) {
            case i:
                Log.i(tag, outPutMsg);
                break;
            case d:
                Log.d(tag, outPutMsg);
                break;
            case w:
                Log.w(tag, outPutMsg);
                break;
            case e:
                Log.e(tag, outPutMsg);
                break;
            case v:
                Log.v(tag, outPutMsg);
                break;
            default:
                Log.i(tag, outPutMsg);
                break;
        }
    }


    private static String getLogMsg(String msg, Throwable throwable) {
        if (!TextUtils.isEmpty(msg)) return wrapLogWithMethodLocation(msg);
        if (throwable != null) return wrapLogWithMethodLocation(getCrashInfo(throwable));
        return "没有日志哦";
    }


    private static boolean checkOpenLog() {
        return mIsDebugMode[0];
    }

    public static void setOpenLog(boolean openLog) {
        mIsDebugMode[0] = openLog;
    }

    public static boolean isOpenLog() {
        return mIsDebugMode[0];
    }


    //-----------------------------------------tool-----------------------------------------


    /**
     * 代码定位
     */
    private static String wrapLogWithMethodLocation(String msg) {
        StackTraceElement element = getCurrentStackTrace();
        String className = "unknow";
        String methodName = "unknow";
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

    public static String getCrashInfo(Throwable tr) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        tr.printStackTrace(printWriter);
        Throwable cause = tr.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        String crashInfo = writer.toString();
        printWriter.close();
        return crashInfo;
    }

    /**
     * 获取当前栈信息
     *
     * @return
     */
    private static StackTraceElement getCurrentStackTrace() {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        int stackOffset = getStackOffset(trace, PopupLogUtil.class);
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


}
