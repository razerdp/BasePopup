# BasePopup
## 抽象出一个方便自定义的Basepopup类，更加方便的创建出一个popup以及动画效果


| **Release**            |  **Candy**  | **License**            |  **Author**  |
| ---- | ---- | ---- | ---- |
| [![Download](https://api.bintray.com/packages/razerdp/maven/BasePopup/images/download.svg) ](https://bintray.com/razerdp/maven/BasePopup/_latestVersion) | [![Download](https://api.bintray.com/packages/razerdp/maven/BasePopup_Candy/images/download.svg) ](https://bintray.com/razerdp/maven/BasePopup_Candy/_latestVersion)| [![license](https://img.shields.io/badge/license-Apache--2.0-blue.svg )](https://github.com/razerdp/BasePopup/blob/master/LICENSE) | [![Author](https://img.shields.io/badge/Author-razerdp-blue.svg)](https://github.com/razerdp) |


## 请注意：

**从1.8.6.1开始，将不再支持Jitpack**

**请注意引用版本的问题，Release版本是稳定版，可商用。Candy不稳定，但包含着新功能或者新的优化，不建议商用。**

### [更新日志](https://github.com/razerdp/BasePopup#%E6%9B%B4%E6%96%B0%E6%97%A5%E5%BF%97)

### [例子预览](https://github.com/razerdp/BasePopup#%E4%B8%80%E4%BA%9B%E4%BE%8B%E5%AD%90)

### 最低SDK版本要求 : API 14

# 依赖  

| **Release**            |  **Candy**  |
| --------      | ---- |
| [![Download](https://api.bintray.com/packages/razerdp/maven/BasePopup/images/download.svg) ](https://bintray.com/razerdp/maven/BasePopup/_latestVersion) | [![Download](https://api.bintray.com/packages/razerdp/maven/BasePopup_Candy/images/download.svg) ](https://bintray.com/razerdp/maven/BasePopup_Candy/_latestVersion)|


添加依赖（请把{latestVersion}替换成上面的Jcenter标签所示版本【candy版本不一定稳定，包含有新功能或者新的修复，完善后将会发布其release版】）
```xml
	dependencies {
	        compile 'com.github.razerdp:BasePopup:{latestVersion}'
	        
	        //candy版本，不稳定，但会带有新功能
	        //compile 'com.github.razerdp:BasePopup_Candy:{latestVersion}'
	}
```

ps:如果你懒。。。也可以这么添加（不是替换{latest.release}，直接copy就好）
```xml
	dependencies {
            compile 'com.github.razerdp:BasePopup:latest.release'
            
            //candy版本，不稳定，但会带有新功能
            //compile 'com.github.razerdp:BasePopup_Candy:latest.release'
	}
```

# 使用方法

ps:从1.9.0-alpha开始支持背景模糊（只需要一个方法：`setBlurBackgroundEnable`），最低支持api 17，您需要在gradle配置

```xml
defaultConfig {
        renderscriptTargetApi 25
        renderscriptSupportModeEnabled true
    }
```

**Step 1:**

像您平时定制activity布局文件一样定制您的popup布局（请注意，展示动画的那个view必须是popupview的子view）

etc.
```xml
<?xml version="1.0" encoding="utf-8"?>

<!--根布局，常用作蒙层（就是变暗的背景）-->
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#8f000000">
    
    <!--播放动画的内容，可以认为是popup的主要内容布局-->
    <RelativeLayout
        android:id="@+id/popup_anima"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:background="@drawable/bg_dialog"
        android:layout_centerInParent="true"
        android:layout_margin="25dp">
        
        
        <... many views>
        
        

    </RelativeLayout>
</RelativeLayout>
```
![image](https://github.com/razerdp/BasePopup/blob/master/img/etc.png)


**Step 2:**

新建一个类继承Basepopup

**Step 3:**

实现必要的几个方法：

`initShowAnimation()`:初始化一个进入动画，该动画将会用到`initAnimaView()`返回的view

`onCreatePopupView()`:初始化您的popupwindow界面，建议直接使用`createPopupById()`

`getClickToDismissView()`:如果有需要的话，可以使用这个方法返回一个点击dismiss popupwindow的view(也许是遮罩层也许是某个view，这个随您喜欢)

例如

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
    protected Animation initShowAnimation() {
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

**Step 4:**

把您刚才实现的popup给new出来并调用show方法

例如

```java
    DialogPopup popup = new DialogPopup(context);
    popup.showPopupWindow();
```

# 方法介绍：
请看wiki（陆续完善中）

**Link👉**[WIKI](https://github.com/razerdp/BasePopup/wiki)

### 打赏（您的支持是我维护的动力-V-愿意的话，给个零食呗）
![wechat](https://github.com/razerdp/BasePopup/blob/master/img/wechat.jpg)


## 更新日志：

#### 1.9.0-alpha2（candy）
  - 修复了可能导致的动画duration为-1而崩溃的问题

#### 1.9.0-alpha（candy）
  - 增加了一个window用于模糊层，增加模糊功能（请注意，并不一定稳定，仍然迭代中。。慎用！！！）
    + 如果您需要模糊功能，仅仅需要调用一个方法：`setBlurBackgroundEnable()`
    + gradle请在`defaultConfig`下添加两句：
      - renderscriptTargetApi 25
      - enderscriptSupportModeEnabled true

#### 1.8.9-beta2
  - 增加演示demo：`DismissControlPopupFrag`
  - 增加两个方法用于touchEvent监听：`onTouchEvent()`&`onOutSideTouch()`
  - `HackPopupDecorView`继承`ViewGroup`而非`FrameLayout`，以解决PopupWindow的`decorView.getLayoutParams()`无法强转为`WindowManager.LayoutParams`的异常
  - 其余问题暂时没发现

#### ~~1.8.8~~(1.8.9)

**【重大修复，不一定稳定,但因为解决了某个大问题，因此强烈建议升级到该版本，希望您可以尽量提交问题】**
  - `BasePopupWindowProxy`和`PopupWindowProxy`权限收拢，不暴露放开
  - 优化`SimpleAnimUtil`，修改部分动画时间和插值器
  - 增加`setOutsideTouchable()`方法，和`setDismissWhenTouchOutside()`搭配使用有奇效哦
  - 增加`BasePopupHelper`优化`BasePopupWindow`代码可读性
  - 动画方面修正`AnimaView.clearAnimation()`->`Animation.cancel()`
  - 优化`showOnTop()`/`showOnDown()`方法。。。虽然可能没什么人用
  - 1.8.8版本因为一些问题而去除[#50](https://github.com/razerdp/BasePopup/issues/50)，替换为1.8.9
  - 【已解决】`setBackPressEnable()`在M以上已经可以自行决定是否允许返回键dismiss了，同时开放了keyEvent
    + 解决方案：[1.8.9 解决方案](https://github.com/razerdp/BasePopup/blob/master/%E5%85%B3%E4%BA%8EAndorid%20M%E4%BB%A5%E4%B8%8AsetBackPressEnable()%E5%A4%B1%E6%95%88%E7%9A%84%E9%97%AE%E9%A2%98%E7%9A%84%E5%88%86%E6%9E%90.md#189-%E8%A7%A3%E5%86%B3%E6%96%B9%E6%A1%88)
    + issue:[#33](https://github.com/razerdp/BasePopup/issues/33)
    + `BasePopupWindow`增加两个方法用于keyEvent的监听：`onDispatchKeyEvent()`&`onBackPressed()`
    + 感谢诸位热烈的讨论~
  - 部分方法名更改
    + `setOutsideTouchable()`->`setInterceptTouchEvent()`，该方法会影响焦点问题，即便是解决了`backPress`若这个方法设置为false，依然不会响应backpress
    
  
#### 1.8.7
  - 抽取`PopupWindowProxy`->`BasePopupWindowProxy`
  - 归类各种蛋疼的`showAsDropDown`适配->`PopupCompatManager`
  - 修正部分命名和方法名以及注释名错误的问题
       + 感谢简书小伙伴的评论，否则我还真发现不了。。。
       + 评论地址：[点我](http://www.jianshu.com/p/069f57e14a9c#comment-17669137)
       + 根据简书id，只能猜测他的github id：[Chenley](https://github.com/Chenley)，如果您见到并发现我这个猜测是错的，请及时联系我-V- 
       + 非常感谢你们的issue
  - 修复部分issue：[#46](https://github.com/razerdp/BasePopup/issues/46)
  


# 一些例子

| 对应popup            |  预览  |
| :--------      | ---- |
| [BlurSlideFromBottomPopup.java](https://github.com/razerdp/BasePopup/blob/master/app/src/main/java/razerdp/demo/popup/BlurSlideFromBottomPopup.java)     | ![image](https://github.com/razerdp/BasePopup/blob/master/img/blur_popup.gif) |
| [CommentPopup.java](https://github.com/razerdp/BasePopup/blob/master/app/src/main/java/razerdp/demo/popup/CommentPopup.java)     | ![image](https://github.com/razerdp/BasePopup/blob/master/img/comment_popup_with_exitAnima.gif) |
| [ScalePopup.java](https://github.com/razerdp/BasePopup/blob/master/app/src/main/java/razerdp/demo/popup/ScalePopup.java)     | ![image](https://github.com/razerdp/BasePopup/blob/master/img/scale_popup.gif) |
| [SlideFromBottomPopup.java](https://github.com/razerdp/BasePopup/blob/master/app/src/main/java/razerdp/demo/popup/SlideFromBottomPopup.java)     | ![image](https://github.com/razerdp/BasePopup/blob/master/img/slide_from_bottom_popup.gif) |
| [InputPopup.java](https://github.com/razerdp/BasePopup/blob/master/app/src/main/java/razerdp/demo/popup/InputPopup.java)     | ![image](https://github.com/razerdp/BasePopup/blob/master/img/input_popup.gif) |
| [ListPopup.java](https://github.com/razerdp/BasePopup/blob/master/app/src/main/java/razerdp/demo/popup/ListPopup.java)     | ![image](https://github.com/razerdp/BasePopup/blob/master/img/list_popup.gif) |
| [MenuPopup.java](https://github.com/razerdp/BasePopup/blob/master/app/src/main/java/razerdp/demo/popup/MenuPopup.java)     | ![image](https://github.com/razerdp/BasePopup/blob/master/img/menu_popup.gif) |

例子更新日志:

https://github.com/razerdp/BasePopup/blob/master/UpdateLog.md


# 代码解析：
http://www.jianshu.com/p/069f57e14a9c


### License
Apache-2.0
