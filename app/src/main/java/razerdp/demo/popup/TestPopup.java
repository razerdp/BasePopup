package razerdp.demo.popup;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by 大灯泡 on 2018/5/11.
 */
public class TestPopup extends BasePopupWindow {
    public TestPopup(Context context) {
        super(context);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return null;
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return null;
    }

    @Override
    public View onCreateContentView() {
        return null;
    }
}
