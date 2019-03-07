[**1.x版本**](./README_OLD.md) | **2.x版本** | [**English**](./README_V2_EN.md)

<p align="center"><img src="./img/logo.png" alt="Logo图片似乎加载不出来" height="360"/></p>
<h2 align="center">BasePopup - Android下打造通用便捷的PopupWindow</h2>
<div align="center">
<table>
    <thead>
        <tr>
            <th>Release</th>
            <th>Candy</th>
            <th>License</th>
			<th>Api</th>
			<th>Author</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>
				<a href ="https://bintray.com/razerdp/maven/BasePopup/_latestVersion">
					<img src="https://api.bintray.com/packages/razerdp/maven/BasePopup/images/download.svg"/>
				</a>
			</td>
			<td>
				<a href = "https://bintray.com/razerdp/maven/BasePopup_Candy/_latestVersion">
					<img src="https://api.bintray.com/packages/razerdp/maven/BasePopup_Candy/images/download.svg"/>
				</a>
			</td>
			<td>
				<a href = "https://github.com/razerdp/BasePopup/blob/master/LICENSE">
					<img src="https://img.shields.io/badge/license-Apache--2.0-blue.svg"/>		
				</a>
			</td>
			<td>
				<a href="https://img.shields.io/badge/Api-14%2B-green.svg">
					<img src="https://img.shields.io/badge/Api-16%2B-green.svg"/>
				</a>
			</td>
			<td>
				<a href = "https://github.com/razerdp">
					<img src="https://img.shields.io/badge/Author-razerdp-blue.svg"/>
				</a>
			</td>
		</tr>
    </tbody>
</table>
</div>

---

