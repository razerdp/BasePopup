1.x版本 | [2.x版本](./README.md)

---
**抽象出一个方便自定义的Basepopup类，更加方便的创建出一个popup以及动画效果**


| **Release** | **Candy** | **License** | **Api** | **Author** |
| ---- | ---- | ---- | ---- | ---- |
|[ ![Download](https://api.bintray.com/packages/razerdp/maven/BasePopup/images/download.svg?version=1.9.4) ](https://bintray.com/razerdp/maven/BasePopup/1.9.4/link) | [ ![Download](https://api.bintray.com/packages/razerdp/maven/BasePopup_Candy/images/download.svg?version=2.0.8-alpha3) ](https://bintray.com/razerdp/maven/BasePopup_Candy/2.0.8-alpha3/link) | [![license](https://img.shields.io/badge/license-Apache--2.0-blue.svg )](https://github.com/razerdp/BasePopup/blob/master/LICENSE) | [![Api](https://img.shields.io/badge/Api-14%2B-green.svg)](https://img.shields.io/badge/Api-14%2B-green.svg) | [![Author](https://img.shields.io/badge/Author-razerdp-blue.svg)](https://github.com/razerdp) |


注意事项
---

### Android P未进行适配！！！Android P请慎用

---

**请务必查看更新日志和例子预览，里面会详细解释每个版本增加或修复的功能**

**请注意引用版本的问题，Release版本是稳定版，可商用。**

**Candy不稳定（且更新很频繁），但包含着新功能或者新的优化，不建议商用。**

### Candy版本反馈请点->[**Candy反馈**](https://github.com/razerdp/BasePopup/issues/66)

[**更新日志**](https://github.com/razerdp/BasePopup#%E6%9B%B4%E6%96%B0%E6%97%A5%E5%BF%97%E5%8E%86%E5%8F%B2%E6%9B%B4%E6%96%B0)

[**例子预览**](https://github.com/razerdp/BasePopup#%E4%B8%80%E4%BA%9B%E4%BE%8B%E5%AD%90)

其他
---

[关于V2版本更新（现已在Candy发布2.0.0alpha-1）](https://github.com/razerdp/BasePopup/blob/master/About_v2.md)


依赖
---

| **Release** | **Candy** |
| ---- | ---- |
|[ ![Download](https://api.bintray.com/packages/razerdp/maven/BasePopup/images/download.svg?version=1.9.4) ](https://bintray.com/razerdp/maven/BasePopup/1.9.4/link) | [ ![Download](https://api.bintray.com/packages/razerdp/maven/BasePopup_Candy/images/download.svg?version=2.0.8-alpha3) ](https://bintray.com/razerdp/maven/BasePopup_Candy/2.0.8-alpha3/link) |


添加依赖（请把{latestVersion}替换成上面的Jcenter标签所示版本

**【candy版本不一定稳定，包含有新功能或者新的修复，完善后将会发布其release版】**

```xml
	dependencies {
	        compile 'com.github.razerdp:BasePopup:{latestVersion}'
	        
	        //candy版本，不稳定，但会带有新功能
	        //compile 'com.github.razerdp:BasePopup_Candy:{latestVersion}'
	}
```


使用方法
---

ps:从1.9.0-alpha开始支持背景模糊（只需要一个方法：`setBlurBackgroundEnable()`）

**RenderScript最低支持api 17（更低的情况将会使用fastblur），您需要在gradle配置一下代码**

```xml
defaultConfig {
        renderscriptTargetApi 25
        renderscriptSupportModeEnabled true
    }
```

* **Step 1:**

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


* **Step 2:**

新建一个类继承BasePopupWindow

* **Step 3:**

实现必要的几个方法：

`initShowAnimation()`:初始化一个进入动画，该动画将会用到`initAnimaView()`返回的view

`onCreatePopupView()`:初始化您的popupwindow界面，建议直接使用`createPopupById()`，不能返回空值

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

* **Step 4:**

把您刚才实现的popup给new出来并调用show方法

例如

```java
    DialogPopup popup = new DialogPopup(context);
    popup.showPopupWindow();
```

方法介绍：
---

请看wiki（陆续完善中）

**Link👉**[WIKI](https://github.com/razerdp/BasePopup/wiki)

交流群：590777418
---

因为目前还有朋友圈项目，建立了一个交流群，出于懒得管理那么多，所以如果有想法或者优化建议或者其他问题，欢迎加入“朋友圈交流群”

![](https://github.com/razerdp/FriendCircle/blob/master/qqgroup.png)

打赏（看在我那么努力维护的份上。。。给个零食呗~）
---

| 微信 |支付宝 | 
| ---- | ---- | 
| ![](https://github.com/razerdp/FriendCircle/blob/master/wechat.png)      | ![](https://github.com/razerdp/FriendCircle/blob/master/alipay.png) |




更新日志([历史更新](https://github.com/razerdp/BasePopup/blob/master/UpdateLog.md))
---------------------------------------------------------------------------
* **2.0.0alpha-1(candy)**
  * 发布v2预览版本
  * 本次candy用作2.0预览版发布，2.0对整体结构有破坏性改变，如果您在商业项目中用得比较多，请勿轻易使用candy版本。
  * 2.0.0版本相关请查看分支:[razerdp.basepopup v2](https://github.com/razerdp/BasePopup/tree/basepopup_v2)

* **1.9.4(release)**
  * 修复autolocate的问题
  * 发布1.9.4

* **1.9.4-alpha2(candy)**
  * 修复误打包测试代码的alpha1

* **1.9.4-alpha(candy)**
  * 本版本是预览版本，如果您有需要，可以更新到Candy版本，但不保证没有任何问题
  * 针对8.0进行修复
      * link: [issue#56](https://github.com/razerdp/BasePopup/issues/56)
      * link: [issue#61](https://github.com/razerdp/BasePopup/issues/61)
      * link: [issue#64](https://github.com/razerdp/BasePopup/issues/64)
  * 优化代码，HackWindowManager与HackPopupDecorView部分重构
  * showOnTop/showOnDown更名->onAnchorTop/onAnchorBottom，避免误导。

* **1.9.3(release)**
  * 修复了在popup外滑动时`ViewGroup.LayoutParams`的cast异常
      * link: [issue#52](https://github.com/razerdp/BasePopup/issues/52)

* **1.9.2(release)**
  * 修复`HackDecorView`针对PopupWindow高度问题
  * 增加`setBlurBackgroundEnable()`模糊设置回调，允许自定义模糊操作
  * 修改为默认子线程模糊背景，同时增加blurImageView的模糊等待操作

* **1.9.1(release)**
  * 修复可能出现的死循环问题以及去掉manifest文件冲突的问题
  * 部分方法名字修改，默认关闭 Log，如果您需要打印内部调试日志，请使用该方法：`BasePopupWindow.debugLog(true)`
  * 增加位移动画（百分比传值）,位移动画名字修正：`getTranslateAnimation()` -> `getTranslateVerticalAnimation()`
  * 模糊背景功能已经开放，针对单个View的模糊方法开放
  * 模糊背景允许子线程执行，默认主线程执行
  * gradle请在`defaultConfig`下添加两句：
      * **renderscriptTargetApi 25**
      * **enderscriptSupportModeEnabled true**
  * 发布1.9.1，其余bug修复


一些例子
---

| 对应popup | 预览 |
| :---- | ---- |
| [BlurSlideFromBottomPopup.java](https://github.com/razerdp/BasePopup/blob/master/app/src/main/java/razerdp/demo/popup/BlurSlideFromBottomPopup.java)     | ![image](https://github.com/razerdp/Pics/blob/master/BasePopup/old/blur_popup.gif) |
| [CommentPopup.java](https://github.com/razerdp/BasePopup/blob/master/app/src/main/java/razerdp/demo/popup/CommentPopup.java)     | ![image](https://github.com/razerdp/Pics/blob/master/BasePopup/old/comment_popup_with_exitAnima.gif) |
| [ScalePopup.java](https://github.com/razerdp/BasePopup/blob/master/app/src/main/java/razerdp/demo/popup/ScalePopup.java)     | ![image](https://github.com/razerdp/Pics/blob/master/BasePopup/old/scale_popup.gif) |
| [SlideFromBottomPopup.java](https://github.com/razerdp/BasePopup/blob/master/app/src/main/java/razerdp/demo/popup/SlideFromBottomPopup.java)     | ![image](https://github.com/razerdp/Pics/blob/master/BasePopup/old/slide_from_bottom_popup.gif) |
| [InputPopup.java](https://github.com/razerdp/BasePopup/blob/master/app/src/main/java/razerdp/demo/popup/InputPopup.java)     | ![image](https://github.com/razerdp/Pics/blob/master/BasePopup/old/input_popup.gif) |
| [ListPopup.java](https://github.com/razerdp/BasePopup/blob/master/app/src/main/java/razerdp/demo/popup/ListPopup.java)     | ![image](https://github.com/razerdp/Pics/blob/master/BasePopup/old/list_popup.gif) |
| [MenuPopup.java](https://github.com/razerdp/BasePopup/blob/master/app/src/main/java/razerdp/demo/popup/MenuPopup.java)     | ![image](https://github.com/razerdp/Pics/blob/master/BasePopup/old/menu_popup.gif) |


代码解析
---

(很旧的文章了，考虑迟点重新编写)

[http://www.jianshu.com/p/069f57e14a9c](http://www.jianshu.com/p/069f57e14a9c)


License
---

Apache-2.0
