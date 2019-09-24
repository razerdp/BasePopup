package razerdp.demo.model.common;

import android.view.View;

import razerdp.demo.model.DemoCommonUsageInfo;
import razerdp.demo.ui.ActivityLauncher;
import razerdp.demo.ui.friendcircle.FriendCircleActivity;
import razerdp.demo.utils.UIHelper;

/**
 * Created by 大灯泡 on 2019/9/24
 * <p>
 * Description：朋友圈
 */
public class CommonFriendCircleInfo extends DemoCommonUsageInfo {

    public CommonFriendCircleInfo() {
        title = "朋友圈评论";
    }

    @Override
    public void toShow(View v) {
        ActivityLauncher.start(v.getContext(), FriendCircleActivity.class);
    }

    @Override
    public void toOption(View v) {
        UIHelper.toast("请到朋友圈页面设置哦");
    }
}
