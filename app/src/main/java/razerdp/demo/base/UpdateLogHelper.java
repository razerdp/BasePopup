package razerdp.demo.base;

import java.util.ArrayList;
import java.util.List;

import razerdp.demo.model.updatelog.UpdateLogInfo;

/**
 * Created by 大灯泡 on 2019/9/24.
 */
public class UpdateLogHelper {

    private static List<UpdateLogInfo> sCache;

    public static List<UpdateLogInfo> getUpdateLogs() {
        if (sCache == null) {
            sCache = new ArrayList<>();
            buildUpdateLogs();
        }
        return sCache;
    }

    private static void buildUpdateLogs() {
        addTips();
        addCandy2_2_2();
        addRelease2_2_1();
        addCandy2_2_1();
        addRelease2_2_0();
        addRelease2_1_9();
        addRelease2_1_8();
        addRelease2_1_7();
        addRelease2_1_6();
        addRelease2_1_5();
        addRelease2_1_4();
        addRelease2_1_3();
        addRelease2_1_1();
        addRelease2_1_0();
        addRelease2_0_8_1();
        addRelease2_0_8();
        addRelease2_0_7();
        addRelease2_0_6();
    }


    private static void addTips() {
        sCache.add(new UpdateLogInfo("为防止内容过多，2.2.1之前的Candy版本不再展示，仅展示Release版本")
                .append("如果内容过多可以左右滑动哦~")
                .append("#issue是可以点击的~")
                .root());
    }

    private static void addCandy2_2_2() {
        sCache.add(new UpdateLogInfo("【Candy】2.2.2(2020/02)")
                .append("重构BasePopupWindow，增加BaseLazyPopupWindow")
                .append("精简代码，重构整体框架，去掉多数冗余的、实现逻辑复杂的方法")
                .append("解决遗留的内存泄漏问题")
                .append("去除 limitScreed()方法")
                .append("重构DecorViewProxy，现在蒙层的适配重新交由系统，BasePopupWindow只解决位置问题")
                .append("优化Measure，添加跟AnchorView关联时高度或者宽度为Match_parent无法填满剩余空间的问题")
                .append("解决Android 10 黑/灰名单问题")
                .append("修复outSideTouchable下键盘适配的问题")
                .append("去除onAnchorTop/onAnchorBottom方法，后续将会重构这一部分，替换为别的方法，暂时屏蔽该方法")
                .append("bug fixed：")
                .child("#236/#242").keyAt("#236").keyAt("#242").end()
                .root());
    }

    private static void addRelease2_2_1() {
        sCache.add(new UpdateLogInfo("【Release】2.2.1(2019/06/24)")
                .append("支持Service或者非ActivityContext里弹窗")
                .append("优化PopupUiUtils，优化获取屏幕宽高算法")
                .child("fixed #186、#167").keyAt("#186").keyAt("#167").end()
                .child("fixed #188(not perfect)").keyAt("#188").end()
                .append("修改并优化键盘判断逻辑")
                .append("优化全屏状态下点击范围的判定，fixed #200").keyAt("#200").url("https://github.com/razerdp/BasePopup/issues/200")
                .root());
    }

    private static void addCandy2_2_1() {
        sCache.add(new UpdateLogInfo("【Candy】2.2.1(2019/05/16)")
                .append("【Candy】190516").child("支持Service或者非ActivityContext里弹窗")
                .append("【Candy】190517")
                .child("优化PopupUiUtils，优化获取屏幕宽高算法").parent()
                .child("fixed #186、#167").keyAt("#186").keyAt("#167").end()
                .child("fixed #188(not perfect)").keyAt("#188").end()
                .append("【Candy】190522").child("修改并优化键盘判断逻辑")
                .append("【Candy】190611").child("优化全屏状态下点击范围的判定，fixed #200").keyAt("#200").root());
    }

