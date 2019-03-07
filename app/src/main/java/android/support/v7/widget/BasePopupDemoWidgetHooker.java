package android.support.v7.widget;

import android.text.TextUtils;
import android.view.View;

/**
 * Created by 大灯泡 on 2019/3/7.
 */
public class BasePopupDemoWidgetHooker {

    public static View getActionMenu(DecorToolbar mToolBar) {
        if (mToolBar instanceof ToolbarWidgetWrapper) {
            ToolbarWidgetWrapper toolBarWrapper = ((ToolbarWidgetWrapper) mToolBar);
            Toolbar toolbar = toolBarWrapper.mToolbar;
            for (int i = 0; i < toolbar.getChildCount(); i++) {
                View v = toolbar.getChildAt(i);
                if (TextUtils.equals(v.getClass().getSimpleName(), "ActionMenuView")) {
                    return v;
                }
            }
        }
        return null;
    }
}
