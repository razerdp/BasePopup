package razerdp.basepopup;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.lang.ref.WeakReference;

import razerdp.util.log.LogTag;
import razerdp.util.log.PopupLogUtil;

/**
 * Created by 大灯泡 on 2017/12/25.
 * <p>
 * 代理掉popup的windowmanager，在addView操作，拦截decorView的操作
 */
final class WindowManagerWrapper extends InnerPopupWindowStateListener implements WindowManager {
    private static final String TAG = "WindowManagerWrapper";
    private WindowManager mWindowManager;
    private WeakReference<PopupTouchController> mPopupController;
    private WeakReference<PopupDecorViewProxy> mHackPopupDecorView;
    private WeakReference<BasePopupHelper> mPopupHelper;
    private WeakReference<PopupMaskLayout> mMaskLayout;
    private static int statusBarHeight;

    public WindowManagerWrapper(WindowManager windowManager, PopupTouchController popupTouchController) {
        mWindowManager = windowManager;
        mPopupController = new WeakReference<PopupTouchController>(popupTouchController);
    }

    @Override
    public Display getDefaultDisplay() {
        return mWindowManager == null ? null : mWindowManager.getDefaultDisplay();
    }

    @Override
    public void removeViewImmediate(View view) {
        PopupLogUtil.trace(LogTag.i, TAG, "WindowManager.removeViewImmediate  >>>  " + (view == null ? null : view.getClass().getSimpleName()));
        if (mWindowManager == null || view == null) return;
        checkStatusBarHeight(view.getContext());
        if (isPopupInnerDecorView(view) && getHackPopupDecorView() != null) {
            if (getMaskLayout() != null) {
                try {
                    mWindowManager.removeViewImmediate(getMaskLayout());
                    mMaskLayout.clear();
                    mMaskLayout = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            PopupDecorViewProxy popupDecorViewProxy = getHackPopupDecorView();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (!popupDecorViewProxy.isAttachedToWindow()) return;
            }
            mWindowManager.removeViewImmediate(popupDecorViewProxy);
            mHackPopupDecorView.clear();
            mHackPopupDecorView = null;
        } else {
            mWindowManager.removeViewImmediate(view);
        }
    }

    @Override
    public void addView(View view, ViewGroup.LayoutParams params) {
        PopupLogUtil.trace(LogTag.i, TAG, "WindowManager.addView  >>>  " + (view == null ? null : view.getClass().getSimpleName()));
        if (mWindowManager == null || view == null) return;
        checkStatusBarHeight(view.getContext());
        if (isPopupInnerDecorView(view)) {
            /**
             * 此时的params是WindowManager.LayoutParams，需要留意强转问题
             * popup内部有scrollChangeListener，会有params强转为WindowManager.LayoutParams的情况
             */
            BasePopupHelper helper = getBasePopupHelper();

            //添加popup主体
            final PopupDecorViewProxy popupDecorViewProxy = new PopupDecorViewProxy(view.getContext());
            params = popupDecorViewProxy.addPopupDecorView(view, params, helper, getPopupController());
            mMaskLayout = new WeakReference<>(PopupMaskLayout.create(view.getContext(), helper, fitLayoutParamsPosition(params)));
            mHackPopupDecorView = new WeakReference<PopupDecorViewProxy>(popupDecorViewProxy);

            if (getPopupController() instanceof BasePopupWindow) {
                ((BasePopupWindow) getPopupController()).setOnInnerPopupWindowStateListener(this);
            }
            popupDecorViewProxy.setOnAttachListener(new PopupDecorViewProxy.OnAttachListener() {
                @Override
                public void onAttachtoWindow() {
                    if (getMaskLayout() != null) {
                        getMaskLayout().handleStart(-2);
                    }
                }
            });
            if (getMaskLayout() != null) {
                mWindowManager.addView(getMaskLayout(), createBlurBackgroundWindowParams(params));
            }
            mWindowManager.addView(popupDecorViewProxy, fitLayoutParamsPosition(params));
        } else {
            mWindowManager.addView(view, params);
        }
    }

    private ViewGroup.LayoutParams fitLayoutParamsPosition(ViewGroup.LayoutParams params) {
        if (params instanceof WindowManager.LayoutParams) {
            WindowManager.LayoutParams p = (LayoutParams) params;
            p.type =  LayoutParams.TYPE_APPLICATION_SUB_PANEL;
            BasePopupHelper helper = getBasePopupHelper();
            if (helper != null && helper.isShowAsDropDown() && p.y <= helper.getAnchorY()) {
                int y = helper.getAnchorY() + helper.getAnchorHeight() + helper.getInternalOffsetY();
                p.y = y <= 0 ? 0 : y;
            }
        }
        return params;
    }

    @Override
    public void updateViewLayout(View view, ViewGroup.LayoutParams params) {
        PopupLogUtil.trace(LogTag.i, TAG, "WindowManager.updateViewLayout  >>>  " + (view == null ? null : view.getClass().getSimpleName()));
        if (mWindowManager == null || view == null) return;
        checkStatusBarHeight(view.getContext());
        if (isPopupInnerDecorView(view) && getHackPopupDecorView() != null) {
            PopupDecorViewProxy popupDecorViewProxy = getHackPopupDecorView();
            mWindowManager.updateViewLayout(popupDecorViewProxy, fitLayoutParamsPosition(params));
        } else {
            mWindowManager.updateViewLayout(view, params);
        }
    }

    @Override
    public void removeView(View view) {
        PopupLogUtil.trace(LogTag.i, TAG, "WindowManager.removeView  >>>  " + (view == null ? null : view.getClass().getSimpleName()));
        if (mWindowManager == null || view == null) return;
        checkStatusBarHeight(view.getContext());
        if (isPopupInnerDecorView(view) && getHackPopupDecorView() != null) {
            if (getMaskLayout() != null) {
                try {
                    mWindowManager.removeView(getMaskLayout());
                    if (mMaskLayout != null) {
                        mMaskLayout.clear();
                        mMaskLayout = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            PopupDecorViewProxy popupDecorViewProxy = getHackPopupDecorView();
            mWindowManager.removeView(popupDecorViewProxy);
            mHackPopupDecorView.clear();
            mHackPopupDecorView = null;
        } else {
            mWindowManager.removeView(view);
        }
    }

    public void clear() {
        try {
            removeViewImmediate(mHackPopupDecorView.get());
            removeViewImmediate(getMaskLayout());
            mHackPopupDecorView.clear();
            if (mMaskLayout != null) {
                mMaskLayout.clear();
            }
        } catch (Exception e) {
            //no print
        }
    }


    /**
     * 生成blurimageview的params
     *
     * @param params
     * @return
     */
    private ViewGroup.LayoutParams createBlurBackgroundWindowParams(ViewGroup.LayoutParams params) {
        ViewGroup.LayoutParams result = new ViewGroup.LayoutParams(params);

        if (params instanceof WindowManager.LayoutParams) {
            WindowManager.LayoutParams mResults = new WindowManager.LayoutParams();
            mResults.copyFrom((LayoutParams) params);

            mResults.flags |= LayoutParams.FLAG_NOT_FOCUSABLE;
            mResults.flags |= LayoutParams.FLAG_NOT_TOUCHABLE;
            mResults.flags |= LayoutParams.FLAG_LAYOUT_IN_SCREEN;
            mResults.windowAnimations = 0;
            mResults.type = LayoutParams.TYPE_APPLICATION_PANEL;
            mResults.x = 0;
            mResults.y = 0;
            mResults.format = PixelFormat.RGBA_8888;

            result = mResults;
        }
        result.width = ViewGroup.LayoutParams.MATCH_PARENT;
        result.height = ViewGroup.LayoutParams.MATCH_PARENT;
        return result;
    }

    private boolean isPopupInnerDecorView(View v) {
        if (v == null) return false;
        String viewSimpleClassName = v.getClass().getSimpleName();
        return TextUtils.equals(viewSimpleClassName, "PopupDecorView") || TextUtils.equals(viewSimpleClassName, "PopupViewContainer");
    }

    private PopupDecorViewProxy getHackPopupDecorView() {
        if (mHackPopupDecorView == null) return null;
        return mHackPopupDecorView.get();
    }

    private PopupTouchController getPopupController() {
        if (mPopupController == null) return null;
        return mPopupController.get();
    }

    private BasePopupHelper getBasePopupHelper() {
        if (mPopupHelper == null) return null;
        return mPopupHelper.get();
    }

    private PopupMaskLayout getMaskLayout() {
        if (mMaskLayout == null) return null;
        return mMaskLayout.get();
    }

    void bindPopupHelper(BasePopupHelper helper) {
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

    @Override
    void onAnimateDismissStart() {
        if (getMaskLayout() != null) {
            getMaskLayout().handleDismiss(-2);
        }
    }

    @Override
    void onNoAnimateDismiss() {
        if (getMaskLayout() != null) {
            getMaskLayout().handleDismiss(0);
        }
    }
}
