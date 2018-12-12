package razerdp.demo.fragment.basedemo;

import android.graphics.Color;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.fragment.other.SimpleBaseFrag;
import razerdp.demo.popup.InputPopup;
import razerdp.demo.utils.MultiSpanUtil;

/**
 * Created by 大灯泡 on 2018/12/11.
 */
public class InputPopupFrag extends SimpleBaseFrag {
    private ViewHolder vh;
    private static final String DESC = "· 设置BasePopup在弹出时是否自动打开输入法（当存在EditText）。\n" +
            "· 设置输入法适配flag（仅有ADJUST_PAN或者ADJUST_RESIZE）有效，其余等同于不适配";
    private InputPopup mDemoPopup;

    @Override
    public void onInitView(View rootView) {
        vh = new ViewHolder(rootView);
        MultiSpanUtil.create(DESC)
                .append("当存在EditText").setTextColor(Color.RED)
                .append("仅有ADJUST_PAN或者ADJUST_RESIZE").setTextColor(Color.RED)
                .into(vh.tvDesc);
        mDemoPopup = new InputPopup(getContext());
        vh.popupShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDemoPopup.setAutoShowInputMethod(mDemoPopup.getInputEdittext(), vh.checkAutoShowInput.isChecked());
                if (vh.checkNeedAdjust.isChecked()) {
                    mDemoPopup.setAdjustInputMethod(true);
                    if (vh.checkAdjustNothing.isChecked()) {
                        mDemoPopup.setAdjustInputMethod(true, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
                    }
                    if (vh.checkAdjustPan.isChecked()) {
                        mDemoPopup.setAdjustInputMethod(true, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                    }
                } else {
                    mDemoPopup.setAdjustInputMethod(false);
                }
                mDemoPopup.showPopupWindow();
            }
        });

    }

    @Override
    public BasePopupWindow getPopup() {
        return null;
    }

    @Override
    public Button getButton() {
        return null;
    }

    @Override
    public View getFragment() {
        return mInflater.inflate(R.layout.frag_demo_input, container, false);
    }

    public static class ViewHolder {
        public View rootView;
        public TextView tvDesc;
        public Button popupShow;
        public AppCompatCheckBox checkAutoShowInput;
        public AppCompatCheckBox checkNeedAdjust;
        public AppCompatCheckBox checkAdjustNothing;
        public AppCompatCheckBox checkAdjustPan;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.tvDesc = (TextView) rootView.findViewById(R.id.tv_desc);
            this.popupShow = (Button) rootView.findViewById(R.id.popup_show);
            this.checkAutoShowInput = (AppCompatCheckBox) rootView.findViewById(R.id.check_auto_show_input);
            this.checkNeedAdjust = (AppCompatCheckBox) rootView.findViewById(R.id.check_needAdjust);
            this.checkAdjustNothing = (AppCompatCheckBox) rootView.findViewById(R.id.check_adjust_nothing);
            this.checkAdjustPan = (AppCompatCheckBox) rootView.findViewById(R.id.check_adjust_pan);
        }

    }
}