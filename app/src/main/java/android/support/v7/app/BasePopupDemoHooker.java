package android.support.v7.app;

import android.support.v7.widget.BasePopupDemoWidgetHooker;
import android.support.v7.widget.DecorToolbar;
import android.view.View;

/**
 * Created by 大灯泡 on 2019/3/7.
 * z和中
 */
public class BasePopupDemoHooker {

    public static View getToolBarActionMenuView(ActionBar actionBar) {
        if (!(actionBar instanceof WindowDecorActionBar)) return null;
        WindowDecorActionBar windowDecorActionBar = ((WindowDecorActionBar) actionBar);
        DecorToolbar mDecorToolbar = windowDecorActionBar.mDecorToolbar;
        return BasePopupDemoWidgetHooker.getActionMenu(mDecorToolbar);
    }
}
