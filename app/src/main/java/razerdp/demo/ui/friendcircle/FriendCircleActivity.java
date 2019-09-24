package razerdp.demo.ui.friendcircle;

import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import razerdp.basepopup.R;
import razerdp.demo.base.TestData;
import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.base.baseadapter.SimpleRecyclerViewAdapter;
import razerdp.demo.model.friendcircle.FriendCircleInfo;
import razerdp.demo.popup.options.PopupCircleOption;
import razerdp.demo.utils.RandomUtil;
import razerdp.demo.widget.DPRecyclerView;

/**
 * Created by 大灯泡 on 2019/9/24
 * <p>
 * Description：朋友圈
 */
public class FriendCircleActivity extends BaseActivity {
    @BindView(R.id.rv_content)
    DPRecyclerView rvContent;

    PopupCircleOption popupCircleOption;

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public int contentViewLayoutId() {
        return R.layout.activity_friend_circle;
    }

    @Override
    protected void onInitView(View decorView) {
        SimpleRecyclerViewAdapter<FriendCircleInfo> adapter = new SimpleRecyclerViewAdapter<>(this, fakeData());
        adapter.setHolder(FriendCircleViewHolder.class);
        rvContent.setLayoutManager(new LinearLayoutManager(this));
        rvContent.setItemAnimator(null);
        rvContent.setAdapter(adapter);
    }

    private List<FriendCircleInfo> fakeData() {
        List<FriendCircleInfo> result = new ArrayList<>();
        for (int i = 0; i < RandomUtil.randomInt(20, 80); i++) {
            FriendCircleInfo info = new FriendCircleInfo();
            info.avatar = TestData.getAvatar();
            info.content = RandomUtil.randomString(RandomUtil.randomInt(0, 140));
            info.name = "BasePopup";
            int picsCount = RandomUtil.randomInt(0, 9);
            if (picsCount > 0) {
                info.pics = new ArrayList<>();
                for (int j = 0; j < picsCount; j++) {
                    info.pics.add(TestData.getPicUrl());
                }
            }
            result.add(info);
        }
        return result;
    }

    @Override
    public void onTitleRightClick(View view) {
        if (popupCircleOption == null) {
            popupCircleOption = new PopupCircleOption(this);
        }
        popupCircleOption.showPopupWindow();
    }
}
