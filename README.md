# BasePopup
抽象出一个方便自定义的Basepopup类，更加方便的创建出一个popup以及动画效果

---

[![](https://jitpack.io/v/razerdp/BasePopup.svg)](https://jitpack.io/#razerdp/BasePopup)
[![Download](https://api.bintray.com/packages/razerdp/maven/BasePopup/images/download.svg) ](https://bintray.com/razerdp/maven/BasePopup/_latestVersion)
[![license](https://img.shields.io/badge/license-Apache--2.0-blue.svg )](https://github.com/razerdp/BasePopup/blob/master/LICENSE)
[![Author](https://img.shields.io/badge/Author-razerdp-blue.svg)](https://github.com/razerdp) 

## 请注意：

**在1.8.6.1版本之前，请使用JitPack，从1.8.6.1开始，将使用Jcenter**

---

## 更新日志：

#### 1.8.7
  - 抽取`PopupWindowProxy`->`BasePopupWindowProxy`
  - 归类各种蛋疼的`showAsDropDown`适配->`PopupCompatManager`
  - 修正部分命名和方法名以及注释名错误的问题
       + 感谢简书小伙伴的评论，否则我还真发现不了。。。
       + 评论地址：[点我](http://www.jianshu.com/p/069f57e14a9c#comment-17669137)
       + 根据简书id，只能猜测他的github id：[Chenley](https://github.com/Chenley)，如果您见到并发现我这个猜测是错的，请及时联系我-V- 
       + 非常感谢你们的issue
  - 修复部分issue：[#46](https://github.com/razerdp/BasePopup/issues/46)
  
##### 1.8.6.1 
  - 使用Jcenter代替JitPack

##### 1.8.5.1
  - 现在可以在onCreate里面showPopup啦~
  - set方法返回`BasePopupWindow`，可以来个“伪链式”调用哈哈
  - 针对诸位提出的setBackPress在6.0以上失效的问题，请查看这份[MD](https://github.com/razerdp/BasePopup/blob/master/%E5%85%B3%E4%BA%8EAndorid%20M%E4%BB%A5%E4%B8%8AsetBackPressEnable()%E5%A4%B1%E6%95%88%E7%9A%84%E9%97%AE%E9%A2%98%E7%9A%84%E5%88%86%E6%9E%90.md)文件（没错，暂时无法修复）
  - 删除`setRelativeToAnchorView()`方法，该方法本身就没有什么用处。。。

##### 1.8.4:
  - 补充PopupWindowProxy的scanForActivity方法（不知明原因在merged后丢失了）


### 最低SDK版本要求 : API 11

# 依赖 

### Before 1.8.6.1:
 
[![](https://jitpack.io/v/razerdp/BasePopup.svg)](https://jitpack.io/#razerdp/BasePopup)

#### 请注意【1.8.1之前记得带"v"，如"v1.8.0"，1.8.1之后不需要】

**Step 1.**

**添加Jitpack到您的root gradle，如果无法导包，一般情况下都是这个原因，请仔细检查**

```xml
	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
```

**Step 2.**

添加依赖（请把{latestVersion}替换成上面的jitpack标签所示版本）

```xml
	dependencies {
	        compile 'com.github.razerdp:BasePopup:{latestVersion}'
	}
```

### After 1.8.6.1:
[ ![Download](https://api.bintray.com/packages/razerdp/maven/BasePopup/images/download.svg) ](https://bintray.com/razerdp/maven/BasePopup/_latestVersion)

添加依赖（请把{latestVersion}替换成上面的jitpack标签所示版本）
```xml
	dependencies {
	        compile 'com.github.razerdp:BasePopup:{latestVersion}'
	}
```

# 使用方法

----------

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

### 打赏（您的支持是我维护的动力-V-愿意的话，给个零食呗）
![wechat](https://github.com/razerdp/BasePopup/blob/master/img/wechat.jpg)


# 一些例子
![image](https://github.com/razerdp/BasePopup/blob/master/img/comment_popup_with_exitAnima.gif)
![image](https://github.com/razerdp/BasePopup/blob/master/img/scale_popup.gif)
![image](https://github.com/razerdp/BasePopup/blob/master/img/slide_from_bottom_popup.gif)
![image](https://github.com/razerdp/BasePopup/blob/master/img/input_popup.gif)
![image](https://github.com/razerdp/BasePopup/blob/master/img/list_popup.gif)
![image](https://github.com/razerdp/BasePopup/blob/master/img/menu_popup.gif)

例子更新日志:

https://github.com/razerdp/BasePopup/blob/master/UpdateLog.md

# 方法介绍：
请看wiki（陆续完善中）

**Link👉**[WIKI](https://github.com/razerdp/BasePopup/wiki)

# 代码解析：
http://www.jianshu.com/p/069f57e14a9c


### License
Apache-2.0