    private static void addRelease2_2_0() {
        sCache.add(new UpdateLogInfo("【Release】2.2.0(2019/05/15)")
                .append("正式版2.2.0隆重归来，这次正式版又是一个重构版本哦~")
                .append("优化输入法对齐逻辑")
                .append("重构模糊逻辑：").bold()
                .child("经测试，720p的手机在默认参数下全屏模糊时间平均在6ms~16ms之间").parent()
                .child("增大默认参数的模糊程度").parent()
                .child("模糊淡入淡出时间跟随Popup的动画时间").parent()
                .child("修复模糊偶尔失效的情况").parent()
                .append("测量/布局相关：").bold()
                .child("现在在clipToScreen的情况下，会根据剩余空间对PopupDecor进行重新测量，以保证Popup完整的显示，如果您需要保持原始的测量值，请调用keepSize(true)").keyAt("clipToScreen").bold().keyAt("keepSize(true)").bold().end()
                .child("重构layout逻辑，针对outSideTouch优化").keyAt("outSideTouch").end()
                .child("适配屏幕旋转，fix #180").keyAt("#180").end()
                .child("采取flag代替各种boolean，清爽更简洁").parent()
                .child("减少冗余代码").parent()
                .append("优化相关：").bold()
                .child("增加GravityMode值，现在允许您配置PopupGravity的参考模式啦~").keyAt("GravityMode").bold().keyAt("PopupGravity").bold().self()
                .child("RELATIVE_TO_ANCHOR：默认模式，以Anchor为参考点，指定PopupWindow显示在Anchor的方位").keyAt("RELATIVE_TO_ANCHOR").bold().end()
                .child("ALIGN_TO_ANCHOR_SIDE：对齐模式，以Anchor的边为参考点，指定PopupWindow的边与Anchor的哪条边对齐").keyAt("ALIGN_TO_ANCHOR_SIDE").bold().end().end()
                .child("增加minWidth/minHeight 方法，增加maxWidth/maxHeight 方法，让他们相互对应~").parent()
                .child("修复高度为match_parent和wrap_content的测量差异，现在可以安心地玩耍啦").parent()
                .child("部分Api标记过时：")
                .child("setAllowDismissWhenTouchOutside -> setOutSideDismiss").keyAt("setAllowDismissWhenTouchOutside").delete().keyAt("setOutSideDismiss").bold().end()
                .child("setAllowInterceptTouchEvent -> setOutSideTouchable").keyAt("setAllowInterceptTouchEvent").delete().keyAt("setOutSideTouchable").bold().end().end()
                .child("增加setBackgroundView(View)方法，现在BasePopup的背景控件可以随意由你定制啦~当然PopupWindow的背景动画控制方法依旧生效").keyAt("setBackgroundView(View)").bold().end()
                .append("包拆分：").bold()
                .child("现在BasePopup将会进行包的拆分，源工程仅针对没有任何依赖的原生Android进行适配，如果您需要别的适配，请分别依赖以下模块或多个模块：")
                .child("如果您需要support库的支持，比如DialogFragment支持，请依赖").keyAt("support").bold().self()
                .child("implementation 'com.github.razerdp:BasePopup-compat-support:{$latestVersion}'").keySelf().tag().end().end()
                .child("如果您需要lifecycle库的支持，比如destroy里自动释放或者关闭等，请依赖").keyAt("lifecycle").self()
                .child("implementation 'com.github.razerdp:BasePopup-compat-lifecycle:{$latestVersion}'").keySelf().tag().end().end()
                .child("如果您需要androidX库的支持，请依赖").keyAt("androidX").bold().self()
                .child("implementation 'com.github.razerdp:BasePopup-compat-androidx:{$latestVersion}'").keySelf().tag().end().end()
                .child("请注意，如果您依赖了androidX支持组件，请不要依赖另外两个支持组件，否则会冲突").bold().end()
                .append("Bug fixed：").bold()
                .child("fix #171、#181、#182、#183").keyAt("#171").keyAt("#181").keyAt("#182").keyAt("#183").end()
                .child("fix #180").keyAt("#180").end()
                .child("fix #164").keyAt("#164").end()
                .append("Other：").bold()
                .child("add 996 license")
                .root());
    }

    private static void addRelease2_1_9() {
        sCache.add(new UpdateLogInfo("【Release】2.1.9(2019/03/07)")
                .append("优化对android P刘海的支持，允许PopupWindow布局到刘海，fixed #154").keyAt("#154").end()
                .append("修复quickpopup没有设置回调的问题")
                .append("OnDismissListener添加退出动画开始的回调")
                .append("优化模糊逻辑")
                .append("优化退出动画逻辑")
                .append("fixed #152").keyAt("#152").end()
                .append("优化代码，修复覆盖动画监听器的bug，优化layout逻辑")
                .append("为模糊图片方法添加oom捕捉")
                .append("优化背景和局部模糊逻辑")
                .append("去除lib的AndroidManifest内容，预防冲突，fixed #149").keyAt("#149").end()
                .append("针对DialogFragment适配，fixed #145").keyAt("#149").end()
                .root());
    }