[**apk体验下载**](https://fir.im/pfc9)

### 导航
 
 - [特性](#特性)
 - [文章分享](https://github.com/razerdp/Article/blob/master/%E4%BA%B2%EF%BC%8C%E8%BF%98%E5%9C%A8%E4%B8%BAPopupWindow%E7%83%A6%E6%81%BC%E5%90%97.md)
 - [注意事项](#注意事项)
 - [快速入门](#快速入门)
   - [配置](#配置)
     - [模糊配置](#模糊配置)
   - [依赖](#依赖)
   - [普通使用](#普通使用)
     - [1.编写您的xml文件](#1编写您的xml文件)
     - [2.创建您的Popup类并继承BasePopupWindow](#2创建您的Popup类并继承BasePopupWindow)
     - [3.补充对应方法](#3补充对应方法)
     - [4.show！](#4show)
   - [QuickPopupBuilder链式调用](#QuickPopupBuilder链式调用)
     - [示例代码](#示例代码) 
 - [Api（请看Wiki）](#api请看wiki)
 - [更新日志](#更新日志-历史更新)
   - [历史更新](./UpdateLog.md)
 - [例子预览](#例子预览)
 - [打赏](#打赏看在我那么努力维护的份上给个零食呗)
 - [交流群](#交流群590777418)
 - [常见问题](#常见问题)
 - [LICENSE](#license)
   
<br>
<br>

### 特性

 - 更简单更精准的控制显示位置，通过[**Gravity**](https://github.com/razerdp/BasePopup/wiki/API#setpopupgravityint-popupgravity)和[**offset**](https://github.com/razerdp/BasePopup/wiki/API#setoffsetxint-offsetx)来控制您的PopupWindow
 - 本库为抽象类，对子类几乎没有约束，您完全可以像定制Activity一样来定制您的PopupWindow
 - 支持[**Animation**](https://github.com/razerdp/BasePopup/wiki/API#setshowanimationanimation-showanimation)、[**Animator**](https://github.com/razerdp/BasePopup/wiki/API#setshowanimatoranimator-showanimator)，随意控制您的PopupWindow的动画，再也不用去写蛋疼的xml了
 - [**背景变暗**](https://github.com/razerdp/BasePopup/wiki/API#setbackgroundcolorint-color)、背景换色甚至背景给个Drawable都是一句话的事情
 - [**背景模糊**](https://github.com/razerdp/BasePopup/wiki/API#setblurbackgroundenableboolean-blurbackgroundenable)亦或是[**局部模糊**](https://github.com/razerdp/BasePopup/wiki/API#setbluroptionpopupbluroption-option)也仅仅需要您一句话的配置
 - [**返回键控制**](https://github.com/razerdp/BasePopup/wiki/API#setbackpressenableboolean-backpressenable)、[**点击外部Dismiss控制**](https://github.com/razerdp/BasePopup/wiki/API#setallowdismisswhentouchoutsideboolean-dismisswhentouchoutside)，[**外部事件响应控制**](https://github.com/razerdp/BasePopup/wiki/API#setallowintercepttoucheventboolean-touchable)三者分离，再也不用担心我的PopupWindow各种按键响应问题
   - 如果不满足默认的事件，没问题，我们还提供了事件传递，您的事件您来把握
 - PopupWindow跟随AnchorView位置不准？屏幕外不消失？在这里，[**Link**](https://github.com/razerdp/BasePopup/wiki/API#linktoview-anchorview)方法为您排忧解难
 - 支持链式调用，还在为简单的PopupWindow使用不得不继承库的抽象类而感到烦躁？不妨来试试[**QuickPopupBuilder**](https://github.com/razerdp/BasePopup/wiki/API#QuickPopupBuilder)，想必您会爱上它的

<br>
<br>

### 注意事项

**WARN：**
 
  - **请务必仔细阅读本README,每个版本升级请务必查阅更新日志，这可以为您减少不必要弯路**
  - **请注意引用版本的问题，Release版本是稳定版，Candy是预览版。**
    - Release版本：一般在Candy版本反复验证修复后发布到Release，如果您对稳定性要求较高，请使用Release版本。
    - Candy版本：一般新功能、issue修复都会发布到Candy版本，Candy版本发布比较频繁，但通常会拥有新的功能，如果您喜欢试验新功能同时对稳定性要求不高，请使用Candy版本。
    - **Release和Candy两个版本互相切换可能会导致Build失败，这时候您Clean一下Project即可**
  - **如果您是以前1.x版本的用户，现在想更新到2.x，请在更新前查阅：[1.x迁移到2.x帮助文档](https://github.com/razerdp/BasePopup/blob/master/1.x%E8%BF%81%E7%A7%BB2.x%E5%B8%AE%E5%8A%A9%E6%96%87%E6%A1%A3.md)**


>Android P已经适配，感谢[@Guolei1130](https://github.com/Guolei1130)收集的方法。<br><br>文章地址：[android_p_no_sdkapi_support](https://github.com/Guolei1130/android_p_no_sdkapi_support)<br><br>本库一开始采用360的方法，但不得不走Native，为了个Popup不得不引入so感觉很不值得，在看到这篇文章后，才想起UnSafe类，因此本库采用方法5。<br><br>如果以后UnSafe类移除掉的话，再考虑Native方法。<br><br><b>最后再一次感谢大牛提供的方法~</b>

<br>
<br>

### 快速入门
---

更多使用方法请查看[Wiki#使用方法](https://github.com/razerdp/BasePopup/wiki/%E4%BD%BF%E7%94%A8%E6%96%B9%E6%B3%95)

### 依赖

| **Release** | **Candy** |
| ---- | ---- |
| [![Download](https://api.bintray.com/packages/razerdp/maven/BasePopup/images/download.svg) ](https://bintray.com/razerdp/maven/BasePopup/_latestVersion) | [![Download](https://api.bintray.com/packages/razerdp/maven/BasePopup_Candy/images/download.svg) ](https://bintray.com/razerdp/maven/BasePopup_Candy/_latestVersion) |


添加依赖到Gradle（请把<b>{$latestVersion}</b>替换成上面的Jcenter标签所示版本）

```xml
	dependencies {
	        implementation 'com.github.razerdp:BasePopup:{$latestVersion}'
	        
	        //candy版本
	        //implementation 'com.github.razerdp:BasePopup_Candy:{$latestVersion}'
	}
```
<br>

### 配置

#### 模糊配置

**从1.9.0-alpha开始支持背景模糊（只需要一个方法：`setBlurBackgroundEnable(boolean)`）**

**RenderScript最低支持api 17（更低的情况将会使用fastblur），您需要在gradle配置一下代码**

```xml
defaultConfig {
        renderscriptTargetApi 25
        renderscriptSupportModeEnabled true
    }
```

<br>

### 普通使用

#### 1.编写您的xml文件

像您平时定制View布局文件一样定制您的PopupWindow布局

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_gravity="center"
    android:background="@android:color/holo_blue_dark"
    android:orientation="vertical"
    >

    <TextView
        android:id="@+id/tx_1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:gravity="center"
        android:padding="16dp"
        android:text="test1"
        android:textColor="@color/color_black1"/>

</LinearLayout>
```
<p align="left"><img src="https://github.com/razerdp/Pics/blob/master/BasePopup/%E7%BC%96%E5%86%99xml.png" height="360"/></p>



#### 2.创建您的Popup类并继承BasePopupWindow

```java
public class DemoPopup extends BasePopupWindow {
    public DemoPopup(Context context) {
        super(context);
    }

    @Override
    public View onCreateContentView() {
        return null;
    }
}
```


#### 3.补充对应方法

**强烈建议在`onCreateContentView()`里使用`createPopupById()`来进行inflate，这样本库才能正确的做出对应的解析和适配**

```java
public class DemoPopup extends BasePopupWindow {
    public DemoPopup(Context context) {
        super(context);
    }

    // 必须实现，这里返回您的contentView
    // 为了让库更加准确的做出适配，强烈建议使用createPopupById()进行inflate
    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_normal);
    }
    
    // 以下为可选代码（非必须实现）
    // 返回作用于PopupWindow的show和dismiss动画，本库提供了默认的几款动画，这里可以自由实现
    @Override
    protected Animation onCreateShowAnimation() {
        return getDefaultScaleAnimation(true);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getDefaultScaleAnimation(false);
    }
}
```


#### 4.show！

展示PopupWindow的方法有三种，分别是`showPopupWindow()`、`showPopupWindow(View anchor)`和`showPopupWindow(int x, int y)`：

```java
new DemoPopup(getContext()).showPopupWindow();
//new DemoPopup(getContext()).showPopupWindow(v);
//new DemoPopup(getContext()).showPopupWindow(x,y);
```
<br>

这三个方法有不同的含义：

 - `showPopupWindow()`：无参传入，此时PopupWindow参考对象为屏幕（或者说整个DecorView），Gravity的表现就像在FrameLayout里面的Gravity表现一样，表示其处于屏幕的哪个方位
 - `showPopupWindow(View anchor)`：传入AnchorView，此时PopupWindow参考对象为传入的anchorView，Gravity的表现则意味着这个PopupWindow应该处于目标AnchorView的哪个方位
 - `showPopupWindow(int x, int y)`：传入位置信息，此时PopupWindow将会在指定位置弹出。

>建议：如果PopupWindow需要重复展示或者保留状态，建议作为成员变量使用，而不要作为局部变量每次都创建

>关于Gravity的更多api，请查看：[Wiki-Api：Gravity](https://github.com/razerdp/BasePopup/wiki/API#setpopupgravityint-popupgravity)

例子展示：

 - `showPopupWindow()无参传入`

| **gravity = CENTER<br>上述例子中xml写明了layout_gravity=center** | **gravity = RIGHT \| CENTER_VERTICAL** |
| - | - |
| <p align="center"><img src="https://github.com/razerdp/Pics/blob/master/BasePopup/show_1.gif" height="360"/></p> | <p align="center"><img src="https://github.com/razerdp/Pics/blob/master/BasePopup/show_2.gif" height="360"/></p> |

 - `showPopupWindow(View v)传入anchorView`

| **gravity = CENTER<br>上述例子中xml写明了layout_gravity=center** | **gravity = RIGHT \| CENTER_VERTICAL** |
| - | - |
| <p align="center"><img src="https://github.com/razerdp/Pics/blob/master/BasePopup/show_3.gif" height="360"/></p> | <p align="center"><img src="https://github.com/razerdp/Pics/blob/master/BasePopup/show_4.gif" height="360"/></p> |


 - `showPopupWindow(int x, int y)传入位置x,y坐标`

 | **gravity = CENTER<br>上述例子中xml写明了layout_gravity=center** |
 | - |
 | <p align="center"><img src="https://github.com/razerdp/Pics/blob/master/BasePopup/wiki/anypos/anypos.gif" height="360"/></p> |

<br>

### QuickPopupBuilder链式调用

QuickPopupBuilder支持链式调用生成一个基于QuickPopup的PopupWindow，该Builder旨在快速建立一个简单的不包含复杂逻辑的PopupWindow，如上述案例，避免过于简单的PopupWindow也要继承BasePopupWindow，导致存在过多的类。

#### 示例代码


----
如果您并不需要很详细的定义一个PopupWindow，您也可以选择`QuickPopupBuilder`采取链式写法快速编写出一个Popup以使用。

>注意：默认QuickPopupBuilder.QuickPopupConfig配置中PopupWindow动画为缩放弹出和消失

```java
        QuickPopupBuilder.with(getContext())
                .contentView(R.layout.popup_normal)
                .config(new QuickPopupConfig()
                        .gravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL)
                        .withClick(R.id.tx_1, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getContext(), "clicked", Toast.LENGTH_LONG).show();
                            }
                        }))
                .show();
		//.show(anchorView);
````

| **show()** | **show(anchorView)** |
| - | - |
| <p align="center"><img src="https://github.com/razerdp/Pics/blob/master/BasePopup/show_5.gif" height="360"/></p> | <p align="center"><img src="https://github.com/razerdp/Pics/blob/master/BasePopup/show_6.gif" height="360"/></p> |

<br>

### Api（请看Wiki）

请看wiki（陆续完善中）

**Link👉**[WIKI](https://github.com/razerdp/BasePopup/wiki)

<br>


### 更新日志 ([历史更新](https://github.com/razerdp/BasePopup/blob/master/UpdateLog.md))

* **【Candy】2.1.9**
  * **【Candy】2.1.9-prerelease**(2019/03/07)
    * 优化对android P刘海的支持，允许PopupWindow布局到刘海，fixed [**#154**](https://github.com/razerdp/BasePopup/issues/154)
  * **【Candy】2.1.9-beta4~5**(2019/03/1)
    * 修复quickpopup没有设置回调的问题
    * OnDismissListener添加退出动画开始的回调
    * 优化模糊逻辑
    * 优化退出动画逻辑
  * **【Candy】2.1.9-beta3**(2019/03/1)
    * fixed [**#152**](https://github.com/razerdp/BasePopup/issues/152)
  * **【Candy】2.1.9-beta1**(2019/02/28)
    * 优化代码，修复覆盖动画监听器的bug，优化layout逻辑
  * **【Candy】2.1.9-beta**(2019/2/26)
    * 为模糊图片方法添加oom捕捉
  * **【Candy】2.1.9-alpha4**(2019/2/21)
    * 优化背景和局部模糊逻辑
  * **【Candy】2.1.9-alpha3**(2019/2/21)
    * 紧急修复alpha2留下的坑
  * **【Candy】2.1.9-alpha2**(2019/2/19)
    * 去除lib的AndroidManifest内容，预防冲突，fixed [**#149**](https://github.com/razerdp/BasePopup/issues/149)
  * **【Candy】2.1.9-alpha1**(2019/02/18)
    * 针对DialogFragment适配，fixed [**#145**](https://github.com/razerdp/BasePopup/issues/145)

* **【Release】2.1.8**(2019/01/26)
  * 本次版本更新添加了许多新特性哦~特别是不拦截事件的背景黑科技又回来了
  * 更新细节：
    * 适配使用了[**ImmersionBar**](https://github.com/gyf-dev/ImmersionBar)的情况
    * 修复对横屏不兼容的问题
    * 修复构造器传入宽高无效的问题
    * **支持不拦截事件下的背景蒙层，没错！那个黑科技换了个更友好的方式来啦~**
    * 修复popup弹出的时候，金刚键（虚拟按键）一同弹出的问题（锁屏回来导致焦点变化从而导致全屏Activity又出现虚拟导航栏这个不算哈）
      * fixed  [**#141**](https://github.com/razerdp/BasePopup/issues/120)、[**#59**](https://github.com/razerdp/BasePopup/issues/141)
      * fixed  [**#120**](https://github.com/razerdp/BasePopup/issues/120)、[**#59**](https://github.com/razerdp/BasePopup/issues/59)
    * QuickPopupConfig增加`dismissOnOutSideTouch()`方法
    * 优化QuickPopupBuilder，增加[**Wiki**](https://github.com/razerdp/BasePopup/wiki/QuickPopupBuilder)
    * 针对[**#138**](https://github.com/razerdp/BasePopup/issues/138)出现的问题进行优化
    * 修复`setAlignBackgroundGravity()`与`setAlignBackground()`互相覆盖导致的顺序硬性要求问题

* **【Candy】2.1.8**
  * **【Candy】2.1.8-prerelease2**(2019/01/24)
    * 适配使用了[**ImmersionBar**](https://github.com/gyf-dev/ImmersionBar)的情况
  * **【Candy】2.1.8-prerelease**(2019/01/23)
    * 修复对横屏不兼容的问题
  * **【Candy】2.1.8-beta7**(2019/01/22)
    * beta3和beta4和beta5和beta6被我吃了~
    * 修复beta2关于focusable的问题，去掉无用代码
    * 修复构造器传入宽高无效的问题
    * **支持不拦截事件下的背景蒙层，没错！那个黑科技换了个更友好的方式来啦~**
  * **【Candy】2.1.8-beta2**(2019/01/22)
    * 修复popup弹出的时候，金刚键（虚拟按键）一同弹出的问题 **该功能目前测试中，如果有问题请务必反馈到candy**
      * fixed  [**#141**](https://github.com/razerdp/BasePopup/issues/120)、[**#59**](https://github.com/razerdp/BasePopup/issues/141)
    * QuickPopupConfig增加`dismissOnOutSideTouch()`方法
  * **【Candy】2.1.8-beta1**(2019/01/21)
    * 修复popup弹出的时候，金刚键（虚拟按键）一同弹出的问题 **该功能目前测试中，如果有问题请务必反馈到candy**
      * fixed  [**#120**](https://github.com/razerdp/BasePopup/issues/120)、[**#59**](https://github.com/razerdp/BasePopup/issues/59)
  * **【Candy】2.1.8-alpha2**(2019/01/18)
    * 优化QuickPopupBuilder，增加[**Wiki**](https://github.com/razerdp/BasePopup/wiki/QuickPopupBuilder)
  * **【Candy】2.1.8-alpha**(2019/01/17)
    * 针对[**#138**](https://github.com/razerdp/BasePopup/issues/138)出现的问题进行优化
    * 修复`setAlignBackgroundGravity()`与`setAlignBackground()`互相覆盖导致的顺序硬性要求问题

* **【Release】2.1.7**(2019/01/16)
  * 修复在`setAutoLocatePopup(true)`时，`onAnchorTop()`或`onAnchorBottom()`多次被调用的问题
  * 修复`setAllowInterceptTouchEvent(false)`时，因受默认限制而导致的无法定位到anchorView的问题
  * 优化弹起软键盘默认偏移量计算逻辑
  * 优化键盘高度计算逻辑
  * 感谢[**@ParfoisMeng**](https://github.com/ParfoisMeng)发现软键盘偏移问题并提交了PR[**PR#130**](https://github.com/razerdp/BasePopup/pull/130)
  * 发布2.1.7 release

* **【Candy】2.1.7-beta**(2019/01/10~2019/01/13)
  * 修复`setAllowInterceptTouchEvent(false)`时，因受默认限制而导致的无法定位到anchorView的问题
  * 优化弹起软键盘默认偏移量计算逻辑
  * 优化键盘高度计算逻辑

<br>

### 例子预览

| [**GravityPopupFrag**](./app/src/main/java/razerdp/demo/fragment/basedemo/GravityPopupFrag.java)  | [**LocatePopupFrag**](./app/src/main/java/razerdp/demo/fragment/other/LocatePopupFrag.java) |
| - | - |
| ![](https://github.com/razerdp/Pics/blob/master/BasePopup/demo_gravity.gif) | ![](https://github.com/razerdp/Pics/blob/master/BasePopup/demo_locatepopup.gif) |
| [**AnyPosPopupFrag**](./app/src/main/java/razerdp/demo/fragment/basedemo/AnyPosPopupFrag.java)  | [**UpdatePopupFrag**](./app/src/main/java/razerdp/demo/fragment/basedemo/UpdatePopupFrag.java) |
| ![](https://github.com/razerdp/Pics/blob/master/BasePopup/wiki/anypos/anypos.gif) | ![](https://github.com/razerdp/Pics/blob/master/BasePopup/wiki/update/update.gif) |
| [**BlurSlideFromBottomPopupFrag**](./app/src/main/java/razerdp/demo/popup/BlurSlideFromBottomPopup.java)  | [**CommentPopup**](./app/src/main/java/razerdp/demo/popup/CommentPopup.java) |
| ![](https://github.com/razerdp/Pics/blob/master/BasePopup/demo_blur_from_bottom.gif) | ![](https://github.com/razerdp/Pics/blob/master/BasePopup/demo_comment.gif) |
| [**SlideFromBottomPopup**](./app/src/main/java/razerdp/demo/popup/SlideFromBottomPopup.java)  | [**InputPopup**](./app/src/main/java/razerdp/demo/popup/InputPopup.java) |
| ![](https://github.com/razerdp/Pics/blob/master/BasePopup/demo_slide_from_bottom.gif) | ![](https://github.com/razerdp/Pics/blob/master/BasePopup/demo_input.gif) |
| [**ListPopup**](./app/src/main/java/razerdp/demo/popup/ListPopup.java)  | [**MenuPopup**](./app/src/main/java/razerdp/demo/popup/MenuPopup.java) |
| ![](https://github.com/razerdp/Pics/blob/master/BasePopup/demo_list.gif) | ![](https://github.com/razerdp/Pics/blob/master/BasePopup/demo_menu.gif) |


<br>


### 打赏（看在我那么努力维护的份上。。。给个零食呗~）

| 微信 |支付宝 | 
| ---- | ---- | 
| ![](https://github.com/razerdp/FriendCircle/blob/master/wechat.png)      | ![](https://github.com/razerdp/FriendCircle/blob/master/alipay.png) |

<br>

### 交流群：590777418

因为目前还有朋友圈项目，建立了一个交流群，出于懒得管理那么多，所以如果有想法或者优化建议或者其他问题，欢迎加入“朋友圈交流群”

![](https://github.com/razerdp/FriendCircle/blob/master/qqgroup.png)


<br>

### 常见问题

### 更多常见问题请看[**WIKI#常见问题**](https://github.com/razerdp/BasePopup/wiki/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98)

#### Q：如何取消默认的背景颜色

A：调用[**setBackgroundColor**](https://github.com/razerdp/BasePopup/wiki/API#setbackgroundcolorint-color)(Color.TRANSPARENT)或者[**setBackground**](https://github.com/razerdp/BasePopup/wiki/API#setbackgroundint-drawableids)(0)

<br>

***

<br>

#### Q：如何在dismiss()时不执行退出动画

A：调用dismiss(false)或者dismissWithOutAnimate()

<br>

***

<br>

#### Q：点击popupwindow背景部分不想让popupwindow隐藏怎么办

A：设置[**setAllowDismissWhenTouchOutside**](https://github.com/razerdp/BasePopup/wiki/API#setallowdismisswhentouchoutsideboolean-dismisswhentouchoutside)(false)

<br>

***

<br>

#### Q：Service里无法弹出

A：PopupWindow需要windowToken，因此ApplicationContext或者Service里面是无法弹出的，建议通过发出事件通知栈顶Activity来弹出

<br>

***

<br>

#### Q：为什么PopupWindow里面的EditText无法粘贴

>ISSUE REF：[**#140**](https://github.com/razerdp/BasePopup/issues/140)

>Google Issue Tracker：[**#36984016**](https://issuetracker.google.com/issues/36984016)

A：PopupWindow内的View是无法获取WindowToken的，而粘贴功能也是一个PopupWindow，它的显示必定需要WindowToken，因此无法粘贴。

<br>

***

<br>

#### Q：如何不让PopupWindow的蒙层覆盖状态栏

A：设置[**setPopupWindowFullScreen**](https://github.com/razerdp/BasePopup/wiki/API#setpopupwindowfullscreenboolean-isfullscreen)(false)

<br>

***

<br>

#### Q：如何点击back键不关闭pop

A：设置[**setBackPressEnable**](https://github.com/razerdp/BasePopup/wiki/API#setBackPressEnableboolean-backPressEnable)(false)

<br>

***

<br>

#### Q：为什么设置setAllowInterceptTouchEvent(false)后，蒙层或者背景模糊都消失了

A：~~在2.0.0到2.0.9之间，setAllowInterceptTouchEvent（）不影响蒙层或背景，但从2.1.0开始，不再开启这个黑科技，而是选择跟官方保持同步，原因在于如果背景模糊或者有蒙层，那么该PopupWindow理应拦截事件，而不应该穿透事件，否则不应该拥有背景蒙层。<br><br>~~
**但是！从2.1.8-beta6之后，该黑科技又支持啦~换了个比较友好的方式**<br><br>
同时，因为系统PopupWindow默认是clipToScreen，也就是限制PopupWindow在屏幕内显示，当view边缘超过屏幕的时候，PopupWindow会反向移动以完整展示内容，因此如果您的PopupWindow需要突破屏幕显示（比如高度是全屏，但展示在某个view下面，此时bottom大于屏幕底部），请设置`setClipToScreen(false)`。


<br>

***

<br>

#### Q：根布局高度`match_parent`和`wrap_content`的区别

A：当根布局是match_parent的时候，basepopup会做一定的差异处理。
<br>
当您设置了[**setClipToScreen(true)**](https://github.com/razerdp/BasePopup/wiki/API#setcliptoscreenboolean-cliptoscreen)时，如果您的根布局是`match_parent`，那么意味着您的布局最大高度仅为屏幕高度，如果您的根布局是`wrap_content`，那么最大高度可能是可以突破屏幕高度的。
<br>
**例如demo中的全屏listview**

```xml
<RelativeLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"   // 请留意这里
    android:background="@android:color/white"
    >

    <ListView
        android:id="@+id/popup_list"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:divider="@color/line_bg"
        android:dividerHeight="0.5dp"
        android:scrollbars="vertical"
        />
</RelativeLayout>
```
| **layout_height = match_parent** | **layout_height = wrap_content** |
| - | - |
| <p align="center"><img src="https://github.com/razerdp/Pics/blob/master/BasePopup/qa/qa_match_parent.png" height="360"/></p> | <p align="center"><img src="https://github.com/razerdp/Pics/blob/master/BasePopup/qa/qa_wrap_content.png" height="360"/></p> |

<br>
**留意两张图的listview底部区别，其中wrap_content底部已经突破屏幕底部，无法完整显示。**

<br>

***

<br>

### License

[Apache-2.0](./LICENSE)
