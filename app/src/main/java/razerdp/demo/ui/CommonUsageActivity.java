package razerdp.demo.ui;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import razerdp.basepopup.R;
import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.base.baseadapter.BaseMultiRecyclerViewHolder;
import razerdp.demo.base.baseadapter.MultiRecyclerViewAdapter;
import razerdp.demo.base.baseadapter.MultiType;
import razerdp.demo.base.baseadapter.OnItemClickListener;
import razerdp.demo.model.DemoCommonUsageInfo;
import razerdp.demo.model.DemoCommonUsageTitle;
import razerdp.demo.model.common.CommonAnimateInfo;
import razerdp.demo.model.common.CommonSlideInfo;
import razerdp.demo.utils.ButterKnifeUtil;
import razerdp.demo.utils.DescBuilder;
import razerdp.demo.utils.UIHelper;
import razerdp.demo.widget.DPRecyclerView;
import razerdp.demo.widget.DPTextView;
import razerdp.demo.widget.decoration.GridItemDecoration;
import razerdp.demo.widget.decoration.SpaceOption;

/**
 * Created by 大灯泡 on 2019/9/20
 * <p>
 * Description：常见例子
 */
public class CommonUsageActivity extends BaseActivity {
    public static final String DESC = DescBuilder.get()
            .append("常见样式的弹窗")
            .append("朋友圈评论，跟随朋友圈滑动更新")
            .append("背景模糊/局部模糊")
            .append("控制弹窗位置")
            .append("输入法适配")
            .append("更多")
            .build();
    @BindView(R.id.rv_content)
    DPRecyclerView rvContent;

    MultiRecyclerViewAdapter mAdapter;


    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public int contentViewLayoutId() {
        return R.layout.activity_common_usage;
    }

    @Override
    protected void onInitView(View decorView) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mAdapter.getItemViewType(position) == 0 ? 2 : 1;
            }
        });
        rvContent.setLayoutManager(gridLayoutManager);
        rvContent.addItemDecoration(new GridItemDecoration(new SpaceOption.Builder().size(UIHelper.DP8).build()));
        mAdapter = new MultiRecyclerViewAdapter(this, createItem());
        mAdapter.appendHolder(InnerTitleViewHolder.class, 0)
                .appendHolder(InnerItemViewHolder.class, 1);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position, Object data) {
                if (data instanceof DemoCommonUsageInfo) {
                    ((DemoCommonUsageInfo) data).toShow(v);
                }
            }
        });
        rvContent.setAdapter(mAdapter);

    }

    private List<MultiType> createItem() {
        List<MultiType> result = new ArrayList<>();
        result.add(new DemoCommonUsageTitle("位置类"));
        result.add(new CommonSlideInfo());
        result.add(new DemoCommonUsageTitle("动画类"));
        result.add(new CommonAnimateInfo());
        return result;
    }


    static class InnerTitleViewHolder extends BaseMultiRecyclerViewHolder<DemoCommonUsageTitle> {

        @BindView(R.id.tv_title)
        TextView tvTitle;

        public InnerTitleViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnifeUtil.bind(this, itemView);
        }

        @Override
        public int inflateLayoutResourceId() {
            return R.layout.item_common_usage_title;
        }

        @Override
        public void onBindData(DemoCommonUsageTitle data, int position) {
            tvTitle.setText(data.title);
        }
    }


    static class InnerItemViewHolder extends BaseMultiRecyclerViewHolder<DemoCommonUsageInfo> {

        @BindView(R.id.iv_tips)
        ImageView ivTips;
        @BindView(R.id.tv_fun)
        TextView tvFun;
        @BindView(R.id.divider)
        View divider;
        @BindView(R.id.tv_option)
        DPTextView tvOption;

        public InnerItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnifeUtil.bind(this, itemView);
        }

        @Override
        public int inflateLayoutResourceId() {
            return R.layout.item_common_usage;
        }

        @Override
        public void onBindData(DemoCommonUsageInfo data, int position) {
            tvFun.setText(data.title);

        }

        @OnClick(R.id.iv_tips)
        void showTips(View v) {
            getData().shoTips(v);
        }

        @OnClick(R.id.tv_option)
        void showOption(View v) {
            getData().toOption(v);
        }

    }

}
