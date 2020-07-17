 **中文** | [**English**](./README_V2_EN.md)

<p align="center"><img src="./img/logo.png" alt="Logo图片似乎加载不出来" height="360"/></p>
<h2 align="center">BasePopup - Android下打造通用便捷的PopupWindow</h2>
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

### 导航
 
 - [特性](#特性)
 - [注意事项](#注意事项)
 - [环境依赖](#环境依赖)
 - [快速入门](#快速入门)
   - [BasePopup手册](https://www.yuque.com/razerdp/basepopup)
 - [Api](#api)
 - [更新日志](#更新日志-历史更新)
   - [历史更新](https://www.yuque.com/razerdp/basepopup/uyrsxx)
 - [例子预览](#例子预览)
   - [**apk体验下载（密码123）**](https://www.pgyer.com/basepopup)
    <img src="./img/download.png"  width="256"/>
 - [打赏](#打赏看在我那么努力维护的份上给个零食呗)
 - [交流群](#交流群)
 - [常见问题](#常见问题)
 - [LICENSE](#license)
   
<br>

### 特性

 - 本库作为基类，对您的实现没有任何干预，再也不需要担心实现某些方法的时候被Api限制了
 - 无需头疼如何计算offset来进行位置控制，只需要简简单单的设置[**Gravity**](https://www.yuque.com/razerdp/basepopup/qnu3qd)便能随心所欲的控制您的Popup
 - 无论是[**Animation**](https://www.yuque.com/razerdp/basepopup/mg3bcw#onCreateShowAnimation)还是[**Animator**](https://www.yuque.com/razerdp/basepopup/mg3bcw#onCreateShowAnimator)，只需要跟您平时一样写动画，就可以完成Popup的动效设计了，不需要xml不需要关心别的兼容性问题
 - 背景与主体分离，无论是[**背景模糊**](https://www.yuque.com/razerdp/basepopup/udccdq#12bedc89)，亦或是[**背景颜色**](https://www.yuque.com/razerdp/basepopup/gscx3g#aiRz7)，甚至[**把背景换成您的View**](https://www.yuque.com/razerdp/basepopup/gscx3g#e96cp)，都可以通过简单的设置完成，主体与背景隔离，不用担心事件的问题
 - 还在为Popup的触摸事件头疼吗？BasePopup帮你解决烦恼~返回键控制、外部点击透传、点击外部是否消失都只需要您动动手指头完成配置即可
 - PopupWindow自动锚定AnchorView，滑动到屏幕外自动跟随AnchorView消失，不需要复杂的逻辑设置，只需要通过[**linkTo**](https://www.yuque.com/razerdp/basepopup/api)方法告诉BasePopup即可帮您完成
 - 简单的PopupWindow不想新建一个类，希望拥有链式调用？没问题，[**QuickPopupBuilder**](https://www.yuque.com/razerdp/basepopup/ob329t)为此而生，相信你会越用越爱~

<br>

### 注意事项

  - **请务必仔细阅读本README,每个版本升级请务必查阅更新日志，这可以为您减少不必要弯路**
  - **2.2.2版本开始不再支持Support，建议您尽快升级到AndroidX**
  - **请注意引用版本的问题，Release版本是稳定版，Candy是预览版。**
    - Release版本：一般在Candy版本反复验证修复后发布到Release，如果您对稳定性要求较高，请使用Release版本。
    - Candy版本：一般新功能、issue修复都会发布到Candy版本，Candy版本发布比较频繁，但通常会拥有新的功能，如果您喜欢试验新功能同时对稳定性要求不高，请使用Candy版本。
    - **Release和Candy两个版本互相切换可能会导致Build失败，这时候您Clean一下Project即可**
  - 从16年[**第一次**](https://github.com/razerdp/BasePopup/commit/c92b7088270d5757269d9b79213627a4a0392d31)提交到现在，本人技术也一直在进步，BasePopup也会一直迭代更新，所以，请谨慎选择版本哦~一不小心就颠覆了之前的实现。

<br>

### 环境依赖

请把下述 **{$latestVersion}** 替换为上面表格中对应的版本。

**自2.2.2版本开始，BasePopup将完全迁移至AndroidX，不再提供扩展组件了**，BasePopup建议您尽早迁移到AndroidX

 - **Release：** `implementation 'com.github.razerdp:BasePopup:{$latestVersion}'`
 - **Candy：** `implementation 'com.github.razerdp:BasePopup_Candy:{$latestVersion}'`

<br>

### 快速入门

请参考文档：[**BasePopup手册**](https://www.yuque.com/razerdp/basepopup)

<br>

### Api

请参考文档：[**BasePopup手册-Api**](https://www.yuque.com/razerdp/basepopup/api)

<br>


### 更新日志 [(历史更新)](https://www.yuque.com/razerdp/basepopup/uyrsxx)

* **【Candy】2.2.4**
  * **【2.2.4.0512】**
    * 修复全屏activity下focusable没有恢复的问题
  * **【2.2.4.0517】**
    * 修复全屏Activity下SystemUiVisibility被强制修改的问题
    * 添加线程检查
  * **【2.2.4.0618】**
    * 修复Quickpopupbuilder中backpressenable设置失效的问题([#296](https://github.com/razerdp/BasePopup/issues/296))
  * **【未发布版本】**（2020/06/19）
    * 修复非BasePopup内EditText弹出键盘时挡住EditText的问题([#297](https://github.com/razerdp/BasePopup/issues/297))
  * **【2.2.4.0702】** （2020/07/02）
    * 优化BasePopupHelper代码
    * 增加`OnKeyboardChangeListener`，该监听允许用户监听键盘显示、关闭事件，同时回调键盘大小
    * 增加`KeyEventListener`，允许用户外部监听并处理传递到BasePopup的KeyEvent [#296](https://github.com/razerdp/BasePopup/issues/296)
    * 动画优化（进度70%）
    * 增加BasePopup覆盖导航栏的功能：`setOverlayNavigationBar`，相关讨论：[关于MIUI小白条及类似的“全面屏手势提示线”覆盖问题描述](https://github.com/razerdp/BasePopup/issues/307)
  * **【2.2.4.0703】** （2020/07/03）
    * 修复lazypopup中gravity覆盖的问题 [#310](https://github.com/razerdp/BasePopup/issues/310)
    * 增加`setWidthAsAnchorView`：该方法将会设置Popup的宽度为AnchorView的宽度
    * 增加`setHeightAsAnchorView`：该方法将会设置Popup的高度为AnchorView的高度
  * **【2.2.4.0705】** （2020/07/05）
    * 修复onCreate()下关联锚点View时因未完成测量过程而导致的位置获取不成功问题。[#313](https://github.com/razerdp/BasePopup/issues/313)
  * **【2.2.4.0711】** （2020/07/11）
    * RTL支持：
        * 增加`setLayoutDirection()`方法，允许您设置依附的窗口的layoutDirection，以适应RTL布局
        * **请务必注意，BasePopup遵循官方做法，当您需要RTL布局的时候，请使用START代替LEFT，使用END代替RIGHT**
  * **【2.2.4.0713】**（2020/07/13）
    * 修复设置最大/最小宽高下测量错误的问题
  * **【2.2.4.0717】**（2020/07/16提交，17号发布）
    * 动画重构完成，添加动画创建类 `AnimationHelper`
    * **删除类/方法**：
        * SimpleAnimationUtils.java
        * BasePopupWindow#getTranslateVerticalAnimation
        * BasePopupWindow#getScaleAnimation
        * BasePopupWindow#getDefaultScaleAnimation
        * BasePopupWindow#getDefaultAlphaAnimation
        * BasePopupWindow#getDefaultSlideFromBottomAnimationSet
    * 修复键盘监听在adjustResize下可能会出现的问题：[#315](https://github.com/razerdp/BasePopup/issues/315)
    * 修复设置softInputMode重复的问题：[#314](https://github.com/razerdp/BasePopup/issues/314)

<br>

### 例子预览

<br>

#### 更多例子请下载Demo：[**apk体验下载（密码123）**](https://www.pgyer.com/basepopup)

<img src="./img/download.png"  width="256"/>

<br>

|  |  |  |
| - | - | - |
| ![](https://github.com/razerdp/Pics/blob/master/BasePopup/demo_1.gif) | ![](https://github.com/razerdp/Pics/blob/master/BasePopup/new_demo_2.gif) | ![](https://github.com/razerdp/Pics/blob/master/BasePopup/demo_3.gif) |
| ![](https://github.com/razerdp/Pics/blob/master/BasePopup/demo_4.gif) | ![](https://github.com/razerdp/Pics/blob/master/BasePopup/demo_5.gif) | ![](https://github.com/razerdp/Pics/blob/master/BasePopup/demo_6.gif) |

<br>

### 打赏（看在我那么努力维护的份上。。。给个零食呗~）

<img src="https://github.com/razerdp/BasePopup/blob/master/img/alipay.png" alt="Logo图片似乎加载不出来" width="480"/>

<br>

### 交流群

因QQ没时间管理，因此解散，同时开通微信群，主要用于交流和BasePopup的反馈，为了保证微信群的质量，因此只有打赏了之后才能进群~

<br>

### 常见问题

请查阅[**手册：常见问题**](https://www.yuque.com/razerdp/basepopup/dgf6ry)

<br>

### License

[Apache-2.0](./LICENSE)
