package razerdp.demo.ui.apidemo.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import butterknife.BindView;
import butterknife.OnClick;
import razerdp.basepopup.R;
import razerdp.demo.popup.DemoPopup;
import razerdp.demo.ui.apidemo.ApiDemoActivity;
import razerdp.demo.ui.apidemo.ApiDemoFragment;
import razerdp.demo.utils.SpanUtil;
import razerdp.demo.utils.UIHelper;
import razerdp.demo.widget.DPTextView;

/**
 * Created by 大灯泡 on 2020/4/4.
 * 构造器api
 */
public class ConstructorApiFragment extends ApiDemoFragment {
    @BindView(R.id.tv_api)
    TextView mTvApi;
    @BindView(R.id.tv_context)
    DPTextView mTvContext;
    @BindView(R.id.tv_dialog)
    DPTextView mTvDialog;
    @BindView(R.id.tv_dialogfragment)
    DPTextView mTvDialogFragment;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.tv_tips)
    TextView mTvTips;

    int index = 0;
    DemoPopup curPopup;
    TestDialog mTestDialog;
    TestDialogFragment mTestDialogFragment;

    SparseArray<DemoPopup> mDemoPopupSparseArray;


    @Override
    public int contentViewLayoutId() {
        return R.layout.api_demo_constructor;
    }

    @Override
    protected void onInitViews(View mRootView) {
        mDemoPopupSparseArray = new SparseArray<>();
        mTestDialog = new TestDialog(getContext());
        mTestDialog.setOnClickListener(v -> curPopup.showPopupWindow());
        mTestDialogFragment = new TestDialogFragment();
        mTestDialogFragment.setOnClickListener(v -> curPopup.showPopupWindow());
        onSettingPopupSelected("BasePopupWindow(Context context)", 0);
    }


    @Override
    protected void onInitSettingPopup(@NonNull ApiDemoActivity.SimpleSelectorPopupConfig config) {
        config.setTitle("构造器选择")
                .append("BasePopupWindow(Context context)")
                .append("BasePopupWindow(Dialog dialog)")
                .append("BasePopupWindow(Fragment fragment)");
    }

    @Override
    protected void onSettingPopupSelected(String selected, int index) {
        mTvApi.setText(selected);
        this.index = index;
        curPopup = mDemoPopupSparseArray.get(index);
        boolean needPut = curPopup == null;
        switch (index) {
            case 0:
                mTvContent.setText(R.string.api_constructor_context);
                if (curPopup == null) {
                    curPopup = new DemoPopup(getContext());
                }
                break;
            case 1:
                mTvContent.setText(R.string.api_constructor_dialog);
                if (curPopup == null) {
                    curPopup = new DemoPopup(mTestDialog);
                }
                break;
            case 2:
                SpanUtil.create(R.string.api_constructor_fragment)
                        .append("【注意】：DialogFragment在getDialog()!=null的情况下，会获取其Dialog的WindowToken。")
                        .setTextStyle(Typeface.DEFAULT_BOLD)
                        .setTextColorRes(R.color.common_red_light)
                        .into(mTvContent);
                if (curPopup == null) {
                    curPopup = new DemoPopup(mTestDialogFragment);
                }
                break;
        }
        if (needPut) {
            mDemoPopupSparseArray.put(index, curPopup);
        }
    }

    @OnClick(R.id.tv_context)
    void onContextPopupClick() {
        curPopup.showPopupWindow();
        if (index == 1) {
            mTvTips.setText("此时Popup依附于Dialog，在Activity的Window中无法弹窗，待Dialog显示时会弹出");
        } else if (index == 2) {
            mTvTips.setText("此时Popup依附于DialogFragment，由于DialogFragment在显示的时候才能获取到WindowToken，此时无法弹出");
        } else {
            mTvTips.setText(null);
        }
    }

    @OnClick(R.id.tv_dialog)
    void onDialogPopupClick() {
        mTestDialog.show();
        if (index == 2) {
            mTvTips.setText("此时Popup依附于DialogFragment，由于DialogFragment在显示的时候才能获取到WindowToken，此时无法弹出");
        } else {
            mTvTips.setText(null);
        }
    }

    @OnClick(R.id.tv_dialogfragment)
    void onDialogFragmentPopupClick() {
        mTestDialogFragment.show(getChildFragmentManager(), getClass().toString());
        if (index == 1) {
            mTvTips.setText("此时Popup依附于Dialog，在Activity的Window中无法弹窗，待Dialog显示时会弹出");
        } else {
            mTvTips.setText(null);
        }
    }

    static class TestDialog extends Dialog {
        TextView tvShow;
        View.OnClickListener mOnClickListener;

        TestDialog(@NonNull Context context) {
            super(context);
            setContentView(R.layout.dialog_api_constructor);
            tvShow = findViewById(R.id.tv_show);
            tvShow.setOnClickListener(v -> {
                if (mOnClickListener != null) {
                    mOnClickListener.onClick(v);
                }
            });
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Window dialogWindow = getWindow();
            if (dialogWindow != null) {
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                lp.width = Math.round(UIHelper.getScreenWidth() * 0.8f);
                lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;
                dialogWindow.setAttributes(lp);
            }
        }

        public TestDialog setOnClickListener(View.OnClickListener onClickListener) {
            mOnClickListener = onClickListener;
            return this;
        }
    }

    public static class TestDialogFragment extends DialogFragment {
        View.OnClickListener mOnClickListener;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View contentView = inflater.inflate(R.layout.dialog_fragment_api_constructor, container, false);
            View tvShow = contentView.findViewById(R.id.tv_show);
            tvShow.setOnClickListener(v -> {
                if (mOnClickListener != null) {
                    mOnClickListener.onClick(v);
                }
            });
            return contentView;
        }

        @Override
        public void onStart() {
            super.onStart();
            Window dialogWindow = getDialog().getWindow();
            if (dialogWindow != null) {
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                lp.width = Math.round(UIHelper.getScreenWidth() * 0.8f);
                lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;
                dialogWindow.setAttributes(lp);
            }
        }

        public TestDialogFragment setOnClickListener(View.OnClickListener onClickListener) {
            mOnClickListener = onClickListener;
            return this;
        }
    }
}
