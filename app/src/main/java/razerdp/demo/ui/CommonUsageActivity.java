package razerdp.demo.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
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
import razerdp.demo.model.common.CommonAnchorMatchInfo;
import razerdp.demo.model.common.CommonAnimateInfo;
import razerdp.demo.model.common.CommonAnyPosInfo;
import razerdp.demo.model.common.CommonArrowInfo;
import razerdp.demo.model.common.CommonBackgroundAlignInfo;
import razerdp.demo.model.common.CommonBackgroundInfo;
import razerdp.demo.model.common.CommonBarControllerInfo;
import razerdp.demo.model.common.CommonBlurInfo;
import razerdp.demo.model.common.CommonBottomSheetDialogInfo;
import razerdp.demo.model.common.CommonControllerInfo;
import razerdp.demo.model.common.CommonDialogActivityInfo;
import razerdp.demo.model.common.CommonFriendCircleInfo;
import razerdp.demo.model.common.CommonFullScreenActivityInfo;
import razerdp.demo.model.common.CommonGestureNavInfo;
import razerdp.demo.model.common.CommonInputInfo;
import razerdp.demo.model.common.CommonRTLInfo;
import razerdp.demo.model.common.CommonSlideInfo;
import razerdp.demo.model.common.CommonUpdateInfo;
import razerdp.demo.model.common.ScreenRotateActivityInfo;
import razerdp.demo.model.lifecycle.ShowInServiceInfo;
import razerdp.demo.model.lifecycle.ShowOnCreateInfo;
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
    @BindView(R.id.tv_any_position)
    TextView tvAnyPos;

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
        rvContent.addItemDecoration(new GridItemDecoration(new SpaceOption.Builder().size(UIHelper.DP8)
                .build()));
        mAdapter = new MultiRecyclerViewAdapter(this, createItem());
        mAdapter.appendHolder(InnerTitleViewHolder.class, 0)
                .appendHolder(InnerItemViewHolder.class, 1);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position, Object data) {
                if (data instanceof CommonAnyPosInfo) {
                    ((CommonAnyPosInfo) data).toShow(tvAnyPos);
                    return;
                }
                if (data instanceof DemoCommonUsageInfo) {
                    ((DemoCommonUsageInfo) data).toShow(v);
                }
            }
        });
        rvContent.setAdapter(mAdapter);

    }

    private List<MultiType> createItem() {
        List<MultiType> result = new ArrayList<>();
        result.add(new DemoCommonUsageTitle("位置相关"));
        result.add(new CommonSlideInfo());
        result.add(new CommonAnyPosInfo());
        result.add(new CommonFriendCircleInfo());
        result.add(new CommonAnchorMatchInfo());
        result.add(new CommonArrowInfo());
        result.add(new DemoCommonUsageTitle("PopupWindow控制相关"));
        result.add(new CommonControllerInfo());
        result.add(new CommonBarControllerInfo());
        result.add(new DemoCommonUsageTitle("动画相关"));
        result.add(new CommonAnimateInfo());
        result.add(new DemoCommonUsageTitle("背景相关"));
        result.add(new CommonBackgroundInfo());
        result.add(new CommonBackgroundAlignInfo());
        result.add(new CommonBlurInfo());
        result.add(new DemoCommonUsageTitle("生命期相关"));
        result.add(new ShowOnCreateInfo());
        result.add(new ShowInServiceInfo());
        result.add(new DemoCommonUsageTitle("输入法相关"));
        result.add(new CommonInputInfo());
        result.add(new DemoCommonUsageTitle("兼容性测试"));
        result.add(new CommonBottomSheetDialogInfo());
        result.add(new CommonGestureNavInfo());
        result.add(new CommonFullScreenActivityInfo());
        result.add(new CommonDialogActivityInfo());
        result.add(new CommonRTLInfo());
        result.add(new CommonUpdateInfo());
        result.add(new ScreenRotateActivityInfo());

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
            tvOption.setText(TextUtils.isEmpty(data.option) ? "配置" : data.option);
        }

        @OnClick(R.id.tv_option)
        void showOption(View v) {
            getData().toOption(v);
        }

    }

}
