package razerdp.demo.popup;

import android.content.Context;
import android.view.View;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;

/**
 * Created by 大灯泡 on 2016/1/15.
 * 普通的popup
 */
public class DemoPopup extends BasePopupWindow {

    public DemoPopup(Context context) {
        super(context);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_normal);
    }

}
