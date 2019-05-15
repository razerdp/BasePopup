package razerdp.basepopup;

import android.app.Activity;
import android.view.View;

/**
 * Created by 大灯泡 on 2019/5/13
 * <p>
 * Description：BasePopup支持，for support and lifecycle and android x
 */
public interface BasePopupSupporter {


    View findDecorView(BasePopupWindow basePopupWindow, Activity activity);

    BasePopupWindow attachLifeCycle(BasePopupWindow basePopupWindow, Object owner);

    BasePopupWindow removeLifeCycle(BasePopupWindow basePopupWindow, Object owner);

}
