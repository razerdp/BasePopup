package razerdp.demo.base.baseactivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import pub.devrel.easypermissions.EasyPermissions;
import razerdp.basepopup.R;
import razerdp.demo.base.StatusBarHelper;
import razerdp.demo.base.interfaces.ClearMemoryObject;
import razerdp.demo.event.LiveDataBus;
import razerdp.demo.utils.StringUtil;
import razerdp.demo.utils.ToolUtil;
import razerdp.demo.widget.StatusBarViewPlaceHolder;
import razerdp.demo.widget.TitleBarView;
import razerdp.demo.widget.dialog.LoadingDialog;
import razerdp.util.KeyboardUtils;
import razerdp.util.log.PopupLog;

public abstract class BaseActivity<T extends BaseActivity.IntentData>
        extends AppCompatActivity
        implements ClearMemoryObject, TitleBarView.OnTitleBarClickCallback {

    protected final String TAG = getClass().getSimpleName();
    public static final String INTENT_DATA = "INTENT_DATA";
    protected T mActivityData;


    private StatusBarConfig mStatusBarConfig;
    protected TitleBarView mTitleBar;
    protected StatusBarViewPlaceHolder mStatusBarHolder;
    protected final State mState = new State();
    private Dialog mLoadingDialog;
    private Unbinder mUnbinder;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PopupLog.d("openAct", "当前打开activity： " + PopupLog.wrapLocation(this.getClass(), 1));

        onStartCreate(savedInstanceState);
        onHandleIntent(getIntent());
        mStatusBarConfig = new StatusBarConfig();
        onApplyStatusBarConfig(mStatusBarConfig);
        if (contentViewLayoutId() != 0) {
            setContentView(contentViewLayoutId());
        }
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        LiveDataBus.INSTANCE.getActivityReenterLiveData().send(Pair.create(resultCode, data));
    }

    protected void onStartCreate(Bundle savedInstanceState) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    //region ===============================abstract===============================

    protected abstract void onHandleIntent(Intent intent);

    @LayoutRes
    public abstract int contentViewLayoutId();


    protected abstract void onInitView(View decorView);

    //endregion ===============================abstract===============================

    //region ===============================tools===============================
    public BaseActivity self() {
        return BaseActivity.this;
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        onAfterInitContentView();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        onAfterInitContentView();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        onAfterInitContentView();
    }

    public T getActivityData() {
        if (mActivityData != null) {
            return mActivityData;
        }
        Intent intent = getIntent();
        try {
            if (intent != null) {
                mActivityData = (T) intent.getSerializableExtra(INTENT_DATA);
            }
        } catch (ClassCastException e) {

        }
        return mActivityData;
    }

    //endregion ===============================tools===============================


    //region ===============================views===============================
    protected void onAfterInitContentView() {
        if (mUnbinder == null) {
            mUnbinder = ButterKnife.bind(this);
        }
        onInitStatusBar();
        this.mTitleBar = findViewById(R.id.title_bar_view);
        this.mStatusBarHolder = findViewById(R.id.statusbar_placeholder);
        if (mTitleBar != null) {
            mTitleBar.setOnTitlebarClickCallback(this);
        }

        if (!mState.hasContent()) {
            onInitView(getWindow().getDecorView());
            mState.setContent();
        }
    }

    protected void onInitStatusBar() {
        if (mStatusBarConfig == null || mState.isInitStatus()) return;
        onInitStatusBarInternal(mStatusBarConfig);
        mState.initStatus();
    }

    protected void onInitStatusBarInternal(StatusBarConfig config) {
        if (config == null) return;
        if (config.translucentStatus) {
            StatusBarHelper.setTranslucentStatus(this);
        }
        StatusBarHelper.setRootViewFitsSystemWindows(this, config.fitsSystemWindows);
        StatusBarHelper.setStatusBarFontIconDark(this, config.darkMode);
        if (!config.translucentStatus) {
            StatusBarHelper.setStatusBarColor(this, config.statusBarColor);
        }
    }
    //endregion ===============================views===============================

    //region ===============================statusbar相关===============================

    public static class StatusBarConfig {
        private boolean fitsSystemWindows = false;
        private boolean translucentStatus = true;
        private boolean darkMode = false;
        private int statusBarColor = Color.TRANSPARENT;
        boolean changed;
        private String name;

        public String getName() {
            return name;
        }

        public StatusBarConfig setName(String name) {
            this.name = name;
            return this;
        }

        public boolean isFitsSystemWindows() {
            return fitsSystemWindows;
        }

        public boolean isTranslucentStatus() {
            return translucentStatus;
        }

        public boolean isDarkMode() {
            return darkMode;
        }

        public int getStatusBarColor() {
            return statusBarColor;
        }

        public StatusBarConfig setFitsSystemWindows(boolean fitsSystemWindows) {
            this.fitsSystemWindows = fitsSystemWindows;
            changed = true;
            return this;
        }

        public StatusBarConfig setTranslucentStatus(boolean translucentStatus) {
            this.translucentStatus = translucentStatus;
            changed = true;
            return this;
        }

        public StatusBarConfig setDarkMode(boolean darkMode) {
            this.darkMode = darkMode;
            changed = true;
            return this;
        }

        public StatusBarConfig setStatusBarColor(int statusBarColor) {
            this.statusBarColor = statusBarColor;
            changed = true;
            return this;
        }

        public boolean isChanged() {
            return changed;
        }

        public void reset() {
            fitsSystemWindows = false;
            translucentStatus = true;
            darkMode = false;
            statusBarColor = Color.TRANSPARENT;
            changed = false;
        }
    }

    public StatusBarViewPlaceHolder getStatusBarHolder() {
        return mStatusBarHolder;
    }

    //endregion ===============================statusbar相关===============================

    //region ===============================other===============================

    private String mActivityJumpTag;
    private long mActivityJumpTime;

    @SuppressLint("RestrictedApi")
    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        if (canStartActivity(intent)) {
            super.startActivityForResult(intent, requestCode, options);
        }
    }

    /**
     * 检查当前 Activity 是否重复跳转了，不需要检查则重写此方法并返回 true 即可
     *
     * @param intent 用于跳转的 Intent 对象
     * @return 检查通过返回true, 检查不通过返回false
     */
    protected boolean canStartActivity(Intent intent) {
        // 标记对象
        String tag;
        if (intent.getComponent() != null) { // 显式跳转
            tag = intent.getComponent().getClassName();
        } else if (intent.getAction() != null) { // 隐式跳转
            tag = intent.getAction();
        } else {
            return true;
        }

        long curTime = SystemClock.uptimeMillis();

        if (TextUtils.equals(tag, mActivityJumpTag) && curTime - mActivityJumpTime < 500) {
            // 检查不通过
            return false;
        }

        // 记录启动标记和时间
        mActivityJumpTag = tag;
        mActivityJumpTime = curTime;
        return true;
    }

    @Override
    public void clearMemory() {

    }


    public void showLoadingDialog(boolean cancelable) {
        if ((mState.state & State.STATE_SHOWING_LOADING) != 0) return;
        mState.state |= State.STATE_SHOWING_LOADING;
        if (mLoadingDialog == null) {
            mLoadingDialog = onCreateLoadingDialog();
        }
        mLoadingDialog.setCancelable(cancelable);
        if (mLoadingDialog != null && !mLoadingDialog.isShowing()) {
            runOnUiThread(() -> mLoadingDialog.show());
        }
    }

    public void dismissLoadingDialog() {
        if (mLoadingDialog == null || !mLoadingDialog.isShowing() || (mState.state & State.STATE_SHOWING_LOADING) == 0) {
            return;
        }
        mState.state &= ~State.STATE_SHOWING_LOADING;
        if (ToolUtil.isMainThread()) {
            mLoadingDialog.dismiss();
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLoadingDialog.dismiss();
                }
            });
        }
    }


    public void setLoadingDialogText(String txt) {
        if (mLoadingDialog instanceof LoadingDialog) {
            ((LoadingDialog) mLoadingDialog).setDesc(txt);
        }
    }

    public void setActionDialogText(String txt, View.OnClickListener l) {
        if (mLoadingDialog instanceof LoadingDialog) {
            ((LoadingDialog) mLoadingDialog).setAction(txt, l);
        }
    }

    protected Dialog onCreateLoadingDialog() {
        return LoadingDialog.create(this);
    }

    //endregion ===============================other===============================

    //region life


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
            mUnbinder = null;
        }
    }

    @Override
    public void finish() {
        KeyboardUtils.close(this);
        super.finish();
    }

    //endregion

    public static abstract class IntentData implements Serializable {
        int requestCode = -1;
        //该map不参与serializable的序列化，仅仅在put into intent之前和新activity获取到intent之后存在
        private HashMap<String, Parcelable> mParcelableMap;

        public int getRequestCode() {
            return requestCode;
        }

        public IntentData setRequestCode(int requestCode) {
            this.requestCode = requestCode;
            return this;
        }


        public <P extends Parcelable> IntentData appendParcelable(@NonNull P who) {
            Class cls = who.getClass();
            return appendParcelable(cls.getName(), who);
        }

        public <P extends Parcelable> IntentData appendParcelable(@NonNull String key, @NonNull P who) {
            if (mParcelableMap == null) {
                mParcelableMap = new HashMap<>();
            }
            mParcelableMap.put(key, who);
            return this;
        }

        public final Intent writeToIntent(@NonNull Intent intent) {
            if (!ToolUtil.isEmpty(mParcelableMap)) {
                for (Map.Entry<String, Parcelable> entry : mParcelableMap.entrySet()) {
                    intent.putExtra(entry.getKey(), entry.getValue());
                }
                //put 完之后必须清楚parcelablemap，否则Serializable和Parcelable一起存在的时候会冲突
                mParcelableMap.clear();
                mParcelableMap = null;
            }

            //处理完parcelable再处理自己（Serializable）
            intent.putExtra(INTENT_DATA, this);
            return intent;
        }

        @Nullable
        public <P extends Parcelable> P getParcel(Activity act, Class<P> parcelClass) {
            return getParcel(act, parcelClass.getName());
        }

        @Nullable
        public <P extends Parcelable> P getParcel(Activity act, String key) {
            return act.getIntent().getParcelableExtra(key);
        }
    }

    protected void onApplyStatusBarConfig(@NonNull StatusBarConfig config) {

    }

    //region ===============================titlebar===============================

    @Override
    public void onTitleLeftClick(View view) {
        onBackPressed();
    }

    @Override
    public void onTitleRightClick(View view) {

    }

    @Override
    public void onTitleClick() {

    }

    @Override
    public void onTitleDoubleClick() {

    }

    @Override
    public boolean onTitleLongClick() {
        return false;
    }


    public void setTitleMode(@TitleBarView.TitleBarMode int mode) {
        if (mTitleBar != null) {
            mTitleBar.setMode(mode);
        }
    }

    @Override
    public void setTitle(int titleId) {
        setTitle(StringUtil.getString(titleId));
    }

    @Override
    public void setTitle(CharSequence title) {
        if (mTitleBar != null) {
            mTitleBar.setTitle(title);
        }
    }

    public void setTitleLeftText(@StringRes int leftText) {
        setTitleLeftText(StringUtil.getString(leftText));
    }

    public void setTitleLeftText(CharSequence leftText) {
        if (mTitleBar != null) {
            mTitleBar.setLeftText(leftText);
        }
    }

    public void setTitleRightTextColor(int color) {
        if (mTitleBar != null) {
            mTitleBar.setRightTextColor(color);
        }
    }

    public void setRightTextEnable(boolean enable) {
        if (mTitleBar != null) {
            mTitleBar.setRightTextEnable(enable);
        }
    }

    public void setTitleRightText(@StringRes int rightText) {
        setTitleRightText(StringUtil.getString(rightText));
    }

    public void setTitleRightText(CharSequence rightText) {
        if (mTitleBar != null) {
            mTitleBar.setRightText(rightText);
        }
    }
    //endregion ===============================titlebar===============================


    @Override
    public final void onBackPressed() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment f : fragments) {
            if (f instanceof BaseFragment) {
                if (((BaseFragment) f).onBackPressed()) return;
            }
        }
        if (!onBackPressedInternal()) {
            super.onBackPressed();
        }
    }

    public boolean onBackPressedInternal() {
        return false;
    }

    private class State {
        static final int STATE_INIT = 0x0000;
        static final int STATE_INIT_STATUS = 0x0001;
        static final int STATE_SETCONTENT = 0x0010;
        static final int STATE_SHOWING_LOADING = 0x0100;

        int state = STATE_INIT;

        void initStatus() {
            state |= STATE_INIT_STATUS;
        }

        boolean isInitStatus() {
            return (state & STATE_INIT_STATUS) != 0;
        }

        void setContent() {
            state |= STATE_SETCONTENT;
        }

        boolean hasContent() {
            return (state & STATE_SETCONTENT) != 0;
        }
    }
}
