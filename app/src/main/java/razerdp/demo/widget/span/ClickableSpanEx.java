package razerdp.demo.widget.span;

import android.text.TextPaint;
import android.text.style.ClickableSpan;

/**
 * Created by 大灯泡 on 2017/4/18.
 * <p>
 * 可以设置颜色的clickspan
 */

public abstract class ClickableSpanEx extends ClickableSpan {
    private int backgroundColor = -1;
    private int linkColor = -1;
    private boolean needUnderLine;

    public ClickableSpanEx() {
        super();
    }

    public ClickableSpanEx(int backgroundColor) {
        this(backgroundColor, true);
    }

    public ClickableSpanEx(int backgroundColor, boolean needUnderLine) {
        this(backgroundColor, -1, needUnderLine);
    }

    public ClickableSpanEx(int backgroundColor, int linkColor, boolean needUnderLine) {
        this.backgroundColor = backgroundColor;
        this.linkColor = linkColor;
        this.needUnderLine = needUnderLine;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(needUnderLine);
        ds.setColor(backgroundColor == -1 ? (linkColor != -1 ? linkColor : ds.linkColor) : backgroundColor);
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public boolean isNeedUnderLine() {
        return needUnderLine;
    }

    public void setNeedUnderLine(boolean needUnderLine) {
        this.needUnderLine = needUnderLine;
    }

    public int getLinkColor() {
        return linkColor;
    }

    public void setLinkColor(int linkColor) {
        this.linkColor = linkColor;
    }
}
