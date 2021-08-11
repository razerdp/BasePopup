package razerdp.demo.popup.options;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import razerdp.basepopup.R;
import razerdp.basepopup.databinding.PopupOptionAnyposBinding;
import razerdp.demo.model.common.CommonAnyPosInfo;

/**
 * Created by 大灯泡 on 2019/9/20.
 *
 * @see CommonAnyPosInfo
 */
public class PopupAnyPosOption extends BaseOptionPopup<CommonAnyPosInfo> {
    PopupOptionAnyposBinding mBinding;

    public PopupAnyPosOption(Context context) {
        super(context);
        setContentView(R.layout.popup_option_anypos);
        mBinding.tvGo.setOnClickListener(v -> ok());
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        mBinding = PopupOptionAnyposBinding.bind(contentView);
    }

    void ok() {
        mInfo.outSideTouchable = mBinding.checkOutsideTouch.isChecked();
        mInfo.blur = mBinding.checkBlur.isChecked();
        dismiss();
    }
}
