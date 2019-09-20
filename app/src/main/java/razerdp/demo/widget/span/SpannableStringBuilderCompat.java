package razerdp.demo.widget.span;

import android.text.SpannableStringBuilder;

/**
 * Created by 大灯泡 on 2019/4/9.
 */
public class SpannableStringBuilderCompat extends SpannableStringBuilder {
    public SpannableStringBuilderCompat() {
        super("");
    }

    public SpannableStringBuilderCompat(CharSequence text) {
        super(text, 0, text.length());
    }

    public SpannableStringBuilderCompat(CharSequence text, int start, int end) {
        super(text, start, end);
    }

    public SpannableStringBuilderCompat append(CharSequence text) {
        if (text == null) return this;
        int length = length();
        return (SpannableStringBuilderCompat) replace(length, length, text, 0, text.length());
    }

    /** 该方法在原API里面只支持API21或者以上，这里抽取出来以适应低版本 */
    public SpannableStringBuilderCompat append(CharSequence text, Object what, int flags) {
        if (text == null) return this;
        int start = length();
        append(text);
        setSpan(what, start, length(), flags);
        return this;
    }
}
