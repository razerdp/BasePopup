package razerdp.util.log;

import android.view.MotionEvent;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.view.MotionEvent.ACTION_BUTTON_PRESS;
import static android.view.MotionEvent.ACTION_BUTTON_RELEASE;
import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_HOVER_ENTER;
import static android.view.MotionEvent.ACTION_HOVER_EXIT;
import static android.view.MotionEvent.ACTION_HOVER_MOVE;
import static android.view.MotionEvent.ACTION_MASK;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_OUTSIDE;
import static android.view.MotionEvent.ACTION_POINTER_DOWN;
import static android.view.MotionEvent.ACTION_POINTER_INDEX_MASK;
import static android.view.MotionEvent.ACTION_POINTER_INDEX_SHIFT;
import static android.view.MotionEvent.ACTION_POINTER_UP;
import static android.view.MotionEvent.ACTION_SCROLL;
import static android.view.MotionEvent.ACTION_UP;

class LogPrinterParser {

    static String parseContent(Object... obj) {
        StringBuilder buffer = new StringBuilder();
        if (obj != null) {
            if (obj.length > 1) {
                buffer.append(" {  ");
            }
            int i = 0;
            for (Object o : obj) {
                buffer.append("params【")
                        .append(i)
                        .append("】")
                        .append(" = ")
                        .append(parseContentInternal(o));
                if (i < obj.length - 1) {
                    buffer.append(" , ");
                }
                i++;
            }
            if (obj.length > 1) {
                buffer.append("  }");
            }
        }
        return buffer.toString();
    }

    private static String parseContentInternal(Object obj) {
        String result = null;
        if (obj instanceof String) {
            result = (String) obj;
        } else if (obj instanceof Throwable) {
            result = fromThrowable((Throwable) obj);
        } else if (obj instanceof List) {
            result = fromList((List) obj);
        } else if (obj instanceof Map) {
            result = fromMap((Map) obj);
        } else if (obj instanceof MotionEvent) {
            result = fromMotionEvent((MotionEvent) obj);
        } else {
            result = String.valueOf(obj);
        }
        return result;
    }

    private static String fromMotionEvent(MotionEvent motionEvent) {
        return actionToString(motionEvent.getAction());
    }

    public static String actionToString(int action) {
        switch (action) {
            case ACTION_DOWN:
                return "ACTION_DOWN";
            case ACTION_UP:
                return "ACTION_UP";
            case ACTION_CANCEL:
                return "ACTION_CANCEL";
            case ACTION_OUTSIDE:
                return "ACTION_OUTSIDE";
            case ACTION_MOVE:
                return "ACTION_MOVE";
            case ACTION_HOVER_MOVE:
                return "ACTION_HOVER_MOVE";
            case ACTION_SCROLL:
                return "ACTION_SCROLL";
            case ACTION_HOVER_ENTER:
                return "ACTION_HOVER_ENTER";
            case ACTION_HOVER_EXIT:
                return "ACTION_HOVER_EXIT";
            case ACTION_BUTTON_PRESS:
                return "ACTION_BUTTON_PRESS";
            case ACTION_BUTTON_RELEASE:
                return "ACTION_BUTTON_RELEASE";
        }
        int index = (action & ACTION_POINTER_INDEX_MASK) >> ACTION_POINTER_INDEX_SHIFT;
        switch (action & ACTION_MASK) {
            case ACTION_POINTER_DOWN:
                return "ACTION_POINTER_DOWN(" + index + ")";
            case ACTION_POINTER_UP:
                return "ACTION_POINTER_UP(" + index + ")";
            default:
                return Integer.toString(action);
        }
    }

    static String fromThrowable(Throwable tr) {
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

    static String fromMap(Map map) {
        if (map == null) {
            return "map is null";
        }
        if (map.isEmpty()) {
            return "map is empty";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("\n")
                .append("{")
                .append("\n")
                .append("\t");
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            builder.append(String.format("\t%1$s : %2$s", String.valueOf(entry.getKey()), String.valueOf(entry.getValue())));
            builder.append("\n");
        }
        builder.append("}");
        return builder.toString();
    }

    static String fromList(List list) {
        if (list == null) {
            return "list is null";
        }
        if (list.isEmpty()) {
            return "list is empty";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("\n")
                .append("{\n ");
        for (Object o : list) {
            if (o instanceof List) {
                builder.append(fromList((List) o));
            } else {
                builder.append(String.valueOf(o))
                        .append(" ,\n ");
            }
        }
        builder.append("}");
        return builder.toString();

    }
}
