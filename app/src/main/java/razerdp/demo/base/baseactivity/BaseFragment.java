package razerdp.demo.base.baseactivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import razerdp.basepopup.R;
import razerdp.demo.base.interfaces.ClearMemoryObject;
import razerdp.demo.utils.StringUtil;
import razerdp.demo.widget.StatusBarViewPlaceHolder;
import razerdp.demo.widget.TitleBarView;
import razerdp.util.KeyboardUtils;
import razerdp.util.log.PopupLog;

/**
 * Created by 大灯泡 on 2019/4/9
 * <p>
 * Description：
 */
public abstract class BaseFragment extends Fragment
        implements ClearMemoryObject, TitleBarView.OnTitleBarClickCallback {
    protected final String TAG = getClass().getSimpleName();

    private Context mContext;
    protected View mRootView;
    protected TitleBarView mTitleBar;
    protected StatusBarViewPlaceHolder mStatusBarHolder;
    protected final State mState = new State();
    private Unbinder mUnbinder;
    private Dialog mLoadingDialog;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        onHandledArguments(getArguments());
    }

    private void onHandledArguments(@Nullable Bundle arguments) {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mContext == null) {
            mContext = requireContext();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            PopupLog.d("openFragment", "当前打开fragment： " + PopupLog.wrapLocation(this.getClass(), 1));
            mRootView = inflater.inflate(contentViewLayoutId(), container, false);
            mUnbinder = ButterKnife.bind(this, mRootView);
            initTitleBarAndEmptyView(mRootView);
            onInitViews(mRootView);
            onAfterInitViews();
        }
        mState.handleShow();
        mState.state |= State.FLAG_INIT;
        return mRootView;

    }


    private void initTitleBarAndEmptyView(View rootView) {
        if (rootView == null) return;
        this.mTitleBar = rootView.findViewById(R.id.title_bar_view);
        this.mStatusBarHolder = rootView.findViewById(R.id.statusbar_placeholder);
        if (mTitleBar != null) {
            mTitleBar.setOnTitlebarClickCallback(this);
        }
    }

    //region abstract
    @LayoutRes
    public abstract int contentViewLayoutId();

    protected abstract void onInitViews(View mRootView);


    //endregion
    protected void onAfterInitViews() {

    }

    @Override
    public void clearMemory() {

    }

    protected boolean onBackPressed() {
        KeyboardUtils.close(getActivity());
        try {
            if (getFragmentManager() != null && getFragmentManager().getBackStackEntryCount() > 0) {
                getFragmentManager().popBackStack();
                return true;
            } else {
                getActivity().finish();
            }
        } catch (Exception e) {

        }
        return false;
    }

    protected void finishActivity() {
        KeyboardUtils.close(getActivity());
        try {
            getActivity().finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public Context getContext() {
        if (mContext != null) {
            return mContext;
        }
        return super.getContext();
    }


    protected Dialog onCreateLoadingDialog() {
        return null;
    }
    //region life


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //for viewpager，因为创建的时候就回调这货，可能ui还没开始创建，需要判断
        if (mState.hasFlag(State.FLAG_INIT)) {
            if (isVisibleToUser) {
                mState.handleShow();
            } else {
                mState.handleHided();
            }
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //for show / hide
        if (mState.hasFlag(State.FLAG_INIT)) {
            if (hidden) {
                mState.handleHided();
            } else {
                mState.handleShow();
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        //当activity回调resume的时候，其所有fragment都会回调，因此需要判断具体的
        //getUserVisibleHint不在vp的时候默认就是true
        if (getUserVisibleHint() && !isHidden()) {
            mState.handleShow();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getUserVisibleHint()) {
            mState.handleHided();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
            mUnbinder = null;
        }
        mContext = null;
        mRootView = null;
    }

    //endregion

    public final View getRootView() {
        return mRootView;
    }

    //region titlebar

    @Override
    public void onTitleLeftClick(View view) {

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

    public void setTitle(@StringRes int titleText) {
        setTitle(StringUtil.getString(titleText));
    }

    public void setTitle(CharSequence titleText) {
        if (mTitleBar != null) {
            mTitleBar.setTitle(titleText);
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

    public void setTitleRightText(@StringRes int rightText) {
        setTitleRightText(StringUtil.getString(rightText));
    }

    public void setTitleRightText(CharSequence rightText) {
        if (mTitleBar != null) {
            mTitleBar.setRightText(rightText);
        }
    }

    public final BaseFragment self() {
        return this;
    }
    //endregion

    //region callback

    public void onShow() {
    }

    public void onHided() {
    }

    //endregion

    public CharSequence getTitle() {
        return null;
    }

    private class State {
        private long lastShowingTime = 0;
        static final int FLAG_IDLE = 0x00000000;
        static final int FLAG_INIT = 0x00000001;
        static final int STATE_SHOWING_LOADING = 0x00000100;

        static final int FLAG_SHOWING = 0x00010000;
        static final int FLAG_HIDED = FLAG_SHOWING << 1;

        int state = FLAG_IDLE;


        void handleShow() {
            if ((state & FLAG_SHOWING) != 0) return;
            state |= FLAG_SHOWING;
            onShow();
            state &= ~FLAG_HIDED;
        }


        void handleHided() {
            if ((state & FLAG_HIDED) != 0) return;
            state |= FLAG_HIDED;
            onHided();
            lastShowingTime = SystemClock.uptimeMillis();
            state &= ~FLAG_SHOWING;
        }

        void computeFlag(int mFlag) {
            state |= mFlag;
        }

        void removeFlag(int mFlag) {
            state &= ~mFlag;
        }

        void resetFlag() {
            state = FLAG_IDLE;
        }

        boolean hasFlag(int mFlag) {
            return (state & mFlag) != 0;
        }
    }
}
