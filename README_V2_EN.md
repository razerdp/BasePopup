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

* **【Release】2.2.7**(2020/08/21)
  * In 2.2.7, we refactored Layout's logic, fixed existing issues, and tested all test cases in landscape and portrait in the demo.
  * **Bugs fixed**
    * Popup in non-full-screen Dialog location display still has anomalies, after troubleshooting found that version 2.2.6 from `getLocationOnScreen` to `getLocationInWindow` reasons, after discussion in the group, decided to take the original program, a unified Screen as a reference to solve the Window location is not Popups are not displayed correctly due to the same problem.
    * The offset in the RTL layout is referenced to the View in 2.2.6, but should actually be referenced to the system coordinate system, as modified in 2.2.7.
    * Blurred background in `overlayStatusBar(false)` still contains the status bar, causing the blurred image to be wrong, fixed in 2.2.7.
    * When the ContentView contains Margin, clicks within the range of Margin are not responsive, we have adapted it for this problem and now it works.
    * ContentView will have a crop problem if given `width>0` or `height>0` when it contains Margin.This is because its parent class, DecorView, is set to the same size. 2.2.7 takes margin into account and adapts the size of DecorView accordingly.
    * When ContentView is given `width>0` or `height>0` and no AnchorView is associated with it, there will be a crop problem, the crop size is just the size of NavigationBar. 2.2.7 fixes this problem.

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
