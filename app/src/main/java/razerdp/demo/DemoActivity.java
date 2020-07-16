package razerdp.demo;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.QuickPopupBuilder;
import razerdp.basepopup.QuickPopupConfig;
import razerdp.basepopup.R;
import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.base.baseadapter.BaseSimpleRecyclerViewHolder;
import razerdp.demo.base.baseadapter.SimpleRecyclerViewAdapter;
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
import razerdp.util.SimpleAnimationUtils;
import razerdp.util.animation.AlphaConfig;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.Direction;
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
        mAdapter = new SimpleRecyclerViewAdapter<>(this, generateItem());
        mAdapter.setHolder(InnerViewHolder.class);
        mAdapter.setOnItemClickListener((v, position, data) -> ActivityLauncher.start(self(), data.toClass));
        rvContent.setAdapter(mAdapter);



        QuickPopupBuilder.with(this)
                .contentView(R.layout.popup_wj)
                .config(new QuickPopupConfig()
                        .withShowAnimation(SimpleAnimationUtils.getDefaultScaleAnimation(true))
                        .withDismissAnimation(SimpleAnimationUtils.getDefaultScaleAnimation(false))
                        .withClick(R.id.tv_go, null, true)
                        .blurBackground(true)
                        .outSideDismiss(false))
                .show();
    }

    private List<DemoMainItem> generateItem() {
        List<DemoMainItem> result = new ArrayList<>();
        result.add(new DemoMainItem(GuideActivity.class, "简介", GuideActivity.DESC, null));
        result.add(new DemoMainItem(CommonUsageActivity.class, "快速入门", CommonUsageActivity.DESC, "入门推荐"));
        result.add(new DemoMainItem(ApiListActivity.class, "Api列表", ApiListActivity.DESC, "Api"));
        result.add(new DemoMainItem(IssueHomeActivity.class, "Issue测试Demo", IssueHomeActivity.DESC, "issue"));
        result.add(new DemoMainItem(UpdateLogActivity.class, "历史更新", UpdateLogActivity.DESC, "ChangeLog"));
        return result;
    }

    void onHeaderClick() {
/*        QuickPopupBuilder.with(DemoActivity.this)
                .contentView(R.layout.popup_demo)
                .config(new QuickPopupConfig()
                        .withShowAnimation(SimpleAnimationUtils.getDefaultAlphaAnimation(true))
                        .withDismissAnimation(SimpleAnimationUtils.getDefaultAlphaAnimation(false))
                        .backpressEnable(false)
                        .blurBackground(true))
                .show();*/
        new T(this).showPopupWindow();
    }

    class T extends BasePopupWindow {

        public T(Context context) {
            super(context);
        }

        @Override
        public View onCreateContentView() {
            return createPopupById(R.layout.popup_demo);
        }


//        @Override
//        protected Animation onCreateShowAnimation() {
//            return AnimationHelper.asAnimation()
////                    .withTranslation(new TranslationConfig()
////                            .from(BOTTOM)
////                            .to(IDLE))
//                    .withScale(new ScaleConfig()
//                            .from(LEFT)
//                            .to(RIGHT))
//                    .toShow();
//        }
//
//        @Override
//        protected Animation onCreateDismissAnimation() {
//            return AnimationHelper.asAnimation()
////                    .withTranslation(new TranslationConfig()
////                            .from(TOP)
////                            .to(BOTTOM))
//                    .withScale(new ScaleConfig()
//                            .from(LEFT)
//                            .to(RIGHT))
//                    .toDismiss();
//        }

        @Override
        protected Animator onCreateShowAnimator() {
            return AnimationHelper.asAnimator()
//                    .withTranslation(new TranslationConfig()
//                            .from(Direction.BOTTOM)
//                            .to(Direction.IDLE))
                    .withAlpha(new AlphaConfig().duration(300))
                    .withScale(new ScaleConfig()
                            .from(Direction.BOTTOM, Direction.CENTER_HORIZONTAL)
                            .to(Direction.TOP))
                    .toShow();
        }

        @Override
        protected Animator onCreateDismissAnimator() {
            return AnimationHelper.asAnimator()
//                    .withTranslation(new TranslationConfig()
//                            .from(Direction.IDLE)
//                            .to(Direction.BOTTOM))
                    .withAlpha(new AlphaConfig().duration(300))
                    .withScale(new ScaleConfig()
                            .from(Direction.TOP, Direction.CENTER_HORIZONTAL)
                            .to(Direction.BOTTOM))
                    .toDismiss();
        }
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
