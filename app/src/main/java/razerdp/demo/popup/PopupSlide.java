package razerdp.demo.popup;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.basepopup.databinding.PopupSlideBinding;
import razerdp.demo.utils.UIHelper;

/**
 * Created by 大灯泡 on 2019/9/20
 * <p>
 * Description：{@link razerdp.demo.model.common.CommonSlideInfo}
 */
public class PopupSlide extends BasePopupWindow {
    PopupSlideBinding mBinding;

    public PopupSlide(Context context) {
        super(context);
        setContentView(R.layout.popup_slide);
        setViewClickListener(this::click, mBinding.tvItem1, mBinding.tvItem2, mBinding.tvItem3);
    }

    @Override
    public void onViewCreated(View contentView) {
        mBinding = PopupSlideBinding.bind(contentView);
    }

    void click(View v) {
        UIHelper.toast(((TextView) v).getText().toString());
    }
}
