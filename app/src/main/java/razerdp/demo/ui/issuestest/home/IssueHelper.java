package razerdp.demo.ui.issuestest.home;

import java.util.Collections;
import java.util.List;

import razerdp.demo.model.issue.IssueInfo;
import razerdp.demo.ui.issuestest.Issue210TestActivity;
import razerdp.demo.ui.issuestest.Issue224TestActivity;
import razerdp.demo.ui.issuestest.Issue226TestActivity;
import razerdp.demo.ui.issuestest.Issue230TestActivity;
import razerdp.demo.ui.issuestest.Issue236TestActivity;
import razerdp.demo.ui.issuestest.Issue238TestActivity;
import razerdp.demo.ui.issuestest.Issue242TestActivity;
import razerdp.demo.ui.issuestest.Issue277TestActivity;
import razerdp.demo.ui.issuestest.Issue358TestActivity;
import razerdp.demo.ui.issuestest.Issue369TestActivity;
import razerdp.demo.utils.DescBuilder;
import razerdp.demo.utils.StringUtil;

/**
 * Created by 大灯泡 on 2019/10/8.
 */
class IssueHelper {

    static List<IssueInfo> addIssues(List<IssueInfo> result) {
        add_224(result);
        add_210(result);
        add_226(result);
        add_230(result);
        add_236(result);
        add_238(result);
        add_242(result);
        add_277(result);
        add_358(result);
        add_369(result);

        sort(result);

        return result;
    }

    private static void sort(List<IssueInfo> result) {
        Collections.sort(result, (o1, o2) -> {
            int issue = StringUtil.toInt(o1.issue);
            int issue2 = StringUtil.toInt(o2.issue);
            return Integer.compare(issue, issue2);
        });
    }

    private static void add_369(List<IssueInfo> result) {
        IssueInfo issue369 = new IssueInfo();
        issue369.setActivityClass(Issue369TestActivity.class)
                .setIssue("369")
                .setFixed(true)
                .setTitle("弹出popupwindow不收起键盘")
                .setDesc(DescBuilder.get()
                        .append("系统版本：Unknown")
                        .append("库版本：2.2.9")
                        .append("键盘是已经打开的的时候，长按item会触发pop弹出，会影响键盘自动收起。\n" +
                                "使用原生的popwindow，宽高使用WRAP_CONTENT，弹出是不会影响的，希望此库也能支持。谢谢作者")
                        .build());

        result.add(issue369);
    }

    private static void add_358(List<IssueInfo> result) {
        IssueInfo issue358 = new IssueInfo();
        issue358.setActivityClass(Issue358TestActivity.class)
                .setIssue("358")
                .setFixed(true)
                .setTitle("让键盘跟随recyclerview中的edittext")
                .setDesc(DescBuilder.get()
                        .append("系统版本：10.0")
                        .append("库版本：2.2.8")
                        .append("Popup内包含RecyclerView，希望在键盘弹出之前能够定位到RecyclerView内的某个View")
                        .build());

        result.add(issue358);
    }

    private static void add_277(List<IssueInfo> result) {
        IssueInfo issue277 = new IssueInfo();
        issue277.setActivityClass(Issue277TestActivity.class)
                .setIssue("277")
                .setFixed(true)
                .setTitle("BasePopupHelper#GlobalLayoutListener#onGlobalLayout键盘检测问题")
                .setDesc(DescBuilder.get()
                        .append("系统版本：UnKnown")
                        .append("库版本：unknown")
                        .append("键盘可以正常弹出，但是在 BasePopupHelper#GlobalLayoutListener#onGlobalLayout 的上述判断位置存在问题\n设置 adjustResize 会导致 Activity content 高度减小，从而得出的键盘高度会为负值或者比content 高度的 1/4 小。因此\n" +
                                "boolean isVisible = keyboardRect.height() > (screenHeight >> 2) && isOpen();\n" +
                                "会出错，导致弹窗不会上移从而键盘遮挡弹窗的输入框。")
                        .build());

        result.add(issue277);
    }

    private static void add_224(List<IssueInfo> result) {
        IssueInfo issue224 = new IssueInfo();
        issue224.setActivityClass(Issue224TestActivity.class)
                .setIssue("224")
                .setFixed(true)
                .setTitle("想实现autocompletetextview类似的EditText模糊查找数据功能")
                .setDesc(DescBuilder.get()
                        .append("系统版本：UnKnown")
                        .append("库版本：unknown")
                        .append("想实现autocompletetextview类似的EditText模糊查找数据功能，弹出popupwindow后EditText的键盘输入事件被popwindow拦截了\nContentView大小变化后无法挂载到原AnchorView")
                        .build())
                .appendPic("https://user-images.githubusercontent.com/15073931/64840685-f17f4400-d62e-11e9-99e1-4f324e77c813.gif");

        result.add(issue224);
    }

