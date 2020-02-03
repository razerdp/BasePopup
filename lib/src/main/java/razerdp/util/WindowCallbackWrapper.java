package razerdp.util;

import android.os.Build;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

import java.util.List;

/**
 * Created by 大灯泡 on 2020/2/3.
 */
public class WindowCallbackWrapper implements Window.Callback {
    private Window.Callback mWrapped;

    public WindowCallbackWrapper(Window.Callback wrapped) {
        if (wrapped == null) {
            throw new IllegalArgumentException("Window callback may not be null");
        }
        mWrapped = wrapped;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return mWrapped.dispatchKeyEvent(event);
    }

    @Override
    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        return mWrapped.dispatchKeyShortcutEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return mWrapped.dispatchTouchEvent(event);
    }

    @Override
    public boolean dispatchTrackballEvent(MotionEvent event) {
        return mWrapped.dispatchTrackballEvent(event);
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        return mWrapped.dispatchGenericMotionEvent(event);
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        return mWrapped.dispatchPopulateAccessibilityEvent(event);
    }

    @Override
    public View onCreatePanelView(int featureId) {
        return mWrapped.onCreatePanelView(featureId);
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        return mWrapped.onCreatePanelMenu(featureId, menu);
    }

    @Override
    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        return mWrapped.onPreparePanel(featureId, view, menu);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        return mWrapped.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        return mWrapped.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onWindowAttributesChanged(WindowManager.LayoutParams attrs) {
        mWrapped.onWindowAttributesChanged(attrs);
    }

    @Override
    public void onContentChanged() {
        mWrapped.onContentChanged();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        mWrapped.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onAttachedToWindow() {
        mWrapped.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        mWrapped.onDetachedFromWindow();
    }

    @Override
    public void onPanelClosed(int featureId, Menu menu) {
        mWrapped.onPanelClosed(featureId, menu);
    }

    @Override
    public boolean onSearchRequested(SearchEvent searchEvent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return mWrapped.onSearchRequested(searchEvent);
        }
        return false;
    }

    @Override
    public boolean onSearchRequested() {
        return mWrapped.onSearchRequested();
    }

    @Override
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        return mWrapped.onWindowStartingActionMode(callback);
    }

    @Override
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int type) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return mWrapped.onWindowStartingActionMode(callback, type);
        }
        return null;
    }

    @Override
    public void onActionModeStarted(ActionMode mode) {
        mWrapped.onActionModeStarted(mode);
    }

    @Override
    public void onActionModeFinished(ActionMode mode) {
        mWrapped.onActionModeFinished(mode);
    }

    @Override
    public void onProvideKeyboardShortcuts(
            List<KeyboardShortcutGroup> data, Menu menu, int deviceId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mWrapped.onProvideKeyboardShortcuts(data, menu, deviceId);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mWrapped.onPointerCaptureChanged(hasCapture);
        }
    }

    public Window.Callback getWrapped() {
        return mWrapped;
    }
}
