package razerdp.demo.popup.options;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.base.baseadapter.OnItemClickListener;
import razerdp.demo.base.baseadapter.SimpleRecyclerViewAdapter;
import razerdp.demo.model.common.CommonInputInfo;
import razerdp.demo.utils.UIHelper;
import razerdp.demo.widget.DPTextView;
import razerdp.demo.widget.decoration.GridItemDecoration;
import razerdp.demo.widget.decoration.SpaceOption;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;

/**
 * Created by 大灯泡 on 2019/9/22.
 */
public class PopupInputOption extends BaseOptionPopup<CommonInputInfo> {
    AppCompatCheckBox mCheckAlignToRoot;
    AppCompatCheckBox mCheckAlignToView;
    AppCompatCheckBox mCheckAlignAnimate;
    AppCompatCheckBox mCheckAjustInput;
    AppCompatCheckBox mCheckAutoOpen;
    AppCompatCheckBox mCheckIgnore;
    AppCompatCheckBox mCheckForce;
    DPTextView mTvGo;
    RecyclerView rvContent;

    SimpleRecyclerViewAdapter<PopupSlideOption.Info> mAdapter;

    public PopupInputOption(Context context) {
        super(context);
        setContentView(R.layout.popup_option_input);
        List<PopupSlideOption.Info> infos = new ArrayList<>();
        infos.add(new PopupSlideOption.Info(Gravity.LEFT, "Gravity.Left"));
        infos.add(new PopupSlideOption.Info(Gravity.TOP, "Gravity.Top"));
        infos.add(new PopupSlideOption.Info(Gravity.RIGHT, "Gravity.RIGHT"));
        infos.add(new PopupSlideOption.Info(Gravity.BOTTOM, "Gravity.BOTTOM", true));
        infos.add(new PopupSlideOption.Info(Gravity.CENTER_VERTICAL, "Gravity.CENTER_VERTICAL"));
        infos.add(new PopupSlideOption.Info(Gravity.CENTER_HORIZONTAL, "Gravity.CENTER_HORIZONTAL"));
        infos.add(new PopupSlideOption.Info(Gravity.CENTER, "Gravity.CENTER"));
        mAdapter = new SimpleRecyclerViewAdapter<>(context, infos);
        mAdapter.setHolder(PopupSlideOption.InnerViewHolder.class);
        rvContent.setLayoutManager(new GridLayoutManager(context, 2));
        rvContent.addItemDecoration(new GridItemDecoration(new SpaceOption.Builder().size(UIHelper.DP12)
                                                                   .build()));
        rvContent.setItemAnimator(null);
        mAdapter.setOnItemClickListener(new OnItemClickListener<PopupSlideOption.Info>() {
            @Override
            public void onItemClick(View v, int position, PopupSlideOption.Info data) {
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

    void ok() {
        int keyboardFlag = 0;
        if (mCheckAlignAnimate.isChecked()) {
            keyboardFlag |= BasePopupWindow.FLAG_KEYBOARD_ANIMATE_ALIGN;
        }
        if (mCheckAlignToRoot.isChecked()) {
            keyboardFlag |= BasePopupWindow.FLAG_KEYBOARD_ALIGN_TO_ROOT;
        }
        if (mCheckAlignToView.isChecked()) {
            keyboardFlag |= BasePopupWindow.FLAG_KEYBOARD_ALIGN_TO_VIEW;
        }
        if (mCheckForce.isChecked()) {
            keyboardFlag |= BasePopupWindow.FLAG_KEYBOARD_FORCE_ADJUST;
        }
        if (mCheckIgnore.isChecked()){
            keyboardFlag |= BasePopupWindow.FLAG_KEYBOARD_IGNORE_OVER;
        }
        mInfo.keyboardFlag = keyboardFlag;
        mInfo.adjust = mCheckAjustInput.isChecked();
        mInfo.autoOpen = mCheckAutoOpen.isChecked();
        int gravity = Gravity.NO_GRAVITY;
        for (PopupSlideOption.Info data : mAdapter.getDatas()) {
            if (data.checked) {
                gravity |= data.gravity;
            }
        }
        mInfo.keyboardGravity = gravity;
        dismiss();
    }
}
