package razerdp.demo.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.PictureDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import razerdp.basepopup.R;
import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.base.imageloader.GlideApp;
import razerdp.demo.base.imageloader.SvgSoftwareLayerSetter;
import razerdp.demo.model.DependenceInfo;
import razerdp.demo.utils.ButterKnifeUtil;
import razerdp.demo.utils.DescBuilder;
import razerdp.demo.utils.FillViewUtil;
import razerdp.demo.utils.SpanUtil;
import razerdp.demo.utils.UIHelper;

/**
 * Created by 大灯泡 on 2019/9/20
 * <p>
 * Description：
 */
public class GuideActivity extends BaseActivity {
    private static final String FORMAT = "• %s\n";
    private static final String FORMAT2 = "• %s\n\n";

    public static final String DESC = DescBuilder.get()
            .append("关于BasePopup")
            .append("BasePopup的特性")
            .append("BasePopup的依赖")
            .append("更多")
            .build();
    @BindView(R.id.tv_feature)
    TextView tvFeature;
    @BindView(R.id.tv_dependence)
    TextView tvDependence;
    @BindView(R.id.layout_dependence_release)
    LinearLayout layoutDependenceRelease;
    @BindView(R.id.layout_dependence_candy)
    LinearLayout layoutDependenceCandy;

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public int contentViewLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void onInitView(View decorView) {
        setFeature();
        setDependence();
    }

