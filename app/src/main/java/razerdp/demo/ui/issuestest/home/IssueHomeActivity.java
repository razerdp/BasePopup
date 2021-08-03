package razerdp.demo.ui.issuestest.home;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewbinding.ViewBinding;
import razerdp.basepopup.R;
import razerdp.basepopup.databinding.ActivityIssueBinding;
import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.base.baseactivity.BaseBindingActivity;
import razerdp.demo.base.baseadapter.BaseSimpleRecyclerViewHolder;
import razerdp.demo.base.baseadapter.SimpleRecyclerViewAdapter;
import razerdp.demo.base.imageloader.ImageLoaderManager;
import razerdp.demo.model.issue.IssueInfo;
import razerdp.demo.ui.ActivityLauncher;
import razerdp.demo.ui.photobrowser.PhotoBrowserImpl;
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
public class IssueHomeActivity extends BaseBindingActivity<ActivityIssueBinding> {
    public static final String DESC = DescBuilder.get()
            .append("关于Issue部分问题的测试Demo")
            .build();
    SimpleRecyclerViewAdapter<IssueInfo> mAdapter;
    PhotoBrowserProcessor mPhotoBrowserProcessor;

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public ActivityIssueBinding onCreateViewBinding(LayoutInflater layoutInflater) {
        return ActivityIssueBinding.inflate(layoutInflater);
    }

    @Override
    protected void onInitView(View decorView) {
        mBinding.rvContent.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SimpleRecyclerViewAdapter<>(this, IssueHelper.addIssues(new ArrayList<>()));
        mAdapter.setHolder(InnerViewHolder.class)
                .outher(this);
        mBinding.rvContent.setAdapter(mAdapter);
    }


    class InnerViewHolder extends BaseSimpleRecyclerViewHolder<IssueInfo> {

        TextView mTvIssue;
        TextView mTvTitle;
        TextView mTvDesc;
        FlowLayout mLayoutPic;
        View mDivider;
        DPTextView mTvToWeb;
        DPTextView mTvGo;
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
                        mPhotoBrowserProcessor = PhotoBrowserProcessor.with(new PhotoBrowserImpl(
                                data))
                                .setExitViewProvider((from, exitPosition) -> {
                                    InnerPicViewHolder holder = ToolUtil.cast(FillViewUtil.getHolder(
                                            mLayoutPic
                                                    .getChildAt(exitPosition)),
                                                                              InnerPicViewHolder.class);
                                    return holder == null ? null : holder.ivIssuePreview;
                                });
                    }
                    mPhotoBrowserProcessor
                            .setPhotos(PhotoBrowserImpl.fromList(getData().pics, null))
                            .fromView((ImageView) v)
                            .setStartPosition(position)
                            .start(ToolUtil.cast(ActivityUtil.getActivity(v.getContext()),
                                                 ComponentActivity.class));
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

        void toWeb() {
            ToolUtil.openInSystemBroswer(getContext(), getData().url);
        }

        void toTarget() {
            ActivityLauncher.start(getContext(), getData().activityClass);
        }
    }
}
