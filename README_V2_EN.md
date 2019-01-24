[**Chinese**](./README.md) | **English**

<p align="center"><img src="./img/logo.png" alt="Logo load failed" height="360"/></p>
<h2 align="center">BasePopup - A powerful and convenient PopupWindow library for Android</h2>
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

[**apk demo download**](https://fir.im/pfc9)

### Guide

 - [Feature](#Feature)
 - [Precautions](#Precautions)
 - [Quick start](#Quick-start)
   - [Configuration](#Configuration)
     - [Blur Configuration](#Blur-Configuration)
   - [Dependence](#Dependence)
   - [Common Usage](#Common-Usage)
     - [1.Create your popup xml file](#1Create-your-popup-xml-file)
     - [2.Create popup class which extends BasePopupWindow](#2Create-popup-class-which-extends-BasePopupWindow)
     - [3.Complete abstract method](#3Complete-abstract-method)
     - [4.show！](#4show)
   - [QuickPopupBuilder chained usage](#QuickPopupBuilder-chained-usage)
     - [Sample](#Sample)
 - [Api (see wiki)](#Api-see-wiki)
 - [Update log](#Update-log-Historical-update)
   - [Historical update](./UpdateLog.md)
 - [Demo preview](#Demo-preview)
 - [Coffee me](#Coffee-me)
 - [Q&A](#qa)
 - [LICENSE](#license)

<br>
<br>

### Feature

 - Simple and precise control of display position with [**Gravity**](https://github.com/razerdp/BasePopup/wiki/API#setpopupgravityint-popupgravity) and [**offset**](https://github.com/razerdp/BasePopup/wiki/API#setoffsetxint-offsetx).
 - Basepopup is an abstract class with almost no constraints on subclasses. You can customize your PopupWindow just like a custom View.
 - Support [**Animation**](https://github.com/razerdp/BasePopup/wiki/API#setshowanimationanimation-showanimation), [**Animator**](https://github.com/razerdp/BasePopup/wiki/API#setshowanimatoranimator-showanimator), freely control the animation of your PopupWindow, no longer need to write animation xml.
 - Darkening the background, [**changing the background color**](https://github.com/razerdp/BasePopup/wiki/API#setbackgroundcolorint-color) will be very easy.
 - [**Blur background**](https://github.com/razerdp/BasePopup/wiki/API#setblurbackgroundenableboolean-blurbackgroundenable) or [**partial blur**](https://github.com/razerdp/BasePopup/wiki/API#setbluroptionpopupbluroption-option) is also very easy.
 - [**Backpress control**](https://github.com/razerdp/BasePopup/wiki/API#setbackpressenableboolean-backpressenable) , [**click outside to dismiss**](https://github.com/razerdp/BasePopup/wiki/API#setallowdismisswhentouchoutsideboolean-dismisswhentouchoutside) , [**outside touch event**](https://github.com/razerdp/BasePopup/wiki/API#setallowintercepttoucheventboolean-touchable) all separation，no longer have to worry about my PopupWindow various key events problems.
   - We also support event delivery at the same time.
 - Support [**linkTo**](https://github.com/razerdp/BasePopup/wiki/API#linktoview-anchorview) anchorview.
 - Support for chained calls. For a simple scene,try [**QuickPopupBuilder**](https://github.com/razerdp/BasePopup/wiki/API#QuickPopupBuilder),I bet you will love it.

<br>
<br>

### Precautions

**WARNING：**

  - **Please be sure to read this README carefully. Please check the update log for each version upgrade, which can reduce unnecessary detours for you.**
  - **Please pay attention on the dependence version, the Release version is a stable version, and Candy is a preview version.**
    - Release version: Generally published to Release after repeated verification of the Candy version. If you have higher stability requirements, please use the Release version.
    - Candy version: new features, issue fixes will be published  to the Candy version, Candy version is updated more frequently, but usually has new features, if you like to test new features and stability requirements are not high, please use the Candy version.
    - **Switching between Release and Candy versions may cause Build to fail. At this time, you can clean Project.**
  - **If you are a previous 1.x user and want to update to 2.x now, please check before the update: [1.x migration to 2.x help documentation](https://github.com/razerdp/BasePopup/blob/master/1.x%E8%BF%81%E7%A7%BB2.x%E5%B8%AE%E5%8A%A9%E6%96%87%E6%A1%A3.md)**


>Android P has been adapted, thanks to the method of [@Guolei1130](https://github.com/Guolei1130) collection.<br><br>Article address：[android_p_no_sdkapi_support](https://github.com/Guolei1130/android_p_no_sdkapi_support)

<br>
<br>

### Quick start
---

See more:[Wiki#Usage](https://github.com/razerdp/BasePopup/wiki/%E4%BD%BF%E7%94%A8%E6%96%B9%E6%B3%95)

### Dependence

| **Release** | **Candy** |
| ---- | ---- |
| [![Download](https://api.bintray.com/packages/razerdp/maven/BasePopup/images/download.svg) ](https://bintray.com/razerdp/maven/BasePopup/_latestVersion) | [![Download](https://api.bintray.com/packages/razerdp/maven/BasePopup_Candy/images/download.svg) ](https://bintray.com/razerdp/maven/BasePopup_Candy/_latestVersion) |


Add dependencies to Gradle (Please replace <b>{$latestVersion}</b> with the version shown in the Jcenter tab above)

```xml
	dependencies {
	        implementation 'com.github.razerdp:BasePopup:{$latestVersion}'

	        //candy版本
	        //implementation 'com.github.razerdp:BasePopup_Candy:{$latestVersion}'
	}
```
<br>

### Configuration

#### Blur Configuration

**Support blur background from 1.9.0-alpha（Just call：`setBlurBackgroundEnable(boolean)`）**

**RenderScript minimum support api 17 (lower case will use fastblur),you need to configure the following code in gradle**

```xml
defaultConfig {
        renderscriptTargetApi 25
        renderscriptSupportModeEnabled true
    }
```

<br>

### Common Usage

#### 1.Create your popup xml file

Customize your PopupWindow layout just like you would normally customize a View layout.

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



#### 2.Create popup class which extends BasePopupWindow

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


#### 3.Complete abstract method

**It is strongly recommended to use `createPopupById()` in `onCreateContentView()` to inflate view so that the library can correctly parse and adapt.**

```java
public class DemoPopup extends BasePopupWindow {
    public DemoPopup(Context context) {
        super(context);
    }

    // Must be implemented, return your contentView here
    // recommended to use createPopupById() for inflate
    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_normal);
    }

    // The following are optional codes (not required)
    // Return to the show and dismiss animations for PopupWindow. Basepopup provides several default animations, which can be freely implemented here.
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

There are three ways to show PopupWindow:`showPopupWindow()`,`showPopupWindow(View anchor)` and `showPopupWindow(int x, int y)`：


```java
new DemoPopup(getContext()).showPopupWindow();
//new DemoPopup(getContext()).showPopupWindow(v);
//new DemoPopup(getContext()).showPopupWindow(x,y);
```
<br>

These three methods have different meanings:

 - `showPopupWindow()`：No-params method，At this point, the PopupWindow reference object is the screen (or the entire DecorView).Gravity behaves just like the Gravity in FrameLayout, indicating which position it is on the screen.
 - `showPopupWindow(View anchor)`：Set an anchorView.At this point, the PopupWindow reference object is the incoming anchorView.The performance of Gravity means that this PopupWindow should be in the orientation of the target AnchorView.
 - `showPopupWindow(int x, int y)`：Set the position for basepopup,At this point PopupWindow will pop up at the specified location.

>Suggestion：If PopupWindow needs to repeat the display or retain state, it is recommended to be used as a member variable instead of being created as a local variable each time.

>For more apis on Gravity, check out：[Wiki-Api：Gravity](https://github.com/razerdp/BasePopup/wiki/API#setpopupgravityint-popupgravity)

Sample for `showPopupWindow()`：

 - `showPopupWindow()`

| **gravity = CENTER<br>In the above example<br>xml specifies layout_gravity=center** | **gravity = RIGHT \| CENTER_VERTICAL** |
| - | - |
| <p align="center"><img src="https://github.com/razerdp/Pics/blob/master/BasePopup/show_1.gif" height="360"/></p> | <p align="center"><img src="https://github.com/razerdp/Pics/blob/master/BasePopup/show_2.gif" height="360"/></p> |

 - `showPopupWindow(View v)`

| **gravity = CENTER<br>In the above example<br>xml specifies layout_gravity=center** | **gravity = RIGHT \| CENTER_VERTICAL** |
| - | - |
| <p align="center"><img src="https://github.com/razerdp/Pics/blob/master/BasePopup/show_3.gif" height="360"/></p> | <p align="center"><img src="https://github.com/razerdp/Pics/blob/master/BasePopup/show_4.gif" height="360"/></p> |


 - `showPopupWindow(int x, int y)`

 | **gravity = CENTER<br>In the above example<br>xml specifies layout_gravity=center** |
 | - |
 | <p align="center"><img src="https://github.com/razerdp/Pics/blob/master/BasePopup/wiki/anypos/anypos.gif" height="360"/></p> |

<br>

### QuickPopupBuilder chained usage

QuickPopupBuilder supports chained calls to generate a PopupWindow based on QuickPopup.The Builder is designed to quickly build a simple PopupWindow that does not contain complex logic, such as the above case.Avoid creating too many BasePopupWindow implementation classes.

#### Sample


----

>Attention：The PopupWindow animation in the default `QuickPopupBuilder.QuickPopupConfig` is zoomed out and disappears.

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

### Api (see wiki)

See more in wiki (continuous improvement)

**Link👉**[WIKI](https://github.com/razerdp/BasePopup/wiki)

<br>


### Update log ([Historical update](https://github.com/razerdp/BasePopup/blob/master/UpdateLog.md))

* **【Candy】2.1.8**
  * **【Candy】2.1.8-prerelease**(2019/01/23)
    * Repair of horizontal screen incompatibility problems
  * **【Candy】2.1.8-beta7**(2019/01/22)
    * Beta3 and beta4 and beta5 and beta6 were deleted~
    * Fix beta2's problem with focusable, remove useless code
    * Fix the problem that the constructor width is not valid
    * **Support for background masks without intercepting events. That black technology has changed to a more friendly way!**
  * **【Candy】2.1.8-beta2**(2019/01/22)
    * Fix the problem that the virtual button is displayed together when the popup is displayed.**This function is currently tested, please be sure to feedback to candy if you have any questions.**
      * fixed  [**#141**](https://github.com/razerdp/BasePopup/issues/120)、[**#59**](https://github.com/razerdp/BasePopup/issues/141)
    * QuickPopup adds `dismissOnOutSideTouch()` method
  * **【Candy】2.1.8-beta1**(2019/01/21)
    * Fix the problem that the virtual button is displayed together when the popup is displayed.**This function is currently tested, please be sure to feedback to candy if you have any questions.**
      * fixed  [**#120**](https://github.com/razerdp/BasePopup/issues/120)、[**#59**](https://github.com/razerdp/BasePopup/issues/59)
  * **【Candy】2.1.8-alpha2**(2019/01/18)
    * Optimize QuickPopupBuilder, see more in[**Wiki**](https://github.com/razerdp/BasePopup/wiki/QuickPopupBuilder)
  * **【Candy】2.1.8-alpha**(2019/01/17)
    * Optimize for problems with [**#138**](https://github.com/razerdp/BasePopup/issues/138)
    * Fix the call order of `setAlignBackgroundGravity()` and `setAlignBackground()`

* **【Release】2.1.7**(2019/01/16)
  * Fixed an issue where `onAnchorTop()` or `onAnchorBottom()` was called multiple times during `setAutoLocatePopup(true)`
  * Fixing 'setAllowInterceptTouchEvent(false)`, the problem of not being able to locate anchorView due to default restrictions
  * Optimize the soft keyboard default offset calculation method
  * Optimize keyboard height calculation method
  * Thanks to [**@ParfoisMeng**] (https://github.com/ParfoisMeng) for finding the soft keyboard offset problem and submitting [**PR#130**] (https://github.com/razerdp/ BasePopup/pull/130)
  * 发布2.1.7 release

* **【Candy】2.1.7-beta**(2019/01/10~2019/01/13)
  * Fixing 'setAllowInterceptTouchEvent(false)`, the problem of not being able to locate anchorView due to default restrictions
  * Optimize the soft keyboard default offset calculation method
  * Optimize keyboard height calculation method

* **【Release】2.1.6**（2019/01/08）
  * Fix the problem with the `preMeasure` method error
  * Fix problems displayed under wrap_content

* **【Candy】2.1.6-alpha2**（2019/01/03）
  * Fix the problem with the `preMeasure` method error
    * Fixed [**#125**](https://github.com/razerdp/BasePopup/issues/125)

* **【Candy】2.1.6-alpha**（2019/01/03）
  * Fix problems displayed under wrap_content

* **【Release】2.1.5**(2019/01/02)
  * **Happy New Year**
  * 2.1.5 As scheduled for the New Year, the changes are as follows:
    * Optimized method for judging whether a virtual button has been displayed
    * Use another method to determine whether the full screen contains virtual buttons
      * >Method Source: [**Nuggets**] (https://juejin.im/post/5bb5c4e75188255c72285b54)
    * Fix `showPopupWindow(anchorview)` at the same time `clipToScreen(true)`, it is not possible to fully display the view with height match_parent
    * Add the `setAlignBackgroundGravity()` method
      * <img src="https://github.com/razerdp/Pics/blob/master/BasePopup/wiki/align/alignbg_plus.gif" height="360"/>
    * Add `update(int width ,int height)` method
    * Fix the problem that the constructor width is not valid.Add `setWidth` and `setHeight` method
    * The constructor adds lazy loading parameters, if your Popup needs lazy loading, pass in the constructor where true
      * If you use lazy loading, you need to call the `delayInit()` method to initialize the BasePopup.

<br>

### Demo preview

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


### Coffee me

| Wechat | Ali-pay |
| ---- | ---- |
| ![](https://github.com/razerdp/FriendCircle/blob/master/wechat.png)      | ![](https://github.com/razerdp/FriendCircle/blob/master/alipay.png) |

<br>

### Q&A

### More Q&A：[**WIKI#Q&A**](https://github.com/razerdp/BasePopup/wiki/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98)

#### Q：How to cancel the default background color

A：Call [**setBackgroundColor**](https://github.com/razerdp/BasePopup/wiki/API#setbackgroundcolorint-color)(Color.TRANSPARENT) or [**setBackground**](https://github.com /razerdp/BasePopup/wiki/API#setbackgroundint-drawableids)(0)

<br>

***

<br>

#### Q：How to perform no animation when dismiss()

A：Call `dismiss(false)` or `dismissWithOutAnimate()`

<br>

***

<br>

#### Q：How to prevent popupwindow from dismissing when click on the popupwindow background

A：Call [**setAllowDismissWhenTouchOutside**](https://github.com/razerdp/BasePopup/wiki/API#setallowdismisswhentouchoutsideboolean-dismisswhentouchoutside)(false)

<br>

***

<br>

#### Q：Why can't I pop up in the Service?

A：PopupWindow needs windowToken, so the ApplicationContext or Service can't be popped up. It is recommended to pop the popupwindow by event notification to the top of the stack.

<br>

***

<br>

#### Q：Why is the EditText inside PopupWindow not available for pasting?

>ISSUE REF：[**#140**](https://github.com/razerdp/BasePopup/issues/140)

>Google Issue Tracker：[**#36984016**](https://issuetracker.google.com/issues/36984016)

A：The View in PopupWindow can't get WindowToken, and the paste function is also a PopupWindow. Its display must require WindowToken, so it can't be pasted.

<br>

***

<br>

#### Q：How to prevent PopupWindow for overlaying the status bar

A：Set [**setPopupWindowFullScreen**](https://github.com/razerdp/BasePopup/wiki/API#setpopupwindowfullscreenboolean-isfullscreen)(false)

<br>

***

<br>

#### Q：How to prevent dismiss under backpress

A：Set [**setBackPressEnable**](https://github.com/razerdp/BasePopup/wiki/API#setBackPressEnableboolean-backPressEnable)(false)

<br>

***

<br>


#### Q：The difference between the root layout height `match_parent` and `wrap_content`

A：When the root layout is match_parent, basepopup will do some difference handling.
<br>
When you set [**setClipToScreen(true)**](https://github.com/razerdp/BasePopup/wiki/API#setcliptoscreenboolean-cliptoscreen), if your root layout is `match_parent`, then it means The maximum height of your layout is the screen height. If your root layout is `wrap_content`, the maximum height may be higher than the screen height.
<br>
**Such as full screen listview in demo**

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

**Pay attention to the difference between the bottom of the listview of the two images, where the bottom of the `wrap_content` has exceeded the bottom of the screen and cannot be displayed completely.**

<br>

***

<br>

### License

[Apache-2.0](./LICENSE)
