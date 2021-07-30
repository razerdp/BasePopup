package razerdp.demo.popup;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.utils.ButterKnifeUtil;
import razerdp.demo.utils.UIHelper;

/**
 * Created by 大灯泡 on 2019/9/20
 * <p>
 * Description：{@link razerdp.demo.model.common.CommonSlideInfo}
 */
public class PopupSlide extends BasePopupWindow {

    TextView tvItem1;
    TextView tvItem2;
    TextView tvItem3;

    public PopupSlide(Context context) {
        super(context);
        setContentView(R.layout.popup_slide);
    }
    @Override
    public void onViewCreated(View contentView) {
        ButterKnifeUtil.bind(this, contentView);
    }

    void click(View v) {
        UIHelper.toast(((TextView) v).getText().toString());
    }
}
