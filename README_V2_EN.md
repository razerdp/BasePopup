[**Chinese**](./README.md) | **English**

<p align="center"><img src="./img/logo.png" alt="Logo load failed" height="360"/></p>
<h2 align="center">BasePopup - A powerful and convenient PopupWindow library for Android</h2>
<div align="center">

<table align="center">
        <tr>
            <th align="center" width="9999">Release</th>
            <th align="center" width="9999">Snapshot</th>
            <th align="center" width="9999">License</th>
			<th align="center" width="9999">Api</th>
			<th align="center" width="9999">Author</th>
        </tr>
           <tr>
                    <td align="center">
        				<a href ="https://search.maven.org/artifact/io.github.razerdp/BasePopup">
        					<img src="https://img.shields.io/maven-central/v/io.github.razerdp/BasePopup"/>
        				</a>
        			</td>
        			<td align="center">
        				<a href = "https://s01.oss.sonatype.org/content/repositories/snapshots/io/github/razerdp/BasePopup/">
        					<img src="https://img.shields.io/nexus/s/io.github.razerdp/BasePopup?server=https%3A%2F%2Fs01.oss.sonatype.org%2F"/>
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
 - [Document](#Document)
   - [BasePopup manual](https://www.yuque.com/razerdp/basepopup)
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
  - **Please pay attention on the dependence version, the Release version is a stable version, and Snapshot is a preview version.**
    - Release version: Generally published to Release after repeated verification of the Snapshot version. If you have higher stability requirements, please use the Release version.
    - Snapshot version: new features, issue fixes will be published  to the Snapshot version, Snapshot version is updated more frequently, but usually has new features, if you like to test new features and stability requirements are not high, please use the Snapshot version.
    - **Switching between Release and Snapshot versions may cause Build to fail. At this time, you can clean Project.**

<br>

### Download

#### Gradle settings

```
allprojects {
    repositories {
        mavenCentral() // release dependency repository (available by default configuration after 4.1)
        maven { url 'https://s01.oss.sonatype.org/content/repositories/snapshots' } // snapshot repository (configure this maven if you need snapshot dependencies)
        google()
    }
}
```

#### Dependency

Please replace **{$latestVersion}** with the version shown in the Jcenter tab above.

**Since JCenter is no longer in service, starting with version 2.3, this library will be migrated to Maven with a new dependency groupId of [io.github.razerdp]**

 - **Release：** `implementation 'io.github.razerdp:BasePopup:{$latestVersion}'`
    - e.g.  `implementation 'io.github.razerdp:BasePopup:2.3.0'`
 - **Snapshot：** `implementation 'io.github.razerdp:BasePopup:{$latestVersion_for_snapshot}-SNAPSHOT'`
    - e.g. `implementation 'io.github.razerdp:BasePopup:2.3.2-SNAPSHOT'`

<br>

### Document

Reference document(CN)：[**BasePopup manual**](https://www.yuque.com/razerdp/basepopup)

<br>

### ChangeLog ([Historical update](https://www.yuque.com/razerdp/basepopup/uyrsxx))

**Snapshot dev log see dev branch:** [**branch-dev**](https://github.com/razerdp/BasePopup/tree/dev)

* **【Release】3.1.0** (2021/05/28)
    * This update is a destructive update, so if you are still on version 2.x before updating, please make sure you read the following two notes.
        * As JCenter is no longer in service, it will be migrated to MavenCentral starting with version 3.0. The new dependency groupId is `io.github.razerdp`, see [Download](#Download) above for details.
        * Version 3.0 is a destructive update that affects all BasePopupWindow subclasses, but the changes will not be extensive, see [Note on destructive update to BasePopup 3.0](./Update_3.0.md)
    * 【Optimise】
        * Add api: `hideKeyboardOnShow(boolean)`, whether to put away the keyboard when BasePopup is displayed, default is put away
            * demo adds issue 369 test cases
            * fixed [#369](https://github.com/razerdp/BasePopup/issues/369)
        * The mask now only performs a dismiss when it is clicked (previously it only determined if action_up was inside the mask)
            * `onOutSideTouch` adds the `isMaskPressed` param
    * 【Api Modification】
        * Delete **BaseLazyPopupWindow**: henceforth there is no need to distinguish between lazy loading and normal BasePopupWindow, and the reliance on BasePopupWindow is uniform
        * Remove the method `onCreateConstructor`: this method is actually for the BaseLazyPopupWindow and is not needed now that there is no more BaseLazyPopupWindow.
        * Delete method `onCreateContentView`: the deletion of this method will affect all BasePopupWindow subclasses and you will need to change them manually
            * This method will be replaced by `setContentView(@LayoutRes int layoutResID)` or `setContentView(final View view)`, which you will need to modify to use.
            * If you use `setContentView(final View view)`, we still recommend that you use setContentView(createPopupById(layoutResID)) so that we parse to the correct xml configuration.
    * 【Bug fixed】
        * Fix Popup size not updating after screen rotation
        * Fix `update(float,float)` not working

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

<p align="center">
  Visit Count（from 2020/08/21）<br>
  <img src="https://profile-counter.glitch.me/razerdp-basepopup-en/count.svg" />
</p>
