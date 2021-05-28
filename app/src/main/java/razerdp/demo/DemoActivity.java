package razerdp.demo;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.PictureDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import razerdp.basepopup.QuickPopupBuilder;
import razerdp.basepopup.QuickPopupConfig;
import razerdp.basepopup.R;
import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.base.baseadapter.BaseSimpleRecyclerViewHolder;
import razerdp.demo.base.baseadapter.SimpleRecyclerViewAdapter;
import razerdp.demo.base.imageloader.GlideApp;
import razerdp.demo.base.imageloader.SvgSoftwareLayerSetter;
import razerdp.demo.model.DemoMainItem;
import razerdp.demo.ui.ActivityLauncher;
import razerdp.demo.ui.ApiListActivity;
import razerdp.demo.ui.CommonUsageActivity;
import razerdp.demo.ui.GuideActivity;
import razerdp.demo.ui.UpdateLogActivity;
import razerdp.demo.ui.issuestest.home.IssueHomeActivity;
import razerdp.demo.utils.ButterKnifeUtil;
import razerdp.demo.utils.UIHelper;
import razerdp.demo.utils.ViewUtil;
import razerdp.demo.widget.DPRecyclerView;
import razerdp.demo.widget.DPTextView;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.ScaleConfig;


public class DemoActivity extends BaseActivity {

    @BindView(R.id.rv_content)
    DPRecyclerView rvContent;

    SimpleRecyclerViewAdapter<DemoMainItem> mAdapter;

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public int contentViewLayoutId() {
        return R.layout.activity_demo;
    }

    @Override
    protected void onInitView(View decorView) {

        rvContent.setLayoutManager(new LinearLayoutManager(this));
        View header = ViewUtil.inflate(this, R.layout.item_main_demo_header, rvContent, false);
        header.setOnClickListener(v -> onHeaderClick());
        rvContent.addHeaderView(header);
        rvContent.addHeaderView(genVersionHeader());
        mAdapter = new SimpleRecyclerViewAdapter<>(this, generateItem());
        mAdapter.setHolder(InnerViewHolder.class);
        mAdapter.setOnItemClickListener((v, position, data) -> ActivityLauncher.start(self(),
                                                                                      data.toClass));
        rvContent.setAdapter(mAdapter);


        showWjPopup();
    }

    private View genVersionHeader() {
        View header = ViewUtil.inflate(this, R.layout.item_main_demo_version, rvContent, false);
        ImageView release = header.findViewById(R.id.iv_release);
        GlideApp.with(release)
                .as(PictureDrawable.class)
                .placeholder(new ColorDrawable(UIHelper.getColor(R.color.color_loading)))
                .error(R.drawable.ic_error_gray)
                .transition(DrawableTransitionOptions.withCrossFade())
                .listener(new SvgSoftwareLayerSetter())
                .load("https://img.shields.io/maven-central/v/io.github.razerdp/BasePopup")
                .into(release);
        ImageView snapshot = header.findViewById(R.id.iv_snapshot);
        GlideApp.with(snapshot)
                .as(PictureDrawable.class)
                .placeholder(new ColorDrawable(UIHelper.getColor(R.color.color_loading)))
                .error(R.drawable.ic_error_gray)
                .transition(DrawableTransitionOptions.withCrossFade())
                .listener(new SvgSoftwareLayerSetter())
                .load("https://img.shields.io/nexus/s/io.github.razerdp/BasePopup?server=https%3A%2F%2Fs01.oss.sonatype.org%2F")
                .into(snapshot);
        return header;
    }

    private List<DemoMainItem> generateItem() {
        List<DemoMainItem> result = new ArrayList<>();
        result.add(new DemoMainItem(GuideActivity.class, "简介", GuideActivity.DESC, null));
        result.add(new DemoMainItem(CommonUsageActivity.class,
                                    "快速入门",
                                    CommonUsageActivity.DESC,
                                    "入门推荐"));
        result.add(new DemoMainItem(ApiListActivity.class, "Api列表", ApiListActivity.DESC, "Api"));
        result.add(new DemoMainItem(IssueHomeActivity.class,
                                    "Issue测试Demo",
                                    IssueHomeActivity.DESC,
                                    "issue"));
        result.add(new DemoMainItem(UpdateLogActivity.class,
                                    "历史更新",
                                    UpdateLogActivity.DESC,
                                    "ChangeLog"));
        return result;
    }

    void showWjPopup() {
        QuickPopupBuilder.with(this)
                .contentView(R.layout.popup_wj)
                .config(new QuickPopupConfig()
                                .withShowAnimation(AnimationHelper.asAnimation()
                                                           .withScale(ScaleConfig.CENTER)
                                                           .toShow())
                                .withDismissAnimation(AnimationHelper.asAnimation()
                                                              .withScale(ScaleConfig.CENTER)
                                                              .toDismiss())
                                .withClick(R.id.tv_go, null, true)
                                .blurBackground(true)
                                .outSideDismiss(false))
                .show();
    }

    void onHeaderClick() {
        showWjPopup();
    }


    static class InnerViewHolder extends BaseSimpleRecyclerViewHolder<DemoMainItem> {

        @BindView(R.id.tv_tag)
        TextView tvTag;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_desc)
        TextView tvDesc;
        @BindView(R.id.divider)
        View divider;
        @BindView(R.id.tv_go)
        DPTextView tvGo;

        public InnerViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnifeUtil.bind(this, itemView);
        }

        @Override
        public int inflateLayoutResourceId() {
            return R.layout.item_main_demo;
        }

        @Override
        public void onBindData(DemoMainItem data, int position) {
            tvTag.setVisibility(TextUtils.isEmpty(data.tag) ? View.INVISIBLE : View.VISIBLE);
            tvTag.setText(data.tag);
            tvTitle.setText(data.title);
            tvDesc.setText(data.desc);
        }

        @OnClick(R.id.tv_go)
        void toTarget() {
            ActivityLauncher.start(getContext(), getData().toClass);
        }
    }


    private long lastClickBackTime;

    @Override
    public boolean onBackPressedInternal() {
        if (System.currentTimeMillis() - lastClickBackTime > 2000) {
            UIHelper.toast("再点一次退出");
            lastClickBackTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }

}
