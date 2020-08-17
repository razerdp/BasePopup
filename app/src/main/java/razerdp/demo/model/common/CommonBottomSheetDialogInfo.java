package razerdp.demo.model.common;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import razerdp.basepopup.R;
import razerdp.demo.model.DemoCommonUsageInfo;
import razerdp.demo.popup.DemoPopup;
import razerdp.demo.popup.PopupDesc;

/**
 * Created by 大灯泡 on 2019/9/26.
 * 兼容性测试 - BottomSheetDialog
 */
public class CommonBottomSheetDialogInfo extends DemoCommonUsageInfo {
    TestSheetDialog mSheetDialog;
    PopupDesc mPopupDesc;

    public CommonBottomSheetDialogInfo() {
        title = "BottomSheetDialog中弹出BasePopup";
        option = "描述";
//        问题描述：在没有依赖BasePopup Compat组件时，在Fragment或者BottomSheetDialog里弹出，会显示在这些组件下层。
    }

    @Override
    public void toShow(View v) {
        if (mSheetDialog == null) {
            mSheetDialog = new TestSheetDialog(v.getContext());
        }
        mSheetDialog.show();
    }

    @Override
    public void toOption(View v) {
        if (mPopupDesc == null) {
            mPopupDesc = new PopupDesc(v.getContext());
            mPopupDesc.setTitle("BottomSheetDialog中弹出BasePopup")
                    .setDesc(new StringBuilder("问题描述：")
                            .append('\n')
                            .append("在BottomSheetDialog或Fragment中弹出BasePopup时，由于默认取Activity的DecorView的WindowToken的缘故，因此会默认显示在BottomSheetDialog或Fragment的下层。")
                            .append('\n')
                            .append("BasePopup为了解决该问题，提供了BasePopup-Compat组件，用于适配此类兼容性问题。"));
        }
        mPopupDesc.showPopupWindow();
    }

    static class TestSheetDialog extends BottomSheetDialog {
        TextView tvShow;
        DemoPopup mDemoPopup;

        TestSheetDialog(@NonNull Context context) {
            super(context);
            setContentView(R.layout.view_bottom_sheet);
            tvShow = findViewById(R.id.tv_show);
            tvShow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopup();
                }
            });
        }

        private void showPopup() {
            if (mDemoPopup == null) {
                mDemoPopup = new DemoPopup(this).setText("兼容性测试\n\nBttomSheetDialog内弹出BasePopup");
            }
            mDemoPopup.setPopupGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
            mDemoPopup.showPopupWindow(tvShow);
        }
    }
}
