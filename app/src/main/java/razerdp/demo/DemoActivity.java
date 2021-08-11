package razerdp.demo;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.PictureDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import razerdp.basepopup.R;
import razerdp.basepopup.databinding.ActivityDemoBinding;
import razerdp.basepopup.databinding.ItemMainDemoBinding;
import razerdp.demo.base.baseactivity.BaseBindingActivity;
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
import razerdp.demo.utils.UIHelper;
import razerdp.demo.utils.ViewUtil;


public class DemoActivity extends BaseBindingActivity<ActivityDemoBinding> {

    SimpleRecyclerViewAdapter<DemoMainItem> mAdapter;

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public ActivityDemoBinding onCreateViewBinding(LayoutInflater layoutInflater) {
        return ActivityDemoBinding.inflate(layoutInflater);
    }

    @Override
    protected void onInitView(View decorView) {
        mBinding.rvContent.setLayoutManager(new LinearLayoutManager(this));
        View header = ViewUtil.inflate(this,
                                       R.layout.item_main_demo_header,
                                       mBinding.rvContent,
                                       false);
        header.setOnClickListener(v -> onHeaderClick());
        mBinding.rvContent.addHeaderView(header);
        mBinding.rvContent.addHeaderView(genVersionHeader());
        mAdapter = new SimpleRecyclerViewAdapter<>(this, generateItem());
        mAdapter.setHolder(InnerViewHolder.class);
        mAdapter.setOnItemClickListener((v, position, data) -> ActivityLauncher.start(self(),
                                                                                      data.toClass));
        mBinding.rvContent.setAdapter(mAdapter);

    }

    private View genVersionHeader() {
        View header = ViewUtil.inflate(this,
                                       R.layout.item_main_demo_version,
                                       mBinding.rvContent,
                                       false);
        ImageView release = header.findViewById(R.id.iv_release);
        GlideApp.with(release)
                .as(PictureDrawable.class)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(new ColorDrawable(UIHelper.getColor(R.color.color_loading)))
                .error(R.drawable.ic_error_gray)
                .transition(DrawableTransitionOptions.withCrossFade())
                .listener(new SvgSoftwareLayerSetter())
                .load("https://img.shields.io/maven-central/v/io.github.razerdp/BasePopup")
                .into(release);
        ImageView snapshot = header.findViewById(R.id.iv_snapshot);
        GlideApp.with(snapshot)
                .as(PictureDrawable.class)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
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


    void onHeaderClick() {
        UIHelper.toast("感谢您的支持，您的star和issue是我维护BasePopup最大的动力");
//        ActivityLauncher.start(this, MyTestActivity.class);
    }


    static class InnerViewHolder extends BaseSimpleRecyclerViewHolder<DemoMainItem> {
        ItemMainDemoBinding mBinding;

        public InnerViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding =ItemMainDemoBinding.bind(itemView);
            mBinding.tvGo.setOnClickListener(v -> toTarget());
        }

        @Override
        public int inflateLayoutResourceId() {
            return R.layout.item_main_demo;
        }

        @Override
        public void onBindData(DemoMainItem data, int position) {
            mBinding.tvTag.setVisibility(TextUtils.isEmpty(data.tag) ? View.INVISIBLE : View.VISIBLE);
            mBinding.tvTag.setText(data.tag);
            mBinding.tvTitle.setText(data.title);
            mBinding.tvDesc.setText(data.desc);
        }

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
