package razerdp.basepopup;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;

/**
 * Created by 大灯泡 on 2017/12/25.
 */
public class HackPopupDecorView extends FrameLayout {
    private static final String TAG = "HackPopupDecorView";
    public HackPopupDecorView(Context context) {
        super(context);
    }

    public HackPopupDecorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HackPopupDecorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.i(TAG, "dispatchKeyEvent: hook成功");
        return super.dispatchKeyEvent(event);
    }
}
