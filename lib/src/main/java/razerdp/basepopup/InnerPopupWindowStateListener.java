package razerdp.basepopup;

/**
 * Created by 大灯泡 on 2017/12/28.
 */
abstract class InnerPopupWindowStateListener {
    abstract void onAnimateDismissStart();

    abstract void onNoAnimateDismiss();

    public void onTryToShow(boolean hasAnimate) {
    }

    public void onAnimateShowStar(){

    }

}
