package razerdp.demo.popup;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.basepopup.databinding.PopupSourceBinding;
import razerdp.demo.ui.ActivityLauncher;
import razerdp.demo.ui.WebActivity;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;

/**
 * Created by 大灯泡 on 2019/9/20
 * <p>
 * Description：{@link razerdp.demo.model.common.CommonSlideInfo}
 */
public class PopupSource extends BasePopupWindow {
    PopupSourceBinding mBinding;


    public PopupSource(Context context, String name, String javaUrl, String resUrl) {
        super(context);
        setContentView(R.layout.popup_source);
        mBinding.tvTitle.setText(name);
        mBinding.tvItem1.setOnClickListener(v -> {
            ActivityLauncher.start(context, WebActivity.class, new WebActivity.Data().setTitle(name)
                    .setUrl(javaUrl));
            dismiss();
        });
        mBinding.tvItem2.setOnClickListener(v -> {
            ActivityLauncher.start(context, WebActivity.class, new WebActivity.Data().setTitle(name)
                    .setUrl(resUrl));
            dismiss();
        });
    }

    @Override
    public void onViewCreated(View contentView) {
        mBinding = PopupSourceBinding.bind(contentView);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return AnimationHelper.asAnimation()
                .withTranslation(TranslationConfig.FROM_BOTTOM)
                .toShow();
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return AnimationHelper.asAnimation()
                .withTranslation(TranslationConfig.TO_BOTTOM)
                .toDismiss();
    }
}
