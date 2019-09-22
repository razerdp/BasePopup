package razerdp.demo.ui.issuestest.home;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
import razerdp.demo.ui.issuestest.Issue210TestActivity;
import razerdp.demo.ui.photobrowser.PhotoBrowserProcessor;
import razerdp.demo.utils.ActivityUtil;
import razerdp.demo.utils.ButterKnifeUtil;
import razerdp.demo.utils.DescBuilder;
import razerdp.demo.utils.FillViewUtil;
import razerdp.demo.utils.StringUtil;
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
        mAdapter = new SimpleRecyclerViewAdapter<IssueInfo>(this, generateItem());
        mAdapter.setHolder(InnerViewHolder.class)
                .outher(this);
        rvContent.setAdapter(mAdapter);
    }

    private List<IssueInfo> generateItem() {
        List<IssueInfo> result = new ArrayList<>();

        IssueInfo issue210 = new IssueInfo();
        issue210.setActivityClass(Issue210TestActivity.class)
                .setIssue("210")
                .setTitle("setOutSideTouchable（true）显示不正常")
                .setDesc(DescBuilder.get()
                        .append("系统版本：android p")
                        .append("库版本：release 2.2.1")
                        .append("问题描述/重现步骤：调用setOutSideTouchable（true）方法，再showPopupWindow（view)时，位置发生偏移，去掉该方法或者setOutSideTouchable（false）则正常显示。")
                        .build())
                .appendPic("https://user-images.githubusercontent.com/11664870/61995671-feb39400-b0bd-11e9-8176-81388d0703d5.png")
                .appendPic("https://user-images.githubusercontent.com/11664870/61995674-070bcf00-b0be-11e9-996c-253955d32969.png");

        result.add(issue210);

        Collections.sort(result, new Comparator<IssueInfo>() {
            @Override
            public int compare(IssueInfo o1, IssueInfo o2) {
                int issue = StringUtil.toInt(o1.issue);
                int issue2 = StringUtil.toInt(o2.issue);
                return Integer.compare(issue, issue2);
            }
        });

        return result;
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
