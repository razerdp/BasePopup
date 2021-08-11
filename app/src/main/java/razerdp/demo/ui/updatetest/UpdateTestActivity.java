package razerdp.demo.ui.updatetest;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.viewbinding.ViewBinding;
import razerdp.basepopup.databinding.ActivityUpdateTestBinding;
import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.base.baseactivity.BaseBindingActivity;
import razerdp.demo.popup.PopupUpdateTest;
import razerdp.demo.utils.RandomUtil;

/**
 * Created by 大灯泡 on 2020/12/29
 * <p>
 * Description：
 */
public class UpdateTestActivity extends BaseBindingActivity<ActivityUpdateTestBinding> {
    PopupUpdateTest updateTest;

    int popupWidth;
    int popupHeight;

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public ActivityUpdateTestBinding onCreateViewBinding(LayoutInflater layoutInflater) {
        return ActivityUpdateTestBinding.inflate(layoutInflater);
    }

    @Override
    protected void onInitView(View decorView) {
        mBinding.tvTest.setOnClickListener(v -> {
            if (updateTest == null) {
                updateTest = new PopupUpdateTest(self());
                updateTest.setOnTvChangeViewClickCallback(UpdateTestActivity.this::randomViewPosition);
                updateTest.setOnUpdateClickCallback(() -> updateTest.update(v));
                updateTest.setOnTvChangeViewSizeClickCallback(this::randomViewSize);
            }
            updateTest.showPopupWindow(v);
        });
        mBinding.tvTest.post(this::randomViewPosition);
    }

    void randomViewPosition() {
        View decor = getWindow().getDecorView();
        int x = RandomUtil.randomInt(0, decor.getWidth() - mBinding.tvTest.getWidth());
        int y = RandomUtil.randomInt(0, decor.getHeight() - mBinding.tvTest.getHeight());
        mBinding.tvTest.setTranslationX(x);
        mBinding.tvTest.setTranslationY(y);
    }

    void randomViewSize() {
        if (popupWidth == 0) {
            popupWidth = updateTest.getWidth();
        }
        if (popupHeight == 0) {
            popupHeight = updateTest.getHeight();
        }
        View decor = getWindow().getDecorView();
        int width = RandomUtil.randomInt(popupWidth >> 1, decor.getWidth());
        int height = RandomUtil.randomInt(popupHeight, decor.getHeight());
        updateTest.update(width * 1.0f, height * 1.0f);
    }
}
