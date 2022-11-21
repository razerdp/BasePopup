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
  implementation 'io.github.razerdp:BasePopup:3.2.0'

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

* **【Release】3.2.0** (2021/10/15)
    * 时隔两个月，在吸纳了用户反馈的意见后，我们对BasePopup进行了一系列的优化，经过一段时间的测试，已经趋于稳定，因此我们正式发布3.2.0版本，欢迎更新下载。
    * 【优化】
        * 针对历史遗留问题，我们这次统一了match_parent和wrap_content下根布局margin的作用，现在该作用跟系统一致，用作margin而非offset。
        * 现在`setFitSize()`方法缺省值为True，即BasePopup会在空间不足时默认调整大小以满足显示，如果您不希望BasePopup自动调整大小，请设置该方法为False。
            * 设置镜像时自动关闭fitSize并缓存原来的值
        * QuickPopup添加新增Api，同时修改QuickPopupConfig的实现，使代码更为清爽整洁。
    * 【Bug修复】
        * 修复measure问题：[#435](https://github.com/razerdp/BasePopup/issues/435)
        * 修复默认关联AnchorView时，水平方向上显示在目标左边而不是对齐左边缘的问题
        * 修复旋转屏幕时update错误的问题
        * 修复事件分发问题：[#443](https://github.com/razerdp/BasePopup/issues/443)
    * 【新增功能】
        * 增加dismiss时不隐藏键盘的功能：`hideKeyboardOnDismiss(boolean)`
    * 【其他】
        * 清除所有标记过时的方法

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

### 交流群

为了保证微信群的质量（主要是远离斗图党和广告党），因此只有打赏了之后才能进群~

【**打赏后请加微信小号：razerdp_small，并注明付款账号，定期核验后会通过**】

### License

[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Frazerdp%2FBasePopup.svg?type=large)](https://app.fossa.com/projects/git%2Bgithub.com%2Frazerdp%2FBasePopup?ref=badge_large)

[Apache-2.0](./LICENSE)


<p align="center">
  Visit Count（from 2020/08/19）<br>
  <img src="https://profile-counter.glitch.me/razerdp-basepopup/count.svg" />
</p>
