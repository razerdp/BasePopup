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

**Candy dev log see dev branch: [**branch-dev**](https://github.com/razerdp/BasePopup/tree/dev)

* **【Release】2.2.3**(2020/05/07)
  * We have fixed the 2.2.2 series of problems and added some new features.
  * **New features/methods added:**
    * Added `setPopupGravityMode()`: You can set BasePopup alignment individually instead of always bringing the gravity params.
    * Added `OnPopupWindowShowListener` interface: Notify this interface after BasePopup is displayed, when this method is called back, it means the popup window has been completed and ui has been displayed on the screen.
    * Added `bindLifecycleOwner()` method: You can now freely bind your LifecycleOwner.
    * Added `onPreShow()` method: call this method before BasePopup popup, if it returns **false**, it won't show.
    * Added `onShowing()` method: Same as `OnPopupWindowShowListener`, but this is the protect method in BasePopup.
    * Added `onPopupLayout()` method: If the popup is associated with an anchor View, the method will be called when BasePopup is in layout(). And return the position of BasePopup on the screen and the position of the anchor View on the screen respectively.
    * Added `computeGravity()`: Equipped with `onPopupLayout()` callback to calculate the BasePopup center point's orientation at the anchor View.
  * **Method of abandonment and replacement:**
    * `BasePopupWindow#dismissWithOutAnimate()` is Deprecated，please use **dismiss(false)** instead.
    * `BasePopupWindow#setPopupWindowFullScreen()` is Deprecated，please use **setOverlayStatusbar()** instead.
    * `QuickPopupConfig#dismissOnOutSideTouch()` is Deprecated，please use **outSideDismiss()** instead.
    * `QuickPopupConfig#allowInterceptTouchEvent()` is Deprecated，please use **outSideTouchable()** instead.
  * **Optimisation:**
    * Optimize the query of the DecorView, the original logic will cache the query of the DecorView, but the display error may be caused by the destruction or change of the DecorView host.
    * The lowest supported version is down to Api 16.
    * Abandon the reflective WindowManager approach, take the ContextWrapper proxy, no longer Worried about experiencing a black ash list block ~ thanks [@xchengDroid](https://github.com/xchengDroid).
  * **Bug fixes:**
    * Fix offsets in event delivery when overriding the status bar
    * Fix `isShowing()` with NullPointerException.[#267](https://github.com/razerdp/BasePopup/issues/267)
    * Fix position error when associated with Anchor in case of `setOverlayStatusbar(false)`.
    * Fix memory leaks that **might** exist due to some references not being empty (no leaks were actually detected).
    * Fix the problem that the input method of Activity popup is displayed under BasePopup when BasePopup is showing.
    * Fix the problem of error in judging full-screen Activity.
    * Fix the missing QuickPopupConfig configuration issue.
    * Fix the problem of invoking dismiss() directly when there is no popup window, and then call showPopupWindow() for the first time when it is invalid.
    * fixed issue:[#224](https://github.com/razerdp/BasePopup/issues/224)

* **【Release】2.2.2.2**(2020/03/01)
  * Fix a serious problem that may cause a crash
    * Reappearance: Finishing the activity when the dismiss animation is not completed will cause the null pointer to crash
    * This issue cannot be **try & catch** in previous versions
  * Fixed an issue that did not force dismiss when finished
  * Fixed some flags wrong
  * Fix the problem that the position of outsideTouch deviates in non-full screen
  * **I feel very sorry, I did not find such errors for my own reasons. After receiving the feedback, I immediately checked all similar places and fixed them one by one. This time the reconfiguration cycle is longer, involving more plates, and the test cycle It is also long, but there are still omissions due to limited personal energy. I hope that I can get your support and try to report any problems during the Candy test period to reduce such problems.**

* **【Release】2.2.2.1**(2020/02/26)
  * Fix the problem that the input method can not pop up again automatically.

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
