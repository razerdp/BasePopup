package razerdp.demo.popup.issue;

import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.basepopup.databinding.ItemPopup230Binding;
import razerdp.basepopup.databinding.PopupIssue230Binding;
import razerdp.demo.base.baseadapter.BaseSimpleRecyclerViewHolder;
import razerdp.demo.base.baseadapter.SimpleRecyclerViewAdapter;
import razerdp.demo.utils.RandomUtil;
import razerdp.demo.utils.UIHelper;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.ScaleConfig;
import razerdp.util.log.PopupLog;

/**
 * Created by 大灯泡 on 2019/10/9.
 */
public class PopupIssue230 extends BasePopupWindow {
    PopupIssue230Binding mBinding;

    SimpleRecyclerViewAdapter<String> mAdapter;

    public PopupIssue230(Context context) {
        super(context);
        setContentView(R.layout.popup_issue_230);
        setPopupGravity(Gravity.CENTER);
        setMaxHeight(UIHelper.getScreenHeight() >> 1);
        setMaxWidth(UIHelper.getScreenWidth() >> 1);

        List<String> data = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            data.add("pos ： " + (i + 1));
        }
        mAdapter = new SimpleRecyclerViewAdapter<>(context, data);
        mAdapter.setHolder(InnerViewHolder.class)
                .outher(this);
        mBinding.rvContent.setLayoutManager(new LinearLayoutManager(context));
        mBinding.rvContent.setAdapter(mAdapter);
    }

    @Override
    public void onViewCreated(View contentView) {
        mBinding = PopupIssue230Binding.bind(contentView);
    }


    void delItem(String which) {
        mAdapter.remove(which);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return AnimationHelper.asAnimation()
                .withScale(ScaleConfig.CENTER)
                .toShow();
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return AnimationHelper.asAnimation()
                .withScale(ScaleConfig.CENTER)
                .toDismiss();
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
