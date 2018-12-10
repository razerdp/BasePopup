package razerdp.demo.utils;

import android.graphics.Typeface;
import android.os.Parcel;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import razerdp.demo.app.AppContext;

/**
 * Created by 大灯泡 on 2017/12/21.
 * <p>
 * 多span样式util
 */
public class MultiSpanUtil {
    private MultiSpanUtil() {
    }

    public static MultiSpanOption create(@StringRes int strId, Object... objs) {
        return create(StringUtil.getString(strId, objs));
    }

    public static MultiSpanOption create(@StringRes int source) {
        return create(StringUtil.getString(source));
    }

    public static MultiSpanOption create(CharSequence source) {
        return new MultiSpanOption(source);
    }

    abstract static class BaseItemOption<T> {
        private MultiSpanOption mOption;
        private CharSequence keyWord;

        private BaseItemOption(@StringRes int keyWord, MultiSpanOption option) {
            this(StringUtil.getString(keyWord), option);
        }

        private BaseItemOption(CharSequence keyWord, MultiSpanOption option) {
            this.mOption = option;
            this.keyWord = keyWord;
        }

        public T append() {
            return append(mOption.sourceToString());
        }

        public T append(@StringRes int keyWord, Object... formatted) {
            return append(StringUtil.getString(keyWord, formatted));
        }

        public T append(@StringRes int keyWord) {
            return append(StringUtil.getString(keyWord));
        }

        public T append(String keyWord) {
            return appendInternal(mOption, (T) this, keyWord);
        }

        abstract T appendInternal(MultiSpanOption mOption, T option, CharSequence keyWord);

        public String getKeyWord() {
            return TextUtils.isEmpty(keyWord) ? "" : keyWord.toString();
        }

        public SpannableStringBuilder getSpannableStringBuilder() {
            return getSpannableStringBuilder(-1);
        }

