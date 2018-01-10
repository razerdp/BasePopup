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
 * Created by 大灯泡 on 2017/12/27.
 * <p>
 * log日志类
 */
public class PopupLogUtil {

    // 获取堆栈信息会影响性能，发布应用时记得关闭DebugMode
    private static final boolean[] mIsDebugMode = {false};
    private static final String TAG = "basepopup";
    private static final String SUFFIX = ".java";
    //从栈底第三个开始追踪，因为第一个一般是虚拟机，第二个是线程，第三个是log
    private static final int sStartStackTraceOffset = 3;

    private static final String CLASS_LogTag_LINE_FORMAT = "%s.%s()_%s";
    private static final String sPACKAGENAME = "razerdp";
    //当精确模式启动，则会定位到具体的类，而非父类(更加耗时)
    private static final boolean isExactMode = false;
    //logcat最大长度为4*1024，此处取4000
    private static final int MAX_LOG_MSG_LENGTH = 4000;

    public static void trace(String msg) {
        trace(i, msg);
    }

    public static void trace(Throwable throwable) {
        trace(i, throwable);
    }

    public static void trace(LogTag LogTag, String msg) {
        trace(LogTag, TAG, msg);
    }

    public static void trace(LogTag LogTag, Throwable throwable) {
        trace(LogTag, TAG, throwable);
    }

    public static void trace(LogTag LogTag, String tag, String msg) {
        trace(LogTag, tag, msg, null);
    }

    public static void trace(LogTag LogTag, String tag, Throwable throwable) {
        trace(LogTag, tag, null, throwable);
    }

    public static void trace(LogTag LogTag, String tag, String msg, Throwable throwable) {
        if (!checkOpenLog()) return;
        traceInternal(LogTag, tag, getLogMsg(msg, throwable));
    }

    /**
     * 此处会将过长的logcat拆分多次log
     *
     * @param LogTag
     * @param tag
     * @param content
     */
    private static void traceInternal(LogTag LogTag, String tag, String content) {
//        logByLogTag(LogTag, tag, content);
        // !!暂时不采取拆分超长log!!!
        try {
            long length = content.length();
            if (length <= MAX_LOG_MSG_LENGTH) {
                logByLogTag(LogTag, tag, content);
            } else {
                while (content.length() > MAX_LOG_MSG_LENGTH) {
                    String logContent = content.substring(0, MAX_LOG_MSG_LENGTH);
                    content = content.replace(logContent, "");
                    logByLogTag(LogTag, tag, logContent);
                }
                logByLogTag(LogTag, tag, content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void logByLogTag(LogTag LogTag, String tag, String outPutMsg) {
        switch (LogTag) {
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
        if (!TextUtils.isEmpty(msg)) return wrapLogWithLogTagLocation(msg);
        if (throwable != null) return wrapLogWithLogTagLocation(getCrashInfo(throwable));
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
    private static String wrapLogWithLogTagLocation(String msg) {
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
                .append("： ")
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
        StackTraceElement caller = trace[stackOffset];
        return caller;
    }

    private static int getStackOffset(StackTraceElement[] trace, Class cla) {
        for (int i = sStartStackTraceOffset; i < trace.length; i++) {
            StackTraceElement e = trace[i];
            String name = e.getClassName();
            //寻找到调用logtrace的最后一个偏移
            if (cla.equals(PopupLogUtil.class) && i < trace.length - 1 && trace[i + 1].getClassName()
                                                                                      .equals(PopupLogUtil.class.getName())) {
                continue;
            }
            if (isExactMode) {
                //如果是精准模式，则开始匹配包名
                if (i < trace.length - 1) {
                    String nextStackName = trace[i + 1].getClassName();
                    if (name.startsWith(sPACKAGENAME) && !name.contains("$") &&
                            nextStackName != null && nextStackName.startsWith(sPACKAGENAME) && !nextStackName.contains("$")) {
                        //排除匿名内部类
                        continue;
                    } else {
                        return i;
                    }
                } else {
                    return i;
                }
            } else {
                if (name.equals(cla.getName())) {
                    return ++i;
                }
            }
        }
        return -1;
    }


}
