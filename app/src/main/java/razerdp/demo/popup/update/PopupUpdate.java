package razerdp.demo.popup.update;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.javabean.AppBean;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.widget.DPTextView;
import razerdp.util.animation.AlphaConfig;
import razerdp.util.animation.AnimationHelper;

/**
 * Created by 大灯泡 on 2020/3/4.
 */
public class PopupUpdate extends BasePopupWindow {
    TextView mTvTitle;
    TextView mTvContent;
    DPTextView mTvIgnore;
    DPTextView mTvUpdate;
    View controller;
    ProgressBar mProgressBar;

    AppBean mAppBean;

    public PopupUpdate(Context context) {
        super(context);
        setContentView(R.layout.popup_update);
        setOutSideDismiss(false);
        setBackPressEnable(false);
        setBlurBackgroundEnable(true);
    }

    @Override
    public void onViewCreated(View contentView) {
        super.onViewCreated(contentView);
    }


    @Override
    protected Animation onCreateShowAnimation() {
       return AnimationHelper.asAnimation()
               .withAlpha(AlphaConfig.IN)
               .toShow();
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return AnimationHelper.asAnimation()
                .withAlpha(AlphaConfig.OUT)
                .toDismiss();
    }

    public void showPopupWindow(AppBean appBean) {
        if (appBean == null) return;
        this.mAppBean = appBean;
        mTvTitle.setText(String.format("发现新版本：【%s】", appBean.getVersionName()));
        mTvContent.setText(appBean.getReleaseNote());
        showPopupWindow();
    }

    void ignore() {
        dismiss();
    }

    void download() {
        if (mAppBean == null) return;
        PgyUpdateManager.downLoadApk(mAppBean.getDownloadURL());
    }

    public void reset(){
        if (controller.getVisibility() != View.VISIBLE) {
            controller.setVisibility(View.VISIBLE);
            mProgressBar.setProgress(0);
            mProgressBar.setVisibility(View.GONE);
        }
        mTvUpdate.setText("立即升级");
    }

    public void onProgress(int progress) {
        if (controller.getVisibility() == View.VISIBLE) {
            controller.setVisibility(View.GONE);
            mProgressBar.setProgress(0);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        mProgressBar.setProgress(progress);
    }

    public void onError() {
        if (controller.getVisibility() != View.VISIBLE) {
            controller.setVisibility(View.VISIBLE);
            mProgressBar.setProgress(0);
            mProgressBar.setVisibility(View.GONE);
        }
        mTvUpdate.setText("重新下载");
    }

}