        public SpannableStringBuilder getSpannableStringBuilder(int defaultTextColor) {
            append(null);
            SpannableStringBuilder result = new SpannableStringBuilder(mOption.source);
            if (defaultTextColor != -1) {
                result.setSpan(new ForegroundColorSpan(defaultTextColor), 0, mOption.source.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (TextUtils.isEmpty(mOption.source)) return result;
            final int sourceStrLen = mOption.source.length();
            final String source = mOption.sourceToString();

            for (ItemOption itemOption : mOption.mItemOptions) {
                if (TextUtils.isEmpty(itemOption.getKeyWord())) continue;
                if (itemOption.isMatchLast()) {
                    int index = source.lastIndexOf(itemOption.getKeyWord());

                    if (index < sourceStrLen && index >= 0) {
                        applySpan(result, itemOption.getKeyWord(), itemOption, index);
                    }
                } else {
                    int index = source.indexOf(itemOption.getKeyWord());
                    if (index >= 0) {
                        // 遍历关键字，当关键字没有的时候,跳出循环
                        while (index < sourceStrLen && index >= 0) {
                            applySpan(result, itemOption.getKeyWord(), itemOption, index);
                            index = source.indexOf(itemOption.getKeyWord(), index + itemOption.getKeyWord().length());
                        }
                    }
                }
            }
            return result;

        }

        public void into(TextView textView) {
            if (textView == null) return;
            textView.setText(getSpannableStringBuilder(textView.getCurrentTextColor()));
            if (mOption != null && mOption.mItemOptions != null) {
                for (ItemOption itemOption : mOption.mItemOptions) {
                    if (itemOption.getOnSpanClickListener() != null || itemOption.isUrl) {
                        textView.setMovementMethod(LinkMovementMethod.getInstance());
                    }
                }
            }
        }

        private void applySpan(SpannableStringBuilder spanBuilder, String keyWord, @NonNull ItemOption option, int index) {
            if (index <= -1 || spanBuilder == null || option == null || TextUtils.isEmpty(keyWord) || (index + keyWord.length() <= 0))
                return;
            //color
            if (option.changeColor) {
                spanBuilder.setSpan(new ForegroundColorSpan(option.getTextColor()), index, index + keyWord
                        .length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            //texttype
            if (option.getTextType() != Typeface.DEFAULT) {
                spanBuilder.setSpan(new StyleSpan(option.getTextType().getStyle()), index, index + keyWord
                        .length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            //textsize
            if (option.getTextSize() != -1) {
                spanBuilder.setSpan(new AbsoluteSizeSpan(option.getTextSize()), index, index + keyWord
                        .length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            //underline
            if (option.isUnderLine()) {
                spanBuilder.setSpan(new UnderlineSpan(), index, index + keyWord
                        .length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            //deleteline
            if (option.isDeleteLine()) {
                spanBuilder.setSpan(new StrikethroughSpan(), index, index + keyWord
                        .length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            //urlSpan
            if (option.isUrl()) {
                spanBuilder.setSpan(new UrlSpanEx(keyWord, option.urlColor, option.onUrlClickListener), index, index + keyWord
                        .length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            //bgcolor
            if (option.getBgColor() != -1) {
                spanBuilder.setSpan(new BackgroundColorSpan(option.getBgColor()), index, index + keyWord.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            //gravity
            if (option.getTextGravity() != null) {
                spanBuilder.setSpan(new AlignmentSpan.Standard(option.getTextGravity()), index, index + keyWord.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            //clickspan
            if (option.getOnSpanClickListener() != null) {
                spanBuilder.setSpan(new ClickableSpanEx(option.getOnSpanClickListener()).setTextColor(option.getTextColor()), index, index + keyWord.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

        }
    }

    public static class MultiSpanOption {

        final CharSequence source;
        final List<ItemOption> mItemOptions;

        private MultiSpanOption(@StringRes int resid) {
            this(StringUtil.getString(resid));
        }

        private MultiSpanOption(@StringRes int resid, Object... formats) {
            this(StringUtil.getString(resid, formats));
        }

        private MultiSpanOption(CharSequence source) {
            this.source = TextUtils.isEmpty(source) ? "" : source;
            mItemOptions = new ArrayList<>();
        }

        public ItemOption append() {
            return append(sourceToString());
        }

        public ItemOption append(@StringRes int strId, Object... objs) {
            return append(StringUtil.getString(strId, objs));
        }

        public ItemOption append(@StringRes int keyWord) {
            return appendInternal(null, StringUtil.getString(keyWord));
        }

        public ItemOption append(String keyWord) {
            return appendInternal(null, keyWord);
        }

        ItemOption appendInternal(ItemOption option, CharSequence keyWord) {
            ItemOption result = new ItemOption(keyWord, this);
            if (option != null) {
                mItemOptions.add(option);
            }
            return result;
        }

        String sourceToString() {
            return source.toString();
        }

    }

    public static class ItemOption extends BaseItemOption<ItemOption> {
        private boolean matchLast = false;
        private int textSize = -1;
        private Typeface textType = Typeface.DEFAULT;
        private int textColor = -1;
        private boolean underLine = false;
        private boolean isUrl = false;
        private boolean deleteLine = false;
        private View.OnClickListener onUrlClickListener;
        private int urlColor = -1;
        private int bgColor = -1;
        private Layout.Alignment textGravity = null;
        private View.OnClickListener onSpanClickListener;
        private boolean changeColor;

        public ItemOption(CharSequence keyWord, MultiSpanOption option) {
            super(keyWord, option);
        }

        @Override
        ItemOption appendInternal(MultiSpanOption mOption, ItemOption option, CharSequence keyWord) {
            return mOption.appendInternal(option, keyWord);
        }

        public boolean isMatchLast() {
            return matchLast;
        }

        public ItemOption matchLast(boolean matchLast) {
            this.matchLast = matchLast;
            return this;
        }

        public int getTextSize() {
            return textSize;
        }

        public ItemOption setTextSize(int spTextSize) {
            this.textSize = sp2px(spTextSize);
            return this;
        }

        public Typeface getTextType() {
            return textType;
        }

        public ItemOption setTextType(Typeface textType) {
            this.textType = textType;
            return this;
        }

        public int getTextColor() {
            return textColor;
        }

        public ItemOption setTextColor(int textColor) {
            this.textColor = textColor;
            changeColor = true;
            return this;
        }

        public ItemOption setTextColorFromRes(@ColorRes int textColor) {
            return setTextColor(UIHelper.getColor(textColor));
        }

        public boolean isUnderLine() {
            return underLine;
        }

        public ItemOption setUnderLine(boolean underLine) {
            this.underLine = underLine;
            return this;
        }

        public boolean isDeleteLine() {
            return deleteLine;
        }

        public ItemOption setDeleteLine(boolean deleteLine) {
            this.deleteLine = deleteLine;
            return this;
        }

        public boolean isUrl() {
            return isUrl;
        }

        public ItemOption setUrl(boolean url) {
            return setUrl(url, null);
        }

        public ItemOption setUrl(boolean url, View.OnClickListener l) {
            return setUrl(url, -1, l);
        }

        public ItemOption setUrl(boolean url, int urlTextColor, View.OnClickListener l) {
            isUrl = url;
            urlColor = urlTextColor;
            onUrlClickListener = l;
            return this;
        }

        public ItemOption setBgColorFromRes(@ColorRes int bgColor) {
            this.bgColor = UIHelper.getColor(bgColor);
            return this;
        }

        public int getBgColor() {
            return bgColor;
        }

        public Layout.Alignment getTextGravity() {
            return textGravity;
        }

        public ItemOption setTextGravity(Layout.Alignment textGravity) {
            this.textGravity = textGravity;
            return this;
        }

        public View.OnClickListener getOnSpanClickListener() {
            return onSpanClickListener;
        }

        public ItemOption setSpanClickListener(View.OnClickListener onSpanClickListener) {
            this.onSpanClickListener = onSpanClickListener;
            return this;
        }
    }


    public static class UrlSpanEx extends URLSpan {
        int color = -1;
        private View.OnClickListener mOnClickListener;

        public UrlSpanEx(String url) {
            super(url);
        }

        public UrlSpanEx(Parcel src) {
            super(src);
        }

        public UrlSpanEx(String url, int color) {
            super(url);
            this.color = color;
        }

        public UrlSpanEx(String url, int color, View.OnClickListener onClickListener) {
            super(url);
            this.color = color;
            this.mOnClickListener = onClickListener;
        }

        public UrlSpanEx(Parcel src, int color) {
            super(src);
            this.color = color;
        }


        public View.OnClickListener getOnClickListener() {
            return mOnClickListener;
        }

        public void setOnClickListener(View.OnClickListener onClickListener) {
            mOnClickListener = onClickListener;
        }

        @Override
        public void onClick(View widget) {
            if (mOnClickListener != null) {
                mOnClickListener.onClick(widget);
            } else {
                super.onClick(widget);
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            if (color != -1) {
                ds.setColor(color);
            }
            ds.setUnderlineText(false);
        }

    }

    static class ClickableSpanEx extends ClickableSpan {
        private int backgroundColor = -1;
        private int textColor = -1;
        private boolean needUnderLine;
        private View.OnClickListener mOnClickListener;

        public ClickableSpanEx() {
            super();
        }

        public ClickableSpanEx(View.OnClickListener onClickListener) {
            mOnClickListener = onClickListener;
        }

        @Override
        public void onClick(View widget) {
            if (mOnClickListener != null) {
                mOnClickListener.onClick(widget);
            }
        }

        public ClickableSpanEx(int backgroundColor) {
            this(backgroundColor, true);
        }

        public ClickableSpanEx(int backgroundColor, boolean needUnderLine) {
            this(backgroundColor, -1, needUnderLine);
        }

        public ClickableSpanEx(int backgroundColor, int linkColor, boolean needUnderLine) {
            this.backgroundColor = backgroundColor;
            this.textColor = linkColor;
            this.needUnderLine = needUnderLine;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(needUnderLine);
            ds.setColor(backgroundColor == -1 ? (textColor != -1 ? textColor : ds.linkColor) : backgroundColor);
        }

        public int getBackgroundColor() {
            return backgroundColor;
        }

        public ClickableSpanEx setBackgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public int getTextColor() {
            return textColor;
        }

        public ClickableSpanEx setTextColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        public boolean isNeedUnderLine() {
            return needUnderLine;
        }

        public ClickableSpanEx setNeedUnderLine(boolean needUnderLine) {
            this.needUnderLine = needUnderLine;
            return this;
        }

        public View.OnClickListener getOnClickListener() {
            return mOnClickListener;
        }

        public ClickableSpanEx setOnClickListener(View.OnClickListener onClickListener) {
            mOnClickListener = onClickListener;
            return this;
        }
    }

    static int sp2px(float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spValue, AppContext.getAppContext().getResources().getDisplayMetrics());
    }
}
