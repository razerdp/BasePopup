package razerdp.demo.utils;

import android.graphics.Typeface;
import android.os.Parcel;
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

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import razerdp.demo.app.AppContext;
import razerdp.demo.widget.span.CustomImageSpan;

/**
 * Created by 大灯泡 on 2019/4/9.
 * <p>
 * 多span样式util
 */
public class SpanUtil {
    private SpanUtil() {
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
                if (itemOption.matchLast) {
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
                            if (itemOption.matchFirst) {
                                index = -1;
                            } else {
                                index = source.indexOf(itemOption.getKeyWord(), index + itemOption.getKeyWord().length());
                            }
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
                    if (itemOption.onSpanClickListener != null || itemOption.isUrl) {
                        textView.setMovementMethod(LinkMovementMethod.getInstance());
                    }
                }
            }
        }

        public void into(TextView... textView) {
            if (textView == null) return;
            for (TextView view : textView) {
                if (view == null) continue;
                view.setText(getSpannableStringBuilder(view.getCurrentTextColor()));
                if (mOption != null && mOption.mItemOptions != null) {
                    for (ItemOption itemOption : mOption.mItemOptions) {
                        if (itemOption.onSpanClickListener != null || itemOption.isUrl) {
                            view.setMovementMethod(LinkMovementMethod.getInstance());
                        }
                    }
                }
            }

        }

        private void applySpan(SpannableStringBuilder spanBuilder, String keyWord, @NonNull ItemOption option, int index) {
            if (index <= -1 || spanBuilder == null || option == null || TextUtils.isEmpty(keyWord) || (index + keyWord.length() <= 0))
                return;
            //color
            if (option.textColor != 0) {
                spanBuilder.setSpan(new ForegroundColorSpan(option.textColor), index, index + keyWord
                        .length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            //texttype
            if (option.textType != Typeface.DEFAULT) {
                spanBuilder.setSpan(new StyleSpan(option.textType.getStyle()), index, index + keyWord
                        .length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            //textsize
            if (option.textSize != -1) {
                spanBuilder.setSpan(new AbsoluteSizeSpan(option.textSize), index, index + keyWord
                        .length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            //underline
            if (option.underLine) {
                spanBuilder.setSpan(new UnderlineSpan(), index, index + keyWord
                        .length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            //deleteline
            if (option.deleteLine) {
                spanBuilder.setSpan(new StrikethroughSpan(), index, index + keyWord
                        .length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            //urlSpan
            if (option.isUrl) {
                spanBuilder.setSpan(new UrlSpanEx(keyWord, option.urlColor, option.onUrlClickListener), index, index + keyWord
                        .length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            //bgcolor
            if (option.bgColor != -1) {
                spanBuilder.setSpan(new BackgroundColorSpan(option.bgColor), index, index + keyWord.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            //gravity
            if (option.textGravity != null) {
                spanBuilder.setSpan(new AlignmentSpan.Standard(option.textGravity), index, index + keyWord.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            //clickspan
            if (option.onSpanClickListener != null) {
                spanBuilder.setSpan(new ClickableSpanEx(option.onSpanClickListener).setTextColor(option.textColor), index, index + keyWord.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            //img
            if (option.imageId != 0) {
                try {
                    CustomImageSpan imageSpan = new CustomImageSpan(AppContext.getResources().getDrawable(option.imageId));
                    imageSpan.setAequilate(option.aequilate);
                    imageSpan.setImagePdding(option.imagePadding);
                    spanBuilder.setSpan(imageSpan, index, index + keyWord.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
        private boolean matchFirst = false;
        private boolean matchLast = false;
        private int textSize = -1;
        private Typeface textType = Typeface.DEFAULT;
        private int textColor = 0;
        private boolean underLine = false;
        private boolean isUrl = false;
        private boolean deleteLine = false;
        private View.OnClickListener onUrlClickListener;
        private int urlColor = -1;
        private int bgColor = -1;
        private Layout.Alignment textGravity = null;
        private View.OnClickListener onSpanClickListener;
        private int imageId;
        private boolean aequilate;
        private int imagePadding;

        public ItemOption(CharSequence keyWord, MultiSpanOption option) {
            super(keyWord, option);
        }

        @Override
        ItemOption appendInternal(MultiSpanOption mOption, ItemOption option, CharSequence keyWord) {
            return mOption.appendInternal(option, keyWord);
        }


        public ItemOption matchLast(boolean matchLast) {
            this.matchLast = matchLast;
            return this;
        }

        public ItemOption matchFirst(boolean matchFirst) {
            this.matchFirst = matchFirst;
            return this;
        }

        public ItemOption setTextSize(int spTextSize) {
            this.textSize = sp2px(spTextSize);
            return this;
        }


        public ItemOption setTextStyle(Typeface textType) {
            this.textType = textType;
            return this;
        }


        public ItemOption setTextColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        public ItemOption setTextColorRes(@ColorRes int textColor) {
            return setTextColor(UIHelper.getColor(textColor));
        }


        public ItemOption setUnderLine(boolean underLine) {
            this.underLine = underLine;
            return this;
        }


        public ItemOption setDeleteLine(boolean deleteLine) {
            this.deleteLine = deleteLine;
            return this;
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

        public ItemOption setBackgroundResourceColor(@ColorRes int bgColor) {
            this.bgColor = UIHelper.getColor(bgColor);
            return this;
        }

        public ItemOption setTextGravity(Layout.Alignment textGravity) {
            this.textGravity = textGravity;
            return this;
        }

        public ItemOption setSpanClickListener(View.OnClickListener onSpanClickListener) {
            this.onSpanClickListener = onSpanClickListener;
            return this;
        }

        public ItemOption setImageId(int imageId) {
            this.imageId = imageId;
            return this;
        }

        public ItemOption setAequilate(boolean aequilate) {
            this.aequilate = aequilate;
            return this;
        }

        public ItemOption setImagePadding(int imagePadding) {
            this.imagePadding = imagePadding;
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
