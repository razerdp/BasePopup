package razerdp.demo.fragment.basedemo;

import android.graphics.Color;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.blur.PopupBlurOption;
import razerdp.demo.fragment.other.SimpleBaseFrag;
import razerdp.demo.popup.DemoPopup;
import razerdp.demo.utils.MultiSpanUtil;
import razerdp.util.SimpleAnimationUtils;

/**
 * Created by 大灯泡 on 2018/12/11.
 */
public class BlurPopupFrag extends SimpleBaseFrag {
    private ViewHolder vh;
    private static final String DESC = "· 设置是否允许BasePopup弹出时模糊背景。\n" +
            "· 允许传入OnBlurOptionInitListener进行默认BlurOption的配置。\n" +
            "· 或者通过setBlurOption方法配置模糊效果（对单个View模糊或者背景模糊程度，时间等）。";
    private DemoPopup mDemoPopup;

    @Override
    public void onInitView(View rootView) {
        vh = new ViewHolder(rootView);
        MultiSpanUtil.create(DESC)
                .append("OnBlurOptionInitListener").setTextColor(Color.RED)
                .append("setBlurOption").setTextColor(Color.RED)
                .append("单个View模糊").setTextColor(Color.RED)
                .into(vh.tvDesc);
        mDemoPopup = new DemoPopup(getContext());
        mDemoPopup.setPopupGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL)
                .setShowAnimation(SimpleAnimationUtils.getTranslateVerticalAnimation(1f, 0, 500))
                .setDismissAnimation(SimpleAnimationUtils.getTranslateVerticalAnimation(0, 1f, 500))
                .setBackground(null);
        vh.popupShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vh.checkBlurOnlyShow.isChecked()) {
                    PopupBlurOption option = new PopupBlurOption();
                    option.setBlurView(vh.popupShow);
                    mDemoPopup.setBlurOption(option);
                } else {
                    mDemoPopup.setBlurBackgroundEnable(vh.checkOpenBlur.isChecked());
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
        return mInflater.inflate(R.layout.frag_demo_blur, container, false);
    }

    public static class ViewHolder {
        public View rootView;
        public TextView tvDesc;
        public Button popupShow;
        public AppCompatCheckBox checkOpenBlur;
        public AppCompatCheckBox checkBlurOnlyShow;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.tvDesc = (TextView) rootView.findViewById(R.id.tv_desc);
            this.popupShow = (Button) rootView.findViewById(R.id.popup_show);
            this.checkOpenBlur = (AppCompatCheckBox) rootView.findViewById(R.id.check_open_blur);
            this.checkBlurOnlyShow = (AppCompatCheckBox) rootView.findViewById(R.id.check_blur_only_show);
        }

    }
}