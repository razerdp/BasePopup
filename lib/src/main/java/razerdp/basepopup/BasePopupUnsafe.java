package razerdp.basepopup;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by 大灯泡 on 2020/9/9.
 * <p>
 * 这是一个BasePopup的后门，该后门允许你做出对BasePopup不安全的动作，请谨慎使用
 * <p>
 * 该类将会标记为过时，但那只是唬你的哈哈，只是想说尽可能不要用到它
 */
@Deprecated
public enum BasePopupUnsafe {
    INSTANCE;

    /**
     * 关闭所有的PopupWindow
     */
    @Deprecated
    public void dismissAllPopup(boolean animateDismiss) {
        HashMap<String, LinkedList<WindowManagerProxy>> allCache = WindowManagerProxy.PopupWindowQueueManager.sQueueMap;
        if (!allCache.isEmpty()) {
            for (LinkedList<WindowManagerProxy> value : allCache.values()) {
                for (WindowManagerProxy proxy : value) {
                    if (proxy.mPopupHelper != null && proxy.mPopupHelper.mPopupWindow != null) {
                        proxy.mPopupHelper.mPopupWindow.dismiss(animateDismiss);
                    }
                }
            }
        }
    }

}
