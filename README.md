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

### 开发计划 & 需求投票

当前开发：动画迭代

当前需求投票：[允许蒙层高亮一个或多个其依附的View](https://github.com/razerdp/BasePopup/issues/300)

需求提单：[【BasePopup需求提单】](https://github.com/razerdp/BasePopup/issues/299)

讨论：[【关于MIUI小白条及类似的“全面屏手势提示线”覆盖问题描述】](https://github.com/razerdp/BasePopup/issues/307)

有奖调查问卷：

<a href ="https://wj.qq.com/s2/5468287/c24f">
    <img src="./img/qrcode.png"/>
</a>

<br>

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


### 更新日志 ([历史更新](https://www.yuque.com/razerdp/basepopup/uyrsxx))

**Candy开发日志请查看dev分支**[**branch-dev**](https://github.com/razerdp/BasePopup/tree/dev)

* **【Release】2.2.3**(2020/05/07)
  * 我们针对2.2.2系列问题进行了修复，同时增加了一些新的功能，欢迎更新到最新版本~
  * **新增功能/方法：**
    * 新增`setPopupGravityMode()`：您可以单独设置BasePopup对齐方式而不需要始终带上Gravity
    * 新增`OnPopupWindowShowListener`接口：在BasePopup显示后回调该接口，当回调该方法时意味着弹窗已经完成，此时ui已经显示在屏幕上
    * 新增`bindLifecycleOwner()`：您现在可以自由绑定您的LifecycleOwner
    * 新增`onPreShow()`回调：在BasePopup弹出之前回调该方法，如果返回false，则不会弹出
    * 新增`onShowing()`回调：在BasePopup显示后回调该方法，当回调该方法时意味着弹窗已经完成，此时ui已经显示在屏幕上
    * 新增`onPopupLayout()`回调：如果弹窗与锚点View关联，当BasePopup在布局的时候回调该方法，分别返回BasePopup在屏幕上的位置和锚点View在屏幕上的位置
    * 新增`computeGravity()`：配套`onPopupLayout()`回调，计算BasePopup中心点在锚点View的方位。
  * **弃用方法及更替：**
    * `BasePopupWindow#dismissWithOutAnimate()`，请使用**dismiss(false)**
    * `BasePopupWindow#setPopupWindowFullScreen()`，请使用**setOverlayStatusbar()**
    * `QuickPopupConfig#dismissOnOutSideTouch()`，请使用**outSideDismiss()**
    * `QuickPopupConfig#allowInterceptTouchEvent()`，请使用**outSideTouchable()**
  * **优化：**
    * 优化DecorView的查询方式，原逻辑会缓存下查询后的DecorView，但可能会因为该DecorView宿主已经销毁或者变更而导致显示错误
    * 支持的最低版本降至Api 16
    * 放弃反射WindowManager的方式，采取ContextWrapper代理，不再担心遭遇黑灰名单封锁了~感谢[@xchengDroid](https://github.com/xchengDroid)提供的方案
  * **bug修复：**
    * 修复覆盖状态栏时事件传递存在偏移的情况
    * 修复`isShowing()`存在空指针的情况(issue：[#267](https://github.com/razerdp/BasePopup/issues/267))
    * 修复`setOverlayStatusbar(false)`情况下与Anchor关联时显示位置错误的问题
    * 修复部分引用没有置空导致**可能**存在的内存泄漏问题（事实上并没发现泄漏）
    * 修复BasePopup弹出时，Activity弹出的输入法显示在BasePopup下层的问题
    * 修复全屏Activity判断错误的问题
    * 修复QuickPopupConfig配置缺漏的问题
    * 修复未弹窗时直接调用dismiss(),然后首次调用showPopupWindow()失效的问题
    * fixed issue：[#224](https://github.com/razerdp/BasePopup/issues/224)

* **【Release】2.2.2.2**(2020/03/01)
  * 修复一个很严重的可能会导致崩溃的问题
    * 重现方式：dismiss动画没执行完的情况下finish了activity，会引发空指针崩溃
    * 该问题在此之前的版本中是无法被 **try & catch**的
  * 修复了finish的时候没有强制dismiss的问题
  * 修复了一些flag的问题
  * 修复outsideTouch在非全屏下位置偏离的问题
  * **非常抱歉，因为自身原因没有查到此类错误，在收到反馈后我第一时间排查了所有类似的地方并逐一修复，这次重构的周期跨度较长，涉及板块较多，测试周期也很长，但因为个人精力有限仍然会有遗漏的地方。在此希望能够得到您的支持，尽量在Candy测试期及时反馈问题，减少此类问题的发生。**

* **【Release】2.2.2.1**(2020/02/26)
  * 修复输入法自动弹出后不能再次弹出的问题

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
