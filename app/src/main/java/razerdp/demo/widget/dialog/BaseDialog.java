package razerdp.demo.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import razerdp.basepopup.R;
import razerdp.demo.utils.UIHelper;
import razerdp.demo.utils.WeakHandler;
import razerdp.util.log.PopupLog;

/**
 * Created by 大灯泡 on 2019/4/22.
 * <p>
 * dialog基类
 */
public abstract class BaseDialog<L extends DialogClickListener> extends Dialog {
    protected final String TAG = this.getClass().getSimpleName();
    private boolean hasSetWindowAttr;


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({POSITIVE, NEGATIVE, BOTH, NONE})
    public @interface DialogMode {
    }

    public static final int POSITIVE = 0x10;
    public static final int NEGATIVE = 0x11;
    public static final int BOTH = 0x12;
    public static final int NONE = 0x13;

    private int mode = BOTH;

    private View mDialogView;
    private WeakHandler mWeakHandler;

    private L onDialogButtonClickListener;

    protected BaseDialog(@NonNull Context context) {
        this(context, R.style.BaseDialog);
    }

    protected BaseDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        mWeakHandler = new WeakHandler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mDialogView == null) {
            mDialogView = onCreateView(LayoutInflater.from(getContext()));
        }
        if (mDialogView == null) return;
        onFindView(mDialogView);
        onInitMode(mode);
        setContentView(mDialogView);
        Window dialogWindow = getWindow();
        if (dialogWindow != null && !hasSetWindowAttr) {
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = onInitWindowWidth();
            lp.height = onInitWindowHeight();
            lp.gravity = onInitWindowGravity();
            dialogWindow.setAttributes(lp);
            hasSetWindowAttr = true;
        }
    }

    protected abstract View onCreateView(@NonNull LayoutInflater inflater);

    protected abstract void onFindView(@NonNull View dialogView);

    protected abstract void onInitMode(@DialogMode int mode);

    @DialogMode
    public int getDialogMode() {
        return mode;
    }

    /**
     * 设置dialog的模式
     * <p>
     * 设置后会回调到{@link #onInitMode(int)}
     *
     * @param mode
     */
    public BaseDialog setDialogMode(@DialogMode int mode) {
        return setDialogMode(mode, true);
    }

    /**
     * 设置dialog的模式
     * <p>
     * 设置后会回调到{@link #onInitMode(int)}
     *
     * @param mode
     * @param callModeChange false 禁止回调{@link #onInitMode(int)}
     */
    protected BaseDialog setDialogMode(@DialogMode int mode, boolean callModeChange) {
        boolean hasChange = this.mode != mode;
        this.mode = mode;
        if (hasChange && callModeChange) {
            onInitMode(mode);
        }
        return this;
    }

    public L getOnDialogButtonClickListener() {
        return onDialogButtonClickListener;
    }

    public BaseDialog setOnDialogButtonClickListener(L onDialogButtonClickListener) {
        this.onDialogButtonClickListener = onDialogButtonClickListener;
        return this;
    }

    /**
     * 初始化window宽度
     * <p>
     * 默认屏幕宽度左右间距25dp
     *
     * @return
     */
    protected int onInitWindowWidth() {
        return Math.round(UIHelper.getScreenWidth() * 0.8f);
    }

    /**
     * 初始化window高度
     * <p>
     * 默认wrap_content
     *
     * @return
     */
    protected int onInitWindowHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    /**
     * 初始化window的gravity
     *
     * @return 默认返回 Gravity.CENTER
     * @see Gravity
     */
    protected int onInitWindowGravity() {
        return Gravity.CENTER;
    }

    public void show(long delayTime) {
        if (delayTime <= 0) {
            super.show();
        } else {
            if (mWeakHandler != null) {
                mWeakHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BaseDialog.super.show();
                    }
                }, delayTime);
            }
        }
    }

    /**
     * 不依附Activity来show，比如在service里面
     * <p>
     * 此举将会把dialog的window level提升为system
     * <p>
     * 需要权限
     * <p>
     * <h3>uses-permission Android:name="android.permission.SYSTEM_ALERT_WINDOW"
     */
    public void showInSystemWindow() {
        try {
            Window window = getWindow();
            if (window == null) return;
            window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            show();
        } catch (Exception e) {
            PopupLog.e(TAG, e);
        }
    }

    protected void callPositiveClick() {
        if (onDialogButtonClickListener != null) {
            boolean dismiss = onDialogButtonClickListener.onPositiveClicked();
            if (dismiss && isShowing()) dismiss();
        } else {
            if (isShowing()) {
                dismiss();
            }
        }
    }

    protected void callNegativeClick() {
        if (onDialogButtonClickListener != null) {
            boolean dismiss = onDialogButtonClickListener.onNegativeClicked();
            if (dismiss && isShowing()) dismiss();
        } else {
            if (isShowing()) {
                dismiss();
            }
        }
    }

    public BaseDialog setCanCancel(boolean flag) {
        setCancelable(flag);
        return this;
    }


}
