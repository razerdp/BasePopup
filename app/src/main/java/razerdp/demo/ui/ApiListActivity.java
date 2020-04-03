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
        adapter.setOnItemClickListener((v, position, data) -> ActivityLauncher.start(self(), data.getActivityClass(), data.getExtraData()));
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