    private static void add_210(List<IssueInfo> result) {
        IssueInfo issue210 = new IssueInfo();
        issue210.setActivityClass(Issue210TestActivity.class)
                .setIssue("210")
                .setFixed(true)
                .setTitle("setOutSideTouchable（true）显示不正常")
                .setDesc(DescBuilder.get()
                        .append("系统版本：android p")
                        .append("库版本：release 2.2.1")
                        .append("问题描述/重现步骤：调用setOutSideTouchable（true）方法，再showPopupWindow（view)时，位置发生偏移，去掉该方法或者setOutSideTouchable（false）则正常显示。")
                        .build())
                .appendPic("https://user-images.githubusercontent.com/11664870/61995671-feb39400-b0bd-11e9-8176-81388d0703d5.png")
                .appendPic("https://user-images.githubusercontent.com/11664870/61995674-070bcf00-b0be-11e9-996c-253955d32969.png");

        result.add(issue210);
    }

    private static void add_226(List<IssueInfo> result) {
        IssueInfo issue210 = new IssueInfo();
        issue210.setActivityClass(Issue226TestActivity.class)
                .setIssue("226")
                .setFixed(true)
                .setTitle("横屏下，键盘不会顶起Popup")
                .setDesc(DescBuilder.get()
                        .append("系统版本：华为P9（8.0），Vivo Y67A（6.0）")
                        .append("库版本：release 2.2.1")
                        .append("问题描述/重现步骤：把Demo里面的Activity设置为横屏，更多具体例子-从底部上滑的输入法。")
                        .build());
        result.add(issue210);
    }

    private static void add_230(List<IssueInfo> result) {
        IssueInfo issue230 = new IssueInfo();
        issue230.setIssue("230")
                .setFixed(true)
                .setActivityClass(Issue230TestActivity.class)
                .setTitle("update不起作用")
                .setDesc(DescBuilder.get()
                        .append("系统版本：android 9.0")
                        .append("库版本：release 2.2.1")
                        .append("popup布局里面有recyclerview")
                        .append("填充rv的数据售，有点击按钮，可以删除rv的item")
                        .append("使用update() 或者update(width,height-item的高度)")
                        .append("popup的布局高度都不会改变")
                        .build());
        result.add(issue230);
    }

    private static void add_236(List<IssueInfo> result) {
        IssueInfo issue236 = new IssueInfo();
        issue236.setIssue("236")
                .setFixed(true)
                .setActivityClass(Issue236TestActivity.class)
                .setTitle("2.2.1 release版本 xml里面的match_parent无效")
                .setDesc(DescBuilder.get()
                        .append("系统版本：UNKNOWN")
                        .append("库版本：release 2.2.1")
                        .append("2.2.1 release版本。xml里面的match_parent无效，必须在代码里再设置一次setWidht(getScreenWidth())才行。2.2.0没有此问题")
                        .build());
        result.add(issue236);
    }

    private static void add_238(List<IssueInfo> result) {
        IssueInfo issue238 = new IssueInfo();
        issue238.setIssue("238")
                .setFixed(true)
                .setActivityClass(Issue238TestActivity.class)
                .setTitle("2.2.0 release版本 contentView的点击事件，需要点击两次才能触发")
                .setDesc(DescBuilder.get()
                        .append("系统版本：9.0")
                        .append("库版本：release 2.2.0")
                        .append("contentView的点击事件，需要点击两次才能触发")
                        .build());
        result.add(issue238);
    }

    private static void add_242(List<IssueInfo> result) {
        IssueInfo issue242 = new IssueInfo();
        issue242.setIssue("242")
                .setFixed(true)
                .setActivityClass(Issue242TestActivity.class)
                .setTitle("Release 2.2.1 Service Popup 失效")
                .setDesc(DescBuilder.get()
                        .append("系统版本：UNKNOWN")
                        .append("库版本：release 2.2.1")
                        .append("Activity不在前台，同时Service中弹出BasePopup，无法弹出。")
                        .build());
        result.add(issue242);
    }
}
