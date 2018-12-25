[1.x版本](./README_OLD.md) | 2.x版本

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

**致歉：非常抱歉~因为一时疏忽忘记合并一些东西，导致2.1.3版本在不拦截事件的情况下无anchorView弹窗会导致位置问题，在2.1.4重新合并了代码，对此造成的影响，深表歉意。在下表示以后一定会经过candy至少三次迭代后并检查完后发布release，避免发生类似错误。**

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

 - 更简单更精准的控制显示位置，通过[**Gravity**](https://github.com/razerdp/BasePopup/wiki/API#setpopupgravityint-popupgravity)和offset来控制您的PopupWindow
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

展示PopupWindow的方法有三种种，分别是`showPopupWindow()`、`showPopupWindow(View anchor)`和`showPopupWindow(int x, int y)`：

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

>注意：默认QuickPopupBuilder.QuickPopupConfig配置中PopupWindow动画为淡入淡出

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

* **【Candy】2.1.5-beta**(2018/12/25)
  * 增加`setAlignBackgroundGravity()`方法，背景对齐的位置由您来制定~
  * 增加`update(int width ,int height)`方法
  * 修复构造器传入width/height失效的问题，增加setWidth/setHeight方法

<img src="https://github.com/razerdp/Pics/blob/master/BasePopup/wiki/align/alignbg_plus.gif" height="360"/>


* **【Candy】2.1.5-alpha2**(2018/12/23)
  * 构造器增加延迟加载参数，如果您的Popup需要提前传参后，请在构造其中传入true以确认延迟加载
    * 如果使用延迟加载，初始化时机由您来制定，您需要调用`delayInit()`方法来进行BasePopup的初始化

* **【Candy】2.1.5-alpha**(2018/12/23)
  * 适配刘海屏全面平（双显示屏暂不适配）
  * 感谢[#114](https://github.com/razerdp/BasePopup/issues/114)的提供~
  * **Release年后发布，如果您有需要，请更新到此candy版。**

* **【Release】2.1.4**(2018/12/21)
  * **建议更新到这个版本！**
  * 非常抱歉~因为一时疏忽忘记合并一些东西，导致2.1.3版本在不拦截事件的情况下，无anchorView弹窗会导致位置问题，在2.1.4重新合并了代码，对此造成的影响，深表歉意。
    * 以后的版本一定会经过3个或以上的candy迭代仔细检查后再发！

* **【Release】2.1.3**(2018/12/21)
  * 正式发布2.1.3release
  * 增加[**linkTo(View)**](https://github.com/razerdp/BasePopup/wiki/API#linktoview-anchorview)方法
  * 支持update方法来跟随view或者指定位置更新
  * 全面优化系统原有的popupwindow定位方法，全版本统一。
  * 2.x的坑基本补完
  * 19年，我们再见-V-

* **【Candy】2.1.3-alpha2**(2018/12/20)
  * 增加[**linkTo(View)**](https://github.com/razerdp/BasePopup/wiki/API#linktoview-anchorview)方法，跟随anchorView状态？一个方法就足够了~
  * 2.x的坑基本补完~如无意外，这个功能将会是18年最后一个功能性更新了

<img src="https://github.com/razerdp/Pics/blob/master/BasePopup/wiki/linkto/linkto.gif" height="360"/>


* **【Candy】2.1.3-alpha**(2018/12/19)
  * 支持update方法来跟随view或者指定位置更新
  * 调用`updatePopup()`方法即可~
  * 全面覆盖系统原有的popupwindow定位方法，全版本统一。

<img src="https://github.com/razerdp/Pics/blob/master/BasePopup/wiki/update/update.gif" height="360"/>

* **【Release】2.1.2**(2018/12/19)
  * 正式发布2.1.2release
  * 增加指定位置弹出的方法[**showPopupWindow(int x, int y)**](https://github.com/razerdp/BasePopup/blob/master/lib/src/main/java/razerdp/basepopup/BasePopupWindow.java#L681)
  * 修复内容宽高超过屏幕后`ClipToScreen()`修正不正确的问题
  * 输入法适配修复 fixed [#107](https://github.com/razerdp/BasePopup/issues/107)
  * preview:

<img src="https://github.com/razerdp/Pics/blob/master/BasePopup/wiki/anypos/anypos.gif" height="360"/>


* **【Candy】2.1.2-alpha2**(2018/12/17)
  * 修复内容宽高超过屏幕后`ClipToScreen()`修正不正确的问题
  * 输入法适配修复 fixed [#107](https://github.com/razerdp/BasePopup/issues/107)

* **【Release】2.1.1**(2018/12/13)
  * 针对setAlignBackground()失效的问题修复

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

#### Q：如何取消默认的背景颜色

A：调用[**setBackgroundColor**](https://github.com/razerdp/BasePopup/wiki/API#setbackgroundcolorint-color)(Color.TRANSPARENT)或者[**setBackground**](https://github.com/razerdp/BasePopup/wiki/API#setbackgroundint-drawableids)(0)

#### Q：如何在dismiss()时不执行退出动画

A：调用dismiss(false)或者dismissWithOutAnimate()

#### Q：点击popupwindow背景部分不想让popupwindow隐藏怎么办

A：设置[**setAllowDismissWhenTouchOutside**](https://github.com/razerdp/BasePopup/wiki/API#setallowdismisswhentouchoutsideboolean-dismisswhentouchoutside)(false)

#### Q：Service里无法弹出

A：PopupWindow需要windowToken，因此ApplicationContext或者Service里面是无法弹出的，建议通过发出事件通知栈顶Activity来弹出

#### Q：如何不让PopupWindow的蒙层覆盖状态栏

A：设置[**setPopupWindowFullScreen**](https://github.com/razerdp/BasePopup/wiki/API#setpopupwindowfullscreenboolean-isfullscreen)(false)

#### Q：如何点击back键不关闭pop

A：设置[**setBackPressEnable**](https://github.com/razerdp/BasePopup/wiki/API#setBackPressEnableboolean-backPressEnable)(false)

#### Q：为什么设置setAllowInterceptTouchEvent(false)后，蒙层或者背景模糊都消失了

A：在2.0.0到2.0.9之间，setAllowInterceptTouchEvent（）不影响蒙层或背景，但从2.1.0开始，不再开启这个黑科技，而是选择跟官方保持同步，原因在于如果背景模糊或者有蒙层，那么该PopupWindow理应拦截事件，而不应该穿透事件，否则不应该拥有背景蒙层。<br><br>
同时，因为系统PopupWindow默认是clipToScreen，也就是限制PopupWindow在屏幕内显示，当view边缘超过屏幕的时候，PopupWindow会反向移动以完整展示内容，因此如果您的PopupWindow需要突破屏幕显示（比如高度是全屏，但展示在某个view下面，此时bottom大于屏幕底部），请设置`setClipToScreen(false)`。


<br>

### License

[Apache-2.0](./LICENSE)
