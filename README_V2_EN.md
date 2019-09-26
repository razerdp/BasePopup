[**Chinese**](./README.md) | **English**

<p align="center"><img src="./img/logo.png" alt="Logo load failed" height="360"/></p>
<h2 align="center">BasePopup - A powerful and convenient PopupWindow library for Android</h2>
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
			<td rowspan="3" align="center">Compat library</td>
			<td align="center"></td>
			<td align="center">support(Deprecated)</td>
			<td align="center">lifecycle(Deprecated)</td>
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

### Guide

 - [Feature](#Feature)
 - [Precautions](#Precautions)
 - [Download](#Download)
 - [Quick start](#quick-start)
 - [Api(Wiki)](#api)
 - [ChangeLog](#changelog-historical-update)
   - [Historical update](./UpdateLog.md)
 - [Preview](#Preview)
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
  - **Please pay attention on the dependence version, the Release version is a stable version, and Candy is a preview version.**
    - Release version: Generally published to Release after repeated verification of the Candy version. If you have higher stability requirements, please use the Release version.
    - Candy version: new features, issue fixes will be published  to the Candy version, Candy version is updated more frequently, but usually has new features, if you like to test new features and stability requirements are not high, please use the Candy version.
    - **Switching between Release and Candy versions may cause Build to fail. At this time, you can clean Project.**
  - **If you are a previous 1.x user and want to update to 2.x now, please check before the update: [1.x migration to 2.x help documentation](https://github.com/razerdp/BasePopup/blob/master/1.x%E8%BF%81%E7%A7%BB2.x%E5%B8%AE%E5%8A%A9%E6%96%87%E6%A1%A3.md)**

<br>

### Download

Please replace <b>{$latestVersion}</b> with the version shown in the Jcenter tab above.

**Based on version 2.2.2, BasePopup will no longer support for SupportLibrary**, and BasePopup recommends that you migrate to AndroidX as soon as possible.

 - **Release：**
   - BasePopup main library(**Required**)：**`implementation 'com.github.razerdp:BasePopup:{$latestVersion}'`**
   - BasePopup Support library(**optional**)：**`implementation 'com.github.razerdp:BasePopup-compat-support:{$latestVersion}'`**
   - Lifecycle support library(**optional**)：**`implementation 'com.github.razerdp:BasePopup-compat-lifecycle:{$latestVersion}'`**
   - Androidx support library(optional，**Cannot coexist with the above two support libraries**)：**`implementation 'com.github.razerdp:BasePopup-compat-androidx:{$latestVersion}'`**

 - **Candy**
    - BasePopup main library(**Required**)：**`implementation 'com.github.razerdp:BasePopup_Candy:{$latestVersion}'`**
    - BasePopup Support library(**optional**)：**`implementation 'com.github.razerdp:BasePopup_Candy-compat-support:{$latestVersion}'`**
    - Lifecycle support library(**optional**)：**`implementation 'com.github.razerdp:BasePopup_Candy-compat-lifecycle:{$latestVersion}'`**
    - Androidx support library(optional，**Cannot coexist with the above two support libraries**)：**`implementation 'com.github.razerdp:BasePopup_Candy-compat-androidx:{$latestVersion}'`**

<br>

### Quick start

Reference document(CN)：[**BasePopup manual**](https://www.kancloud.cn/razerdp/basepopup/content)

<br>

### Api

**Link👉**[WIKI](https://github.com/razerdp/BasePopup/wiki)

<br>


### ChangeLog ([Historical update](https://github.com/razerdp/BasePopup/blob/master/UpdateLog.md))

* **BasePopup is being refactored recently, designed to make the code easier to read, so recent updates may be slower and the new version will be handled uniformly for issues**

* **【Candy】2.2.2**
    * **【Candy】190704**
      * Fix time issues with AnimatorSet [**#203**](https://github.com/razerdp/BasePopup/issues/203)
    * **【Candy】190722**
      * Rollback [#188](https://github.com/razerdp/BasePopup/issues/188)
      * After checking, [#188](https://github.com/razerdp/BasePopup/issues/188) brings more serious problems, it is recommended to upgrade to this version.
    * **【Candy】190816**
      * Considering that many users suggest that the full screen will not fill the mask, so add the `setMaskLayoutWidth` and `setMaskLayoutHeight` methods, which allow you to customize the height of the mask.
      * Start refactoring BasePopupWindow, this refactoring will solve the remaining problems while reducing redundant code and improve code readability
    * **【Candy】190904**
      * Optimize screen width and height acquisition method
      * Modify the supporter name to component
    * **【Candy】190912**
      * Refactoring keyboard logic, now keyboard alignment supports selection mode~
      * Please refer to the documentation for related parameters [**Keyboard (Input Method)**](https://www.kancloud.cn/razerdp/basepopup/1277045)
    * **2019/09/20**
      * Refactoring Demo
    * **2019/09/22**
      * Increase input method Demo
      * Demo support update
      * Demo adds issue fix test case

* **【Release】2.2.1**(2019/06/24)
  * Support for showing popupwindow in Service or non-ActivityContext
  * Refactoring PopupUiUtils to optimize the screen width and height algorithm
    * fixed [**#186**](https://github.com/razerdp/BasePopup/issues/186)、[**#167**](https://github.com/razerdp/BasePopup/issues/167)
    * fixed [**#188**](https://github.com/razerdp/BasePopup/issues/188)(not perfect)
  * Modify and optimize keyboard displacement logic
  * Optimize the determination of the click range in full screen state,fixed [**#200**](https://github.com/razerdp/BasePopup/issues/200)

* **【Release】2.2.0**(2019/05/15)
  * The release version 2.2.0 was upgraded, this version is a refactored version~
  * Optimize input method alignment logic
  * **Refactoring blur implementation:**
    * After testing, the 720p mobile phone has a full-screen blur time average between **6ms~16ms**
    * Increase the blurriness of the default parameters
    * Blur progressive time follows BasePopup's animation time
    * Fix the situation of blur invalidation
  * **Measurement/Layout:**
    * Reconstruction measurement implementation：
      * Now in the case of `clipToScreen`, PopupDecor will be re-measured according to the remaining space to ensure the complete display of Popup. If you need to keep the original measured value, please call `keepSize(true)`
      * Refactor layout implementation, optimized for **outSideTouch**
      * Adapt screen rotation,fix [#180](https://github.com/razerdp/BasePopup/issues/180)
      * Use a flag instead of a variety of boolean
      * Reduce redundant code
  * **Optimization:**
    * Add the GravityMode parameter, now allows you to configure the reference mode of `PopupGravity`~
      * **RELATIVE_TO_ANCHOR**：Default mode，Use Anchor as a reference to specify the orientation of PopupWindow displayed in Anchor.
      * **ALIGN_TO_ANCHOR_SIDE**：Align mode，Specify the edge of the PopupWindow and which side of the Anchor is aligned with the side of the Anchor.
    * Add the `minWidth()`/`minHeight()` apis
    * Add the `maxWidth()`/`maxHeight()` apis.
    * Fix measurement differences for `match_parent` and `wrap_content`
    * Deprecated some apis：
      * ~~setAllowDismissWhenTouchOutside~~ -> **setOutSideDismiss**
      * ~~setAllowInterceptTouchEvent~~ -> **setOutSideTouchable**
    * Add `setBackgroundView(View)` api，Now the background control of BasePopup can be customized by you~ Of course, the background animation control method of PopupWindow still takes effect.
  * **Function split:**
    * Now BasePopup will split the package.
    * The source project will only adapt to the native Android without any dependencies.
    * If you need other adaptations, please rely on the following modules or modules:
      * If you need support for the `support` library, such as DialogFragment support, please implementation
        * `implementation 'com.github.razerdp:BasePopup-compat-support:{$latestVersion}'`
      * If you need support for the `lifecycle` library, such as automatic release or dismiss in destroy, etc., please implementation
        * `implementation 'com.github.razerdp:BasePopup-compat-lifecycle:{$latestVersion}'`
      * If you need support for the `androidX` library, please implementation
        * `implementation 'com.github.razerdp:BasePopup-compat-androidx:{$latestVersion}'`
      * **Attention!，If you use the androidX support library,please don't dependence the other two support library, otherwise it will conflict**
  * **Bug fixed：**
    * fix [#171](https://github.com/razerdp/BasePopup/issues/171)、[#181](https://github.com/razerdp/BasePopup/issues/181)、[#182](https://github.com/razerdp/BasePopup/issues/182)、[#183](https://github.com/razerdp/BasePopup/issues/183)
    * fix [#180](https://github.com/razerdp/BasePopup/issues/180)
    * fixed [#164](https://github.com/razerdp/BasePopup/issues/164)
  * **Other：**
    * Add 996 license

<br>

### Preview

<br>

#### For more examples, please download Demo: [**Demo**](https://fir.im/pfc9)

<br>

|  |  |
| - | - |
| ![](https://github.com/razerdp/Pics/blob/master/BasePopup/demo_1.gif) | ![](https://github.com/razerdp/Pics/blob/master/BasePopup/demo_2.gif) |
| ![](https://github.com/razerdp/Pics/blob/master/BasePopup/demo_3.gif) | ![](https://github.com/razerdp/Pics/blob/master/BasePopup/demo_4.gif) |
| ![](https://github.com/razerdp/Pics/blob/master/BasePopup/demo_5.gif) | Coming soon |

<br>

### QA

Please refer to [**Basepopup manual: Frequently QA**](https://www.kancloud.cn/razerdp/basepopup/1277047)

<br>

### License

[Apache-2.0](./LICENSE)
