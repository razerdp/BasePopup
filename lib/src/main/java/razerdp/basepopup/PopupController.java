package razerdp.basepopup;

import android.view.KeyEvent;

/**
 * Created by 大灯泡 on 2017/1/13.
 */

interface PopupController {


    boolean onBeforeDismiss();

    boolean callDismissAtOnce();

    boolean onBackPressed();

    boolean onDispatchKeyEvent(KeyEvent event);

}
