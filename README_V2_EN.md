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
        				<a href="https://img.shields.io/badge/Api-19%2B-green.svg">
        					<img src="https://img.shields.io/badge/Api-19%2B-green.svg"/>
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
   - [BasePopup manual](https://www.kancloud.cn/razerdp/basepopup/content)
 - [Api(Wiki)](#api)
 - [ChangeLog](#changelog-historical-update)
   - [Historical update](./UpdateLog.md)
 - [Preview](#Preview)
   - [**Demo apk(pass:123)**](https://www.pgyer.com/basepopup)
 - [QA](#QA)
 - [LICENSE](#license)

<br>
<br>

### Feature

 - Simple and precise control of display position with [**Gravity**](https://github.com/razerdp/BasePopup/wiki/API#setpopupgravityint-popupgravity) and [**offset**](https://github.com/razerdp/BasePopup/wiki/API#setoffsetxint-offsetx).
 - Basepopup is an abstract class with almost no constraints on subclasses. You can customize your PopupWindow just like a custom View.
 - Darkening the background, [**changing the background color**](https://github.com/razerdp/BasePopup/wiki/API#setbackgroundcolorint-color) will be very easy.
 - [**Blur background**](https://github.com/razerdp/BasePopup/wiki/API#setblurbackgroundenableboolean-blurbackgroundenable) or [**partial blur**](https://github.com/razerdp/BasePopup/wiki/API#setbluroptionpopupbluroption-option) is also very easy.
 - [**Backpress control**](https://github.com/razerdp/BasePopup/wiki/API#setbackpressenableboolean-backpressenable) , [**click outside to dismiss**](https://github.com/razerdp/BasePopup/wiki/API#setallowdismisswhentouchoutsideboolean-dismisswhentouchoutside) , [**outside touch event**](https://github.com/razerdp/BasePopup/wiki/API#setallowintercepttoucheventboolean-touchable) all separation，no longer have to worry about my PopupWindow various key events problems.
   - We also support event delivery at the same time.
 - Support [**linkTo**](https://github.com/razerdp/BasePopup/wiki/API#linktoview-anchorview) anchorview.
 - Support for chained calls. For a simple scene,try [**QuickPopupBuilder**](https://github.com/razerdp/BasePopup/wiki/API#QuickPopupBuilder)

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

Reference document(CN)：[**BasePopup manual**](https://www.kancloud.cn/razerdp/basepopup/content)

<br>

### Api

**Link👉**[WIKI](https://github.com/razerdp/BasePopup/wiki)

<br>


### ChangeLog ([Historical update](https://github.com/razerdp/BasePopup/blob/master/UpdateLog.md))

* **【Release】2.2.2.1**(2020/02/26)
  * Fix the problem that the input method can not pop up again automatically.

* **【Release】2.2.2**(2020/02/24)
  * Release 2.2.2, ** This version is a refactored version, please read the change log carefully **
  * **New features:**
    * Added `BaseLazyPopupWindow`, lazy-loaded PopupWindow only needs to extend this, the old version of` delayInit () `has been removed in this version.
    * Added BasePopup queue, optimized for outSideTouch.
    * Adapt to Android 10, solve the problem of Android 10 black / gray list.
    * Adapted to `match_parent`, now` match_parent` will fill the remaining space.
    * The main library is completely migrated to AndroidX, removed the BasePopup extension component library, and it is recommended that you adapt to AndroidX as soon as possible.
    * Solve memory leaks and increase lifetime monitoring.
    * Added support for `Dialog` /` Fragment` / `DialogFragment`.
    * Add `setFitSize ()` method.
      * `setFitSize ()`: BasePopup will adjust the size of Popup for the remaining space, so the actual display may be too small.
    * Optimize QuickPopupBuilder.
    * The mask layer is returned to the system hosting, and the problem that the full screen cannot be covered.
    * Optimized the problem that popup window could not show in onCreate ().
      * Take the suggestion of [# 263](https://github.com/razerdp/BasePopup/issues/263), thanks for the suggestion of [@xchengDroid](https://github.com/xchengDroid).
    * Added `onLogInternal ()` method, where you can print the log during BasePopupWindow execution.
    * Added `onViewCreated ()` method, you can operate ContentView here, or use ButterKnife for injection.
  * **Simplify:**
    * Remove the onAnchorTop / onAnchorBottom method, and it will be replaced by another method.
    * Remove the `limitScreen ()` method.
    * Removed the extension components, now the main body supports AndroidX, and no longer supports the Support package.
  * **bug fixed：**
    * fixed [#184](https://github.com/razerdp/BasePopup/issues/184)、[#207](https://github.com/razerdp/BasePopup/issues/207)、[#210](https://github.com/razerdp/BasePopup/issues/210)
    * fixed [#213](https://github.com/razerdp/BasePopup/issues/213)、[#226](https://github.com/razerdp/BasePopup/issues/226)、[#232](https://github.com/razerdp/BasePopup/issues/232)
    * fixed [#236](https://github.com/razerdp/BasePopup/issues/236)、[#238](https://github.com/razerdp/BasePopup/issues/238)、[#240](https://github.com/razerdp/BasePopup/issues/240)
    * fixed [#242](https://github.com/razerdp/BasePopup/issues/242)、[#244](https://github.com/razerdp/BasePopup/issues/244)、[#247](https://github.com/razerdp/BasePopup/issues/247)
    * fixed [#248](https://github.com/razerdp/BasePopup/issues/248)、[#249](https://github.com/razerdp/BasePopup/issues/249)、[#260](https://github.com/razerdp/BasePopup/issues/260)
    * fixed [#262](https://github.com/razerdp/BasePopup/issues/262)、[#263](https://github.com/razerdp/BasePopup/issues/263)


* **【Release】2.2.1**(2019/06/24)
  * Support for showing popupwindow in Service or non-ActivityContext.
  * Refactoring PopupUiUtils to optimize the screen width and height algorithm.
    * fixed [**#186**](https://github.com/razerdp/BasePopup/issues/186)、[**#167**](https://github.com/razerdp/BasePopup/issues/167).
    * fixed [**#188**](https://github.com/razerdp/BasePopup/issues/188)(not perfect).
  * Modify and optimize keyboard displacement logic.
  * Optimize the determination of the click range in full screen state,fixed [**#200**](https://github.com/razerdp/BasePopup/issues/200).

<br>

### Preview

<br>

#### For more examples, please download Demo: [**Demo (pass:123)**](https://www.pgyer.com/basepopup)

<br>

|  |  |  |
| - | - | - |
| ![](https://github.com/razerdp/Pics/blob/master/BasePopup/demo_1.gif) | ![](https://github.com/razerdp/Pics/blob/master/BasePopup/new_demo_2.gif) | ![](https://github.com/razerdp/Pics/blob/master/BasePopup/demo_3.gif) |
| ![](https://github.com/razerdp/Pics/blob/master/BasePopup/demo_4.gif) | ![](https://github.com/razerdp/Pics/blob/master/BasePopup/demo_5.gif) | ![](https://github.com/razerdp/Pics/blob/master/BasePopup/demo_6.gif) |

<br>

### QA

Please refer to [**Basepopup manual: Frequently QA**](https://www.kancloud.cn/razerdp/basepopup/1277047)

<br>

### License

[Apache-2.0](./LICENSE)
