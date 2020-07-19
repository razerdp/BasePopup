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

### 有奖调查问卷：

<a href ="https://wj.qq.com/s2/5468287/c24f"><img src="./img/qrcode.png"/></a>

### 导航
 
 - [特性](#特性)
 - [注意事项](#注意事项)
 - [环境依赖](#环境依赖)
 - [文档](#文档)
   - [BasePopup手册](https://www.yuque.com/razerdp/basepopup)
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

### 文档

BasePopup配备完善的文档，建议您优先查阅文档。

[**BasePopup手册**](https://www.yuque.com/razerdp/basepopup)

<br>



### 更新日志 [(历史更新)](https://www.yuque.com/razerdp/basepopup/uyrsxx)

**正在开发日志（Candy版本）请查看dev分支：** [**branch-dev**](https://github.com/razerdp/BasePopup/tree/dev)

* **【Release】2.2.4**(2020/07/19)
  * 在2.2.4版本中，我们添加了非常棒的 `AnimationHelper`，在它的帮助下，创建BasePopup动画将会变得十分简单易懂，同时我们也增加了很多很棒的功能，欢迎更新到2.2.4版本~
  * **新增功能/方法：**
    * 添加弹窗线程检查：非主线程下我们将会抛出异常 `CalledFromWrongThreadException`
    * 开放 `OnKeyboardChangeListener`：现在您可以监听键盘显示、关闭事件，同时该回调会返回键盘的大小
    * 增加 `KeyEventListener`：现在允许您在外部添加事件监听，感谢issue：[#296](https://github.com/razerdp/BasePopup/issues/296)
    * 增加 `setOverlayNavigationBar`：通过该方法，您可以让您的BasePopup覆盖到导航栏，默认情况下不允许覆盖，如果您确实有需要，可以通过该api配置覆盖
      * 相关讨论：[《关于MIUI小白条及类似的“全面屏手势提示线”覆盖问题描述》](https://github.com/razerdp/BasePopup/issues/307)
    * 增加 `setWidthAsAnchorView`：您可以调用该Api决定BasePopup的宽度是否设置为AnchorView的宽度
    * 增加 `setHeightAsAnchorView`：您可以调用该Api决定BasePopup的高度是否设置为AnchorView的高度
    * 增加RTL布局支持：该需求早在几个月前就有群友在群里提出了，一直没去弄，直到最近才有空去写，得益于BasePopup根据Gravity来做方向判断，我们修改了极少的代码完成了RTL适配
      * 如果使用RTL，请务必设置 `setLayoutDirection`方向，告诉BasePopup宿主布局方向
      * RTL仅对`showPopupWindow(View anchorView)`和`showPopupWindow(int x,int y)`有效
      * **请务必注意，BasePopup遵循官方做法，当您需要RTL布局的时候，请使用START代替LEFT，使用END代替RIGHT**
    * 增加 `syncMaskAnimationDuration`：您可以设置蒙层动画的时间是否同步您的动画时间（取最长时间），默认同步
    * 增加AnimationHelper：我们希望能减少动画的代码量，同时让动画的创建更加的易懂，因此我们在本版本中正式推出AnimationHelper，相信你一定会喜欢他的
      * 关于AnimationHelper的资料，受篇幅限制，请查阅文档：[【进阶指引-动画-AnimationHelper】](https://www.yuque.com/razerdp/basepopup/zcgtm5)
      * AnimationHelper在2.2.4版本新推出，可能会存在我们没测出来的Bug，不过它只是创建动画的辅助类，因此不影响主体流程，所以如果您发现它不好用或者不满足您的功能，您可以更换为原生的动画创建，同时欢迎提交issue
  * **优化**
    * 优化BasePopupHelper代码
    * 优化蒙层动画：
      * 我们针对蒙层的动画进行了优化，现在蒙层的渐入渐出时间将会跟您设置的动画最长时间同步，我们期望在您的展示/退出动画完成的一刹那，蒙层动画也同步完成，而不希望其提前或延后
      * 您可以通过 `syncMaskAnimationDuration`配置是否同步
  * **删除类/方法**
    * **请注意，这次我们没有标记为@Deprecated，直接删除了代码，如果您使用到这些方法，请务必进行更改**
    * **删除类：** SimpleAnimationUtils.java，我们建议您使用 [AnimationHelper](https://www.yuque.com/razerdp/basepopup/zcgtm5) 代替
    * **删除方法：** BasePopupWindow#getTranslateVerticalAnimation
    * **删除方法：** BasePopupWindow#getScaleAnimation
    * **删除方法：** BasePopupWindow#getDefaultScaleAnimation
    * **删除方法：** BasePopupWindow#getDefaultAlphaAnimation
    * **删除方法：** BasePopupWindow#getDefaultSlideFromBottomAnimationSet
  * **bug修复**
    * 修复全屏Activity下focusable没有恢复的问题
    * 修复全屏Activity下SystemUiVisibility被强制修改的问题
    * 修复Quickpopupbuilder中backpressenable设置失效的问题[#296](https://github.com/razerdp/BasePopup/issues/296)
    * 修复非BasePopup内EditText弹出键盘时挡住EditText的问题[#297](https://github.com/razerdp/BasePopup/issues/297)
    * 修复lazypopup中gravity覆盖的问题[#310](https://github.com/razerdp/BasePopup/issues/310)
    * 修复onCreate()下关联锚点View时因未完成测量过程而导致的位置获取不成功问题[#313](https://github.com/razerdp/BasePopup/issues/313)
    * 修复设置最大/最小宽高下测量错误的问题
    * 修复键盘监听在adjustResize下可能会出现的问题[#315](https://github.com/razerdp/BasePopup/issues/315)
    * 修复设置softInputMode重复的问题[#314](https://github.com/razerdp/BasePopup/issues/314)


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
