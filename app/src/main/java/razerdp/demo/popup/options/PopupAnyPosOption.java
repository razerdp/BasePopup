package razerdp.demo.popup.options;

import android.content.Context;
import android.view.View;

import androidx.appcompat.widget.AppCompatCheckBox;
import butterknife.BindView;
import butterknife.OnClick;
import razerdp.basepopup.R;
import razerdp.demo.model.common.CommonAnyPosInfo;
import razerdp.demo.widget.DPTextView;

/**
 * Created by 大灯泡 on 2019/9/20.
 *
 * @see CommonAnyPosInfo
 */
public class PopupAnyPosOption extends BaseOptionPopup<CommonAnyPosInfo> {
    @BindView(R.id.check_clip_screen)
    AppCompatCheckBox mCheckClipScreen;
    @BindView(R.id.check_blur)
    AppCompatCheckBox mCheckBlur;
    @BindView(R.id.tv_go)
    DPTextView mTvGo;

    public PopupAnyPosOption(Context context) {
        super(context);
        mCheckClipScreen.setChecked(true);
    }


    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_option_anypos);
    }

    @OnClick(R.id.tv_go)
    void ok() {
        mInfo.clipScreen = mCheckClipScreen.isChecked();
        mInfo.blur = mCheckBlur.isChecked();
        dismiss();
    }
}
