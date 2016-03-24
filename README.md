# BasePopup
A abstract class for creating custom popupwindow easily.

[中文介绍](https://github.com/razerdp/BasePopup/blob/master/README-CN.md)


# Download 
Click [here](https://github.com/razerdp/BasePopup/tree/master/lib/src/main/java/razerdp/basepopup) and copy three java to your project.

# HowToUse

----------

**Step 1:**
Create a class whitch extend BasePopupWindow

**Step 2:**
override some methods

etc.

```java
public class DialogPopup extends BasePopupWindow {

    public DialogPopup(Activity context) {
        super(context);
    }

    @Override
    protected Animation getShowAnimation() {
        AnimationSet set=new AnimationSet(false);
        Animation shakeAnima=new RotateAnimation(0,15,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        shakeAnima.setInterpolator(new CycleInterpolator(5));
        shakeAnima.setDuration(400);
        set.addAnimation(getDefaultAlphaAnimation());
        set.addAnimation(shakeAnima);
        return set;
    }

    @Override
    protected View getClickToDismissView() {
        return mPopupView;
    }

    @Override
    public View getPopupView() {
        return getPopupViewById(R.layout.popup_dialog);
    }

    @Override
    public View getAnimaView() {
        return mPopupView.findViewById(R.id.popup_anima);
    }
}
```

**Step 3:**create the object and show

etc.

```java
 new DialogPopup(context).showPopupWindow();
```

# Some Example
![image](https://github.com/razerdp/BasePopup/blob/master/img/comment_popup_with_exitAnima.gif)
![image](https://github.com/razerdp/BasePopup/blob/master/img/scale_popup.gif)
![image](https://github.com/razerdp/BasePopup/blob/master/img/slide_from_bottom_popup.gif)
![image](https://github.com/razerdp/BasePopup/blob/master/img/input_popup.gif)
![image](https://github.com/razerdp/BasePopup/blob/master/img/list_popup.gif)
![image](https://github.com/razerdp/BasePopup/blob/master/img/menu_popup.gif)

click the link to show more:

https://github.com/razerdp/BasePopup/blob/master/UpdateLog.md

#License
MIT
