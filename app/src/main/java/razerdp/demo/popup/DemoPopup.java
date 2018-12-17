package razerdp.demo.popup;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;

/**
 * Created by 大灯泡 on 2016/1/15.
 * 普通的popup
 */
public class DemoPopup extends BasePopupWindow {

    TextView tvTest;

    public DemoPopup(Context context) {
        super(context);
        tvTest = findViewById(R.id.tv_test);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_normal);
    }

    public void setText(CharSequence text) {
        tvTest.setText(text);
    }

}
