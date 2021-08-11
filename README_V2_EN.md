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
        // release依赖仓库（4.1后as默认配置有）
        mavenCentral()

        // snapshot仓库（如果需要snapshot依赖，请配置该maven）
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

 - [**BasePopup手册**](https://www.yuque.com/razerdp/basepopup)

### Precautions

  - Switching between Release and Snapshot versions may cause the Build to fail, so you can clean the Project.
  - Version 3.0 will cause a large range of changes to users who have upgraded from version 2.x. Please be sure to read the upgrade instructions:[关于BasePopup 3.0的破坏性更新说明](./Update_3.0.md)


### ChangeLog [(History ChangeLog)](https://www.yuque.com/razerdp/basepopup/uyrsxx)

* **【Release】3.1.8** (2021/08/11)
    * Optimize margin's measure and layout logic.[issue#429](https://github.com/razerdp/BasePopup/issues/429)
    * Add `onSizeChange(int oldW, int oldH, int newW, int newH)` callback.

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
