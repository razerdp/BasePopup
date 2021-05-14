package razerdp.demo.popup;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import butterknife.BindView;
import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.utils.ButterKnifeUtil;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;

/**
 * Created by 大灯泡 on 2020/12/29
 * <p>
 * Description：测试update方法
 */
public class PopupUpdateTest extends BasePopupWindow {
    @BindView(R.id.tv_change_view)
    TextView tvChangeView;
    @BindView(R.id.tv_change_size)
    TextView tvChangeSize;
    @BindView(R.id.tv_update)
    TextView tvUpdate;

    OnTvChangeViewClickCallback cb;
    OnTvChangeViewSizeClickCallback sizeCb;
    OnUpdateClickCallback updateCb;

    public PopupUpdateTest(Context context) {
        super(context);
        setContentView(R.layout.popup_update_test);
        setBackground(null);
        setPopupGravity(Gravity.BOTTOM);
        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateCb != null) {
                    updateCb.onClick();
                }
            }
        });
        tvChangeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb != null) {
                    cb.onClick();
                }
            }
        });
        tvChangeSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sizeCb != null) {
                    sizeCb.onClick();
                }
            }
        });
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return AnimationHelper.asAnimation()
                .withTranslation(TranslationConfig.FROM_TOP)
                .toShow();
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return AnimationHelper.asAnimation()
                .withTranslation(TranslationConfig.TO_TOP)
                .toDismiss();
    }

    @Override
    public void onViewCreated(View contentView) {
        ButterKnifeUtil.bind(this, contentView);
    }


    public void setOnUpdateClickCallback(OnUpdateClickCallback cb) {
        this.updateCb = cb;
    }

    public void setOnTvChangeViewClickCallback(OnTvChangeViewClickCallback cb) {
        this.cb = cb;
    }

    public void setOnTvChangeViewSizeClickCallback(OnTvChangeViewSizeClickCallback cb) {
        this.sizeCb = cb;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        cb = null;
        updateCb = null;
    }

    public interface OnTvChangeViewClickCallback {
        void onClick();
    }

    public interface OnTvChangeViewSizeClickCallback {
        void onClick();
    }

    public interface OnUpdateClickCallback {
        void onClick();
    }
}
