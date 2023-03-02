 **中文** | [**English**](./README_V2_EN.md)

<p align="center"><img src="./img/logo.png" alt="Logo图片似乎加载不出来" height="360"/></p>
<h2 align="center">BasePopup - Android下打造通用便捷的PopupWindow弹窗库</h2>
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

### 作者的话

近期从苹果13转到华为mate50pro，因此关于popup的迭代可以继续进行了~之前受限于测试机（小米k20），有些问题无法测试出来，因此无从下手。现在总算可以继续干了。
### 介绍

BasePopup是一个对系统PopupWindow进行封装并改进的弹窗库，它是一个基础库类，有着非常高的自由度与丰富的API，您可以在BasePopup的框架下非常轻松的完成各种各样的弹窗。

### 环境依赖

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
  implementation 'io.github.razerdp:BasePopup:3.2.1'

  // for snapshot
  // implementation 'io.github.razerdp:BasePopup:3.2.1-SNAPSHOT'
}

```

### 文档

 - [**BasePopup手册**](https://www.yuque.com/razerdp/basepopup)

### 注意事项

  - Release和Snapshot两个版本互相切换可能会导致Build失败，这时候您Clean一下Project即可
  - 3.0版本会对2.x版本升级上来的用户造成较大范围的改动，请您务必阅读升级提示：[关于BasePopup 3.0的破坏性更新说明](./Update_3.0.md)


### 更新日志 [(历史更新)](https://www.yuque.com/razerdp/basepopup/uyrsxx)

* **【Release】3.2.1** (2022/12/29)
    * 不知不觉，距离上次更新居然已经一年了，不是我弃坑了，实则是工作有点忙
    * 另外作者我现在是在网易撸游戏了（嗯，已经撸了3年了），已经不是一个单纯的安卓程序员了，虽然还有关注并且迭代，但节奏肯定会慢下来不少
    * 不过~我还是会坚持更新的-V-
    * 【优化】
        * 对BlurOption预缩放下限作出限制（如果模糊预缩放小于0，则返回默认值）
        * QuickPopupConfig添加@keep [#462](https://github.com/razerdp/BasePopup/issues/462)
    * 【Bug修复】
        * 修复navigationbar不在DecorView层级下的判断问题（通过WindowInsets二次判断），fixed bug [#452](https://github.com/razerdp/BasePopup/issues/452)

### 例子预览

#### 更多例子请下载Demo：[**apk体验下载（密码123）**](https://www.pgyer.com/basepopup)

<img src="./img/download.png"  width="256"/>

<br>

|  |  |  |
| - | - | - |
| ![](https://github.com/razerdp/Pics/blob/master/BasePopup/demo_1.gif) | ![](https://github.com/razerdp/Pics/blob/master/BasePopup/new_demo_2.gif) | ![](https://github.com/razerdp/Pics/blob/master/BasePopup/demo_3.gif) |
| ![](https://github.com/razerdp/Pics/blob/master/BasePopup/demo_4.gif) | ![](https://github.com/razerdp/Pics/blob/master/BasePopup/demo_5.gif) | ![](https://github.com/razerdp/Pics/blob/master/BasePopup/demo_6.gif) |

### 打赏（您的支持是我持续更新的动力~）

<img src="https://github.com/razerdp/BasePopup/blob/master/img/alipay.png" alt="Logo图片似乎加载不出来" width="320"/>


### License

[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Frazerdp%2FBasePopup.svg?type=large)](https://app.fossa.com/projects/git%2Bgithub.com%2Frazerdp%2FBasePopup?ref=badge_large)

[Apache-2.0](./LICENSE)


<p align="center">
  Visit Count（from 2020/08/19）<br>
  <img src="https://profile-counter.glitch.me/razerdp-basepopup/count.svg" />
</p>
