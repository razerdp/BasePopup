package razerdp.basepopup;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.lang.ref.WeakReference;

import razerdp.util.log.PopupLog;

/**
 * Created by 大灯泡 on 2017/12/25.
 * <p>
 * 代理掉popup的windowmanager，在addView操作，拦截decorView的操作
 */
final class WindowManagerProxy implements WindowManager {
    private static final String TAG = "WindowManagerProxy";
    private WindowManager mWindowManager;
    private WeakReference<PopupDecorViewProxy> mPopupDecorViewProxy;
    private WeakReference<BasePopupHelper> mPopupHelper;
    private static int statusBarHeight;

    WindowManagerProxy(WindowManager windowManager) {
        mWindowManager = windowManager;
    }

    @Override
    public Display getDefaultDisplay() {
        return mWindowManager == null ? null : mWindowManager.getDefaultDisplay();
    }

    @Override
    public void removeViewImmediate(View view) {
        PopupLog.i(TAG, "WindowManager.removeViewImmediate  >>>  " + (view == null ? null : view.getClass().getSimpleName()));
        if (mWindowManager == null || view == null) return;
        checkStatusBarHeight(view.getContext());
        if (isPopupInnerDecorView(view) && getPopupDecorViewProxy() != null) {
            PopupDecorViewProxy popupDecorViewProxy = getPopupDecorViewProxy();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (!popupDecorViewProxy.isAttachedToWindow()) return;
            }
            mWindowManager.removeViewImmediate(popupDecorViewProxy);
            mPopupDecorViewProxy.clear();
            mPopupDecorViewProxy = null;
        } else {
            mWindowManager.removeViewImmediate(view);
        }
    }

    @Override
    public void addView(View view, ViewGroup.LayoutParams params) {
        PopupLog.i(TAG, "WindowManager.addView  >>>  " + (view == null ? null : view.getClass().getSimpleName()));
        if (mWindowManager == null || view == null) return;
        checkStatusBarHeight(view.getContext());
        if (isPopupInnerDecorView(view)) {
            /**
             * 此时的params是WindowManager.LayoutParams，需要留意强转问题
             * popup内部有scrollChangeListener，会有params强转为WindowManager.LayoutParams的情况
             */
            BasePopupHelper helper = getBasePopupHelper();

            applyHelper(params, helper);
            //添加popup主体
            final PopupDecorViewProxy popupDecorViewProxy = PopupDecorViewProxy.create(view.getContext(), this, helper);
            popupDecorViewProxy.addPopupDecorView(view, (LayoutParams) params);
            mPopupDecorViewProxy = new WeakReference<PopupDecorViewProxy>(popupDecorViewProxy);
            mWindowManager.addView(popupDecorViewProxy, fitLayoutParamsPosition(popupDecorViewProxy, params));
        } else {
            mWindowManager.addView(view, params);
        }
    }

    private void applyHelper(ViewGroup.LayoutParams params, BasePopupHelper helper) {
        if (params instanceof LayoutParams && helper != null) {
            LayoutParams p = (LayoutParams) params;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                p.layoutInDisplayCutoutMode = LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT;
            }
            if (helper.isOutSideTouchable()) {
                PopupLog.i(TAG, "applyHelper  >>>  不拦截事件");
                p.flags |= LayoutParams.FLAG_NOT_TOUCH_MODAL;
                p.flags |= LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
                if (!helper.isClipToScreen()) {
                    p.flags |= LayoutParams.FLAG_LAYOUT_NO_LIMITS;
                }
            }
            if (helper.isFullScreen()) {
                PopupLog.i(TAG, "applyHelper  >>>  全屏");
                p.flags |= LayoutParams.FLAG_LAYOUT_IN_SCREEN;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    //允许占用刘海
                    p.layoutInDisplayCutoutMode = LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
                }
                if (!helper.isOutSideTouchable()) {
                    p.flags |= LayoutParams.FLAG_LAYOUT_NO_LIMITS;
                }
            }
        }
    }

    private ViewGroup.LayoutParams fitLayoutParamsPosition(PopupDecorViewProxy viewProxy, ViewGroup.LayoutParams params) {
        if (params instanceof LayoutParams) {
            LayoutParams p = (LayoutParams) params;
            BasePopupHelper helper = getBasePopupHelper();
            if (helper != null) {
                if (helper.getShowCount() > 1) {
                    p.type = LayoutParams.TYPE_APPLICATION_SUB_PANEL;
                }
                if (!helper.isOutSideTouchable()) {
                    //偏移交给PopupDecorViewProxy处理，此处固定为0
                    p.y = 0;
                    p.x = 0;
                } else {
                    viewProxy.fitWindowParams((LayoutParams) params);
                }
            }
            applyHelper(p, helper);
        }
        return params;
    }

    @Override
    public void updateViewLayout(View view, ViewGroup.LayoutParams params) {
        PopupLog.i(TAG, "WindowManager.updateViewLayout  >>>  " + (view == null ? null : view.getClass().getSimpleName()));
        if (mWindowManager == null || view == null) return;
        checkStatusBarHeight(view.getContext());
        if (isPopupInnerDecorView(view) && getPopupDecorViewProxy() != null || view == getPopupDecorViewProxy()) {
            PopupDecorViewProxy popupDecorViewProxy = getPopupDecorViewProxy();
            mWindowManager.updateViewLayout(popupDecorViewProxy, fitLayoutParamsPosition(popupDecorViewProxy, params));
        } else {
            mWindowManager.updateViewLayout(view, params);
        }
    }

    public void updateViewLayoutOriginal(View view, ViewGroup.LayoutParams params) {
        mWindowManager.updateViewLayout(view, params);
    }

    public void updateFocus(boolean focus) {
        if (mWindowManager != null && getPopupDecorViewProxy() != null) {
            PopupDecorViewProxy popupDecorViewProxy = getPopupDecorViewProxy();
            ViewGroup.LayoutParams params = popupDecorViewProxy.getLayoutParams();
            if (params instanceof LayoutParams) {
                if (focus) {
                    ((LayoutParams) params).flags &= ~WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                } else {
                    ((LayoutParams) params).flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                }
            }
            mWindowManager.updateViewLayout(popupDecorViewProxy, params);
        }
    }

    public void update() {
        if (mWindowManager == null) return;
        if (getPopupDecorViewProxy() != null) {
            getPopupDecorViewProxy().updateLayout();
        }
    }

    @Override
    public void removeView(View view) {
        PopupLog.i(TAG, "WindowManager.removeView  >>>  " + (view == null ? null : view.getClass().getSimpleName()));
        if (mWindowManager == null || view == null) return;
        checkStatusBarHeight(view.getContext());
        if (isPopupInnerDecorView(view) && getPopupDecorViewProxy() != null) {
            PopupDecorViewProxy popupDecorViewProxy = getPopupDecorViewProxy();
            mWindowManager.removeView(popupDecorViewProxy);
            mPopupDecorViewProxy.clear();
            mPopupDecorViewProxy = null;
        } else {
            mWindowManager.removeView(view);
        }
    }

    public void clear() {
        try {
            removeViewImmediate(mPopupDecorViewProxy.get());
            mPopupDecorViewProxy.clear();
        } catch (Exception e) {
            //no print
        }
    }

    private boolean isPopupInnerDecorView(View v) {
        if (v == null) return false;
        String viewSimpleClassName = v.getClass().getSimpleName();
        return TextUtils.equals(viewSimpleClassName, "PopupDecorView") || TextUtils.equals(viewSimpleClassName, "PopupViewContainer");
    }

    private PopupDecorViewProxy getPopupDecorViewProxy() {
        if (mPopupDecorViewProxy == null) return null;
        return mPopupDecorViewProxy.get();
    }


    private BasePopupHelper getBasePopupHelper() {
        if (mPopupHelper == null) return null;
        return mPopupHelper.get();
    }

    void attachPopupHelper(BasePopupHelper helper) {
        mPopupHelper = new WeakReference<BasePopupHelper>(helper);
    }

    private void checkStatusBarHeight(Context context) {
        if (statusBarHeight != 0 || context == null) return;
        int result = 0;
        //获取状态栏高度的资源id
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        statusBarHeight = result;
    }

}
