package razerdp.demo.ui.friendcircle;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewbinding.ViewBinding;
import razerdp.basepopup.databinding.ActivityFriendCircleBinding;
import razerdp.demo.base.TestData;
import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.base.baseadapter.SimpleRecyclerViewAdapter;
import razerdp.demo.base.interfaces.ExtSimpleCallback;
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
    DPRecyclerView rvContent;

    PopupCircleOption popupCircleOption;

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public ViewBinding onCreateViewBinding(LayoutInflater layoutInflater) {
        return ActivityFriendCircleBinding.inflate(layoutInflater);
    }

    @Override
    protected void onInitView(View decorView) {
        SimpleRecyclerViewAdapter<FriendCircleInfo> adapter = new SimpleRecyclerViewAdapter<>(this);
        adapter.setHolder(FriendCircleViewHolder.class);
        rvContent.setLayoutManager(new LinearLayoutManager(this));
        rvContent.setItemAnimator(null);
        rvContent.setAdapter(adapter);
        fetchData(adapter);
    }

    private void fetchData(SimpleRecyclerViewAdapter<FriendCircleInfo> adapter) {
        TestData.getTestData(RandomUtil.randomInt(0, 50), new ExtSimpleCallback<List<TestData.TestResult>>() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog(true);
                setLoadingDialogText("加载中...");
            }

            @Override
            public void onCall(List<TestData.TestResult> data) {
                List<FriendCircleInfo> friendCircleInfos = new ArrayList<>();
                for (TestData.TestResult datum : data) {
                    FriendCircleInfo info = new FriendCircleInfo();
                    info.content = datum.text;
                    info.pics = new ArrayList<>(datum.pics);
                    info.name = "BasePopup_id_" + RandomUtil.randomInt(0, 999999999);
                    info.avatar = datum.avatar;
                    friendCircleInfos.add(info);
                }
                dismissLoadingDialog();
                adapter.updateData(friendCircleInfos);
            }

            @Override
            public void onError(int code, String errorMessage) {
                super.onError(code, errorMessage);
                setLoadingDialogText("加载出错，请重新加载");
                setActionDialogText("重新加载", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fetchData(adapter);
                    }
                });
            }
        });
    }


    @Override
    public void onTitleRightClick(View view) {
        if (popupCircleOption == null) {
            popupCircleOption = new PopupCircleOption(this);
        }
        popupCircleOption.showPopupWindow();
    }
}
