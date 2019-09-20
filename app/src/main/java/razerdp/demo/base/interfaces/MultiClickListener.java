package razerdp.demo.base.interfaces;

import android.view.View;

import razerdp.demo.utils.WeakHandler;


/**
 * Created by 大灯泡 on 2019/4/9.
 * <p/>
 * 单双击事件监听
 */
public abstract class MultiClickListener implements View.OnClickListener {
    private static final int DelayedTime = 250;
    private boolean isDouble = false;
    private WeakHandler handler = new WeakHandler();

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            isDouble = false;
            handler.removeCallbacks(this);
            onSingleClick();
        }
    };

    @Override
    public final void onClick(View v) {
        if (isDouble) {
            isDouble = false;
            handler.removeCallbacks(runnable);
            onDoubleClick();
        } else {
            isDouble = true;
            handler.postDelayed(runnable, DelayedTime);
        }
    }

    public abstract void onSingleClick();

    public abstract void onDoubleClick();
}
