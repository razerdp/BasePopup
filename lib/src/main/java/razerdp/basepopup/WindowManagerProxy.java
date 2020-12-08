package razerdp.basepopup;

import android.app.Activity;
import android.os.Build;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.LinkedList;

import razerdp.util.PopupUiUtils;
import razerdp.util.log.PopupLog;

/**
 * Created by 大灯泡 on 2017/12/25.
 * <p>
 * 代理掉popup的windowmanager，在addView操作，拦截decorView的操作
 */
final class WindowManagerProxy implements WindowManager, ClearMemoryObject {
    private static final String TAG = "WindowManagerProxy";
    private WindowManager mWindowManager;
    PopupDecorViewProxy mPopupDecorViewProxy;
    BasePopupHelper mPopupHelper;
    boolean isAddedToQueue;
    static final WindowFlagCompat FLAG_COMPAT;

    static {
        if (Build.VERSION.SDK_INT >= 30) {
            FLAG_COMPAT = new WindowFlagCompat.Api30Impl();
        } else {
            FLAG_COMPAT = new WindowFlagCompat.BeforeApi30Impl();
        }
    }

    WindowManagerProxy(WindowManager windowManager, BasePopupHelper helper) {
        mWindowManager = windowManager;
        mPopupHelper = helper;
    }

    @Override
    public Display getDefaultDisplay() {
        return mWindowManager == null ? null : mWindowManager.getDefaultDisplay();
    }

