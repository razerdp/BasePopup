package razerdp.demo.popup.issue;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.base.baseadapter.BaseSimpleRecyclerViewHolder;
import razerdp.demo.base.baseadapter.SimpleRecyclerViewAdapter;
import razerdp.demo.utils.ButterKnifeUtil;
import razerdp.demo.utils.UIHelper;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;

/**
 * Created by 大灯泡 on 2020/4/6.
 */
public class PopupIssue224 extends BasePopupWindow {
    @BindView(R.id.rv_content)
    RecyclerView mRvContent;

    SimpleRecyclerViewAdapter<Integer> mAdapter;
    private List<Integer> mIntegers;

    public PopupIssue224(Context context) {
        super(context);
        setContentView(R.layout.popup_issue_224);
        mIntegers = new ArrayList<>();
        mAdapter = new SimpleRecyclerViewAdapter<>(context, mIntegers);
        mAdapter.setHolder(ViewHolder.class);
        DividerItemDecoration divider = new DividerItemDecoration(context,
                                                                  DividerItemDecoration.VERTICAL);
        divider.setDrawable(UIHelper.getDrawable(R.drawable.divider));
        mAdapter.setOnItemClickListener((v, position, data) -> UIHelper.toast(String.valueOf(data)));
        mRvContent.setLayoutManager(new LinearLayoutManager(context));
        mRvContent.addItemDecoration(divider);
        mRvContent.setAdapter(mAdapter);
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

    @Override
    public void onViewCreated(@NonNull View contentView) {
        ButterKnifeUtil.bind(this, contentView);
    }

    public void setItemCount(int count) {
        count = Math.max(count, 0);
        mIntegers.clear();
        for (int i = 0; i < count; i++) {
            mIntegers.add(i);
        }
        mAdapter.updateData(mIntegers);
    }

    static class ViewHolder extends BaseSimpleRecyclerViewHolder<Integer> {

        TextView tvContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContent = findViewById(R.id.tv_content);
        }

        @Override
        public void onBindData(Integer data, int position) {
            tvContent.setText(String.format("Item：%s", String.valueOf(data)));
        }

        @Override
        public int inflateLayoutResourceId() {
            return R.layout.item_popup_issue_224;
        }
    }
}
