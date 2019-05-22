[**1.x版本**](./README_OLD.md) | **2.x版本** | [**English**](./README_V2_EN.md)

<p align="center"><img src="./img/logo.png" alt="Logo图片似乎加载不出来" height="360"/></p>
<h2 align="center">BasePopup - Android下打造通用便捷的PopupWindow</h2>
<div align="center">
<table>
        <tr>
            <th>Release</th>
            <th>Candy</th>
            <th>License</th>
			<th>Api</th>
			<th>Author</th>
        </tr>
        <tr>
            <td align="center">
				<a href ="https://bintray.com/razerdp/maven/BasePopup/_latestVersion">
					<img src="https://api.bintray.com/packages/razerdp/maven/BasePopup/images/download.svg"/>
				</a>
			</td>
			<td align="center">
				<a href = "https://bintray.com/razerdp/maven/BasePopup_Candy/_latestVersion">
					<img src="https://api.bintray.com/packages/razerdp/maven/BasePopup_Candy/images/download.svg"/>
				</a>
			</td>
			<td align="center">
				<a href = "https://github.com/razerdp/BasePopup/blob/master/LICENSE">
					<img src="https://img.shields.io/badge/license-Apache--2.0-blue.svg"/>		
				</a>
				<br></br>
				<a href = "https://github.com/razerdp/BasePopup/blob/master/LICENSE_996">
                	<img src="https://img.shields.io/badge/license-Anti%20996-blue.svg?style=flat-square"/>
                </a>
			</td>
			<td align="center">
				<a href="https://img.shields.io/badge/Api-14%2B-green.svg">
					<img src="https://img.shields.io/badge/Api-16%2B-green.svg"/>
				</a>
			</td>
			<td align="center">
				<a href = "https://github.com/razerdp">
					<img src="https://img.shields.io/badge/Author-razerdp-blue.svg"/>
				</a>
			</td>
		</tr>
		<tr>
			<td rowspan="3" align="center">Compat组件</td>
			<td align="center"></td>
			<td align="center">support</td>
			<td align="center">lifecycle</td>
			<td align="center">androidx</td>
			<tr>
			<td align="center">Release</td>
			<td align="center">
				<a href = "https://bintray.com/razerdp/maven/BasePopup-compat-support/_latestVersion">
                	<img src="https://api.bintray.com/packages/razerdp/maven/BasePopup-compat-support/images/download.svg"/>		
                </a>
            </td>
			<td align="center">
				<a href = "https://bintray.com/razerdp/maven/BasePopup-compat-lifecycle/_latestVersion">
                	<img src="https://api.bintray.com/packages/razerdp/maven/BasePopup-compat-lifecycle/images/download.svg"/>		
                </a>
			</td>
			<td align="center">
				<a href = "https://bintray.com/razerdp/maven/BasePopup-compat-androidx/_latestVersion">
                	<img src="https://api.bintray.com/packages/razerdp/maven/BasePopup-compat-androidx/images/download.svg"/>		
                </a>
			</td>
			</tr>
			<tr>
			<td align="center">Candy</td>
			<td align="center">
				<a href = "https://bintray.com/razerdp/maven/BasePopup_Candy-compat-support/_latestVersion">
                	<img src="https://api.bintray.com/packages/razerdp/maven/BasePopup_Candy-compat-support/images/download.svg"/>		
                </a>
			</td>
			<td align="center">
				<a href = "https://bintray.com/razerdp/maven/BasePopup_Candy-compat-lifecycle/_latestVersion">
                	<img src="https://api.bintray.com/packages/razerdp/maven/BasePopup_Candy-compat-lifecycle/images/download.svg"/>		
                </a>
			</td>
			<td align="center">
				<a href = "https://bintray.com/razerdp/maven/BasePopup_Candy-compat-androidx/_latestVersion">
                	<img src="https://api.bintray.com/packages/razerdp/maven/BasePopup_Candy-compat-androidx/images/download.svg"/>		
                </a>
			</td>
		</tr>
			
</table>



</div>

---

