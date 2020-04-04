package razerdp.demo.ui;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import razerdp.basepopup.R;
import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.base.baseadapter.BaseSimpleRecyclerViewHolder;
import razerdp.demo.base.baseadapter.SimpleRecyclerViewAdapter;
import razerdp.demo.model.api.ApiInfo;
import razerdp.demo.ui.apidemo.ApiDemoActivity;
import razerdp.demo.ui.apidemo.fragments.ConstructorApiFragment;
import razerdp.demo.ui.apidemo.fragments.FadeEnableApiFragment;
import razerdp.demo.utils.DescBuilder;
import razerdp.demo.utils.UIHelper;

/**
 * Created by 大灯泡 on 2020/4/3.
 * <p>
 * api列表
 */
public class ApiListActivity extends BaseActivity {
    public static final String DESC = DescBuilder.get()
            .append("Api列表")
            .append("ApiDemo")
            .build();
    @BindView(R.id.rv_content)
    RecyclerView mRvContent;

    private static final List<ApiInfo> sApiInfos;

    static {
        sApiInfos = new ArrayList<>();
        ApiInfo apiInfo = new ApiInfo("BasePopupWindow()", ConstructorApiFragment.class, "构造器");
        sApiInfos.add(apiInfo);

        apiInfo = new ApiInfo("onCreateShowAnimation()", null);
        apiInfo.setTips("请查看【快速入门】-【动画展示】");
        sApiInfos.add(apiInfo);

        apiInfo = new ApiInfo("onCreateDismissAnimation()", null);
        apiInfo.setTips("请查看【快速入门】-【动画展示】");
        sApiInfos.add(apiInfo);

        apiInfo = new ApiInfo("onCreateShowAnimator()", null);
        apiInfo.setTips("请查看【快速入门】-【动画展示】");
        sApiInfos.add(apiInfo);

        apiInfo = new ApiInfo("onCreateDismissAnimator()", null);
        apiInfo.setTips("请查看【快速入门】-【动画展示】");
        sApiInfos.add(apiInfo);

        apiInfo = new ApiInfo("onCreateAnimateView()", null);
        apiInfo.setTips("返回PopupWindow执行动画的View，缺省值为ContentView");
        sApiInfos.add(apiInfo);

        apiInfo = new ApiInfo("setPopupFadeEnable(boolean isPopupFadeAnimate)", FadeEnableApiFragment.class, "淡入淡出");
        sApiInfos.add(apiInfo);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public int contentViewLayoutId() {
        return R.layout.activity_api_list;
    }

    @Override
    protected void onInitView(View decorView) {
        SimpleRecyclerViewAdapter<ApiInfo> adapter = new SimpleRecyclerViewAdapter<>(this, sApiInfos);
        adapter.setHolder(InnerViewHolder.class);
        mRvContent.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(UIHelper.getDrawable(R.drawable.divider));
        mRvContent.addItemDecoration(divider);
        adapter.setOnItemClickListener((v, position, data) -> {
            if (data.getFragmentClass() != null) {
                ActivityLauncher.start(self(), ApiDemoActivity.class, new ApiDemoActivity.Data(data));
            } else {
                UIHelper.toast(data.getTips());
            }
        });
        mRvContent.setAdapter(adapter);

    }

    static class InnerViewHolder extends BaseSimpleRecyclerViewHolder<ApiInfo> {
        TextView tvApi;


        public InnerViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvApi = findViewById(R.id.tv_api);
        }

        @Override
        public int inflateLayoutResourceId() {
            return R.layout.item_api_list;
        }

        @Override
        public void onBindData(ApiInfo data, int position) {
            tvApi.setText(data.getApi());
        }

    }

}
