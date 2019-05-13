package razerdp.basepopup;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import java.util.List;

/**
 * Created by 大灯泡 on 2019/5/13
 * <p>
 * Description：
 */
public class BasePopupSupporterSupport implements BasePopupSupporter {
    @Override
    public View findDecorView(BasePopupWindow basePopupWindow, Activity activity) {
        View result = null;
        if (activity instanceof FragmentActivity) {
            try {
                FragmentActivity supportAct = (FragmentActivity) activity;
                List<Fragment> fragments = supportAct.getSupportFragmentManager().getFragments();
                for (Fragment fragment : fragments) {
                    if (fragment instanceof DialogFragment) {
                        DialogFragment d = ((DialogFragment) fragment);
                        if (d.getDialog() != null && d.getDialog().isShowing() && !d.isRemoving()) {
                            result = d.getView();
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public BasePopupWindow attachLifeCycle(BasePopupWindow basePopupWindow, Object owner) {
        return basePopupWindow;
    }

    @Override
    public BasePopupWindow removeLifeCycle(BasePopupWindow basePopupWindow, Object owner) {
        return basePopupWindow;
    }
}
