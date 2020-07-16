package razerdp.demo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import razerdp.basepopup.R;
import razerdp.demo.base.interfaces.MultiClickListener;
import razerdp.demo.utils.StringUtil;
import razerdp.demo.utils.UIHelper;
import razerdp.demo.utils.ViewUtil;

import static razerdp.demo.widget.TitleBarView.TitleBarMode.MODE_BOTH;
import static razerdp.demo.widget.TitleBarView.TitleBarMode.MODE_LEFT;
import static razerdp.demo.widget.TitleBarView.TitleBarMode.MODE_NONE;
import static razerdp.demo.widget.TitleBarView.TitleBarMode.MODE_RIGHT;


/**
 *
 */
public class TitleBarView extends FrameLayout implements View.OnClickListener, View.OnLongClickListener, View.OnLayoutChangeListener {
    private static final String TAG = "TitleBarView";

    View mRootView;
    View mStatusBarHolderView;
    TextView mTitleBarTitle;
    ImageView mTitleBarIconLeft;
    TextView mTitleBarTextLeft;
    TextView mTitleBarTextRight;
    ImageView mTitleBarIconRight;

    private static final int TITLE_PADDING = UIHelper.dip2px(16);

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({MODE_LEFT, MODE_RIGHT, MODE_BOTH, MODE_NONE})
    public @interface TitleBarMode {
        int MODE_LEFT = 16;
        int MODE_RIGHT = 17;
        int MODE_BOTH = 18;
        int MODE_NONE = 19;
    }

    protected int mode = MODE_LEFT;

    @DrawableRes
    int leftIcon = R.drawable.ic_back;
    @DrawableRes
    int rightIcon;

    CharSequence leftText;
    CharSequence rightText;
    CharSequence titleText = StringUtil.getString(R.string.app_name);

    @ColorInt
    int leftTextColor = Color.WHITE;
    @ColorInt
    int rightTextColor = Color.WHITE;
    @ColorInt
    int titleTextColor = Color.WHITE;

    int leftTextSize = 14;
    int rightTextSize = 14;
    int titleTextSize = 16;

    boolean hideStatusbarHolder = false;

    OnTitleBarClickCallback mTitlebarClickCallback;

    public TitleBarView(Context context) {
        this(context, null);
    }