    @Override
    public void removeViewImmediate(View view) {
        PopupLog.i(TAG,
                   "WindowManager.removeViewImmediate  >>>  " + (view == null ? null : view.getClass()
                           .getSimpleName()));
        PopupWindowQueueManager.getInstance().remove(this);
        if (mWindowManager == null || view == null) return;
        if (isPopupInnerDecorView(view) && mPopupDecorViewProxy != null) {
            PopupDecorViewProxy popupDecorViewProxy = mPopupDecorViewProxy;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (!popupDecorViewProxy.isAttachedToWindow()) return;
            }
            mWindowManager.removeViewImmediate(popupDecorViewProxy);
            mPopupDecorViewProxy.clear(true);
            mPopupDecorViewProxy = null;
        } else {
            mWindowManager.removeViewImmediate(view);
        }
    }

    @Override
    public void addView(View view, ViewGroup.LayoutParams params) {
        PopupLog.i(TAG,
                   "WindowManager.addView  >>>  " + (view == null ? null : view.getClass().getName()));
        PopupWindowQueueManager.getInstance().put(this);
        if (mWindowManager == null || view == null) return;
        if (isPopupInnerDecorView(view)) {
            /**
             * 此时的params是WindowManager.LayoutParams，需要留意强转问题
             * popup内部有scrollChangeListener，会有params强转为WindowManager.LayoutParams的情况
             */
            FLAG_COMPAT.setupFlag(params, mPopupHelper);
            //添加popup主体
            mPopupDecorViewProxy = new PopupDecorViewProxy(view.getContext(), mPopupHelper);
            mPopupDecorViewProxy.wrapPopupDecorView(view, (LayoutParams) params);
            mWindowManager.addView(mPopupDecorViewProxy, fitLayoutParamsPosition(params));
        } else {
            mWindowManager.addView(view, params);
        }
    }

    private ViewGroup.LayoutParams fitLayoutParamsPosition(ViewGroup.LayoutParams params) {
        if (params instanceof LayoutParams) {
            LayoutParams p = (LayoutParams) params;
            if (mPopupHelper != null) {
                if (mPopupHelper.getShowCount() > 1) {
                    p.type = LayoutParams.TYPE_APPLICATION_SUB_PANEL;
                }
                //偏移交给PopupDecorViewProxy处理，此处固定为0
                p.y = 0;
                p.x = 0;
                p.width = ViewGroup.LayoutParams.MATCH_PARENT;
                p.height = ViewGroup.LayoutParams.MATCH_PARENT;
            }
            FLAG_COMPAT.setupFlag(p, mPopupHelper);
            if (mPopupHelper.mOnFitWindowManagerLayoutParamsCallback != null) {
                mPopupHelper.mOnFitWindowManagerLayoutParamsCallback.onFitLayoutParams(p);
            }
        }
        return params;
    }

    @Override
    public void updateViewLayout(View view, ViewGroup.LayoutParams params) {
        PopupLog.i(TAG,
                   "WindowManager.updateViewLayout  >>>  " + (view == null ? null : view.getClass()
                           .getName()));
        if (mWindowManager == null || view == null) return;
        if (isPopupInnerDecorView(view) && mPopupDecorViewProxy != null || view == mPopupDecorViewProxy) {
            mWindowManager.updateViewLayout(mPopupDecorViewProxy, fitLayoutParamsPosition(params));
        } else {
            mWindowManager.updateViewLayout(view, params);
        }
    }

    void updateFocus(boolean focus) {
        if (mWindowManager != null && mPopupDecorViewProxy != null) {
            PopupDecorViewProxy popupDecorViewProxy = mPopupDecorViewProxy;
            ViewGroup.LayoutParams params = popupDecorViewProxy.getLayoutParams();
            if (params instanceof LayoutParams) {
                if (focus) {
                    ((LayoutParams) params).flags &= ~(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                            LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                } else {
                    ((LayoutParams) params).flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                }
            }
            mWindowManager.updateViewLayout(popupDecorViewProxy, params);
        }
    }


    void updateFlag(int mode, boolean updateImmediately, int... flags) {
        if (flags == null || flags.length == 0) return;
        if (mWindowManager != null && mPopupDecorViewProxy != null) {
            PopupDecorViewProxy popupDecorViewProxy = mPopupDecorViewProxy;
            ViewGroup.LayoutParams params = popupDecorViewProxy.getLayoutParams();
            if (params instanceof LayoutParams) {
                for (int flag : flags) {
                    if (mode == BasePopupFlag.MODE_ADD) {
                        ((LayoutParams) params).flags |= flag;
                    } else if (mode == BasePopupFlag.MODE_REMOVE) {
                        ((LayoutParams) params).flags &= ~flag;
                    }
                }
            }
            if (updateImmediately) {
                mWindowManager.updateViewLayout(popupDecorViewProxy, params);
            }
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
        PopupLog.i(TAG,
                   "WindowManager.removeView  >>>  " + (view == null ? null : view.getClass()
                           .getSimpleName()));
        PopupWindowQueueManager.getInstance().remove(this);
        if (mWindowManager == null || view == null) return;
        if (isPopupInnerDecorView(view) && mPopupDecorViewProxy != null) {
            mWindowManager.removeView(mPopupDecorViewProxy);
            mPopupDecorViewProxy = null;
        } else {
            mWindowManager.removeView(view);
        }
    }


    private boolean isPopupInnerDecorView(View v) {
        return PopupUiUtils.isPopupDecorView(v) || PopupUiUtils.isPopupViewContainer(v);
    }

    @Override
    public void clear(boolean destroy) {
        try {
            if (mPopupDecorViewProxy != null) {
                removeViewImmediate(mPopupDecorViewProxy);
            }
        } catch (Exception ignore) {
        }

        if (destroy) {
            String key = PopupWindowQueueManager.getInstance().getKey(this);
            PopupWindowQueueManager.getInstance().clear(key);
            mWindowManager = null;
            mPopupDecorViewProxy = null;
            mPopupHelper = null;
        }
    }

    @Nullable
    WindowManagerProxy preWindow() {
        return PopupWindowQueueManager.getInstance().preWindow(this);
    }

    void dispatchToDecorProxy(MotionEvent ev) {
        if (mPopupDecorViewProxy != null) {
            mPopupDecorViewProxy.dispatchTouchEvent(ev);
        }
    }

    static class PopupWindowQueueManager {

        static final HashMap<String, LinkedList<WindowManagerProxy>> sQueueMap = new HashMap<>();

        private static class SingleTonHolder {
            private static PopupWindowQueueManager INSTANCE = new PopupWindowQueueManager();
        }

        private PopupWindowQueueManager() {
        }

        static PopupWindowQueueManager getInstance() {
            return PopupWindowQueueManager.SingleTonHolder.INSTANCE;
        }

        String getKey(WindowManagerProxy managerProxy) {
            if (managerProxy == null || managerProxy.mPopupHelper == null || managerProxy.mPopupHelper.mPopupWindow == null) {
                return null;
            }
            return String.valueOf(managerProxy.mPopupHelper.mPopupWindow.getContext());
        }

        void put(WindowManagerProxy managerProxy) {
            if (managerProxy == null || managerProxy.isAddedToQueue) return;
            String key = getKey(managerProxy);
            if (TextUtils.isEmpty(key)) return;
            LinkedList<WindowManagerProxy> queue = sQueueMap.get(key);
            if (queue == null) {
                queue = new LinkedList<>();
                sQueueMap.put(key, queue);
            }
            queue.addLast(managerProxy);
            managerProxy.isAddedToQueue = true;

            PopupLog.d(TAG, queue);
        }

        void remove(WindowManagerProxy managerProxy) {
            if (managerProxy == null || !managerProxy.isAddedToQueue) return;
            String key = getKey(managerProxy);
            if (TextUtils.isEmpty(key)) return;
            LinkedList<WindowManagerProxy> queue = sQueueMap.get(key);
            if (queue != null) {
                queue.remove(managerProxy);
            }
            managerProxy.isAddedToQueue = false;
            PopupLog.d(TAG, queue);
        }

        void clear(String key) {
            LinkedList<WindowManagerProxy> queue = sQueueMap.get(key);
            if (queue != null) {
                queue.clear();
            }
            sQueueMap.remove(key);
            PopupLog.d(TAG, queue, sQueueMap);
        }

        @Nullable
        WindowManagerProxy preWindow(WindowManagerProxy managerProxy) {
            if (managerProxy == null) {
                return null;
            }
            String key = getKey(managerProxy);
            if (TextUtils.isEmpty(key)) return null;
            LinkedList<WindowManagerProxy> queue = sQueueMap.get(key);
            if (queue == null) return null;
            int index = queue.indexOf(managerProxy) - 1;
            if (index >= 0 && index < queue.size()) {
                return queue.get(index);
            }
            return null;
        }
    }


    interface WindowFlagCompat {
        void setupFlag(ViewGroup.LayoutParams params, BasePopupHelper helper);

        class BeforeApi30Impl implements WindowFlagCompat {

            @Override
            public void setupFlag(ViewGroup.LayoutParams params, BasePopupHelper helper) {
                if (params instanceof LayoutParams && helper != null) {
                    LayoutParams p = (LayoutParams) params;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        Activity decorAct = helper.mPopupWindow.getContext();
                        if (decorAct != null) {
                            WindowManager.LayoutParams lp = decorAct.getWindow().getAttributes();
                            p.layoutInDisplayCutoutMode = lp.layoutInDisplayCutoutMode;
                        }
                    }
                    if (helper.isOverlayStatusbar()) {
                        PopupLog.i(TAG, "applyHelper  >>>  覆盖状态栏");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            int cutoutGravity = helper.getCutoutGravity();
                            if (cutoutGravity == Gravity.TOP || cutoutGravity == Gravity.BOTTOM) {
                                //垂直方向允许占用刘海
                                p.layoutInDisplayCutoutMode = LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
                            }

                        }
                    }
                    // 状态栏和导航栏相关处理交给decorview proxy，这里永远占用
                    p.flags |= LayoutParams.FLAG_LAYOUT_IN_SCREEN;
                    p.flags |= LayoutParams.FLAG_LAYOUT_NO_LIMITS;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        p.flags |= LayoutParams.FLAG_LAYOUT_IN_OVERSCAN;
                    }
                }
            }
        }

        class Api30Impl implements WindowFlagCompat {

            @Override
            public void setupFlag(ViewGroup.LayoutParams params, BasePopupHelper helper) {
                if (params instanceof LayoutParams && helper != null) {
                    LayoutParams p = (LayoutParams) params;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        Activity decorAct = helper.mPopupWindow.getContext();
                        if (decorAct != null) {
                            WindowManager.LayoutParams lp = decorAct.getWindow().getAttributes();
                            p.layoutInDisplayCutoutMode = lp.layoutInDisplayCutoutMode;
                        }
                    }
                    int insetsType = p.getFitInsetsTypes();
                    if (helper.isOverlayStatusbar()) {
                        PopupLog.i(TAG, "applyHelper  >>>  覆盖状态栏");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            int cutoutGravity = helper.getCutoutGravity();
                            if (cutoutGravity == Gravity.TOP || cutoutGravity == Gravity.BOTTOM) {
                                //垂直方向允许占用刘海
                                p.layoutInDisplayCutoutMode = LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
                            }
                        }
                    }
                    // 状态栏和导航栏相关处理交给decorview proxy，这里永远占用
                    insetsType &= ~WindowInsets.Type.statusBars();
                    insetsType &= ~WindowInsets.Type.navigationBars();
                    p.setFitInsetsTypes(insetsType);
                }

            }
        }
    }
}