    private static void addRelease2_1_8() {
        sCache.add(new UpdateLogInfo("【Release】2.1.8(2019/01/26)")
                .append("本次版本更新添加了许多新特性哦~特别是不拦截事件的背景黑科技又回来了")
                .append("更新细节：").bold()
                .child("适配使用了ImmersionBar的情况").keyAt("ImmersionBar").url("https://github.com/gyf-dev/ImmersionBar").end()
                .child("修复对横屏不兼容的问题").parent()
                .child("修复构造器传入宽高无效的问题").parent()
                .child("支持不拦截事件下的背景蒙层，没错！那个黑科技换了个更友好的方式来啦~").keySelf().bold().end()
                .child("修复popup弹出的时候，金刚键（虚拟按键）一同弹出的问题（锁屏回来导致焦点变化从而导致全屏Activity又出现虚拟导航栏这个不算哈）")
                .child("fixed #120、#141、#59").keyAt("#120").keyAt("#141").keyAt("#59").end().end()
                .child("QuickPopupConfig增加dismissOnOutSideTouch()方法").keyAt("dismissOnOutSideTouch()").tag().end()
                .child("优化QuickPopupBuilder，增加Wiki").keyAt("Wiki").bold().url("https://github.com/razerdp/BasePopup/wiki/QuickPopupBuilder").end()
                .child("针对#138出现的问题进行优化").keyAt("#138").end()
                .child("修复setAlignBackgroundGravity()与setAlignBackground()互相覆盖导致的顺序硬性要求问题").keyAt("setAlignBackgroundGravity()").bold().keyAt("setAlignBackground()").bold()
                .end().root());
    }

    private static void addRelease2_1_7() {
        sCache.add(new UpdateLogInfo("【Release】2.1.7(2019/01/16)")
                .append("修复在setAutoLocatePopup(true)时，onAnchorTop()或onAnchorBottom()多次被调用的问题").keyAt("setAutoLocatePopup(true)").bold().keyAt("onAnchorTop()").bold().keyAt("onAnchorBottom()").bold().end()
                .append("修复setAllowInterceptTouchEvent(false)时，因受默认限制而导致的无法定位到anchorView的问题").keyAt("setAllowInterceptTouchEvent(false)").bold().end()
                .append("优化弹起软键盘默认偏移量计算逻辑")
                .append("优化键盘高度计算逻辑")
                .append("感谢@ParfoisMeng发现软键盘偏移问题并提交了PRPR#130").keyAt("@ParfoisMeng").bold().url("https://github.com/ParfoisMeng").keyAt("PR#130").bold().url("https://github.com/razerdp/BasePopup/pull/130").end()
                .append("发布2.1.7 release")
                .root());
    }

    private static void addRelease2_1_6() {
        sCache.add(new UpdateLogInfo("【Release】2.1.6（2019/01/08）")
                .append("发布2.1.6-Release")
                .append("修复preMeasure方法错误的问题").keyAt("preMeasure").bold().end()
                .append("修复wrap_content下，在某个view显示同时底部空间不足以完整显示内容时无法完整显示内容的问题")
                .root());
    }

    private static void addRelease2_1_5() {
        sCache.add(new UpdateLogInfo("【Release】2.1.5(2019/01/02)")
                .append("新年新气象~祝大家新年快乐，zhu事顺意-V-").bold()
                .append("2.1.5 如期新年发布，改动如下：")
                .child("优化了获取是否展示虚拟按键的方法").parent()
                .child("利用了另外一个骚方法来判断全面屏是否含有虚拟按键")
                .child("方法来源：掘金").keyAt("掘金").bold().url("https://juejin.im/post/5bb5c4e75188255c72285b54").end().end()
                .child("针对showPopupWindow(anchorview)同时clipToScreen(true)时，无法完整展示满屏的View的问题").keyAt("showPopupWindow(anchorview)").bold().keyAt("clipToScreen(true)").bold().end()
                .child("增加setAlignBackgroundGravity()方法，背景对齐的位置由您来制定~").end()
                .child("增加update(int width ,int height)方法").end()
                .child("增加update(int width ,int height)方法").end()
                .child("修复构造器传入width/height失效的问题，增加setWidth/setHeight方法").end()
                .child("构造器增加延迟加载参数，如果您的Popup需要提前传参后，请在构造其中传入true以确认延迟加载")
                .child("如果使用延迟加载，初始化时机由您来制定，您需要调用delayInit()方法来进行BasePopup的初始化").keyAt("delayInit()").bold()
                .root());
    }

