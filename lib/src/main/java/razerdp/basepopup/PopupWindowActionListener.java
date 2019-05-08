package razerdp.basepopup;

/**
 * Created by 大灯泡 on 2018/11/26.
 */
interface PopupWindowActionListener {

    void onShow(boolean hasAnimate);

    void onDismiss(boolean hasAnimate);

    boolean onUpdate();
}
