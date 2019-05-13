package razerdp.basepopup;

import android.app.Activity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import razerdp.util.PopupUtils;
import razerdp.util.log.PopupLog;

/**
 * Created by 大灯泡 on 2019/5/13
 * <p>
 * Description：支持管理器
 */
final class BasePopupSupporterManager implements BasePopupSupporter {

    private static List<BasePopupSupporter> IMPL;
    private static boolean hasInit = false;

    private static final String IMPL_SUPPORT = "razerdp.basepopup.BasePopupSupporterSupport";
    private static final String IMPL_LIFECYCLE = "razerdp.basepopup.BasePopupSupporterLifeCycle";
    private static final String IMPL_X = "razerdp.basepopup.BasePopupSupporterX";

    @Override
    public View findDecorView(BasePopupWindow basePopupWindow, Activity activity) {
        if (PopupUtils.isListEmpty(IMPL)) return null;
        for (BasePopupSupporter basePopupSupporter : IMPL) {
            View result = basePopupSupporter.findDecorView(basePopupWindow, activity);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    @Override
    public BasePopupWindow attachLifeCycle(BasePopupWindow basePopupWindow, Object owner) {
        if (PopupUtils.isListEmpty(IMPL)) return null;
        for (BasePopupSupporter basePopupSupporter : IMPL) {
            if (basePopupWindow.lifeCycleObserver != null) return basePopupWindow;
            basePopupSupporter.attachLifeCycle(basePopupWindow, owner);
        }
        return basePopupWindow;
    }

    @Override
    public BasePopupWindow removeLifeCycle(BasePopupWindow basePopupWindow, Object owner) {
        if (PopupUtils.isListEmpty(IMPL)) return null;
        for (BasePopupSupporter basePopupSupporter : IMPL) {
            if (basePopupWindow.lifeCycleObserver == null) return basePopupWindow;
            basePopupSupporter.removeLifeCycle(basePopupWindow, owner);
        }
        return basePopupWindow;
    }

    private static class SingleTonHolder {
        private static BasePopupSupporterManager INSTANCE = new BasePopupSupporterManager();
    }


    private BasePopupSupporterManager() {

    }

    void init() {
        if (hasInit) return;
        IMPL = new ArrayList<>();
        try {
            if (isClassExist(IMPL_SUPPORT)) {
                IMPL.add((BasePopupSupporter) Class.forName(IMPL_SUPPORT).newInstance());
            }
            if (isClassExist(IMPL_LIFECYCLE)) {
                IMPL.add((BasePopupSupporter) Class.forName(IMPL_LIFECYCLE).newInstance());
            }
            if (isClassExist(IMPL_X)) {
                IMPL.add((BasePopupSupporter) Class.forName(IMPL_X).newInstance());
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        hasInit = true;
        PopupLog.i(IMPL);
    }

    public static BasePopupSupporterManager getInstance() {
        return SingleTonHolder.INSTANCE;
    }

    boolean isClassExist(String name) {
        try {
            Class.forName(name);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
}