    private static void addRelease2_1_4() {
        sCache.add(new UpdateLogInfo("【Release】2.1.4(2018/12/21)")
                .append("建议更新到这个版本！").bold()
                .append("非常抱歉~因为一时疏忽忘记合并一些东西，导致2.1.3版本在不拦截事件的情况下，无anchorView弹窗会导致位置问题，在2.1.4重新合并了代码，对此造成的影响，深表歉意。")
                .child("以后的版本一定会经过3个或以上的candy迭代仔细检查后再发！")
                .root());
    }

    private static void addRelease2_1_3() {
        sCache.add(new UpdateLogInfo("【Release】2.1.3(2018/12/21)")
                .append("正式发布2.1.3release")
                .append("增加linkTo(View)方法").keyAt("linkTo(View)").url("https://github.com/razerdp/BasePopup/wiki/API#linktoview-anchorview")
                .append("支持update方法来跟随view或者指定位置更新")
                .append("全面优化系统原有的popupwindow定位方法，全版本统一。")
                .append("2.x的坑基本补完")
                .append("19年，我们再见-V-")
                .root());

    }

    private static void addRelease2_1_1() {
        sCache.add(new UpdateLogInfo("【Release】2.1.1(2018/12/13)")
                .append("针对setAlignBackground()失效的问题修复")
                .root());
    }

    private static void addRelease2_1_0() {
        sCache.add(new UpdateLogInfo("【Release】2.1.0(2018/12/12)")
                .append("双12大礼包~").bold()
                .append("wiki大更新，留了大半年的坑终于快补完了，有问题请看wiki").keyAt("wiki").url("https://github.com/razerdp/BasePopup/wiki")
                .append("正式发布2.1.0release")
                .append("2.1.0是对2.0的半彻底优化~为啥是一半呢？总得保留提升空间嘛嘿嘿")
                .append("针对2.x的重构，撒花~").bold()
                .append("本次更新内容：")
                .child("去掉每次都add两次View的骚操作，合并成同一个PopupWindow，所以Layout Inspector不再让人迷惑为啥有两个PopupWindow了").end()
                .child("增加Gravity的支持，再也不用蛋疼的去计算蛋疼的offset了，而且布局文件真的可以wrap_content了").end()
                .child("接管layout过程，所以各个版本的PopupWindow都不一样？不存在滴。。。同时autoLocate支持RecyclerView啦~").end()
                .child("增加对contentView的xml的解析（前提是使用createPopupById方法），再也不怕被强制设置宽高了").end()
                .child("fullScreen支持输入法布局适配！心塞了好久的问题终于解决").end()
                .child("增加对5.1和5.1.1官方两个PopupWindow重叠后在切换时层次变化的bug的适配").end()
                .child("优化代码，去除冗余代码").end()
                .child("README大翻新").end()
                .root());
    }

    private static void addRelease2_0_8_1() {
        sCache.add(new UpdateLogInfo("【Release】2.0.8.1(2018/10/29)")
                .append("建议更新到这个版本！").bold()
                .append("fixed #94").keyAt("#94").end()
                .append("紧急修复一个严重的bug#95，感谢@tpnet").keyAt("#95").keyAt("@tpnet").url("https://github.com/tpnet").end()
                .append("优化代码")
                .root());

    }

    private static void addRelease2_0_8() {
        sCache.add(new UpdateLogInfo("【Release】2.0.8(2018/10/29)")
                .append("fixed #93").keyAt("#93").end()
                .append("修复部分崩溃问题，发布release")
                .root());
    }

    private static void addRelease2_0_7() {
        sCache.add(new UpdateLogInfo("2.0.7(2018/10/15)")
                .append("绕开Android P的非公开api方法反射")
                .child("思路参考&&感谢android_p_no_sdkapi_support").keyAt("android_p_no_sdkapi_support").url("https://github.com/Guolei1130/android_p_no_sdkapi_support").end()
                .root());
    }

    private static void addRelease2_0_6() {
        sCache.add(new UpdateLogInfo("2.0.6(2018/10/09)")
                .append("不再抽象强制实现入场和退场动画")
                .append("针对自动弹出输入法的Popup，在dismiss()中默认关闭输入法")
                .root());
    }
}
