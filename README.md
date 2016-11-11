# BasePopup
An abstract class for creating custom popupwindow easily.

[中文介绍](https://github.com/razerdp/BasePopup/blob/master/README-CN.md)

##请注意：

因为在使用过程中发现了一些方法的命名存在误导性，这个问题是因为在初期写下了方法名之后一直忘了改，因此在 **v 1.3.0**之后这些名字将会改变，具体改动如下：

| 原方法名 | 现方法名 | 备注 |
| -------- | :-----------: | :---------: |
| getPopupView() | onCreatePopupView() | 本方法改名为的是减少误导性 |
| getAnimaView() | initAnimaView() | 理由同上 |
| mContext | getContext() | context将会改为private，需要使用方法获取 |
| mPopupView | getPopupWindowView() | mPopupView将会改为private，需要用方法获取 |
| getShowAnimation()/getExitAnimation() |**【protect】** initShowAnimation()/initExitAnimation() | getShowAnimation()/getExitAnimation()将会改为获取进行过初始化的animation |
| getShowAnimator()/getExitAnimator() |**【protect】** initShowAnimator()/initExitAnimator() | getShowAnimator()/getExitAnimator()将会改为获取进行过初始化的animator |


事实上在下也清楚有很多方法命名还是不太好，如果您有更好的命名欢迎提交pr，同时这次的改动对您的使用造成万分不便，在下深表歉意，希望得到您的谅解。


---

##ATTENTION:

Because of some methods' name are misleading,we will change them after **ver 1.3.0**

commit details:

| Old name | New name | Note |
| -------- | :-----------: | :---------: |
| getPopupView() | onCreatePopupView() | getPopupView() will be **Deprecated** |
| getAnimaView() | initAnimaView() | getAnimaView will be **Deprecated**  |
| mContext | getContext() | the **mContext** field will be changed to **private**,use `getContext()` instead  |
| mPopupView | getPopupWindowView() | the **mPopupView ** field will be changed to **private**,use `getContext()` instead |
| getShowAnimation()/getExitAnimation() |**【protect】** initShowAnimation()/initExitAnimation() | getShowAnimation()/getExitAnimation() will return animation whitch has been initialized |
| getShowAnimator()/getExitAnimator() |**【protect】** initShowAnimator()/initExitAnimator() | getShowAnimator()/getExitAnimator() will return animator whitch has been initialized |

Please forgive us for the inconvenience brought to you.

### MinSDK : API 11

# Download  [![](https://jitpack.io/v/razerdp/BasePopup.svg)](https://jitpack.io/#razerdp/BasePopup)
**Step 1.** Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:
```xml
	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
```	

**Step 2.** Add the dependency
```xml
	dependencies {
	        compile 'com.github.razerdp:BasePopup:v1.3.0'
	}
```

# HowToUse

----------
**Step 1:**
create xml file for the popup

etc.
```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#8f000000">

    <RelativeLayout
        android:id="@+id/popup_anima"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:background="@drawable/bg_dialog"
        android:layout_centerInParent="true"
        android:layout_margin="25dp">
        <TextView
            android:id="@+id/title"
            android:layout_width="match_parent"
            android:layout_height="50dp"
            android:textColor="#3dd1a5"
            android:text="This is title"
            android:gravity="left|center_vertical"
            android:textSize="18sp"
            android:paddingLeft="15dp"
            />
        <View
            android:id="@+id/line"
            android:layout_width="match_parent"
            android:layout_height="0.5dp"
            android:background="#3dd1a5"
            android:layout_below="@id/title"
            android:layout_marginLeft="5dp"
            android:layout_marginRight="5dp"/>
        <TextView
            android:id="@+id/content"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_below="@id/line"
            android:paddingTop="20dp"
            android:paddingLeft="15dp"
            android:paddingRight="15dp"
            android:paddingBottom="15dp"
            android:textSize="14sp"
            android:textColor="#1a1a1a"
            android:lineSpacingMultiplier="1.2"
            android:text="Warning:nuclear silo detected.\nWarning:nuclear silo detected.\nWarning:nuclear silo detected."
            />
        <View
            android:id="@+id/line2"
            android:layout_width="match_parent"
            android:layout_height="0.5dp"
            android:layout_below="@id/content"
            android:layout_marginLeft="15dp"
            android:layout_marginRight="15dp"
            android:background="@color/line_bg"
            android:layout_marginTop="15dp"/>
        <TextView
            android:id="@+id/cancel"
            android:layout_width="60dp"
            android:layout_height="40dp"
            android:gravity="center"
            android:padding="3dp"
            android:textColor="#bfbfbf"
            android:textSize="14sp"
            android:layout_below="@id/line2"
            android:layout_alignParentRight="true"
            android:layout_marginRight="15dp"
            android:text="CANCEL"/>
        <TextView
            android:id="@+id/ok"
            android:layout_width="60dp"
            android:layout_height="40dp"
            android:gravity="center"
            android:padding="3dp"
            android:textColor="#3dd1a5"
            android:textSize="14sp"
            android:layout_below="@id/line2"
            android:layout_toLeftOf="@id/cancel"
            android:layout_marginRight="15dp"
            android:text="OK"/>

    </RelativeLayout>
</RelativeLayout>
```
![image](https://github.com/razerdp/BasePopup/blob/master/img/etc.png)


**Step 2:**
Create a class whitch extend BasePopupWindow

**Step 3:**
override some methods

etc.

```java
public class DialogPopup extends BasePopupWindow implements View.OnClickListener{

    private TextView ok;
    private TextView cancel;

    public DialogPopup(Activity context) {
        super(context);

        ok= (TextView) findViewById(R.id.ok);
        cancel= (TextView) findViewById(R.id.cancel);

        setViewClickListener(this,ok,cancel);
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
        return getPopupWindowView();
    }

    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.popup_dialog);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.popup_anima);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ok:
                Toast.makeText(getContext(),"click the ok button",Toast.LENGTH_SHORT).show();
                break;
            case R.id.cancel:
                Toast.makeText(getContext(),"click the cancel button",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

    }
}
```

**Step 4:**create the object and show

etc.

```java
    DialogPopup popup = new DialogPopup(context);
    popup.showPopupWindow();
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
