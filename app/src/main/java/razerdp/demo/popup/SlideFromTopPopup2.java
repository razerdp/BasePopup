package razerdp.demo.popup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.base.baseadapter.BaseRecyclerViewAdapter;
import razerdp.demo.base.baseadapter.BaseRecyclerViewHolder;
import razerdp.demo.base.baseadapter.OnRecyclerViewItemClickListener;
import razerdp.demo.utils.DimensUtils;

/**
 * Created by 大灯泡 on 2016/12/6.
 * <p>
 * 从顶部下滑的Poup
 */

public class SlideFromTopPopup2 extends BasePopupWindow {

    public SlideFromTopPopup2(Context context) {
        super(context);
        setBackPressEnable(false);
        setAllowDismissWhenTouchOutside(true);
        setAlignBackground(true);
        List<String> testList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            testList.add("position - " + i);
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rcv_content);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        InnerPopupAdapter adapter = new InnerPopupAdapter(context, testList);
        recyclerView.setAdapter(adapter);
        adapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<String>() {
            @Override
            public void onItemClick(View v, int position, String data) {
                Toast.makeText(getContext(), data, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected Animation onCreateShowAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(0f, 0f, -DimensUtils.dipToPx(getContext(), 350f), 0);
        translateAnimation.setDuration(450);
        translateAnimation.setInterpolator(new OvershootInterpolator(1));
        return translateAnimation;
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(0f, 0f, 0, -DimensUtils.dipToPx(getContext(), 350f));
        translateAnimation.setDuration(450);
        translateAnimation.setInterpolator(new OvershootInterpolator(-4));
        return translateAnimation;
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_select_from_top2);
    }

    //=============================================================adapter
    private static class InnerPopupAdapter extends BaseRecyclerViewAdapter<String> {
        public InnerPopupAdapter(@NonNull Context context, @NonNull List<String> datas) {
            super(context, datas);
        }

        @Override
        protected int getViewType(int position, @NonNull String data) {
            return 0;
        }

        @Override
        protected int getLayoutResId(int viewType) {
            return R.layout.item_popup_list;
        }

        @Override
        protected BaseRecyclerViewHolder getViewHolder(ViewGroup parent, View rootView, int viewType) {
            return new ViewHolder(rootView, viewType);
        }

        class ViewHolder extends BaseRecyclerViewHolder<String> {
            public TextView mTextView;

            public ViewHolder(View itemView, int viewType) {
                super(itemView, viewType);
                mTextView = findViewById(R.id.item_tx);
            }

            @Override
            public void onBindData(String data, int position) {
                mTextView.setText(data);
            }
        }
    }
}
