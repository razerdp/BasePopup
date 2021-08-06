package razerdp.demo.popup.update;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;

import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.javabean.AppBean;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.basepopup.databinding.PopupUpdateBinding;
import razerdp.util.animation.AlphaConfig;
import razerdp.util.animation.AnimationHelper;

/**
 * Created by 大灯泡 on 2020/3/4.
 */
public class PopupUpdate extends BasePopupWindow {
    PopupUpdateBinding mBinding;

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
        mBinding = PopupUpdateBinding.bind(contentView);
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
        mBinding.tvTitle.setText(String.format("发现新版本：【%s】", appBean.getVersionName()));
        mBinding.tvContent.setText(appBean.getReleaseNote());
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
        if (mBinding.layoutController.getVisibility() != View.VISIBLE) {
            mBinding.layoutController.setVisibility(View.VISIBLE);
            mBinding.progress.setProgress(0);
            mBinding.progress.setVisibility(View.GONE);
        }
        mBinding.tvUpdate.setText("立即升级");
    }

    public void onProgress(int progress) {
        if (mBinding.layoutController.getVisibility() == View.VISIBLE) {
            mBinding.layoutController.setVisibility(View.GONE);
            mBinding.progress.setProgress(0);
            mBinding.progress.setVisibility(View.VISIBLE);
        }

        mBinding.progress.setProgress(progress);
    }

    public void onError() {
        if (mBinding.layoutController.getVisibility() != View.VISIBLE) {
            mBinding.layoutController.setVisibility(View.VISIBLE);
            mBinding.progress.setProgress(0);
            mBinding.progress.setVisibility(View.GONE);
        }
        mBinding.tvUpdate.setText("重新下载");
    }

}