[**apk体验下载**](https://fir.im/pfc9)

### 导航
 
 - [特性](#特性)
 - [文章分享](https://github.com/razerdp/Article/blob/master/%E4%BA%B2%EF%BC%8C%E8%BF%98%E5%9C%A8%E4%B8%BAPopupWindow%E7%83%A6%E6%81%BC%E5%90%97.md)
 - [注意事项](#注意事项)
   - [WARN](#WARN)
   - [声明](#声明)
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
 - [混淆](#混淆)
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

#### WARN
 
  - **请务必仔细阅读本README,每个版本升级请务必查阅更新日志，这可以为您减少不必要弯路**
  - **请注意引用版本的问题，Release版本是稳定版，Candy是预览版。**
    - Release版本：一般在Candy版本反复验证修复后发布到Release，如果您对稳定性要求较高，请使用Release版本。
    - Candy版本：一般新功能、issue修复都会发布到Candy版本，Candy版本发布比较频繁，但通常会拥有新的功能，如果您喜欢试验新功能同时对稳定性要求不高，请使用Candy版本。
    - **Release和Candy两个版本互相切换可能会导致Build失败，这时候您Clean一下Project即可**
  - **如果您是以前1.x版本的用户，现在想更新到2.x，请在更新前查阅：[1.x迁移到2.x帮助文档](https://github.com/razerdp/BasePopup/blob/master/1.x%E8%BF%81%E7%A7%BB2.x%E5%B8%AE%E5%8A%A9%E6%96%87%E6%A1%A3.md)**


>Android P已经适配，感谢[@Guolei1130](https://github.com/Guolei1130)收集的方法。<br><br>文章地址：[android_p_no_sdkapi_support](https://github.com/Guolei1130/android_p_no_sdkapi_support)<br><br>本库一开始采用360的方法，但不得不走Native，为了个Popup不得不引入so感觉很不值得，在看到这篇文章后，才想起UnSafe类，因此本库采用方法5。<br><br>如果以后UnSafe类移除掉的话，再考虑Native方法。<br><br><b>最后再一次感谢大牛提供的方法~</b>

<br>
<br>

#### 声明

BasePopup并非一个 **“一句话完成需求”** 的库，从起名带有 **【Base】** 也应该可以知道这是一个高度抽象的类。<br><br>这也意味着本库所提供的api是基于通用抽象层面的。<br><br>因此，具体的需求比如点击事件，比如列表弹窗等等都需要您自行完成。<br><br>因为众口难调，你的需求并不适用于别人的需求，而作为一个通用库，就如ListAdapter一样，最大程度的开放给用户完成是最好的方案。<br><br>因此，如果您期望使用BasePopup可以一句话完成所有事情，很抱歉~可能本库不能满足您的需求。<br><br>

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

	        //BasePopup主体库
	        implementation 'com.github.razerdp:BasePopup:{$latestVersion}'

	        //BasePopup support支持库
	        implementation 'com.github.razerdp:BasePopup-compat-support:{$latestVersion}'

	        //BasePopup lifecycle支持库
	        implementation 'com.github.razerdp:BasePopup-compat-lifecycle:{$latestVersion}'

	        //BasePopup androidx支持库
	        implementation 'com.github.razerdp:BasePopup-compat-androidx:{$latestVersion}'
	        
	        //candy渠道
		//implementation 'com.github.razerdp:BasePopup_Candy:{$latestVersion}'
		//implementation 'com.github.razerdp:BasePopup_Candy-compat-support:{$latestVersion}'
		//implementation 'com.github.razerdp:BasePopup_Candy-compat-lifecycle:{$latestVersion}'
		//implementation 'com.github.razerdp:BasePopup_Candy-compat-androidx:{$latestVersion}'
	}
```
<br>

### 配置

#### 模糊配置

**从1.9.0-alpha开始支持背景模糊（只需要一个方法：`setBlurBackgroundEnable(boolean)`）**

**RenderScript最低支持api 18（更低的情况将会使用fastblur），您需要在gradle配置一下代码**

**我们建议你设置renderscriptTargetApi为能够提供你正在使用的所有功能的最低API级别**

```xml
defaultConfig {

        renderscriptTargetApi 18
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

### 混淆
---

**如果您需要LifeCycle的支持，请添加LifeCycle的混淆**

```xml
## Android architecture components: Lifecycle
# LifecycleObserver's empty constructor is considered to be unused by proguard
-dontnote android.arch.lifecycle.**
-keepclassmembers class * implements android.arch.lifecycle.LifecycleObserver {
    <init>(...);
}
# ViewModel's empty constructor is considered to be unused by proguard
-keepclassmembers class * extends android.arch.lifecycle.ViewModel {
    <init>(...);
}
# keep Lifecycle State and Event enums values
-keepclassmembers class android.arch.lifecycle.Lifecycle$State { *; }
-keepclassmembers class android.arch.lifecycle.Lifecycle$Event { *; }
# keep methods annotated with @OnLifecycleEvent even if they seem to be unused
# (Mostly for LiveData.LifecycleBoundObserver.onStateChange(), but who knows)
-keepclassmembers class * {
    @android.arch.lifecycle.OnLifecycleEvent *;
}
-keep class * implements android.arch.lifecycle.GeneratedAdapter {
    <init>(...);
}

```

<br>

### Api（请看Wiki）

请看wiki（陆续完善中）

**Link👉**[WIKI](https://github.com/razerdp/BasePopup/wiki)

<br>


### 更新日志 ([历史更新](https://github.com/razerdp/BasePopup/blob/master/UpdateLog.md))

* **【Candy】2.2.1**(2019/05/16)
    * **【Candy】190516**
      * 支持Service或者非ActivityContext里弹窗
    * **【Candy】190517**
      * 优化PopupUiUtils，优化获取屏幕宽高算法
      * fixed [**#186**](https://github.com/razerdp/BasePopup/issues/186)、[**#167**](https://github.com/razerdp/BasePopup/issues/167)
      * fixed [**#188**](https://github.com/razerdp/BasePopup/issues/188)(not perfect)
    * **【Candy】190522**
      * 修改并优化键盘判断逻辑


* **【Release】2.2.0**(2019/05/15)
  * 正式版2.2.0隆重归来，这次正式版又是一个重构版本哦~
  * 优化输入法对齐逻辑
  * **重构模糊逻辑：**
    * 经测试，720p的手机在默认参数下全屏模糊时间平均在**6ms~16ms**之间
    * 增大默认参数的模糊程度
    * 模糊淡入淡出时间跟随Popup的动画时间
    * 修复模糊偶尔失效的情况
  * **测量/布局相关：**
    * 重构测量逻辑：
      * 现在在`clipToScreen`的情况下，会根据剩余空间对PopupDecor进行重新测量，以保证Popup完整的显示，如果您需要保持原始的测量值，请调用`keepSize(true)`
      * 重构layout逻辑，针对**outSideTouch**优化
      * 适配屏幕旋转，fix [#180](https://github.com/razerdp/BasePopup/issues/180)
      * 采取flag代替各种boolean，清爽更简洁
      * 减少冗余代码
  * **优化相关：**
    * 增加GravityMode值，现在允许您配置`PopupGravity`的参考模式啦~
      * **RELATIVE_TO_ANCHOR**：默认模式，以Anchor为参考点，指定PopupWindow显示在Anchor的方位
      * **ALIGN_TO_ANCHOR_SIDE**：对齐模式，以Anchor的边为参考点，指定PopupWindow的边与Anchor的哪条边对齐
    * 增加minWidth/minHeight 方法，增加maxWidth/maxHeight 方法，让他们相互对应~
    * 修复高度为match_parent和wrap_content的测量差异，现在可以安心地玩耍啦
    * 部分Api标记过时：
      * ~~setAllowDismissWhenTouchOutside~~ -> **setOutSideDismiss**
      * ~~setAllowInterceptTouchEvent~~ -> **setOutSideTouchable**
    * 增加`setBackgroundView(View)`方法，现在BasePopup的背景控件可以随意由你定制啦~当然PopupWindow的背景动画控制方法依旧生效
  * **包拆分：**
    * 现在BasePopup将会进行包的拆分，源工程仅针对没有任何依赖的原生Android进行适配，如果您需要别的适配，请分别依赖以下模块或多个模块：
      * 如果您需要`support`库的支持，比如DialogFragment支持，请依赖
        * `implementation 'com.github.razerdp:BasePopup-compat-support:{$latestVersion}'`
      * 如果您需要`lifecycle`库的支持，比如destroy里自动释放或者关闭等，请依赖
        * `implementation 'com.github.razerdp:BasePopup-compat-lifecycle:{$latestVersion}'`
      * 如果您需要`androidX`库的支持，请依赖
        * `implementation 'com.github.razerdp:BasePopup-compat-androidx:{$latestVersion}'`
      * **请注意，如果您依赖了androidX支持组件，请不要依赖另外两个支持组件，否则会冲突**
  * **Bug fixed：**
    * fix [#171](https://github.com/razerdp/BasePopup/issues/171)、[#181](https://github.com/razerdp/BasePopup/issues/181)、[#182](https://github.com/razerdp/BasePopup/issues/182)、[#183](https://github.com/razerdp/BasePopup/issues/183)
    * fix [#180](https://github.com/razerdp/BasePopup/issues/180)
    * fixed [#164](https://github.com/razerdp/BasePopup/issues/164)
  * **Other：**
    * add 996 license

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

A：设置[**setOutSideDismiss**](https://github.com/razerdp/BasePopup/wiki/API#setoutsidedismissboolean-outsidedismiss)(false)

<br>

***

<br>

#### ~~Q：Service里无法弹出~~【自2.2.1版本开始支持非ActivityContext里弹出】

~~A：PopupWindow需要windowToken，因此ApplicationContext或者Service里面是无法弹出的，建议通过发出事件通知栈顶Activity来弹出~~

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

#### Q：如何在BasePopup里使用ButterKnife

A：您可以在构造器中进行绑定：

```java
   public DemoPopup(Context context) {
        super(context);
        ButterKnife.bind(this,getContentView());
    }
```

<br>

***

<br>

#### Q：为什么BasePopup的宽度不对或者留有一条缝隙

A：请务必留意您是否使用了头条类等修改Density的适配方案，BasePopup只遵循官方的测量方法并没有额外的添加别的测量方式，因此如果因为第三方修改导致的适配问题，本库概不负责<br><br>
如果您用的是[**AndroidAutoSize**](https://github.com/JessYanCoding/AndroidAutoSize)，请尝试[issue#13](https://github.com/JessYanCoding/AndroidAutoSize/issues/13)的解决方案：<br><br>
>在任何情况下本来适配正常的布局突然出现适配失效，适配异常等问题，只要重写 Activity 的 getResources() 方法即可，如果是 Dialog、PopupWindow 等控件出现适配失效或适配异常，同样在每次 show() 之前调用 AutoSize#autoConvertDensity() 即可

<br>

***

<br>

### License

[Apache-2.0](./LICENSE)
