package razerdp.demo.model.issue;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import razerdp.demo.base.baseactivity.BaseActivity;

/**
 * Created by 大灯泡 on 2019/9/22.
 */
public class IssueInfo {
    public String issue;
    public String title;
    public String desc;
    public String url;
    public List<String>                       pics;
    public Class<? extends AppCompatActivity> activityClass;
    public boolean                            finished;

    public IssueInfo() {
        pics = new ArrayList<>();
    }

    public IssueInfo setIssue(String issue) {
        this.issue = issue;
        url = "https://github.com/razerdp/BasePopup/issues/" + issue;
        return this;
    }

    public IssueInfo setTitle(String title) {
        this.title = title;
        return this;
    }

    public IssueInfo setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public IssueInfo setUrl(String url) {
        this.url = url;
        return this;
    }

    public IssueInfo appendPic(String pic) {
        this.pics.add(pic);
        return this;
    }

    public IssueInfo setActivityClass(Class<? extends AppCompatActivity> activityClass) {
        this.activityClass = activityClass;
        return this;
    }

    public IssueInfo setFixed(boolean finished) {
        this.finished = finished;
        return this;
    }
}
