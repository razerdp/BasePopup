package razerdp.demo.ui.issuestest.home;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import butterknife.BindView;
import butterknife.OnClick;
import razerdp.basepopup.R;
import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.base.baseadapter.BaseSimpleRecyclerViewHolder;
import razerdp.demo.base.baseadapter.SimpleRecyclerViewAdapter;
import razerdp.demo.base.imageloader.ImageLoaderManager;
import razerdp.demo.model.issue.IssueInfo;
import razerdp.demo.ui.ActivityLauncher;
import razerdp.demo.ui.photobrowser.PhotoBrowserProcessor;
import razerdp.demo.utils.ActivityUtil;
import razerdp.demo.utils.ButterKnifeUtil;
import razerdp.demo.utils.DescBuilder;
import razerdp.demo.utils.FillViewUtil;
import razerdp.demo.utils.ToolUtil;
import razerdp.demo.utils.UIHelper;
import razerdp.demo.widget.DPRecyclerView;
import razerdp.demo.widget.DPTextView;
import razerdp.demo.widget.FlowLayout;

/**
 * Created by 大灯泡 on 2019/9/22.
 */
public class IssueHomeActivity extends BaseActivity {
    public static final String DESC = DescBuilder.get()
            .append("关于Issue部分问题的测试Demo")
            .build();
    @BindView(R.id.rv_content)
    DPRecyclerView rvContent;

    SimpleRecyclerViewAdapter<IssueInfo> mAdapter;
    PhotoBrowserProcessor mPhotoBrowserProcessor;

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public int contentViewLayoutId() {
        return R.layout.activity_issue;
    }

    @Override
    protected void onInitView(View decorView) {
        rvContent.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SimpleRecyclerViewAdapter<>(this, IssueHelper.addIssues(new ArrayList<>()));
        mAdapter.setHolder(InnerViewHolder.class)
                .outher(this);
        rvContent.setAdapter(mAdapter);
    }


    class InnerViewHolder extends BaseSimpleRecyclerViewHolder<IssueInfo> {

        @BindView(R.id.tv_issue)
        TextView mTvIssue;
        @BindView(R.id.tv_title)
        TextView mTvTitle;
        @BindView(R.id.tv_desc)
        TextView mTvDesc;
        @BindView(R.id.layout_pic)
        FlowLayout mLayoutPic;
        @BindView(R.id.divider)
        View mDivider;
        @BindView(R.id.tv_to_web)
        DPTextView mTvToWeb;
        @BindView(R.id.tv_go)
        DPTextView mTvGo;
        @BindView(R.id.iv_state)
        ImageView ivState;

        public InnerViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnifeUtil.bind(this, itemView);
        }

        @Override
        public int inflateLayoutResourceId() {
            return R.layout.item_issue;
        }

        @Override
        public void onBindData(IssueInfo data, int position) {
            mTvIssue.setText(String.format("#%s", data.issue));
            mTvTitle.setText(data.title);
            mTvDesc.setText(data.desc);
            if (ToolUtil.isEmpty(data.pics)) {
                mLayoutPic.setVisibility(View.GONE);
            } else {
                mLayoutPic.setVisibility(View.VISIBLE);
                FillViewUtil.fillView(data.pics, mLayoutPic, R.layout.item_issue_pic, creator);
            }
            ivState.setImageResource(data.finished ? R.drawable.ic_fixed : R.drawable.ic_wait);
        }

        FillViewUtil.OnFillViewsListener<String, InnerPicViewHolder> creator = (itemView, data, position) -> {
            FlowLayout.LayoutParams p = itemView.getLayoutParams();
            p.width = p.height = UIHelper.getScreenWidth() >> 2;
            InnerPicViewHolder holder = new InnerPicViewHolder(itemView);
            holder.ivIssuePreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPhotoBrowserProcessor == null) {
                        mPhotoBrowserProcessor = PhotoBrowserProcessor.with(data)
                                .setExitViewProvider((from, exitPosition) -> {
                                    InnerPicViewHolder holder = ToolUtil.cast(FillViewUtil.getHolder(mLayoutPic.getChildAt(exitPosition)), InnerPicViewHolder.class);
                                    return holder == null ? null : holder.ivIssuePreview;
                                });
                    }
                    mPhotoBrowserProcessor
                            .setPhotos(getData().pics)
                            .fromView((ImageView) v)
                            .setStartPosition(position)
                            .start(ToolUtil.cast(ActivityUtil.getActivity(v.getContext()), ComponentActivity.class));
                }
            });
            return holder;
        };


        class InnerPicViewHolder extends FillViewUtil.FillViewHolder<String> {
            ImageView ivIssuePreview;

            public InnerPicViewHolder(View itemView) {
                super(itemView);
                ivIssuePreview = findViewById(R.id.iv_issue);
            }

            @Override
            public void onBindData(String data, int position, boolean isLast) {
                ImageLoaderManager.INSTANCE.loadImage(ivIssuePreview, data);
            }
        }

        @OnClick(R.id.tv_to_web)
        void toWeb() {
            ToolUtil.openInSystemBroswer(getContext(), getData().url);
        }

        @OnClick(R.id.tv_go)
        void toTarget() {
            ActivityLauncher.start(getContext(), getData().activityClass);
        }
    }
}
