package razerdp.demo.popup;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.basepopup.databinding.ActivityMyTestBinding;
import razerdp.demo.base.baseactivity.BaseBindingActivity;
import razerdp.demo.utils.UIHelper;

/**
 * Created by 大灯泡 on 2021/7/27.
 */
public class MyTestActivity extends BaseBindingActivity<ActivityMyTestBinding> {

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public ActivityMyTestBinding onCreateViewBinding(LayoutInflater layoutInflater) {
        return ActivityMyTestBinding.inflate(layoutInflater);
    }

    @Override
    protected void onInitView(View decorView) {
        mBinding.tvTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TestPopup(view.getContext()).showPopupWindow(view);
            }
        });
    }

    static class TestPopup extends BasePopupWindow {

        private RecyclerView mRvTest;

        public TestPopup(Context context) {
            super(context);
            setContentView(R.layout.popup_my_test);
            mRvTest = findViewById(R.id.rv_test);
            mRvTest.setLayoutManager(new LinearLayoutManager(context));
            List<String> datas = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                datas.add("item:" + i);
            }
            mRvTest.setAdapter(new TestAdapter(datas));
            setPopupGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
        }
    }

    static class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {
        List<String> datas;

        public TestAdapter(List<String> datas) {
            this.datas = datas;
        }

        @NonNull
        @Override
        public TestAdapter.TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new TestViewHolder(LayoutInflater.from(parent.getContext())
                                              .inflate(android.R.layout.simple_list_item_1, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull TestAdapter.TestViewHolder holder, int position) {
            holder.tvText.setText(datas.get(position));
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        class TestViewHolder extends RecyclerView.ViewHolder {
            TextView tvText;

            public TestViewHolder(@NonNull View itemView) {
                super(itemView);
                tvText = itemView.findViewById(android.R.id.text1);
                tvText.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            }
        }
    }
}
