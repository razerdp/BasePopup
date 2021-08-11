package razerdp.demo.popup;

import android.content.Context;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.basepopup.databinding.ItemPopup230Binding;
import razerdp.basepopup.databinding.PopupWidthHeightLimitBinding;
import razerdp.demo.base.baseadapter.BaseSimpleRecyclerViewHolder;
import razerdp.demo.base.baseadapter.SimpleRecyclerViewAdapter;
import razerdp.demo.utils.UIHelper;
import razerdp.util.animation.AlphaConfig;
import razerdp.util.animation.AnimationHelper;

/**
 * Created by 大灯泡 on 2021/8/6.
 */
public class PopupWidthHeightLimit extends BasePopupWindow {
    PopupWidthHeightLimitBinding mBinding;
    SimpleRecyclerViewAdapter<String> mAdapter;

    public PopupWidthHeightLimit(Context context) {
        super(context);
        setContentView(R.layout.popup_width_height_limit);
        mAdapter = new SimpleRecyclerViewAdapter<>(context);
        mAdapter.setHolder(InnerViewHolder.class)
                .outher(this);
        mBinding.rvContent.setLayoutManager(new LinearLayoutManager(context));
        mBinding.rvContent.setAdapter(mAdapter);
        mBinding.tvAddItem.setOnClickListener(v -> addData());
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        mBinding = PopupWidthHeightLimitBinding.bind(contentView);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return AnimationHelper.asAnimation()
                .withAlpha(AlphaConfig.IN)
                .toShow();
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return AnimationHelper.asAnimation()
                .withAlpha(AlphaConfig.OUT)
                .toDismiss();
    }

    public void addData() {
        mAdapter.addData("pos " + (mAdapter.getDatas().size() + 1));
    }

    @Override
    public void onSizeChange(int oldW, int oldH, int newW, int newH) {
        mBinding.tvMysize.setText(String.format("当前Popup宽高信息：\nw = %s \n h = %s",
                                                newW,
                                                newH));
    }

    void delItem(String which) {
        mAdapter.remove(which);
    }

    class InnerViewHolder extends BaseSimpleRecyclerViewHolder<String> {
        ItemPopup230Binding mBinding;

        public InnerViewHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = ItemPopup230Binding.bind(itemView);
            mBinding.btnDel.setOnClickListener(v -> del());
        }

        @Override
        public int inflateLayoutResourceId() {
            return R.layout.item_popup_230;
        }

        @Override
        public void onBindData(String data, int position) {
            mBinding.tvContent.setText(data);
        }

        void del() {
            delItem(getData());
        }
    }
}
