package razerdp.demo.fragment.basedemo;

import android.graphics.Color;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.fragment.other.SimpleBaseFrag;
import razerdp.demo.popup.DemoPopup;
import razerdp.demo.utils.MultiSpanUtil;
import razerdp.util.SimpleAnimationUtils;

/**
 * Created by 大灯泡 on 2018/12/11.
 */
public class OutsideDismissPopupFrag extends SimpleBaseFrag {
    private ViewHolder vh;
    private static final String DESC = "· 设置是否允许点击BasePopup外部时触发dismiss。\n" +
            "· 【注】：此处外部指contentView外。\n";
    private DemoPopup mDemoPopup;

    @Override
    public void onInitView(View rootView) {
        vh = new ViewHolder(rootView);
        MultiSpanUtil.create(DESC)
                .append("外部").setTextColor(Color.RED)
                .append("【注】：此处外部指contentView外。").setTextColor(Color.RED)
                .into(vh.tvDesc);
        mDemoPopup = new DemoPopup(getContext());
        mDemoPopup.setPopupGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL)
                .setShowAnimation(SimpleAnimationUtils.getTranslateVerticalAnimation(1f, 0, 500))
                .setDismissAnimation(SimpleAnimationUtils.getTranslateVerticalAnimation(0, 1f, 500));
        vh.popupShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDemoPopup.setOutSideDismiss(vh.checkDismissOutside.isChecked());
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
        return mInflater.inflate(R.layout.frag_demo_outside_dismiss, container, false);
    }

    public static class ViewHolder {
        public View rootView;
        public TextView tvDesc;
        public Button popupShow;
        public AppCompatCheckBox checkDismissOutside;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.tvDesc = (TextView) rootView.findViewById(R.id.tv_desc);
            this.popupShow = (Button) rootView.findViewById(R.id.popup_show);
            this.checkDismissOutside = (AppCompatCheckBox) rootView.findViewById(R.id.check_dismiss_outside);
        }

    }
}