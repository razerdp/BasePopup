package razerdp.basepopup;

import android.app.Activity;
import android.view.View;

/**
 * Created by 大灯泡 on 2019/5/13
 * <p>
 * Description：BasePopup component
 */
public interface BasePopupComponent {

    View findDecorView(BasePopupWindow basePopupWindow, Activity activity);

    BasePopupWindow attachLifeCycle(BasePopupWindow basePopupWindow, Object owner);
}
