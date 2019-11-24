package razerdp.demo.ui.issuestest.home;

import java.util.Collections;
import java.util.List;

import razerdp.demo.model.issue.IssueInfo;
import razerdp.demo.ui.issuestest.Issue210TestActivity;
import razerdp.demo.ui.issuestest.Issue226TestActivity;
import razerdp.demo.ui.issuestest.Issue230TestActivity;
import razerdp.demo.ui.issuestest.Issue236TestActivity;
import razerdp.demo.utils.DescBuilder;
import razerdp.demo.utils.StringUtil;

/**
 * Created by 大灯泡 on 2019/10/8.
 */
class IssueHelper {

    static List<IssueInfo> addIssues(List<IssueInfo> result) {
        add_210(result);
        add_226(result);
        add_230(result);
        add_236(result);

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
                .setFixed(false)
                .setActivityClass(Issue236TestActivity.class)
                .setTitle("2.2.1 release版本 xml里面的match_parent无效")
                .setDesc(DescBuilder.get()
                        .append("系统版本：UNKNOWN")
                        .append("库版本：release 2.2.1")
                        .append("2.2.1 release版本。xml里面的match_parent无效，必须在代码里再设置一次setWidht(getScreenWidth())才行。2.2.0没有此问题")
                        .build());
        result.add(issue236);
    }
}
