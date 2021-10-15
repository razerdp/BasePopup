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
### About

BasePopup is a popup library that wraps and improves on the system's PopupWindow. It is a basic library class with a high degree of freedom and a rich API that allows you to do a wide range of popups very easily within the framework of BasePopup.

### Download

```groovy
// root gradle
allprojects {
    repositories {
        // release dependency repository (available after 4.1 as default configuration)
        mavenCentral()

        // snapshot repository (if you need snapshot dependency, please configure this maven)
        maven { url 'https://s01.oss.sonatype.org/content/repositories/snapshots' }
    }
}

// project dependencies
dependencies {
  implementation 'io.github.razerdp:BasePopup:3.1.8'

  // for snapshot
  // implementation 'io.github.razerdp:BasePopup:3.1.8-SNAPSHOT'
}

```

### Documentation

 - [**BasePopup Manuals**](https://www.yuque.com/razerdp/basepopup)

### Precautions

  - Switching between Release and Snapshot versions may cause the Build to fail, so you can clean the Project.
  - Version 3.0 will cause a large range of changes to users who have upgraded from version 2.x. Please be sure to read the upgrade instructions:[关于BasePopup 3.0的破坏性更新说明](./Update_3.0.md)


### ChangeLog [(History ChangeLog)](https://www.yuque.com/razerdp/basepopup/uyrsxx)

* **[Release] 3.2.0** (2021/10/15)
    * After two months, we have made a series of optimizations to BasePopup after taking in user feedback. After a period of testing, it has stabilized, so we officially release version 3.2.0, welcome to update and download.
    * [Optimization]
        * To address the historical problems, we have unified the role of the root layout margin under match_parent and wrap_content this time, now the role is consistent with the system, used as margin instead of offset.
        * The default value of `setFitSize()` method is now True, which means BasePopup will resize by default to meet the display when there is not enough space, if you don't want BasePopup to resize automatically, please set this method to False.
            * Automatically turn off fitSize when setting mirroring and cache the original value
        * Add new Api to QuickPopup, and modify the implementation of QuickPopupConfig to make the code more refreshing and neat.
    * [Bug fix]
        * Fix measure issue: [#435](https://github.com/razerdp/BasePopup/issues/435)
        * Fix the problem of displaying on the left side of the target horizontally instead of aligning the left edge when AnchorView is associated by default.
        * Fix the problem of update error when rotating the screen.
        * Fix the problem of event distribution: [#443](https://github.com/razerdp/BasePopup/issues/443)
    * [New features
        * Add the function of not hiding the keyboard when dismiss: `hideKeyboardOnDismiss(boolean)`
    * [Other]
        * Clear all marker obsolete methods

Translated with www.DeepL.com/Translator (free version)

### Demo

#### Demo Apk Download：[**apk download（pass:123）**](https://www.pgyer.com/basepopup)

<img src="./img/download.png"  width="256"/>

<br>

|  |  |  |
| - | - | - |
| ![](https://github.com/razerdp/Pics/blob/master/BasePopup/demo_1.gif) | ![](https://github.com/razerdp/Pics/blob/master/BasePopup/new_demo_2.gif) | ![](https://github.com/razerdp/Pics/blob/master/BasePopup/demo_3.gif) |
| ![](https://github.com/razerdp/Pics/blob/master/BasePopup/demo_4.gif) | ![](https://github.com/razerdp/Pics/blob/master/BasePopup/demo_5.gif) | ![](https://github.com/razerdp/Pics/blob/master/BasePopup/demo_6.gif) |

### License

[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Frazerdp%2FBasePopup.svg?type=large)](https://app.fossa.com/projects/git%2Bgithub.com%2Frazerdp%2FBasePopup?ref=badge_large)

[Apache-2.0](./LICENSE)


<p align="center">
  Visit Count（from 2020/08/19）<br>
  <img src="https://profile-counter.glitch.me/razerdp-basepopup/count.svg" />
</p>
