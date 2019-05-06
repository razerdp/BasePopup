package razerdp.demo.popup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.base.baseadapter.BaseRecyclerViewAdapter;
import razerdp.demo.base.baseadapter.BaseRecyclerViewHolder;
import razerdp.demo.utils.ToastUtils;

/**
 * Created by 大灯泡 on 2019/5/6.
 * <p>
 */
public class RecyclerViewPopup extends BasePopupWindow implements View.OnClickListener {

    private RecyclerView mRecyclerView;


    public RecyclerViewPopup(Context context) {
        this(context, -1);
    }

    public RecyclerViewPopup(Context context, int count) {
        super(context);
        if (count <= 0) {
            count = 10;
        }
        mRecyclerView = findViewById(R.id.rv_content);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        List<String> data = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            data.add("test" + i);
        }
        mRecyclerView.setAdapter(new InnerAdapter(context, data));
        bindEvent();
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getDefaultAlphaAnimation();
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getDefaultAlphaAnimation(false);
    }


    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_menu_rv);
    }

    private void bindEvent() {

    }

    @Override
    public void onAnchorTop() {
        ToastUtils.ToastMessage(getContext(), "显示在上方（下方位置不足）");
    }

    @Override
    public void onAnchorBottom() {
        ToastUtils.ToastMessage(getContext(), "显示在下方（上位置不足）");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tx_1:
                ToastUtils.ToastMessage(getContext(), "click tx_1");
                break;
            case R.id.tx_2:
                ToastUtils.ToastMessage(getContext(), "click tx_2");
                break;
            case R.id.tx_3:
                ToastUtils.ToastMessage(getContext(), "click tx_3");
                break;
            default:
                break;
        }

    }

    class InnerAdapter extends BaseRecyclerViewAdapter<String> {

        public InnerAdapter(@NonNull Context context, @NonNull List<String> datas) {
            super(context, datas);
        }

        @Override
        protected int getViewType(int position, @NonNull String data) {
            return 0;
        }

        @Override
        protected int getLayoutResId(int viewType) {
            return R.layout.item_menu_rv;
        }

        @Override
        protected BaseRecyclerViewHolder getViewHolder(ViewGroup parent, View rootView, int viewType) {
            return new InnerAdapter.InnerViewHolder(rootView, viewType);
        }

        class InnerViewHolder extends BaseRecyclerViewHolder<String> {
            TextView content;

            public InnerViewHolder(View itemView, int viewType) {
                super(itemView, viewType);
                content = findViewById(R.id.tv_content);
            }

            @Override
            public void onBindData(String data, int position) {
                content.setText(data);
            }
        }
    }
}
