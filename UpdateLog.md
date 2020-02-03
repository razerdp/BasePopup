## BasePopup更新日志

* **【里程碑】2.2.2**
    * **【Candy】2.2.2.200203 ** 2020/02/03
      * 重构onCreate中弹出的方案
      * 解决一些检查到的内存泄漏
      * 去除`limitScreen()`方法
      * 重构DecorViewProxy，现在不再需要我来适配宽高了，由系统完成，以此解决各种蛋疼的适配问题
      * 优化Measure方法，当BasePopup显示跟Anchor关联时，如果宽或者高为match_parent，将会填满剩余可用空间
      * 解决Android 10黑/灰名单的问题
      * 即将发布正式版

* **【Candy】2.2.2**
    * **【Candy】190704**
      * 修复AnimatorSet的时间问题 fix [**#203**](https://github.com/razerdp/BasePopup/issues/203)
    * **【Candy】190722**
      * 回滚[#188](https://github.com/razerdp/BasePopup/issues/188)修复
      * 经检查，[#188](https://github.com/razerdp/BasePopup/issues/188)修复带来的问题比较严重，建议升级到该版本
    * **【Candy】190816**
      * 考虑到很多用户提出全面屏会有蒙层无法填充的问题，因此增加`setMaskLayoutWidth`和`setMaskLayoutHeight`方法，该方法允许您自定义蒙层的高度
      * 开始重构BasePopupWindow，本次重构将会解决遗留问题同时缩减冗余代码，并提高代码可读性
    * **【Candy】190904**
      * 重构屏幕宽高获取逻辑
      * 修改supporter名字为component
    * **【Candy】190912**
      * 重构键盘逻辑，现在键盘对齐支持选择模式了~
      * 相关文档请查阅文档 [**键盘（输入法）**](https://www.kancloud.cn/razerdp/basepopup/1277045)
    * **2019/09/20**
      * Demo大翻新
    * **2019/09/22**
      * 增加输入法Demo
      * Demo支持更新
      * Demo增加issue修复测试用例
    * **2019/09/26**
      * 取消对support的支持
    * **2019/09/27**
      * 增加手势导航栏支持
    * **2019/09/29**
      * 发布新版candy
    * **2019/10/09**
      * 添加issue #230测试用例
    * **2019/10/31**
      * 优化autoLocate
      * 优化超出屏幕的位移问题
      * 添加setResize()方法
      * 添加onAutoLocationChange()方法

* **【Release】2.2.1**(2019/06/24)
  * 支持Service或者非ActivityContext里弹窗
  * 优化PopupUiUtils，优化获取屏幕宽高算法
    * fixed [**#186**](https://github.com/razerdp/BasePopup/issues/186)、[**#167**](https://github.com/razerdp/BasePopup/issues/167)
    * fixed [**#188**](https://github.com/razerdp/BasePopup/issues/188)(not perfect)
  * 修改并优化键盘判断逻辑
  * 优化全屏状态下点击范围的判定，fixed [**#200**](https://github.com/razerdp/BasePopup/issues/200)

* **【Candy】2.2.1**(2019/05/16)
    * **【Candy】190516**
      * 支持Service或者非ActivityContext里弹窗
    * **【Candy】190517**
      * 优化PopupUiUtils，优化获取屏幕宽高算法
      * fixed [**#186**](https://github.com/razerdp/BasePopup/issues/186)、[**#167**](https://github.com/razerdp/BasePopup/issues/167)
      * fixed [**#188**](https://github.com/razerdp/BasePopup/issues/188)(not perfect)
    * **【Candy】190522**
      * 修改并优化键盘判断逻辑
    * **【Candy】190611**
      * 优化全屏状态下点击范围的判定，fixed [**#200**](https://github.com/razerdp/BasePopup/issues/200)

* **【Release】2.2.0**(2019/05/15)
  * 正式版2.2.0隆重归来，这次正式版又是一个重构版本哦~
  * 优化输入法对齐逻辑
  * **重构模糊逻辑：**
    * 经测试，720p的手机在默认参数下全屏模糊时间平均在**6ms~16ms**之间
    * 增大默认参数的模糊程度
    * 模糊淡入淡出时间跟随Popup的动画时间
    * 修复模糊偶尔失效的情况
  * **测量/布局相关：**
    * 重构测量逻辑：
      * 现在在`clipToScreen`的情况下，会根据剩余空间对PopupDecor进行重新测量，以保证Popup完整的显示，如果您需要保持原始的测量值，请调用`keepSize(true)`
      * 重构layout逻辑，针对**outSideTouch**优化
      * 适配屏幕旋转，fix [#180](https://github.com/razerdp/BasePopup/issues/180)
      * 采取flag代替各种boolean，清爽更简洁
      * 减少冗余代码
  * **优化相关：**
    * 增加GravityMode值，现在允许您配置`PopupGravity`的参考模式啦~
      * **RELATIVE_TO_ANCHOR**：默认模式，以Anchor为参考点，指定PopupWindow显示在Anchor的方位
      * **ALIGN_TO_ANCHOR_SIDE**：对齐模式，以Anchor的边为参考点，指定PopupWindow的边与Anchor的哪条边对齐
    * 增加minWidth/minHeight 方法，增加maxWidth/maxHeight 方法，让他们相互对应~
    * 修复高度为match_parent和wrap_content的测量差异，现在可以安心地玩耍啦
    * 部分Api标记过时：
      * ~~setAllowDismissWhenTouchOutside~~ -> **setOutSideDismiss**
      * ~~setAllowInterceptTouchEvent~~ -> **setOutSideTouchable**
    * 增加`setBackgroundView(View)`方法，现在BasePopup的背景控件可以随意由你定制啦~当然PopupWindow的背景动画控制方法依旧生效
  * **包拆分：**
    * 现在BasePopup将会进行包的拆分，源工程仅针对没有任何依赖的原生Android进行适配，如果您需要别的适配，请分别依赖以下模块或多个模块：
      * 如果您需要`support`库的支持，比如DialogFragment支持，请依赖
        * `implementation 'com.github.razerdp:BasePopup-compat-support:{$latestVersion}'`
      * 如果您需要`lifecycle`库的支持，比如destroy里自动释放或者关闭等，请依赖
        * `implementation 'com.github.razerdp:BasePopup-compat-lifecycle:{$latestVersion}'`
      * 如果您需要`androidX`库的支持，请依赖
        * `implementation 'com.github.razerdp:BasePopup-compat-androidx:{$latestVersion}'`
      * **请注意，如果您依赖了androidX支持组件，请不要依赖另外两个支持组件，否则会冲突**
  * **Bug fixed：**
    * fix [#171](https://github.com/razerdp/BasePopup/issues/171)、[#181](https://github.com/razerdp/BasePopup/issues/181)、[#182](https://github.com/razerdp/BasePopup/issues/182)、[#183](https://github.com/razerdp/BasePopup/issues/183)
    * fix [#180](https://github.com/razerdp/BasePopup/issues/180)
    * fixed [#164](https://github.com/razerdp/BasePopup/issues/164)
  * **Other：**
    * add 996 license

* **【Candy】2.2.0**
  * **【Candy】2.2.0-preview4**(2019/05/14)
    * 修复某些情况下模糊失效的问题
    * 拆分依赖优化
  * **【Candy】2.2.0-beta3**(2019/05/10)
    * 预计下周发布新版
    * 重构模糊相关逻辑
      * 经测试，全屏模糊在默认情况下控制到6ms~12ms之间
      * 增大模糊程度~
  * **【Candy】2.2.0-beta2**(2019/05/08)
    * 重构代码，使用flag代替各种boolean
    * 屏幕旋转适配，fix [#180](https://github.com/razerdp/BasePopup/issues/180)
    * 补全QuickPopupConfig配置
  * **【Candy】2.2.0-beta**(2019/05/07)
    * 重构BasePopup测量与布局，减少冗余代码
    * 增加GravityMode方法，现在允许您配置PopupGravity的参考模式
      * RELATIVE_TO_ANCHOR：默认模式，以Anchor为参考点
      * ALIGN_TO_ANCHOR_SIDE：对齐模式，以Anchor的边为参考点
    * 增加minWidth/minHeight 方法，允许设置最小宽高
    * fix [#171](https://github.com/razerdp/BasePopup/issues/171)、[#181](https://github.com/razerdp/BasePopup/issues/181)、[#182](https://github.com/razerdp/BasePopup/issues/182)、[#183](https://github.com/razerdp/BasePopup/issues/183)
    * 去除高度match_parent和wrap_content的测量差异
    * 部分Api标记过时：
      * ~~setAllowDismissWhenTouchOutside~~ -> **setOutSideDismiss**
      * ~~setAllowInterceptTouchEvent~~ -> **setOutSideTouchable**
    * 增加对Lifecycle的支持（如果需要混淆请混淆Lifecycle相关）
  * **近期工作**
    * 近期很少更新，除了因为入职新公司外，更重要的是随着使用本库的开发者数量增多，一些遗留的bug出现越来也多，因此，为了更好地适应，决定开始了第三次重构。
    * 本次重构大致构思以及内容：
      * 保持现有Api，部分Api将会被记录过时（仍然可用），将会由新的Api代替
      * 全面优化测量方法，解决遗留的问题
      * 针对issue优化
      * 去掉冗余代码
  * **【Candy】2.2.0-alpha4**(2019/04/17)
    * fixed [#164](https://github.com/razerdp/BasePopup/issues/164)
    * 优化测量逻辑，尝试修复wrap_content等高度问题
  * **【Candy】2.2.0-alpha2**(2019/03/21)
    * 增加`setMaxWidth()`和`setMaxHeight()`方法，想最大半屏显示？走起~
  * **【Candy】2.2.0-alpha**(2019/03/21)
    * 增加`setBackgroundView(View)`方法，现在BasePopup的背景控件可以随意由你定制啦~当然PopupWindow的背景动画控制方法依旧生效
  * **【other】**
    * add 996 license

* **【Release】2.1.9**(2019/03/07)
  * 优化对android P刘海的支持，允许PopupWindow布局到刘海，fixed [**#154**](https://github.com/razerdp/BasePopup/issues/154)
  * 修复quickpopup没有设置回调的问题
  * OnDismissListener添加退出动画开始的回调
  * 优化模糊逻辑
  * 优化退出动画逻辑
  * fixed [**#152**](https://github.com/razerdp/BasePopup/issues/152)
  * 优化代码，修复覆盖动画监听器的bug，优化layout逻辑
  * 为模糊图片方法添加oom捕捉
  * 优化背景和局部模糊逻辑
  * 去除lib的AndroidManifest内容，预防冲突，fixed [**#149**](https://github.com/razerdp/BasePopup/issues/149)
  * 针对DialogFragment适配，fixed [**#145**](https://github.com/razerdp/BasePopup/issues/145)

* **【Candy】2.1.9**
  * **【Candy】2.1.9-prerelease**(2019/03/07)
    * 优化对android P刘海的支持，允许PopupWindow布局到刘海，fixed [**#154**](https://github.com/razerdp/BasePopup/issues/154)
  * **【Candy】2.1.9-beta4~5**(2019/03/1)
    * 修复quickpopup没有设置回调的问题
    * OnDismissListener添加退出动画开始的回调
    * 优化模糊逻辑
    * 优化退出动画逻辑
  * **【Candy】2.1.9-beta3**(2019/03/1)
    * fixed [**#152**](https://github.com/razerdp/BasePopup/issues/152)
  * **【Candy】2.1.9-beta1**(2019/02/28)
    * 优化代码，修复覆盖动画监听器的bug，优化layout逻辑
  * **【Candy】2.1.9-beta**(2019/2/26)
    * 为模糊图片方法添加oom捕捉
  * **【Candy】2.1.9-alpha4**(2019/2/21)
    * 优化背景和局部模糊逻辑
  * **【Candy】2.1.9-alpha3**(2019/2/21)
    * 紧急修复alpha2留下的坑
  * **【Candy】2.1.9-alpha2**(2019/2/19)
    * 去除lib的AndroidManifest内容，预防冲突，fixed [**#149**](https://github.com/razerdp/BasePopup/issues/149)
  * **【Candy】2.1.9-alpha1**(2019/02/18)
    * 针对DialogFragment适配，fixed [**#145**](https://github.com/razerdp/BasePopup/issues/145)

* **【Release】2.1.8**(2019/01/26)
  * 本次版本更新添加了许多新特性哦~特别是不拦截事件的背景黑科技又回来了
  * 更新细节：
    * 适配使用了[**ImmersionBar**](https://github.com/gyf-dev/ImmersionBar)的情况
    * 修复对横屏不兼容的问题
    * 修复构造器传入宽高无效的问题
    * **支持不拦截事件下的背景蒙层，没错！那个黑科技换了个更友好的方式来啦~**
    * 修复popup弹出的时候，金刚键（虚拟按键）一同弹出的问题（锁屏回来导致焦点变化从而导致全屏Activity又出现虚拟导航栏这个不算哈）
      * fixed  [**#141**](https://github.com/razerdp/BasePopup/issues/120)、[**#59**](https://github.com/razerdp/BasePopup/issues/141)
      * fixed  [**#120**](https://github.com/razerdp/BasePopup/issues/120)、[**#59**](https://github.com/razerdp/BasePopup/issues/59)
    * QuickPopupConfig增加`dismissOnOutSideTouch()`方法
    * 优化QuickPopupBuilder，增加[**Wiki**](https://github.com/razerdp/BasePopup/wiki/QuickPopupBuilder)
    * 针对[**#138**](https://github.com/razerdp/BasePopup/issues/138)出现的问题进行优化
    * 修复`setAlignBackgroundGravity()`与`setAlignBackground()`互相覆盖导致的顺序硬性要求问题

* **【Candy】2.1.8**
  * **【Candy】2.1.8-prerelease**(2019/01/23)
    * 修复对横屏不兼容的问题
  * **【Candy】2.1.8-beta7**(2019/01/22)
    * beta3和beta4和beta5和beta6被我吃了~
    * 修复beta2关于focusable的问题，去掉无用代码
    * 修复构造器传入宽高无效的问题
    * **支持不拦截事件下的背景蒙层，没错！那个黑科技换了个更友好的方式来啦~**
  * **【Candy】2.1.8-beta2**(2019/01/22)
    * 修复popup弹出的时候，金刚键（虚拟按键）一同弹出的问题 **该功能目前测试中，如果有问题请务必反馈到candy**
      * fixed  [**#141**](https://github.com/razerdp/BasePopup/issues/120)、[**#59**](https://github.com/razerdp/BasePopup/issues/141)
    * QuickPopupConfig增加`dismissOnOutSideTouch()`方法
  * **【Candy】2.1.8-beta1**(2019/01/21)
    * 修复popup弹出的时候，金刚键（虚拟按键）一同弹出的问题 **该功能目前测试中，如果有问题请务必反馈到candy**
      * fixed  [**#120**](https://github.com/razerdp/BasePopup/issues/120)、[**#59**](https://github.com/razerdp/BasePopup/issues/59)
  * **【Candy】2.1.8-alpha2**(2019/01/18)
    * 优化QuickPopupBuilder，增加[**Wiki**](https://github.com/razerdp/BasePopup/wiki/QuickPopupBuilder)
  * **【Candy】2.1.8-alpha**(2019/01/17)
    * 针对[**#138**](https://github.com/razerdp/BasePopup/issues/138)出现的问题进行优化
    * 修复`setAlignBackgroundGravity()`与`setAlignBackground()`互相覆盖导致的顺序硬性要求问题

* **【Release】2.1.7**(2019/01/16)
  * 修复在`setAutoLocatePopup(true)`时，`onAnchorTop()`或`onAnchorBottom()`多次被调用的问题
  * 修复`setAllowInterceptTouchEvent(false)`时，因受默认限制而导致的无法定位到anchorView的问题
  * 优化弹起软键盘默认偏移量计算逻辑
  * 优化键盘高度计算逻辑
  * 感谢[**@ParfoisMeng**](https://github.com/ParfoisMeng)发现软键盘偏移问题并提交了PR[**PR#130**](https://github.com/razerdp/BasePopup/pull/130)
  * 发布2.1.7 release

* **【Candy】2.1.7-beta**(2019/01/10~2019/01/13)
  * 修复`setAllowInterceptTouchEvent(false)`时，因受默认限制而导致的无法定位到anchorView的问题
  * 优化弹起软键盘默认偏移量计算逻辑
  * 优化键盘高度计算逻辑

* **【Release】2.1.6**（2019/01/08）
  * 发布2.1.6-Release
  * 修复`preMeasure`方法错误的问题
  * 修复wrap_content下，在某个view显示同时底部空间不足以完整显示内容时无法完整显示内容的问题

* **【Candy】2.1.6-alpha2**（2019/01/03）
  * 修复`preMeasure`方法错误的问题
    * 感谢&fixed[**#125**](https://github.com/razerdp/BasePopup/issues/125)

* **【Candy】2.1.6-alpha**（2019/01/03）
  * 修复wrap_content下，在某个view显示同时底部空间不足以完整显示内容时无法完整显示内容的问题

* **【Release】2.1.5**(2019/01/02)
  * **新年新气象~祝大家新年快乐，zhu事顺意-V-**
  * 2.1.5 如期新年发布，改动如下：
    * 优化了获取是否展示虚拟按键的方法
    * 利用了另外一个骚方法来判断全面屏是否含有虚拟按键
      * >方法来源：[**掘金**](https://juejin.im/post/5bb5c4e75188255c72285b54)
    * 针对`showPopupWindow(anchorview)`同时`clipToScreen(true)`时，无法完整展示满屏的View的问题
    * 增加`setAlignBackgroundGravity()`方法，背景对齐的位置由您来制定~
      * <img src="https://github.com/razerdp/Pics/blob/master/BasePopup/wiki/align/alignbg_plus.gif" height="360"/>
    * 增加`update(int width ,int height)`方法
    * 修复构造器传入width/height失效的问题，增加setWidth/setHeight方法
    * 构造器增加延迟加载参数，如果您的Popup需要提前传参后，请在构造其中传入true以确认延迟加载
      * 如果使用延迟加载，初始化时机由您来制定，您需要调用`delayInit()`方法来进行BasePopup的初始化

* **【Candy】2.1.5**
  * **2.1.5-prerelease**(2018/12/29)
    * 优化了获取是否展示虚拟按键的方法
  * **2.1.5-beta5**(2018/12/29)
    * 用了另外一个骚方法来判断全面屏是否有虚拟按键
      * >方法来源：[**掘金**](https://juejin.im/post/5bb5c4e75188255c72285b54)
  * **2.1.5-beta4**(2018/12/27)
    * 针对`showPopupWindow(anchorview)`同时`clipToScreen(true)`时，无法完整展示满屏的view的问题
      * 该方法目前可能不稳定（某些极限情况没想到~）
  * **2.1.5-beta3**(2018/12/25)
    * 针对小米手势全面屏高度获取不准确的修复
    * **说实话，那么多魔改ROM，全面屏高度获取不准这东西也能改API，我表示无力吐槽了**
  * **2.1.5-beta2**(2018/12/25)
    * 修复2.1.5-beta默认backgroundgravity的问题
  * **2.1.5-beta**(2018/12/25)
    * 增加`setAlignBackgroundGravity()`方法，背景对齐的位置由您来制定~
    * 增加`update(int width ,int height)`方法
    * 修复构造器传入width/height失效的问题，增加setWidth/setHeight方法
    * <img src="https://github.com/razerdp/Pics/blob/master/BasePopup/wiki/align/alignbg_plus.gif" height="360"/>
  * **2.1.5-alpha2**(2018/12/23)
    * 构造器增加延迟加载参数，如果您的Popup需要提前传参后，请在构造其中传入true以确认延迟加载
      * 如果使用延迟加载，初始化时机由您来制定，您需要调用`delayInit()`方法来进行BasePopup的初始化
  * **2.1.5-alpha**(2018/12/23)
    * 适配刘海屏全面屏（双显示屏暂不适配）
    * 感谢[#114](https://github.com/razerdp/BasePopup/issues/114)的提供~
    * **Release年后发布，如果您有需要，请更新到此candy版。**

* **【Release】2.1.4**(2018/12/21)
  * **建议更新到这个版本！**
  * 非常抱歉~因为一时疏忽忘记合并一些东西，导致2.1.3版本在不拦截事件的情况下，无anchorView弹窗会导致位置问题，在2.1.4重新合并了代码，对此造成的影响，深表歉意。
    * 以后的版本一定会经过3个或以上的candy迭代仔细检查后再发！

* **【Release】2.1.3**(2018/12/21)
  * 正式发布2.1.3release
  * 增加[**linkTo(View)**](https://github.com/razerdp/BasePopup/wiki/API#linktoview-anchorview)方法
  * 支持update方法来跟随view或者指定位置更新
  * 全面优化系统原有的popupwindow定位方法，全版本统一。
  * 2.x的坑基本补完
  * 19年，我们再见-V-

* **【Candy】2.1.3-alpha2**(2018/12/20)
  * 增加`linkTo(View)`方法，跟随anchorView状态？一个方法就足够了~
  * 2.x的坑基本补完~如无意外，这个功能将会是18年最后一个功能性更新了

<img src="https://github.com/razerdp/Pics/blob/master/BasePopup/wiki/linkto/linkto.gif" height="360"/>

* **【Candy】2.1.3-alpha**(2018/12/19)
  * 支持update方法来跟随view或者指定位置更新
  * 调用`updatePopup()`方法即可~
  * 全面覆盖系统原有的popupwindow定位方法，全版本统一。

<img src="https://github.com/razerdp/Pics/blob/master/BasePopup/wiki/update/update.gif" height="360"/>

* **【Release】2.1.2**(2018/12/19)
  * 正式发布2.1.2release
  * 增加指定位置弹出的方法[**showPopupWindow(int x, int y)**](https://github.com/razerdp/BasePopup/blob/master/lib/src/main/java/razerdp/basepopup/BasePopupWindow.java#L681)
  * 修复内容宽高超过屏幕后`ClipToScreen()`修正不正确的问题
  * 输入法适配修复 fixed [#107](https://github.com/razerdp/BasePopup/issues/107)
  * preview:

<img src="https://github.com/razerdp/Pics/blob/master/BasePopup/wiki/anypos/anypos.gif" height="360"/>

* **【Release】2.1.1**(2018/12/13)
  * 针对setAlignBackground()失效的问题修复

* **【Release】2.1.0**(2018/12/12)
  * **双12大礼包~**
  * wiki大更新，留了大半年的坑终于快补完了，有问题请看[**wiki**](https://github.com/razerdp/BasePopup/wiki)
  * 正式发布2.1.0release
  * 2.1.0是对2.0的半彻底优化~为啥是一半呢？总得保留提升空间嘛嘿嘿
  * 更新的功能请看下方Candy的描述

* **【Candy】2.1.0-betaX**(2018/12)
  * **针对2.x的重构，撒花~**
  * 本次更新内容：
    * 去掉每次都add两次View的骚操作，合并成同一个PopupWindow，所以Layout Inspector不再让人迷惑为啥有两个PopupWindow了
    * 增加Gravity的支持，再也不用蛋疼的去计算蛋疼的offset了，而且布局文件真的可以wrap_content了
    * 接管layout过程，所以各个版本的PopupWindow都不一样？不存在滴。。。同时autoLocate支持RecyclerView啦~
    * 增加对contentView的xml的解析（前提是使用createPopupById方法），再也不怕被强制设置宽高了
    * fullScreen支持输入法布局适配！心塞了好久的问题终于解决
    * 增加对5.1和5.1.1官方两个PopupWindow重叠后在切换时层次变化的bug的适配
    * 优化代码，去除冗余代码
    * README大翻新

* **【Release】2.0.8.1**(2018/10/29)
  * **建议更新到这个版本！**
  * fixed [#94](https://github.com/razerdp/BasePopup/issues/94)
  * 紧急修复一个严重的bug[#95](https://github.com/razerdp/BasePopup/issues/95)，感谢[@tpnet](https://github.com/tpnet)
  * 优化代码

* **【Release】2.0.8**(2018/10/29)
  * fixed [#93](https://github.com/razerdp/BasePopup/issues/93)
  * 修复部分崩溃问题，发布release

* **2.0.8-alpha3**(2018/10/25)
  * fixed [#87](https://github.com/razerdp/BasePopup/issues/87)、[#89](https://github.com/razerdp/BasePopup/issues/89)、[#90](https://github.com/razerdp/BasePopup/issues/90)

* **2.0.8-alpha2**(2018/10/19)
  * 修复QuickPopupBuilder的click事件无响应问题，增加background方法
  * 修复设置background(0)时无法找到资源而崩溃的问题

* **2.0.7**(2018/10/15)
  * 绕开Android P的非公开api方法反射
    * 思路参考&&感谢[android_p_no_sdkapi_support](https://github.com/Guolei1130/android_p_no_sdkapi_support)
  * 发布2.0.7 release

* **2.0.6**(2018/10/09)
  * 不再抽象强制实现入场和退场动画
  * 针对自动弹出输入法的Popup，在dismiss()中默认关闭输入法

* 2018/09/30
  * 针对Match_Parent的dismissOutSide适配
  * 因为代码默认处理childCount==1的情况，如果有别的情况需要忽略点击的view，请调用setIgnoreDismissView()指定
  * 删除~~onInitDismissClickView()~~方法

* 2018/09/19
  * 继续丰富QuickPopupBuilder
  * 增加Demo:根据某个View控制各个方向的Popup。[示例](https://github.com/razerdp/BasePopup/blob/HEAD/app/src/main/java/razerdp/demo/fragment/LocatePopupFrag.java)
  
* **2.0.1-alpha2**(2018/09/06)
  * 增加快速构建QuickPopupBuilder
  * 尝试修复[#59](https://github.com/razerdp/BasePopup/issues/59)

* **2.0.1-alpha1** (2018/08/22)
  * 修复无法在onCreate()里面显示的问题
  * 增加setBackground(Drawable/ResourceId)方法，fixed [#79](https://github.com/razerdp/BasePopup/issues/79)
  * 正式版即将发布，。

* 2018/05/23
  * 添加注释

* 2018/05/14
  * **2.0.0-alpha1(candy)**

* 2018/04/19
  * 发布1.9.4(release) 
  * 修复autolocate的问题

* 2018/04/11
  * 1.9.4-alpha2(candy)
  * 修复误打包测试代码的alpha1

* 2018/04/09
  * 1.9.4-alpha(candy)
  * 本版本是预览版本，如果您有需要，可以更新到Candy版本，但不保证没有任何问题
  * 针对8.0进行修复
      * link: [issue#56](https://github.com/razerdp/BasePopup/issues/56)
      * link: [issue#61](https://github.com/razerdp/BasePopup/issues/61)
      * link: [issue#64](https://github.com/razerdp/BasePopup/issues/64)
  * 优化代码，HackWindowManager与HackPopupDecorView部分重构
  * showOnTop/showOnDown更名->onAnchorTop/onAnchorBottom，避免误导。

* 2018/01/23
  * 修复了在popup外滑动时`ViewGroup.LayoutParams`的cast异常
      * link: [issue#52](https://github.com/razerdp/BasePopup/issues/52)

* 2018/01/10
  * 发布1.9.2
  * 修复`HackDecorView`针对PopupWindow高度问题
  * 增加`setBlurBackgroundEnable()`模糊设置回调，允许自定义模糊操作
  * 修改为默认子线程模糊背景，同时增加blurImageView的模糊等待操作

* 2018/01/02
  * 修复可能出现的死循环问题以及去掉manifest文件冲突的问题
  * 部分方法名字修改，默认关闭 Log，如果您需要打印内部调试日志，请使用该方法：`BasePopupWindow.debugLog(true)`
  * 增加位移动画（百分比传值）,位移动画名字修正：`getTranslateAnimation()` -> `getTranslateVerticalAnimation()`
  * 模糊背景功能已经开放，针对单个View的模糊方法开放
  * 模糊背景允许子线程执行，默认主线程执行
  * gradle请在`defaultConfig`下添加两句：
    * renderscriptTargetApi 25
    * enderscriptSupportModeEnabled true
  * 发布1.9.1，其余bug修复

* 2017/12/28
  * 增加了一个window用于模糊层，增加模糊功能
    *  如果您需要模糊功能，仅仅需要调用一个方法：`setBlurBackgroundEnable()`
    * gradle请在`defaultConfig`下添加两句：
      * renderscriptTargetApi 25
      * enderscriptSupportModeEnabled true

* 2017/12/27
  * 增加演示demo：`DismissControlPopupFrag`
  * 增加两个方法用于touchEvent监听：`onTouchEvent()`&`onOutSideTouch()`
  * `HackPopupDecorView`继承`ViewGroup`而非`FrameLayout`，以解决PopupWindow的`decorView.getLayoutParams()`无法强转为`WindowManager.LayoutParams`的异常
  * 其余问题暂时没发现

* 2017/12/25 （圣诞节快乐~）
  * `BasePopupWindowProxy`和`PopupWindowProxy`权限收拢，不暴露放开
  * 优化`SimpleAnimUtil`，修改部分动画时间和插值器
  * 增加`setOutsideTouchable()`方法，和`setDismissWhenTouchOutside()`搭配使用有奇效哦
  * 增加`BasePopupHelper`优化`BasePopupWindow`代码可读性
  * 动画方面修正`AnimaView.clearAnimation()`->`Animation.cancel()`
  * 优化`showOnTop()`/`showOnDown()`方法。。。虽然可能没什么人用
  * 1.8.8版本因为一些问题而去除[#50](https://github.com/razerdp/BasePopup/issues/50)，替换为1.8.9
  * 【已解决】`setBackPressEnable()`在M以上已经可以自行决定是否允许返回键dismiss了，同时开放了keyEvent
    * 解决方案：[1.8.9 解决方案](https://github.com/razerdp/BasePopup/blob/master/%E5%85%B3%E4%BA%8EAndorid%20M%E4%BB%A5%E4%B8%8AsetBackPressEnable()%E5%A4%B1%E6%95%88%E7%9A%84%E9%97%AE%E9%A2%98%E7%9A%84%E5%88%86%E6%9E%90.md#189-%E8%A7%A3%E5%86%B3%E6%96%B9%E6%A1%88)
    * issue:[#33](https://github.com/razerdp/BasePopup/issues/33)
    * `BasePopupWindow`增加两个方法用于keyEvent的监听：`onDispatchKeyEvent()`&`onBackPressed()`
    * 感谢诸位热烈的讨论~
  * 部分方法名更改
    * `setOutsideTouchable()`->`setInterceptTouchEvent()`，该方法会影响焦点问题，即便是解决了`backPress`若这个方法设置为false，依然不会响应backpress
    

* 2017/11/27
  * 抽取`PopupWindowProxy`->`BasePopupWindowProxy`
  * 归类各种蛋疼的`showAsDropDown`适配->`PopupCompatManager`
  * 修正部分命名和方法名以及注释名错误的问题
    * 感谢简书小伙伴的评论，否则我还真发现不了。。。
    * 评论地址：[点我](http://www.jianshu.com/p/069f57e14a9c#comment-17669137)
    * 根据简书id，只能猜测他的github id：(Chenley)[https://github.com/Chenley]，如果您见到并发现我这个猜测是错的，请及时联系我-V-
    * 非常感谢你们的issue
  * 修复部分issue：[#46](https://github.com/razerdp/BasePopup/issues/46)

* 2017/09/20
  * 构造器不再限定为activity，context采用弱引用

* 2017/07/03
  * 集中修复了offset计算问题、7.0的showAsDropDown的问题，如果您还有什么疑问，请在issue里面提出

* 2017/01/12
  * 使用PopupWindowProxy，覆写dismiss();

* 2016-12-12
  * 现在`showPopupWindow(View v)`或者`showPopupWindow(int resid)`将会把popupwindow与anchorView挂钩哦，左上角会对齐（width=match_parent除外）
  * 另外增加一个执行popup前的回调`OnBeforeShowCallback`，与beforedismiss一样，返回false则不执行showpopup，另外在这里可以先实现offsetX或者offsetY哦~
  * 详情看[issue11](https://github.com/razerdp/BasePopup/issues/11)

* 2016-12-07
  * `ondismissListener`增加`onBeforeDismiss()`方法，在执行dismiss之前根据该值确定是否执行dismiss
  * 也可以在执行dismiss前执行一些操作，详情操作请看`SlideFromTopPopupFrag.java`



* 2016-12-06
  * 因为某些情况下需要用到showAsDropDown，因此增加dropdown方法`setShowAtDown()`详情看issue:[#issue10](https://github.com/razerdp/BasePopup/issues/10)
  * 另外增加一个点击popup外部不消失的方法，默认点击外部消失`setDismissWhenTouchOuside()`
  * 去除复杂的setRelativePivot方法，更新demo工程


* 2016-12-02
  * 修复setOnDismissListener的错误 [#issue9](https://github.com/razerdp/BasePopup/issues/9)


* 2016-11-23
  * 构造方法的view点击事件设置问题修复/getInputView必须返回edittext

* 2016-11-23
 * 增加了一些方法：
   * `setRelativePivot()`：该方法用于设置popup的参考中心点（相对于anchorView左上角）,使用注解，@RelativePivot
     * 在上述方法的前提下，增加了偏移量的方法
       * `setOffsetX()`:x偏移量，跟中心点有关,假如参考点在右边，那么正数则是远离参考view，负数相反，类似于margin
       * `setOffsetX()`:y偏移量，同上

 * `setAutoLocatePopup()`:设置popup是否自适配屏幕，当popup显示位置不足以支撑其完整显示的时候，将会自动调整(比如上方正常显示，在下方无法显示的时候则显示在上方)
   * 此时同样可以使用上述方法`setRelativePivot()`等

 * `getPopupViewWidth()/getPopupViewHeight()`:在创建view的时候就进行了view.measure，但是不能保证这两个值是完全可信的(比如popup内部是个listview?)
 * `setRelativeToAnchorView()`:是否参考锚点view，事实上，如果使用了`setRelativePivot()`，该值自动为ture
 * `setPopupGravity()`:设置poup的gravity，虽然一般情况下不建议设置，毕竟它的gravity很多时候都是相对于整个rootView来说的，容易混乱



* 2016-10-11
  * 修正api>21下出现的popup无法突破状态栏的问题(method:setPopupWindowFullScreen(boolean))

* 2016-05-20
  * 增加了popup的淡入淡出效果，道理很简单。。。弄回sytle，同时开放style的设置方法。淡入淡出效果是默认的，如果您不需要淡入淡出效果，可以通过setNeedPopupFade（false）取消

* 2016-05-20
  * 因为发现展示了Popup后，如果不断地dismiss，会导致动画重复播放，然后最终留下一个蒙层，因此加入了防止执行动画过程中再次被执行的标志
下一阶段期望增加：蒙层淡入淡出的方法

* 2016-05-18
  * 降低最高版本要求23->21，因为开发过程中发现有时候需要dismiss动画有时候不需要，因此增加dismissWithOutAnima方法

* 2016-03-05
  * 修改名字ViewCreate->BasePopup

* 2016-02-23
  * 去除master分支的兼容包，最低要求api 16，需要兼容到2.3请查看master-api 9分支

* 2016-01-28
  * 修改了BasePopupWindow，将部分抽象方法改为public，防止子类继承的时候必须实现过多无用方法，保持整洁性。

* 2016-01-28 因为好奇，研究了一下插值器，发现了一个很好玩的网站http://inloop.github.io/interpolator/ </br>
这个网站有着可视化插值器和公式，于是这回就把公式集成了下来，做出了各种插值器的popup，但其实只有第一个最好玩，其他的马马虎虎。这次的自定义插值器是继承LinearInterpolator然后通过网站上的公式进行计算后复写对应接口实现的。同时将BasePopupWindow的一些anima变量改为protected。
  * CustomInterpolatorPopup.java:
    * ![image](https://github.com/razerdp/BasePopup/blob/master/img/interpolator_popup.gif)

* 2016-01-25 修复了inputView无法自动弹出输入法的问题（原因可能是因为popup在show出来后才可以获取焦点，而我们的inputview一开始就findViewById出来了），所以可能是null。

* 2016-01-25 增加了一个好玩的其实并无卵用的的dialog popup，看着好玩~gif图因为帧率问题，高帧慢，低帧丢细节，所以看起来效果不太好，实际效果很好玩的-V-</br>
  * DialogPopup.java:
    * ![image](https://github.com/razerdp/BasePopup/blob/master/img/dialog_popup.gif)

* 2016-01-23 忽然发现一直以来提交代码的帐号是我的子帐号，现在切换回来。。。。

* 2016-01-22 增加了一个常见的菜单式的popup(demo)，关于动画问题，我配置的是简单的缩放和透明度变化，可以按照个人爱好定义,顺便修正了BasePopupWindow的一个小小的坑</br>
  * MenuPopup.java:
    *![image](https://github.com/razerdp/BasePopup/blob/master/img/menu_popup.gif)

* 2016-01-20 增加了包含listview的popup(demo)，这个popup将采用builder模式构造，同时点击事件可以通过绑定clickTag来建立一个映射关系，这样就不用判断点击的位置来执行对应的步骤（当然，点击位置这个传统的操作还是保留的）</br>
  * ListPopup.java:
    * ![image](https://github.com/razerdp/BasePopup/blob/master/img/list_popup.gif)

* 2016-01-19 稍微重构BasePopupWindow，在构造器把getXXX各种get方法赋值，防止每次调用的时候都new一个对象导致的性能问题&因为对象地址不对导致的各种奇葩问题</br>

* 2016-01-18 增加了含有输入框的popup(demo)，同时修复了dismiss由于调用getExitAnima()但是setListener/addListener无效的问题（原因是getExitAnima()属于重新new出来的动画，调用多次后，listener指向的并非同一个对象，所以无效）</br>
  * InputPopup.java:
    * ![image](https://github.com/razerdp/BasePopup/blob/master/img/input_popup.gif)

* 2016-01-16 增加仿朋友圈评论的popup(demo)</br>

* 同日的16:39 尝试添加了退出动画
  * ![image](https://github.com/razerdp/BasePopup/blob/master/img/comment_popup_with_exitAnima.gif)
    * 解析:http://blog.csdn.net/mkfrank/article/details/50532956

* CommentPopup.java(该frag标题名字忘改回来了。。。录制了gif也就懒得动了):
  * ![image](https://github.com/razerdp/BasePopup/blob/master/img/comment_popup.gif)

* 2016-01-15 增加两种继承basepopup实现的常见Popup(demo)</br>
  * ScalePopup.java:
    * ![image](https://github.com/razerdp/BasePopup/blob/master/img/scale_popup.gif)
      * 解析:http://blog.csdn.net/mkfrank/article/details/50523702

* SlideFromBottomPopup.java:
  * ![image](https://github.com/razerdp/BasePopup/blob/master/img/slide_from_bottom_popup.gif)
    * 解析：http://blog.csdn.net/mkfrank/article/details/50527159

