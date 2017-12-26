package razerdp.basepopup;

import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Created by 大灯泡 on 2017/1/13.
 */

interface PopupController {


    boolean onBeforeDismiss();

    boolean callDismissAtOnce();

    boolean onDispatchKeyEvent(KeyEvent event);

    boolean onTouchEvent(MotionEvent event);

    boolean onBackPressed();

    boolean onOutSideTouch();


}
