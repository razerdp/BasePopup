[**Chinese**](./README.md) | **English**

<p align="center"><img src="./img/logo.png" alt="Logo load failed" height="360"/></p>
<h2 align="center">BasePopup - A powerful and convenient PopupWindow library for Android</h2>
<div align="center">

<table align="center">
        <tr>
            <th align="center" width="9999">Release</th>
            <th align="center" width="9999">Candy</th>
            <th align="center" width="9999">License</th>
			<th align="center" width="9999">Api</th>
			<th align="center" width="9999">Author</th>
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
        				<a href="https://img.shields.io/badge/Api-16%2B-green.svg">
        					<img src="https://img.shields.io/badge/Api-16%2B-green.svg"/>
        				</a>
        			</td>
        			<td align="center">
        				<a href = "https://github.com/razerdp">
        					<img src="https://img.shields.io/badge/Author-razerdp-blue.svg"/>
        				</a>
        			</td>
        		</tr>
</table>


</div>

---

### Guide

 - [Feature](#Feature)
 - [Precautions](#Precautions)
 - [Download](#Download)
 - [Quick start](#quick-start)
   - [BasePopup manual](https://www.yuque.com/razerdp/basepopup)
 - [Api(Wiki)](#api)
 - [ChangeLog](#changelog-historical-update)
   - [Historical update](https://www.yuque.com/razerdp/basepopup/uyrsxx)
 - [Preview](#Preview)
   - [**Demo apk(pass:123)**](https://www.pgyer.com/basepopup)
    <img src="./img/download.png"  width="256"/>
 - [QA](#QA)
 - [LICENSE](#license)

<br>
<br>

### Feature

 - This library acts as a base class and does not interfere with your implementation, don't worry about being restricted by Api when implementing certain methods.
 - No need to worry about how to calculate offset for position control, just simply set [**Gravity**](https://www.yuque.com/razerdp/basepopup/qnu3qd), you can control your popups as you wish.
 - Whether [**Animation**](https://www.yuque.com/razerdp/basepopup/mg3bcw#onCreateShowAnimation) or [**Animator**](https://www.yuque.com/razerdp/basepopup/mg3bcw#onCreateShowAnimator), just write the animation as you normally would. You can complete the dynamic design of Popup, do not need xml, do not care about other compatibility issues.
 - Separation of background from content, support for [**background blurring**](https://www.yuque.com/razerdp/basepopup/udccdq#12bedc89),[**background color Settings**](https://www.yuque.com/razerdp/basepopup/gscx3g#aiRz7),or even [**change the background to your View**](https://www.yuque.com/razerdp/basepopup/gscx3g#e96cp),all of which can be done with a simple setup, with the subject isolated from the background, without worrying about the key event's issues.
 - BasePopup can help you to solve the problem of touch events in Popup. Pass-through, click-through and disappear or not are all just a matter of configuration.
 - PopupWindow automatically anchors the AnchorView, slide to follow it off screen. AnchorView disappears without complex logic setup, just by [**linkTo**](https://www.yuque.com/razerdp/basepopup/api) method by telling BasePopup to do it for you.
 - Simple PopupWindow doesn't want to create a new class and wants to have chain calls? No problem, [**QuickPopupBuilder**](https://www.yuque.com/razerdp/basepopup/ob329t) was created for this purpose, and believe that You'll love it more and more.

<br>

### Precautions

  - **Please be sure to read this README carefully. Please check the changelog for each version upgrade, which can reduce unnecessary detours for you.**
  - **Support is no longer supported in version 2.2.2, it is recommended that you upgrade to AndroidX as soon as possible.**
  - **Please pay attention on the dependence version, the Release version is a stable version, and Candy is a preview version.**
    - Release version: Generally published to Release after repeated verification of the Candy version. If you have higher stability requirements, please use the Release version.
    - Candy version: new features, issue fixes will be published  to the Candy version, Candy version is updated more frequently, but usually has new features, if you like to test new features and stability requirements are not high, please use the Candy version.
    - **Switching between Release and Candy versions may cause Build to fail. At this time, you can clean Project.**

<br>

### Download

Please replace **{$latestVersion}** with the version shown in the Jcenter tab above.

**Support is no longer supported in version 2.2.2, it is recommended that you upgrade to AndroidX as soon as possible.**

 - **Release：** `implementation 'com.github.razerdp:BasePopup:{$latestVersion}'`
 - **Candy：** `implementation 'com.github.razerdp:BasePopup_Candy:{$latestVersion}'`

<br>

### Quick start

Reference document(CN)：[**BasePopup manual**](https://www.yuque.com/razerdp/basepopup)

<br>

### Api

**Link👉**[WIKI](https://github.com/razerdp/BasePopup/wiki)

<br>


### ChangeLog ([Historical update](https://www.yuque.com/razerdp/basepopup/uyrsxx))

**Candy dev log see dev branch:** [**branch-dev**](https://github.com/razerdp/BasePopup/tree/dev)

* **【Release】2.2.4.1**(2020/07/19)
  * 【Note】：In **2.2.4** we found an error in `setMaxWidth/setMaxHeight`, so we urgently release **2.2.4.1**, please update to **2.2.4.1** as soon as possible if you are using **2.2.4**.
  * In version 2.2.4, we added the awesome `AnimationHelper`, which helps with the Creating BasePopup animations will be very easy to understand, and we've added a lot of great features, too. Welcome to update to version 2.2.4~!
  * **New features/methods added:**
    * Add popup thread check: in a non-main thread we will throw an exception `CalledFromWrongThreadException`.
    * Add `OnKeyboardChangeListener`: Now you can listen to the keyboard display, close events, and the callback will return the size of the keyboard.
    * Added `KeyEventListener`: now allows you to add external event listener. issue:[#296](https://github.com/razerdp/BasePopup/issues/296)
    * Add ``setOverlayNavigationBar``: Through this method, you can make your BasePopup overlay to the navigation bar, the default is not allowed to overlay, if you really need, you can configure the overlay through this api.
      * Related discussion:[《关于MIUI小白条及类似的“全面屏手势提示线”覆盖问题描述》](https://github.com/razerdp/BasePopup/issues/307)
    * Add `setWidthAsAnchorView`: You can call this Api to determine whether the width of BasePopup is set to the width of AnchorView or not.
    * Add `setHeightAsAnchorView`: You can call this Api to determine whether the height of BasePopup is set to the height of AnchorView or not.
    * Add RTL layout support:
      * If using RTL, be sure to set `setLayoutDirection` to tell the BasePopup host the layout direction.
      * RTL仅对`showPopupWindow(View anchorView)`和`showPopupWindow(int x,int y)`有效
      * **Be sure to note that BasePopup follows the official practice of using START instead of LEFT and END instead of RIGHT when you need an RTL layout**.
    * Add `syncMaskAnimationDuration`: You can set whether the time of the mask animation is synchronized with your animation (take the longest time) or not, the default is synchronized.
    * Add AnimationHelper: We want to reduce the amount of code for animations and make it easier to create them, so we've officially released AnimationHelper in this version, and we're sure you'll love it!
      * For information on AnimationHelper, subject to space limitations, please consult the documentation:[【进阶指引-动画-AnimationHelper】](https://www.yuque.com/razerdp/basepopup/zcgtm5)
  * **Optimisation**
    * Optimized BasePopupHelper code
    * Optimize mask animation：
      * Now the fade-in and fade-out time of the mask will be synchronized with the maximum time you set for the animation. We expect the mask animation to complete at the same time as your show/exit animation, not earlier or later!
      * You can configure whether or not to synchronize via `syncMaskAnimationDuration`.
  * **Delete class/method**
    * **Please note that this time we didn't mark it as @Deprecated and just removed the code, so be sure to change it if you use these methods**
    * **Deleted class:** SimpleAnimationUtils.java，We recommend that you use [AnimationHelper](https://www.yuque.com/razerdp/basepopup/zcgtm5) instead.
    * **Deleted Method:** BasePopupWindow#getTranslateVerticalAnimation
    * **Deleted Method:** BasePopupWindow#getScaleAnimation
    * **Deleted Method:** BasePopupWindow#getDefaultScaleAnimation
    * **Deleted Method:** BasePopupWindow#getDefaultAlphaAnimation
    * **Deleted Method:** BasePopupWindow#getDefaultSlideFromBottomAnimationSet
  * **Bug fixes**
    * Fix the problem that focusable is not restored in fullscreen Activity.
    * Fix the problem of forced modification of SystemUiVisibility in full-screen Activity.
    * Fix the problem that backpressenable settings in Quickpopupbuilder don't work.[#296](https://github.com/razerdp/BasePopup/issues/296)
    * Fix the issue where EditText is blocked by keyboard in non-BasePopup.[#297](https://github.com/razerdp/BasePopup/issues/297)
    * Fix gravity overlays in lazypopups[#310](https://github.com/razerdp/BasePopup/issues/310)
    * Fix the problem of unsuccessful position acquisition due to incomplete measurement process when associating anchor View under onCreate().[#313](https://github.com/razerdp/BasePopup/issues/313)
    * Fix measurement error with max/min width/height setting
    * Fix a problem with keyboard monitoring that may occur with adjustResize softInput mode.[#315](https://github.com/razerdp/BasePopup/issues/315)
    * Fix the problem of setting softInputMode duplicate.[#314](https://github.com/razerdp/BasePopup/issues/314)

<br>

### Preview

<br>

#### For more examples, please download Demo: [**Demo (pass:123)**](https://www.pgyer.com/basepopup)

<img src="./img/download.png"  width="256"/>

<br>

|  |  |  |
| - | - | - |
| ![](https://github.com/razerdp/Pics/blob/master/BasePopup/demo_1.gif) | ![](https://github.com/razerdp/Pics/blob/master/BasePopup/new_demo_2.gif) | ![](https://github.com/razerdp/Pics/blob/master/BasePopup/demo_3.gif) |
| ![](https://github.com/razerdp/Pics/blob/master/BasePopup/demo_4.gif) | ![](https://github.com/razerdp/Pics/blob/master/BasePopup/demo_5.gif) | ![](https://github.com/razerdp/Pics/blob/master/BasePopup/demo_6.gif) |

<br>

### QA

Please refer to [**Basepopup manual: Frequently QA**](https://www.yuque.com/razerdp/basepopup/dgf6ry)

<br>

### License

[Apache-2.0](./LICENSE)
