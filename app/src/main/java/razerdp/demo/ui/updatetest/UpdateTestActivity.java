package razerdp.demo.ui.updatetest;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import razerdp.basepopup.R;
import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.popup.PopupUpdateTest;
import razerdp.demo.utils.RandomUtil;

/**
 * Created by 大灯泡 on 2020/12/29
 * <p>
 * Description：
 */
public class UpdateTestActivity extends BaseActivity {
    @BindView(R.id.tv_test)
    TextView tvTest;

    PopupUpdateTest updateTest;

    int popupWidth;
    int popupHeight;

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public int contentViewLayoutId() {
        return R.layout.activity_update_test;
    }

    @Override
    protected void onInitView(View decorView) {
        tvTest.setOnClickListener(v -> {
            if (updateTest == null) {
                updateTest = new PopupUpdateTest(self());
                updateTest.setOnTvChangeViewClickCallback(UpdateTestActivity.this::randomViewPosition);
                updateTest.setOnUpdateClickCallback(() -> updateTest.update(v));
                updateTest.setOnTvChangeViewSizeClickCallback(this::randomViewSize);
            }
            updateTest.showPopupWindow(v);
        });
        tvTest.post(this::randomViewPosition);
    }

    void randomViewPosition() {
        View decor = getWindow().getDecorView();
        int x = RandomUtil.randomInt(0, decor.getWidth() - tvTest.getWidth());
        int y = RandomUtil.randomInt(0, decor.getHeight() - tvTest.getHeight());
        tvTest.setTranslationX(x);
        tvTest.setTranslationY(y);
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
