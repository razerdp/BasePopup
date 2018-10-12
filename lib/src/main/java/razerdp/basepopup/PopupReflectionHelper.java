package razerdp.basepopup;

import android.view.WindowManager;
import android.widget.PopupWindow;

import java.lang.reflect.Field;

import razerdp.util.UnsafeHelper;
import razerdp.util.log.PopupLogUtil;

/**
 * Created by 大灯泡 on 2018/10/12.
 * <p>
 * 针对android p 的反射绕过
 */
final class PopupReflectionHelper {

    public PopupReflectionHelper() {
        try {
            //获取伪造的Class类的classLoader地址
            InnerHackClazz hackClazz = new InnerHackClazz();
            Field clazzField = hackClazz.getClass().getDeclaredField("classLoader");
            long offset = UnsafeHelper.objectFieldOffset(clazzField);
            //将上面得到的地址传入到当前对象中，并设置classLoader为null
            UnsafeHelper.putObject(this.getClass(), offset, null);
            PopupLogUtil.trace("绕开android p success");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public WindowManager getPopupWindowManager(PopupWindow popupWindow) throws Exception {
        if (popupWindow == null) return null;
        Field fieldWindowManager = PopupWindow.class.getDeclaredField("mWindowManager");
        fieldWindowManager.setAccessible(true);
        final WindowManager windowManager = (WindowManager) fieldWindowManager.get(popupWindow);
        return windowManager;
    }

    public void setPopupWindowManager(PopupWindow popupWindow, WindowManager windowManager) throws Exception {
        if (popupWindow == null || windowManager == null) return;
        Field fieldWindowManager = PopupWindow.class.getDeclaredField("mWindowManager");
        fieldWindowManager.setAccessible(true);
        fieldWindowManager.set(popupWindow, windowManager);
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
