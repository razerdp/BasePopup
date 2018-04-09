package razerdp.basepopup;

import android.content.Context;
import android.graphics.PixelFormat;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.lang.ref.WeakReference;

import razerdp.blur.BlurImageView;
import razerdp.util.log.LogTag;
import razerdp.util.log.PopupLogUtil;

/**
 * Created by 大灯泡 on 2017/12/25.
 * <p>
 * 代理掉popup的windowmanager，在addView操作，拦截decorView的操作
 */
final class HackWindowManager extends InnerPopupWindowStateListener implements WindowManager {
    private static final String TAG = "HackWindowManager";
    private WindowManager mWindowManager;
    private WeakReference<PopupTouchController> mPopupController;
    private WeakReference<HackPopupDecorView> mHackPopupDecorView;
    private WeakReference<BasePopupHelper> mPopupHelper;
    private WeakReference<BlurImageView> mBlurImageView;
    private static int statusBarHeight;

    public HackWindowManager(WindowManager windowManager, PopupTouchController popupTouchController) {
        mWindowManager = windowManager;
        mPopupController = new WeakReference<PopupTouchController>(popupTouchController);
    }

    @Override
    public Display getDefaultDisplay() {
        return mWindowManager == null ? null : mWindowManager.getDefaultDisplay();
    }

    @Override
    public void removeViewImmediate(View view) {
        if (mWindowManager == null) return;
        checkStatusBarHeight(view.getContext());
        PopupLogUtil.trace(LogTag.i, TAG, "WindowManager.removeViewImmediate  >>>  " + view.getClass().getSimpleName());
        if (isPopupInnerDecorView(view) && getHackPopupDecorView() != null) {
            if (getBlurImageView() != null) {
                try {
                    mWindowManager.removeViewImmediate(getBlurImageView());
                    mBlurImageView.clear();
                    mBlurImageView = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            HackPopupDecorView hackPopupDecorView = getHackPopupDecorView();
            mWindowManager.removeViewImmediate(hackPopupDecorView);
            mHackPopupDecorView.clear();
            mHackPopupDecorView = null;
        } else {
            mWindowManager.removeViewImmediate(view);
        }
    }

    @Override
    public void addView(View view, ViewGroup.LayoutParams params) {
        if (mWindowManager == null) return;
        checkStatusBarHeight(view.getContext());
        PopupLogUtil.trace(LogTag.i, TAG, "WindowManager.addView  >>>  " + view.getClass().getSimpleName());
        if (isPopupInnerDecorView(view)) {
            /**
             * 此时的params是WindowManager.LayoutParams，需要留意强转问题
             * popup内部有scrollChangeListener，会有params强转为WindowManager.LayoutParams的情况
             */
            BasePopupHelper helper = getBasePopupHelper();
            BlurImageView blurImageView = null;

            //添加popup主体
            final HackPopupDecorView hackPopupDecorView = new HackPopupDecorView(view.getContext());
            params = hackPopupDecorView.addPopupDecorView(view, params, helper, getPopupController());
            mHackPopupDecorView = new WeakReference<HackPopupDecorView>(hackPopupDecorView);
            //添加背景模糊层
            if (helper != null && helper.isAllowToBlur()) {
                blurImageView = new BlurImageView(view.getContext());
                blurImageView.attachBlurOption(helper.getBlurOption());
                mBlurImageView = new WeakReference<BlurImageView>(blurImageView);
                if (getPopupController() instanceof BasePopupWindow) {
                    ((BasePopupWindow) getPopupController()).setOnInnerPopupWIndowStateListener(this);
                }
                hackPopupDecorView.setOnAttachListener(new HackPopupDecorView.OnAttachListener() {
                    @Override
                    public void onAttachtoWindow() {
                        if (getBlurImageView() != null) {
                            getBlurImageView().start(-2);
                        }
                    }
                });
                mWindowManager.addView(blurImageView, createBlurBackgroundWindowParams(params));
            }
            mWindowManager.addView(hackPopupDecorView, params);
        } else {
            mWindowManager.addView(view, params);
        }
    }

    @Override
    public void updateViewLayout(View view, ViewGroup.LayoutParams params) {
        if (mWindowManager == null) return;
        checkStatusBarHeight(view.getContext());
        PopupLogUtil.trace(LogTag.i, TAG, "WindowManager.updateViewLayout  >>>  " + view.getClass().getSimpleName());
        if (isPopupInnerDecorView(view) && getHackPopupDecorView() != null) {
            HackPopupDecorView hackPopupDecorView = getHackPopupDecorView();
            if (params instanceof WindowManager.LayoutParams) {
                WindowManager.LayoutParams thisParams = ((LayoutParams) params);
                ViewGroup.LayoutParams hackDecorParams = hackPopupDecorView == null ? null : hackPopupDecorView.getLayoutParams();
                if (hackDecorParams instanceof WindowManager.LayoutParams) {
                    WindowManager.LayoutParams hackParams = (LayoutParams) hackDecorParams;
                    thisParams.x = thisParams.x + hackParams.x;
                    thisParams.y = thisParams.y - statusBarHeight + hackParams.y;
                }
            }
            mWindowManager.updateViewLayout(hackPopupDecorView, params);
        } else {
            mWindowManager.updateViewLayout(view, params);
        }
    }

    @Override
    public void removeView(View view) {
        if (mWindowManager == null) return;
        checkStatusBarHeight(view.getContext());
        PopupLogUtil.trace(LogTag.i, TAG, "WindowManager.removeView  >>>  " + view.getClass().getSimpleName());
        if (isPopupInnerDecorView(view) && getHackPopupDecorView() != null) {
            if (getBlurImageView() != null) {
                try {
                    mWindowManager.removeView(getBlurImageView());
                    mBlurImageView.clear();
                    mBlurImageView = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            HackPopupDecorView hackPopupDecorView = getHackPopupDecorView();
            mWindowManager.removeView(hackPopupDecorView);
            mHackPopupDecorView.clear();
            mHackPopupDecorView = null;
        } else {
            mWindowManager.removeView(view);
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

    private HackPopupDecorView getHackPopupDecorView() {
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

    private BlurImageView getBlurImageView() {
        if (mBlurImageView == null) return null;
        return mBlurImageView.get();
    }

    void bindPopupHelper(BasePopupHelper helper) {
        mPopupHelper = new WeakReference<BasePopupHelper>(helper);
    }

    void checkStatusBarHeight(Context context) {
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
        if (getBlurImageView() != null) {
            getBlurImageView().dismiss(-2);
        }
    }

    @Override
    void onNoAnimateDismiss() {
        if (getBlurImageView() != null) {
            getBlurImageView().dismiss(0);
        }
    }
}