    private void setFeature() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format(FORMAT2, "更简单更精准的控制显示位置，通过Gravity和offset来控制您的PopupWindow"))
                .append(String.format(FORMAT2, "本库为抽象类，对子类几乎没有约束，您完全可以像定制Activity一样来定制您的PopupWindow"))
                .append(String.format(FORMAT2, "支持Animation、Animator，随意控制您的PopupWindow的动画，再也不用去写蛋疼的xml了"))
                .append(String.format(FORMAT2, "顺滑的背景定制，支持背景模糊或局部模糊，展开变暗或者修改颜色甚至是贴图，这一切仅仅需要您通过一句Api完成"))
                .append(String.format(FORMAT2, "不再担心PopupWindow蛋疼的事件拦截，返回键控制、点击外部控制、外部事件响应控制三者分离"))
                .append(String.format(FORMAT2, "PopupWindow自动锚定AnchorView，滑动到屏幕外自动跟随AnchorView消失，不需要复杂的逻辑设置，只需要通过Link方法告诉BasePopup"))
                .append(String.format(FORMAT2, "简单的PopupWindow不想新建一个类，希望拥有链式调用？没问题，QuickPopupBuilder为此而生，相信你会越用越爱~"));

        SpanUtil.create(builder)
                .append("Gravity").setTextColorRes(R.color.color_link).setTextStyle(Typeface.DEFAULT_BOLD).setSpanClickListener(v -> {
            //gravity
        })
                .append("Animation、Animator").setTextColorRes(R.color.color_link).setTextStyle(Typeface.DEFAULT_BOLD).setSpanClickListener(v -> {
            //Animation、Animator
        })
                .append("背景模糊").setTextColorRes(R.color.color_link).setTextStyle(Typeface.DEFAULT_BOLD).setSpanClickListener(v -> {
            //背景模糊
        })
                .append("局部模糊").setTextColorRes(R.color.color_link).setTextStyle(Typeface.DEFAULT_BOLD).setSpanClickListener(v -> {
            //局部模糊
        })
                .append("修改颜色").setTextColorRes(R.color.color_link).setTextStyle(Typeface.DEFAULT_BOLD).setSpanClickListener(v -> {
            //修改颜色
        })
                .append("返回键控制").setTextColorRes(R.color.color_link).setTextStyle(Typeface.DEFAULT_BOLD).setSpanClickListener(v -> {
            //返回键控制
        })
                .append("点击外部控制").setTextColorRes(R.color.color_link).setTextStyle(Typeface.DEFAULT_BOLD).setSpanClickListener(v -> {
            //点击外部控制
        })
                .append("外部事件响应控制").setTextColorRes(R.color.color_link).setTextStyle(Typeface.DEFAULT_BOLD).setSpanClickListener(v -> {
            //外部事件响应控制
        })
                .append("Link").setTextColorRes(R.color.color_link).setTextStyle(Typeface.DEFAULT_BOLD).setSpanClickListener(v -> {
            //Link
        })
                .append("QuickPopupBuilder").setTextColorRes(R.color.color_link).setTextStyle(Typeface.DEFAULT_BOLD).setSpanClickListener(v -> {
            //QuickPopupBuilder
        })
                .into(tvFeature);

    }

    private void setDependence() {
        StringBuilder builder = new StringBuilder();
        builder.append("BasePopup区分为Release版本和Candy版本，Candy版本相当于预览版，其更新较为频繁且可能会包含了新的想法和特性，就像糖果一样甜，但也可能会引起蛀牙。")
                .append('\n')
                .append(String.format(FORMAT, "如果商业用途，请使用Release版本"))
                .append(String.format(FORMAT, "如果希望体验新的特性和功能，请使用Snapshot版本"))
                .append('\n')
                .append("自2.2.2版本开始，BasePopup将完全迁移至AndroidX，不再提供扩展组件了，BasePopup建议您尽早迁移到AndroidX");
        SpanUtil.create(builder)
                .append("Release").setTextStyle(Typeface.DEFAULT_BOLD).setTextColor(Color.BLACK)
                .append("Snapshot").setTextStyle(Typeface.DEFAULT_BOLD).setTextColor(Color.BLACK)
                .append("自2.2.2版本开始，BasePopup将完全迁移至AndroidX，不再提供扩展组件了，BasePopup建议您尽早迁移到AndroidX").setTextColor(Color.RED).setTextStyle(Typeface.DEFAULT_BOLD)
                .into(tvDependence);

        appendReleaseDependence();
        appendCandyDependence();

    }

    private void appendReleaseDependence() {
        List<DependenceInfo> infos = new ArrayList<>();
        infos.add(new DependenceInfo("https://img.shields.io/maven-central/v/io.github.razerdp/BasePopup",
                "基础库（必选）",
                "implementation 'io.github.razerdp:BasePopup:{$latestVersion}'"));
        FillViewUtil.fillView(infos, layoutDependenceRelease, R.layout.item_guide_denpendence, fillViewsListener);
    }

    private void appendCandyDependence() {
        List<DependenceInfo> infos = new ArrayList<>();
        infos.add(new DependenceInfo("https://img.shields.io/nexus/s/io.github.razerdp/BasePopup?server=https%3A%2F%2Fs01.oss.sonatype.org%2F",
                "基础库（必选）",
                "implementation 'io.github.razerdp:BasePopup_Candy:{$latestVersion}'"));
        FillViewUtil.fillView(infos, layoutDependenceCandy, R.layout.item_guide_denpendence, fillViewsListener);
    }

    private FillViewUtil.OnFillViewsListener<DependenceInfo, InnerViewHolder> fillViewsListener = new FillViewUtil.OnFillViewsListener<DependenceInfo, InnerViewHolder>() {
        @Override
        public InnerViewHolder onCreateViewHolder(View itemView, DependenceInfo data, int position) {
            LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) itemView.getLayoutParams();
            if (position != 0) {
                p.topMargin = UIHelper.DP8;
            }
            return new InnerViewHolder(itemView);
        }
    };

    static class InnerViewHolder extends FillViewUtil.FillViewHolder<DependenceInfo> {

        @BindView(R.id.tv_desc)
        TextView tvDesc;
        @BindView(R.id.iv_version)
        ImageView ivVersion;
        @BindView(R.id.tv_dependence)
        TextView tvDependence;

        InnerViewHolder(View itemView) {
            super(itemView);
            ButterKnifeUtil.bind(this, itemView);
        }

        @Override
        public void onBindData(DependenceInfo data, int position, boolean isLast) {
            tvDesc.setText(data.desc);
            tvDependence.setText(data.importText);
            GlideApp.with(ivVersion)
                    .as(PictureDrawable.class)
                    .placeholder(new ColorDrawable(UIHelper.getColor(R.color.color_loading)))
                    .error(R.drawable.ic_error_gray)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .listener(new SvgSoftwareLayerSetter())
                    .load(data.versionIcon)
                    .into(ivVersion);
        }
    }
}
