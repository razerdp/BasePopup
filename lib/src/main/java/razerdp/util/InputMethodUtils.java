package razerdp.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by 大灯泡 on 2016/1/14.
 * 显示键盘d工具类
 */
public class InputMethodUtils {
    /**
     * 显示软键盘
     */
    public static void showInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * 显示软键盘
     */
    public static void showInputMethod(Context context) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 多少时间后显示软键盘
     */
    public static void showInputMethod(final View view, long delayMillis) {
        if (view == null) return;
        // 显示输入法
        view.postDelayed(new Runnable() {

            @Override
            public void run() {
                InputMethodUtils.showInputMethod(view);
            }
        }, delayMillis);
    }

    /**
     * 隐藏软键盘
     */
    public static void close(Activity activity) {
        if (activity == null) {
            return;
        }
        View view = activity.getWindow().getDecorView().getRootView();
        try {
            InputMethodManager imm = (InputMethodManager) view.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 隐藏软键盘
     */
    public static void close(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) view.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ViewTreeObserver.OnGlobalLayoutListener observerKeyboardWithView(final View target) {
        if (target == null) return null;
        Activity act = PopupUtils.getActivity(target.getContext());
        if (act == null) return null;
        return observerKeyboardChange(act, new OnKeyboardChangeListener() {
            private int[] location = {0, 0};

            @Override
            public void onKeyboardChange(Rect keyboardBounds, boolean isVisible) {
                if (isVisible) {
                    target.getLocationOnScreen(location);
                    int offset = keyboardBounds.top - (location[1] + target.getHeight());
                    target.setTranslationY(target.getTranslationY() + offset);
                } else {
                    target.animate().translationY(0).setDuration(300).setStartDelay(100).start();
                }
            }
        });
    }

    public static ViewTreeObserver.OnGlobalLayoutListener observerKeyboardChange(Activity act, final OnKeyboardChangeListener onKeyboardChangeListener) {
        if (act == null || onKeyboardChangeListener == null) return null;
        final View decor = act.getWindow().getDecorView();
        ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            Rect rect = new Rect();
            Rect keyboardRect = new Rect();

            @Override
            public void onGlobalLayout() {
                decor.getWindowVisibleDisplayFrame(rect);
                int screenHeight = decor.getRootView().getHeight();
                keyboardRect.set(rect.left, rect.bottom, rect.right, screenHeight);
                boolean isVisible = keyboardRect.height() > 0;
                onKeyboardChangeListener.onKeyboardChange(keyboardRect, isVisible);

            }
        };
        decor.getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);
        return layoutListener;
    }

    //=============================================================interface
    public interface OnKeyboardChangeListener {
        void onKeyboardChange(Rect keyboardBounds, boolean isVisible);
    }
}
