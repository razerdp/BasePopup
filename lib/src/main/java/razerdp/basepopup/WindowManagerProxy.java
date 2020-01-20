package razerdp.basepopup;

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
final class WindowManagerProxy implements WindowManager, ClearMemoryObject {
    private static final String TAG = "WindowManagerProxy";
    private WindowManager mWindowManager;
    private PopupDecorViewProxy mPopupDecorViewProxy;
    private WeakReference<BasePopupHelper> mPopupHelper;

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
        if (isPopupInnerDecorView(view) && mPopupDecorViewProxy != null) {
            PopupDecorViewProxy popupDecorViewProxy = mPopupDecorViewProxy;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (!popupDecorViewProxy.isAttachedToWindow()) return;
            }
            mWindowManager.removeViewImmediate(popupDecorViewProxy);
            mPopupDecorViewProxy = null;
        } else {
            mWindowManager.removeViewImmediate(view);
        }
    }

    @Override
    public void addView(View view, ViewGroup.LayoutParams params) {
        PopupLog.i(TAG, "WindowManager.addView  >>>  " + (view == null ? null : view.getClass().getSimpleName()));
        if (mWindowManager == null || view == null) return;
        if (isPopupInnerDecorView(view)) {
            /**
             * 此时的params是WindowManager.LayoutParams，需要留意强转问题
             * popup内部有scrollChangeListener，会有params强转为WindowManager.LayoutParams的情况
             */
            BasePopupHelper helper = getBasePopupHelper();

            applyHelper(params, helper);
            if (mPopupDecorViewProxy != null && mPopupDecorViewProxy.getParent() != null) return;
            //添加popup主体
            mPopupDecorViewProxy = new PopupDecorViewProxy(view.getContext(), this, helper);
            mPopupDecorViewProxy.wrapPopupDecorView(view, (LayoutParams) params);
            mWindowManager.addView(mPopupDecorViewProxy, fitLayoutParamsPosition(mPopupDecorViewProxy, params));
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
                    p.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    p.height = ViewGroup.LayoutParams.MATCH_PARENT;
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
        if (isPopupInnerDecorView(view) && mPopupDecorViewProxy != null || view == mPopupDecorViewProxy) {
            PopupDecorViewProxy popupDecorViewProxy = mPopupDecorViewProxy;
            mWindowManager.updateViewLayout(popupDecorViewProxy, fitLayoutParamsPosition(popupDecorViewProxy, params));
        } else {
            mWindowManager.updateViewLayout(view, params);
        }
    }

    public void updateViewLayoutOriginal(View view, ViewGroup.LayoutParams params) {
        try {
            mWindowManager.updateViewLayout(view, params);
        } catch (Exception e) {

        }
    }

    public void updateFocus(boolean focus) {
        if (mWindowManager != null && mPopupDecorViewProxy != null) {
            PopupDecorViewProxy popupDecorViewProxy = mPopupDecorViewProxy;
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
        if (mPopupDecorViewProxy != null) {
            mPopupDecorViewProxy.updateLayout();
        }
    }

    @Override
    public void removeView(View view) {
        PopupLog.i(TAG, "WindowManager.removeView  >>>  " + (view == null ? null : view.getClass().getSimpleName()));
        if (mWindowManager == null || view == null) return;
        if (isPopupInnerDecorView(view) && mPopupDecorViewProxy != null) {
            mWindowManager.removeView(mPopupDecorViewProxy);
            mPopupDecorViewProxy = null;
        } else {
            mWindowManager.removeView(view);
        }
    }


    private boolean isPopupInnerDecorView(View v) {
        if (v == null) return false;
        String viewSimpleClassName = v.getClass().getSimpleName();
        return TextUtils.equals(viewSimpleClassName, "PopupDecorView") || TextUtils.equals(viewSimpleClassName, "PopupViewContainer");
    }


    private BasePopupHelper getBasePopupHelper() {
        if (mPopupHelper == null) return null;
        return mPopupHelper.get();
    }

    void attachPopupHelper(BasePopupHelper helper) {
        mPopupHelper = new WeakReference<BasePopupHelper>(helper);
    }

    @Override
    public void clear(boolean destroy) {
        try {
            removeViewImmediate(mPopupDecorViewProxy);
        } catch (Exception ignore) {
        }
        if (destroy) {
            mWindowManager = null;
            mPopupDecorViewProxy = null;
            if (mPopupHelper != null) {
                mPopupHelper.clear();
            }
            mPopupHelper = null;
        }
    }
}
