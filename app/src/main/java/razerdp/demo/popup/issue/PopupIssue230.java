package razerdp.demo.popup.issue;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.base.baseadapter.BaseSimpleRecyclerViewHolder;
import razerdp.demo.base.baseadapter.SimpleRecyclerViewAdapter;
import razerdp.demo.utils.ButterKnifeUtil;
import razerdp.demo.utils.RandomUtil;
import razerdp.demo.utils.UIHelper;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.ScaleConfig;

/**
 * Created by 大灯泡 on 2019/10/9.
 */
public class PopupIssue230 extends BasePopupWindow {

    @BindView(R.id.rv_content)
    RecyclerView mRvContent;

    SimpleRecyclerViewAdapter<String> mAdapter;

    public PopupIssue230(Context context) {
        super(context);
        setContentView(R.layout.popup_issue_230);
        setPopupGravity(Gravity.CENTER);
        setMaxHeight(UIHelper.getScreenHeight() >> 1);
        setMaxWidth(UIHelper.getScreenWidth() >> 1);

        List<String> data = new ArrayList<>();
        for (int i = 0; i < RandomUtil.randomInt(5, 10); i++) {
            data.add("pos ： " + (i + 1));
        }
        mAdapter = new SimpleRecyclerViewAdapter<>(context, data);
        mAdapter.setHolder(InnerViewHolder.class)
                .outher(this);
        mRvContent.setLayoutManager(new LinearLayoutManager(context));
        mRvContent.setAdapter(mAdapter);
    }

    @Override
    public void onViewCreated(View contentView) {
        ButterKnifeUtil.bind(this, contentView);
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

        @BindView(R.id.tv_content)
        TextView mTvContent;
        @BindView(R.id.btn_del)
        Button mBtnDel;

        public InnerViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnifeUtil.bind(this, itemView);
        }

        @Override
        public int inflateLayoutResourceId() {
            return R.layout.item_popup_230;
        }

        @Override
        public void onBindData(String data, int position) {
            mTvContent.setText(data);
        }

        @OnClick(R.id.btn_del)
        void del() {
            delItem(getData());
        }
    }
}