    public TitleBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (getId() == NO_ID) {
            setId(R.id.title_bar_view);
        }
        initFromAttrs(context, attrs);
        initView(context);
    }

    private void initFromAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TitleBarView);
        mode = a.getInt(R.styleable.TitleBarView_mode, mode);
        leftIcon = a.getResourceId(R.styleable.TitleBarView_left_icon, leftIcon);
        rightIcon = a.getResourceId(R.styleable.TitleBarView_right_icon, rightIcon);
        leftText = a.getString(R.styleable.TitleBarView_left_text);
        rightText = a.getString(R.styleable.TitleBarView_right_text);
        titleText = a.getString(R.styleable.TitleBarView_title_text);

        leftTextSize = a.getDimensionPixelSize(R.styleable.TitleBarView_left_text_size, leftTextSize);
        rightTextSize = a.getDimensionPixelSize(R.styleable.TitleBarView_right_text_size, rightTextSize);
        titleTextSize = a.getDimensionPixelSize(R.styleable.TitleBarView_title_text_size, titleTextSize);

        leftTextColor = a.getColor(R.styleable.TitleBarView_left_text_color, leftTextColor);
        rightTextColor = a.getColor(R.styleable.TitleBarView_right_text_color, rightTextColor);
        titleTextColor = a.getColor(R.styleable.TitleBarView_title_text_color, titleTextColor);

        hideStatusbarHolder = a.getBoolean(R.styleable.TitleBarView_hide_status_bar_holder, hideStatusbarHolder);

        a.recycle();
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.widget_title_bar_view, this);
        this.mRootView = findViewById(R.id.title_bar_root);
        this.mStatusBarHolderView = findViewById(R.id.statusbar_placeholder);
        this.mTitleBarTitle = findViewById(R.id.title_bar_title);
        this.mTitleBarIconLeft = findViewById(R.id.title_bar_icon_left);
        this.mTitleBarTextLeft = findViewById(R.id.title_bar_text_left);
        this.mTitleBarTextRight = findViewById(R.id.title_bar_text_right);
        this.mTitleBarIconRight = findViewById(R.id.title_bar_icon_right);

        mTitleBarIconLeft.addOnLayoutChangeListener(this);
        mTitleBarTextLeft.addOnLayoutChangeListener(this);
        mTitleBarTextRight.addOnLayoutChangeListener(this);
        mTitleBarIconRight.addOnLayoutChangeListener(this);

        if (getBackground() != null) {
            ViewUtil.setBackground(mRootView, getBackground());
        }
        ViewUtil.setViewsClickListener(this, mTitleBarIconLeft, mTitleBarTextLeft, mTitleBarIconRight, mTitleBarTextRight);
        mTitleBarTitle.setOnLongClickListener(this);
        mTitleBarTitle.setOnClickListener(mMultiClickListener);
        setValues();
    }

    private void setValues() {
        setTitleTextSize(titleTextSize);
        setTitleTextColor(titleTextColor);
        setLeftTextSize(leftTextSize);
        setLeftTextColor(leftTextColor);
        setRightTextSize(rightTextSize);
        setRightTextColor(rightTextColor);
        setTitle(titleText);
        setLeftText(leftText);
        setRightText(rightText);
        setLeftIcon(leftIcon);
        setRightIcon(rightIcon);
        setMode(mode);
        hideStatusbarHolder(hideStatusbarHolder);
    }


    //region ===============================setter===============================

    public TitleBarView setMode(@TitleBarMode int mode) {
        boolean changed = this.mode == mode;
        if (changed) {
            switch (mode) {
                case MODE_BOTH:
                    ViewUtil.setViewsVisible(VISIBLE, mTitleBarIconLeft, mTitleBarTextLeft, mTitleBarIconRight, mTitleBarTextRight);
                    break;
                case MODE_NONE:
                    ViewUtil.setViewsVisible(INVISIBLE, mTitleBarIconLeft, mTitleBarTextLeft, mTitleBarIconRight, mTitleBarTextRight);
                    break;
                case MODE_RIGHT:
                    ViewUtil.setViewsVisible(VISIBLE, mTitleBarIconRight, mTitleBarTextRight);
                    ViewUtil.setViewsVisible(INVISIBLE, mTitleBarIconLeft, mTitleBarTextLeft);
                    break;
                case MODE_LEFT:
                default:
                    //都不匹配则按照left模式设置
                    ViewUtil.setViewsVisible(VISIBLE, mTitleBarIconLeft, mTitleBarTextLeft);
                    ViewUtil.setViewsVisible(INVISIBLE, mTitleBarIconRight, mTitleBarTextRight);
                    break;
            }
            this.mode = mode;
        }
        return this;
    }

    public TitleBarView setTitle(CharSequence titleText) {
        this.titleText = titleText;
        mTitleBarTitle.setText(titleText);
        return this;
    }

    public TitleBarView setLeftText(CharSequence leftText) {
        this.leftText = leftText;
        ViewUtil.setViewsVisible(TextUtils.isEmpty(leftText) ? GONE : VISIBLE, mTitleBarTextLeft);
        mTitleBarTextLeft.setText(fitText(leftText));
        return this;
    }

    public TitleBarView setRightText(CharSequence rightText) {
        this.rightText = rightText;
        ViewUtil.setViewsVisible(TextUtils.isEmpty(rightText) ? GONE : VISIBLE, mTitleBarTextRight);
        mTitleBarTextRight.setText(fitText(rightText));
        return this;
    }

    public TitleBarView setTitleTextSize(int titleTextSize) {
        this.titleTextSize = titleTextSize;
        mTitleBarTitle.setTextSize(titleTextSize);
        return this;
    }

    public TitleBarView setLeftTextSize(int leftTextSize) {
        this.leftTextSize = leftTextSize;
        mTitleBarTextLeft.setTextSize(leftTextSize);
        return this;
    }

    public TitleBarView setRightTextSize(int rightTextSize) {
        this.rightTextSize = rightTextSize;
        mTitleBarTextRight.setTextSize(rightTextSize);
        return this;
    }

    public TitleBarView setTitleTextColor(int titleTextColor) {
        this.titleTextColor = titleTextColor;
        mTitleBarTitle.setTextColor(titleTextColor);
        return this;
    }

    public TitleBarView setLeftTextColor(int leftTextColor) {
        this.leftTextColor = leftTextColor;
        mTitleBarTextLeft.setTextColor(leftTextColor);
        return this;
    }

    public TitleBarView setRightTextColor(int rightTextColor) {
        this.rightTextColor = rightTextColor;
        mTitleBarTextRight.setTextColor(rightTextColor);
        return this;
    }

    public TitleBarView setLeftIcon(int leftIcon) {
        this.leftIcon = leftIcon;
        ViewUtil.setViewsVisible(leftIcon != 0 ? VISIBLE : GONE, mTitleBarIconLeft);
        mTitleBarIconLeft.setImageResource(leftIcon);
        return this;
    }

    public TitleBarView setRightIcon(int rightIcon) {
        this.rightIcon = rightIcon;
        ViewUtil.setViewsVisible(rightIcon != 0 ? VISIBLE : GONE, mTitleBarIconRight);
        mTitleBarIconRight.setImageResource(rightIcon);
        return this;
    }


    public TitleBarView setOnTitlebarClickCallback(OnTitleBarClickCallback mTitlebarClickCallback) {
        this.mTitlebarClickCallback = mTitlebarClickCallback;
        return this;
    }

    public TitleBarView setRightTextEnable(boolean enable) {
        this.mTitleBarTextRight.setEnabled(enable);
        return this;
    }

    public TitleBarView hideStatusbarHolder(boolean hide) {
        this.mStatusBarHolderView.setVisibility(hide ? View.GONE : View.VISIBLE);
        return this;
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

        int paddingLeft = (mTitleBarIconLeft.getVisibility() == VISIBLE ? mTitleBarIconLeft.getWidth() : 0) +
                (mTitleBarTextLeft.getVisibility() == VISIBLE ? mTitleBarTextLeft.getWidth() : 0);
        int paddingRight = (mTitleBarIconRight.getVisibility() == VISIBLE ? mTitleBarIconRight.getWidth() : 0) +
                (mTitleBarTextRight.getVisibility() == VISIBLE ? mTitleBarTextRight.getWidth() : 0);

        int padding = Math.max(paddingLeft, paddingRight) + TITLE_PADDING;
        mTitleBarTitle.setPadding(padding, 0, padding, 0);

    }
    //endregion ===============================setter===============================


    CharSequence fitText(CharSequence from) {
        if (TextUtils.isEmpty(from)) return from;
        if (from.length() < 4) {
            final int len = from.length();
            int sub = 4 - len;
            StringBuilder builder = new StringBuilder(from);
            for (int i = 0; i < sub; i++) {
                builder.insert(0, "  ");
                builder.insert(builder.length(), "  ");
            }
            return builder;
        }
        return from;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.title_bar_icon_left || i == R.id.title_bar_text_left) {
            if (mTitlebarClickCallback != null) {
                mTitlebarClickCallback.onTitleLeftClick(v);
            }

        } else if (i == R.id.title_bar_icon_right || i == R.id.title_bar_text_right) {
            if (mTitlebarClickCallback != null) {
                mTitlebarClickCallback.onTitleRightClick(v);
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == R.id.title_bar_root || v.getId() == R.id.title_bar_title) {
            return mTitlebarClickCallback != null && mTitlebarClickCallback.onTitleLongClick();
        }
        return false;
    }

    private MultiClickListener mMultiClickListener = new MultiClickListener() {
        @Override
        public void onSingleClick() {
            if (mTitlebarClickCallback != null) {
                mTitlebarClickCallback.onTitleClick();
            }
        }

        @Override
        public void onDoubleClick() {
            if (mTitlebarClickCallback != null) {
                mTitlebarClickCallback.onTitleDoubleClick();
            }
        }
    };

    public interface OnTitleBarClickCallback {
        void onTitleLeftClick(View view);

        void onTitleRightClick(View view);

        void onTitleClick();

        void onTitleDoubleClick();

        boolean onTitleLongClick();

    }

}
