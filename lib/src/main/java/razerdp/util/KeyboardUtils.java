package razerdp.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import razerdp.basepopup.BasePopupSDK;

/**
 * Created by 大灯泡 on 2016/1/14.
 * 显示键盘d工具类
 */
public class KeyboardUtils {
    /**
     * 显示软键盘
     */
    public static void open(View view) {
        if (view == null) return;
        if (!view.hasFocus()) {
            PopupUiUtils.requestFocus(view);
            View focusedView = view.findFocus();
            if (focusedView != null) {
                view = focusedView;
            }
        }
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * 显示软键盘
     */
    public static void open(Context context) {
        if (context == null) return;
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 多少时间后显示软键盘
     */
    public static void open(final View view, long delayMillis) {
        if (view == null) return;
        // 显示输入法
        view.postDelayed(new Runnable() {

            @Override
            public void run() {
                KeyboardUtils.open(view);
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
                imm.hideSoftInputFromWindow(view.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
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

    public static boolean isOpen() {
        try {
            InputMethodManager imm = (InputMethodManager) BasePopupSDK.getApplication()
                    .getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                return imm.isActive();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isOpen(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) BasePopupSDK.getApplication()
                    .getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                return imm.isActive(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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
            Rect originalContentRect = new Rect();
            boolean lastVisible;
            int lastHeight;

            @Override
            public void onGlobalLayout() {
                View content = decor.findViewById(android.R.id.content);
                if (content == null) return;
                if (originalContentRect.isEmpty()) {
                    //需要从content一直遍历往前找到decorview下的第一个child，那个为准
                    ViewParent parent = content.getParent();
                    for (; ; ) {
                        if (parent.getParent() == decor) {
                            break;
                        }
                        if (!(parent.getParent() instanceof View)) {
                            break;
                        }
                        parent = parent.getParent();
                    }
                    originalContentRect.set(((View) parent).getLeft(),
                            ((View) parent).getTop(),
                            ((View) parent).getRight(),
                            ((View) parent).getBottom());
                }
                decor.getWindowVisibleDisplayFrame(rect);
                keyboardRect.set(rect.left, rect.bottom, rect.right, originalContentRect.bottom);
                boolean isVisible = keyboardRect.height() > (originalContentRect.height() >> 2) && isOpen();
                if (isVisible == lastVisible && keyboardRect.height() == lastHeight) return;
                lastVisible = isVisible;
                lastHeight = keyboardRect.height();
                onKeyboardChangeListener.onKeyboardChange(keyboardRect, isVisible);
            }
        };
        PopupUiUtils.safeAddGlobalLayoutListener(decor, layoutListener);
        return layoutListener;
    }

    //=============================================================interface
    public interface OnKeyboardChangeListener {
        void onKeyboardChange(Rect keyboardBounds, boolean isVisible);
    }
}
