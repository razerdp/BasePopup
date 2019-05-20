package razerdp.basepopup;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import java.lang.reflect.Field;

import razerdp.util.UnsafeHelper;
import razerdp.util.log.PopupLog;

/**
 * Created by 大灯泡 on 2018/10/12.
 * <p>
 * 针对android p 的反射绕过
 * <p>
 * 思路参考：https://github.com/Guolei1130/android_p_no_sdkapi_support#%E6%96%B9%E6%B3%95%E4%BA%94%E8%B6%85%E7%BA%A7%E5%A5%BD1
 * <p>
 * 感谢这位大佬的分享~
 */
final class PopupReflectionHelper {

    private static class PopupReflectionHelperHolder {
        private static PopupReflectionHelper instance = new PopupReflectionHelper();
    }

    private PopupReflectionHelper() {
        try {
            //获取伪造的Class类的classLoader地址
            InnerHackClazz hackClazz = new InnerHackClazz();
            Field clazzField = hackClazz.getClass().getDeclaredField("classLoader");
            long offset = UnsafeHelper.objectFieldOffset(clazzField);
            //将上面得到的地址传入到当前对象中，并设置classLoader为null
            UnsafeHelper.putObject(this.getClass(), offset, null);
            PopupLog.i("绕开android p success,inject offset >>> " + offset);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PopupReflectionHelper getInstance() {
        return PopupReflectionHelperHolder.instance;
    }

     WindowManager getPopupWindowManager(PopupWindow popupWindow) throws Exception {
        if (popupWindow == null) return null;
        Field fieldWindowManager = PopupWindow.class.getDeclaredField("mWindowManager");
        fieldWindowManager.setAccessible(true);
        final WindowManager windowManager = (WindowManager) fieldWindowManager.get(popupWindow);
        return windowManager;
    }

    void preInject(PopupWindow popupWindow, WindowManager windowManager) throws Exception {
        if (popupWindow == null || windowManager == null) return;
        Field fieldWindowManager = PopupWindow.class.getDeclaredField("mWindowManager");
        fieldWindowManager.setAccessible(true);
        fieldWindowManager.set(popupWindow, windowManager);

        Field fieldScroll = PopupWindow.class.getDeclaredField("mOnScrollChangedListener");
        fieldScroll.setAccessible(true);
        fieldScroll.set(popupWindow, null);
    }



    @SuppressLint("PrivateApi")
    public View.OnSystemUiVisibilityChangeListener getSystemUiVisibilityChangeListener(View targetView) throws Exception {
        Field mListenerInfo = View.class.getDeclaredField("mListenerInfo");
        mListenerInfo.setAccessible(true);
        Object viewListenerInfo = mListenerInfo.get(targetView);
        if (viewListenerInfo != null) {
            Field listenerField = viewListenerInfo.getClass().getDeclaredField("mOnSystemUiVisibilityChangeListener");
            listenerField.setAccessible(true);
            return (View.OnSystemUiVisibilityChangeListener) listenerField.get(viewListenerInfo);
        }
        return null;
    }

    //构造出一个类似Class的类，目的是为了可以反射classLoader的field，因为在系统的Class类里面是不允许反射该类的
    private static class InnerHackClazz {
        private static final int ANNOTATION = 0x00002000;
        private static final int ENUM = 0x00004000;
        private static final int SYNTHETIC = 0x00001000;
        private static final int FINALIZABLE = 0x80000000;

        private transient ClassLoader classLoader;

    }
}
