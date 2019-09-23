package razerdp.demo.ui;

import android.content.Intent;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import razerdp.basepopup.R;
import razerdp.demo.base.UpdateLogHelper;
import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.base.baseadapter.BaseSimpleRecyclerViewHolder;
import razerdp.demo.base.baseadapter.SimpleRecyclerViewAdapter;
import razerdp.demo.model.updatelog.UpdateLogInfo;
import razerdp.demo.utils.ButterKnifeUtil;
import razerdp.demo.utils.DescBuilder;

/**
 * Created by 大灯泡 on 2019/9/23.
 */
public class UpdateLogActivity extends BaseActivity {
    public static final String DESC = DescBuilder.get()
            .append("更新日志")
            .build();

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
        mAdapter = new SimpleRecyclerViewAdapter<>(this, UpdateLogHelper.getUpdateLogs());
        mAdapter.setHolder(InnerViewHolder.class);
        mRvContent.setLayoutManager(new LinearLayoutManager(this));
        mRvContent.setItemAnimator(null);
        mRvContent.setAdapter(mAdapter);

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
