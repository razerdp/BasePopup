package razerdp.demo.ui;

import android.content.Intent;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import razerdp.basepopup.R;
import razerdp.demo.base.UpdateLogHelper;
import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.base.baseadapter.BaseSimpleRecyclerViewHolder;
import razerdp.demo.base.baseadapter.SimpleRecyclerViewAdapter;
import razerdp.demo.model.updatelog.UpdateLogInfo;
import razerdp.demo.utils.ButterKnifeUtil;
import razerdp.demo.utils.DescBuilder;
import razerdp.demo.utils.rx.RxHelper;
import razerdp.demo.utils.rx.RxTaskCall;
import razerdp.demo.widget.DPTextView;

/**
 * Created by 大灯泡 on 2019/9/23.
 */
public class UpdateLogActivity extends BaseActivity {
    public static final String DESC = DescBuilder.get()
            .append("更新日志")
            .build();

    @BindView(R.id.tv_loading)
    DPTextView loading;
    @BindView(R.id.rv_content)
    RecyclerView mRvContent;

    SimpleRecyclerViewAdapter<UpdateLogInfo> mAdapter;

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public int contentViewLayoutId() {
        return R.layout.activity_update_log;
    }

    @Override
    protected void onInitView(View decorView) {
        loading.setVisibility(View.VISIBLE);
        loading.setLoadingText("正在加载...", 2000);
        RxHelper.runOnBackground(new RxTaskCall<List<UpdateLogInfo>>() {
            @Override
            public List<UpdateLogInfo> doInBackground() {
                return UpdateLogHelper.getUpdateLogs();
            }

            @Override
            public void onResult(List<UpdateLogInfo> result) {
                loading.setText(null);
                loading.setVisibility(View.GONE);
                mRvContent.setVisibility(View.VISIBLE);
                mAdapter = new SimpleRecyclerViewAdapter<>(self(), result);
                mAdapter.setHolder(InnerViewHolder.class);
                mRvContent.setLayoutManager(new LinearLayoutManager(self()));
                mRvContent.setItemAnimator(null);
                mRvContent.setAdapter(mAdapter);
            }
        });


    }

    static class InnerViewHolder extends BaseSimpleRecyclerViewHolder<UpdateLogInfo> {

        @BindView(R.id.tv_update)
        TextView mTvUpdate;

        public InnerViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnifeUtil.bind(this, itemView);
            mTvUpdate.setMovementMethod(LinkMovementMethod.getInstance());
        }

        @Override
        public int inflateLayoutResourceId() {
            return R.layout.item_update_log;
        }

        @Override
        public void onBindData(UpdateLogInfo data, int position) {
            mTvUpdate.setText(data.get());
        }
    }
}
