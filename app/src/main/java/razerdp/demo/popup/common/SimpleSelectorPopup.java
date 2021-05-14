package razerdp.demo.popup.common;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;

import com.bigkoo.pickerview.listener.OnOptionsSelectListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.ArrayRes;
import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.utils.ToolUtil;
import razerdp.demo.utils.UIHelper;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;

/**
 * Created by 大灯泡 on 2019/4/19.
 * <p>
 * 通用的text action
 */

public class SimpleSelectorPopup extends BasePopupWindow {

    private List<String> datas;
    private FrameLayout layoutContainer;
    private PopupOptionsPickerView<String> mOptionPickerView;
    private boolean changed;
    private int lastSelect = 0;

    public SimpleSelectorPopup(Context context) {
        super(context);
        setContentView(R.layout.popup_text_selector);
        layoutContainer = findViewById(R.id.layout_container);
        datas = new ArrayList<>();
        setBlurBackgroundEnable(true);
        mOptionPickerView = new PopupOptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                lastSelect = options1;
                if (mOnSelectListener != null) {
                    mOnSelectListener.onClicked(datas.get(options1), options1);
                }
                dismiss();
            }
        }).setOnCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        })
                .setBgColor(Color.TRANSPARENT)
                .setOutSideColor(Color.TRANSPARENT)
                .setDecorView(layoutContainer)
                .setCancelText("取消")
                .setSubmitText("确定")
                .setSubmitColor(UIHelper.getColor(R.color.text_black2))//确定按钮文字颜色
                .setCancelColor(UIHelper.getColor(R.color.text_black2))
                .setSubCalSize(12)
                .setOutSideCancelable(false)
                .setTitleBgColor(UIHelper.getColor(R.color.color_EEEEEE))
                .setLineSpacingMultiplier(1.8f)
                .setTextColorCenter(UIHelper.getColor(R.color.text_black2))
                .setContentTextSize(14)
                .build();

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


    public SimpleSelectorPopup append(@ArrayRes int res) {
        if (res == 0) return this;
        return append(getContext().getResources().getStringArray(res));
    }

    public SimpleSelectorPopup append(String[] datas) {
        if (datas == null || datas.length <= 0) return this;
        for (int i = 0; i < datas.length; i++) {
            append(datas[i]);
        }
        return this;

    }

    public SimpleSelectorPopup append(List<String> datas) {
        if (ToolUtil.isEmpty(datas)) return this;
        for (int i = 0; i < datas.size(); i++) {
            append(datas.get(i));
        }
        return this;
    }

    public SimpleSelectorPopup append(String text) {
        this.datas.add(text);
        changed = true;
        return this;
    }

    public void refreshData(List<String> datas) {
        if (datas != null) {
            this.datas.clear();
            this.datas.addAll(datas);
            changed = true;
        }
    }

    public void setSelectedPosition(int index) {
        if (ToolUtil.indexIn(datas, index)) {
            mOptionPickerView.setSelectOptions(index);
        }
    }

    public SimpleSelectorPopup setTitle(String title) {
        mOptionPickerView.setTitleText(title);
        return this;
    }

    @Override
    public void showPopupWindow() {
        if (changed) {
            mOptionPickerView.setPicker(datas);
        }
        mOptionPickerView.setSelectOptions(lastSelect);
        mOptionPickerView.show(false);
        super.showPopupWindow();
    }

    private OnSelectListener mOnSelectListener;

    public OnSelectListener getOnSelectedListener() {
        return mOnSelectListener;
    }

    public void setOnSelectedListener(OnSelectListener listener) {
        mOnSelectListener = listener;
    }

    public interface OnSelectListener {
        void onClicked(String selected, int index);
    }
}
