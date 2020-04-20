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

### 有奖问卷调查（作者自掏腰包哦~）

为了更好地计划及迭代BasePopup，我们（好吧，其实是作者一个人）设立了一份调查问卷，并自掏腰包提供了微信红包作为奖品，希望大家可以认真答题，我也能收到大家的反馈，更好地迭代这个库-V-

(红包一共100个，拼手气，我设了25人中一个~看RP哦)

[调查问卷(需要微信登录)](https://wj.qq.com/s2/5468287/c24f/)

![](./img/qrcode.png)

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
 - PopupWindow自动锚定AnchorView，滑动到屏幕外自动跟随AnchorView消失，不需要复杂的逻辑设置，只需要通过[**Link**](https://github.com/razerdp/BasePopup/wiki/API#linktoview-anchorview)方法告诉BasePopup
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


### 更新日志 ([历史更新](https://www.yuque.com/razerdp/basepopup/uyrsxx)

* **【Candy】2.2.3**
  * **【2.2.3.20200304】**
    * 优化覆盖状态栏情况下的事件传递
    * 方法弃用：~~`setPopupWindowFullScreen()`~~ -> `setOverlayStatusbar()`
    * 新增方法：`setPopupGravityMode()`
    * Demo迁移至蒲公英
  * **【2.2.3.20200311】**
    * 开放BasePopup的PopupShowing回调
      * 现在可以在`onShowing()`中监听BasePopup显示了~
    * 增加`OnPopupWindowShowListener`，效果同上
    * fix [#267](https://github.com/razerdp/BasePopup/issues/267)
    * 修复`setOverlayStatusbar(false)`情况下与Anchor关联时显示位置错误的问题
  * **【2.2.3.20200313】**
    * 增加背景模糊demo
    * 抛弃反射黑科技，感谢[@xchengDroid](https://github.com/xchengDroid)提出的方案
  * **【2.2.3.20200316】**
    * 支持的最低版本降至api 16
  * **【2.2.3.20200404】**
    * 优化decorView寻找方式，释放引用优化
    * 添加LifeCycle绑定方法(`bindLifecycleOwner()`)
    * 添加Api展示
  * **【2.2.3.20200406】**
    * 修复当PopupWindow弹出时，Activity弹出的输入法显示在PopupWindow下层的问题
    * fix [#224](https://github.com/razerdp/BasePopup/issues/224)
  * **【2.2.3.20200411】**
    * 增加`onPreShow()`方法，在真正show之前可以在该方法配置
  * **【2.2.3-dev01】**(2020/04/21)
    * fix[#281](https://github.com/razerdp/BasePopup/issues/281)
    * 补充QuickPopupConfig方法
    * deprecated：
      * BasePopupWindow#dismissWithOutAnimate()
      * QuickPopupConfig#dismissOnOutSideTouch()
      * QuickPopupConfig#allowInterceptTouchEvent()

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

* **【Release】2.2.2**(2020/02/24)
  * 发布2.2.2正式版，**本版本是一个重构版本，请认真阅读更新日志哦**
  * **新增功能/特性：**
    * 新增 `BaseLazyPopupWindow`，以后懒加载的PopupWindow只需要extend这个就可以了，旧版本的`delayInit()`已经在该版本去除
    * 增加BasePopup队列，针对outSideTouch优化
    * 适配Android 10，解决Android 10黑/灰名单的问题
    * 适配`match_parent`，现在`match_parent`将会填充剩余空间
    * 主体完全迁移至AndroidX，去除BasePopup扩展组件库，同时建议您尽快适配AndroidX
    * 针对内存泄漏进行梳理，同时增加生命期监听，在`destroy`中会释放引用
    * 增加对`dialog`/`fragment`/`dialogfragment`的支持
    * 添加`setFitSize()`方法
      * `setFitSize()`：BasePopup会针对剩余空间来调整Popup的大小，因此可能出现实际显示过小的情况
    * 优化QuickPopupBuilder
    * 蒙层交回给系统托管，再也不用担心为啥全面屏无法全覆盖了
    * 优化在`onCreate()`中弹窗无法弹出的问题
      * 采取[#263](https://github.com/razerdp/BasePopup/issues/263)的建议，非常感谢[@xchengDroid](https://github.com/xchengDroid)提出的建议
    * 增加`onLogInternal()`方法，您可以在这里打印BasePopupWindow执行期间的日志
    * 增加`onViewCreated()`方法，您可以在这里对ContentView进行操作，或者使用ButterKnife进行注入
  * **精简：**
    * 去除onAnchorTop/onAnchorBottom方法，后续将会替换为别的方法
    * 去除`limitScreen()`方法
    * 去除扩展组件，现在主体支持AndroidX，同时不再支持Support包了
  * **bug fixed：**
    * fixed [#184](https://github.com/razerdp/BasePopup/issues/184)、[#207](https://github.com/razerdp/BasePopup/issues/207)、[#210](https://github.com/razerdp/BasePopup/issues/210)
    * fixed [#213](https://github.com/razerdp/BasePopup/issues/213)、[#226](https://github.com/razerdp/BasePopup/issues/226)、[#232](https://github.com/razerdp/BasePopup/issues/232)
    * fixed [#236](https://github.com/razerdp/BasePopup/issues/236)、[#238](https://github.com/razerdp/BasePopup/issues/238)、[#240](https://github.com/razerdp/BasePopup/issues/240)
    * fixed [#242](https://github.com/razerdp/BasePopup/issues/242)、[#244](https://github.com/razerdp/BasePopup/issues/244)、[#247](https://github.com/razerdp/BasePopup/issues/247)
    * fixed [#248](https://github.com/razerdp/BasePopup/issues/248)、[#249](https://github.com/razerdp/BasePopup/issues/249)、[#260](https://github.com/razerdp/BasePopup/issues/260)
    * fixed [#262](https://github.com/razerdp/BasePopup/issues/262)、[#263](https://github.com/razerdp/BasePopup/issues/263)

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
