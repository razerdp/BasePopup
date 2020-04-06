package razerdp.demo.ui;

import android.content.Intent;
import android.graphics.Typeface;
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
import razerdp.demo.ui.apidemo.fragments.BackPressEnableApiFragment;
import razerdp.demo.ui.apidemo.fragments.ConstructorApiFragment;
import razerdp.demo.ui.apidemo.fragments.FadeEnableApiFragment;
import razerdp.demo.ui.apidemo.fragments.OverStatusbarApiFragment;
import razerdp.demo.utils.DescBuilder;
import razerdp.demo.utils.SpanUtil;
import razerdp.demo.utils.ToolUtil;
import razerdp.demo.utils.UIHelper;

/**
 * Created by 大灯泡 on 2020/4/3.
 * <p>
 * api列表
 */
public class ApiListActivity extends BaseActivity {
    public static final String DESC = DescBuilder.get()
            .append("部分Api列表")
            .append("部分ApiDemo")
            .build();
    @BindView(R.id.rv_content)
    RecyclerView mRvContent;
    @BindView(R.id.tv_tips)
    TextView tvTips;

    private static final List<ApiInfo> sApiInfos;

    static {
        sApiInfos = new ArrayList<>();
        ApiInfo apiInfo = new ApiInfo("BasePopupWindow()", ConstructorApiFragment.class, "构造器");
        sApiInfos.add(apiInfo);

        apiInfo = new ApiInfo("setPopupFadeEnable(boolean)", FadeEnableApiFragment.class, "淡入淡出");
        sApiInfos.add(apiInfo);

        apiInfo = new ApiInfo("setBackPressEnable(boolean)", BackPressEnableApiFragment.class, "返回键Dismiss");
        sApiInfos.add(apiInfo);

        apiInfo = new ApiInfo("setPopupWindowFullScreen(boolean)", OverStatusbarApiFragment.class, "覆盖状态栏");
        sApiInfos.add(apiInfo);

        apiInfo = new ApiInfo("setOverlayStatusbar(boolean)", OverStatusbarApiFragment.class, "覆盖状态栏");
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
        SpanUtil.create("本页仅展示部分常见Api的使用效果Demo，不列出所有Api方法，如果您需要查阅所有的Api，欢迎点击【这里】浏览文档")
                .append("【这里】")
                .setTextColorRes(R.color.common_red_light)
                .setTextStyle(Typeface.DEFAULT_BOLD)
                .setSpanClickListener(v -> ToolUtil.openInSystemBroswer(self(), "https://www.yuque.com/razerdp/basepopup/api"))
                .into(tvTips);
        SimpleRecyclerViewAdapter<ApiInfo> adapter = new SimpleRecyclerViewAdapter<>(this, sApiInfos);
        adapter.setHolder(InnerViewHolder.class);
        mRvContent.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(UIHelper.getDrawable(R.drawable.divider));
        mRvContent.addItemDecoration(divider);
        adapter.setOnItemClickListener((v, position, data) -> {
            if (data.getFragmentClass() != null) {
                ActivityLauncher.start(self(), ApiDemoActivity.class, new ApiDemoActivity.Data(data));
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
