package razerdp.demo.popup;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.basepopup.databinding.PopupSlideSmallBinding;
import razerdp.demo.model.common.CommonSlideInfo;
import razerdp.demo.utils.UIHelper;

/**
 * Created by 大灯泡 on 2019/9/20
 * <p>
 * Description：{@link CommonSlideInfo}
 */
public class PopupSlideSmall extends BasePopupWindow {
    PopupSlideSmallBinding mBinding;

    public PopupSlideSmall(Context context) {
        super(context);
        setContentView(R.layout.popup_slide_small);
        setViewClickListener(this::click, mBinding.tvItem1, mBinding.tvItem2, mBinding.tvItem3);
    }

    @Override
    public void onViewCreated(View contentView) {
        mBinding = PopupSlideSmallBinding.bind(contentView);
    }


    void click(View v) {
        UIHelper.toast(((TextView) v).getText().toString());
    }
}
