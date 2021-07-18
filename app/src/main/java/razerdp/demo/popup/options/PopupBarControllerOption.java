package razerdp.demo.popup.options;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import razerdp.basepopup.BasePopupFlag;
import razerdp.basepopup.R;
import razerdp.demo.base.baseadapter.BaseSimpleRecyclerViewHolder;
import razerdp.demo.base.baseadapter.OnItemClickListener;
import razerdp.demo.base.baseadapter.SimpleRecyclerViewAdapter;
import razerdp.demo.model.common.CommonBarControllerInfo;
import razerdp.demo.utils.ButterKnifeUtil;
import razerdp.demo.utils.UIHelper;
import razerdp.demo.widget.DPTextView;
import razerdp.demo.widget.decoration.GridItemDecoration;
import razerdp.demo.widget.decoration.SpaceOption;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;

/**
 * Created by 大灯泡 on 2019/9/20
 * <p>
 * Description：Bar控制相关的配置
 */
public class PopupBarControllerOption extends BaseOptionPopup<CommonBarControllerInfo> {

    SimpleRecyclerViewAdapter<Info> mAdapter;

    @BindView(R.id.rv_content)
    RecyclerView rvContent;
    @BindView(R.id.tv_go)
    DPTextView tvGo;
    @BindView(R.id.check_overlay_status)
    AppCompatCheckBox checkOverlayStatus;
    @BindView(R.id.check_status_mask)
    AppCompatCheckBox checkStatusMask;
    @BindView(R.id.check_status_content)
    AppCompatCheckBox checkStatusContent;
    @BindView(R.id.check_overlay_nav)
    AppCompatCheckBox checkOverlayNav;
    @BindView(R.id.check_nav_mask)
    AppCompatCheckBox checkNavMask;
    @BindView(R.id.check_nav_content)
    AppCompatCheckBox checkNavContent;
    @BindView(R.id.check_match_horizontal)
    AppCompatCheckBox checkMatchHorizontal;
    @BindView(R.id.check_match_vertical)
    AppCompatCheckBox checkMatchVertical;

    public PopupBarControllerOption(Context context) {
        super(context);
        setContentView(R.layout.popup_option_control_bar);

        List<Info> infos = new ArrayList<>();
        infos.add(new Info(Gravity.LEFT, "Gravity.Left"));
        infos.add(new Info(Gravity.TOP, "Gravity.Top"));
        infos.add(new Info(Gravity.RIGHT, "Gravity.RIGHT"));
        infos.add(new Info(Gravity.BOTTOM, "Gravity.BOTTOM"));
        infos.add(new Info(Gravity.CENTER_VERTICAL, "Gravity.CENTER_VERTICAL"));
        infos.add(new Info(Gravity.CENTER_HORIZONTAL, "Gravity.CENTER_HORIZONTAL"));
        infos.add(new Info(Gravity.CENTER, "Gravity.CENTER", true));

        mAdapter = new SimpleRecyclerViewAdapter<>(context, infos);
        mAdapter.setHolder(InnerViewHolder.class);
        rvContent.setLayoutManager(new GridLayoutManager(context, 2));
        rvContent.addItemDecoration(new GridItemDecoration(new SpaceOption.Builder().size(UIHelper.DP12)
                .build()));
        rvContent.setItemAnimator(null);
        mAdapter.setOnItemClickListener(new OnItemClickListener<Info>() {
            @Override
            public void onItemClick(View v, int position, Info data) {
                data.checked = !data.checked;
                mAdapter.notifyItemChanged(position);
            }
        });
        rvContent.setAdapter(mAdapter);
    }


    @Override
    protected Animation onCreateShowAnimation() {
        return AnimationHelper.asAnimation()
                .withTranslation(TranslationConfig.FROM_BOTTOM)
                .toShow();
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return AnimationHelper.asAnimation()
                .withTranslation(TranslationConfig.TO_BOTTOM)
                .toDismiss();
    }

    @OnClick(R.id.tv_go)
    void apply() {
        int gravity = Gravity.NO_GRAVITY;
        for (Info data : mAdapter.getDatas()) {
            if (data.checked) {
                gravity |= data.gravity;
            }
        }
        mInfo.gravity = gravity;
        mInfo.matchHorizontal = checkMatchHorizontal.isChecked();
        mInfo.matchVertical = checkMatchVertical.isChecked();
        mInfo.overlayStatusbar = checkOverlayStatus.isChecked();
        mInfo.overlayNavigationBar = checkOverlayNav.isChecked();

        int mode = 0;
        if (checkStatusMask.isChecked()) {
            mode |= BasePopupFlag.OVERLAY_MASK;
        }
        if (checkStatusContent.isChecked()) {
            mode |= BasePopupFlag.OVERLAY_CONTENT;
        }
        mInfo.overlayStatusbarMode = mode;
        mode = 0;
        if (checkNavMask.isChecked()) {
            mode |= BasePopupFlag.OVERLAY_MASK;
        }
        if (checkNavContent.isChecked()) {
            mode |= BasePopupFlag.OVERLAY_CONTENT;
        }
        mInfo.overlayNavigationBarMode = mode;
        dismiss();
    }

    static class InnerViewHolder extends BaseSimpleRecyclerViewHolder<Info> {
        @BindView(R.id.check_box)
        AppCompatCheckBox checkBox;

        public InnerViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnifeUtil.bind(this, itemView);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    getData().checked = isChecked;
                }
            });
        }

        @Override
        public int inflateLayoutResourceId() {
            return R.layout.item_slide_option;
        }

        @Override
        public void onBindData(Info data, int position) {
            checkBox.setChecked(data.checked);
            checkBox.setText(data.name);
        }
    }

    static class Info {
        int gravity;
        String name;
        boolean checked;

        public Info(int gravity, String name) {
            this(gravity, name, false);
        }

        public Info(int gravity, String name, boolean checked) {
            this.gravity = gravity;
            this.name = name;
            this.checked = checked;
        }
    }
}
