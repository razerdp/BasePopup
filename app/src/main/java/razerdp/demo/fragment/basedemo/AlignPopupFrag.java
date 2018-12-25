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
public class AlignPopupFrag extends SimpleBaseFrag {
    private ViewHolder vh;
    private static final String DESC = "· 设置BasePopup的背景是否对齐到ContentView所处位置，默认情况下背景铺满整个屏幕。\n" +
            "· 通常用于Popup上方不需要背景下方需要的场景。一般跟anchorView关联\n" +
            "· 该方法仅对背景颜色层有效，对模糊层无效。";
    private DemoPopup mDemoPopup;

    @Override
    public void onInitView(View rootView) {
        vh = new ViewHolder(rootView);
        MultiSpanUtil.create(DESC)
                .append("该方法仅对背景颜色层有效，对模糊层无效。").setTextColor(Color.RED)
                .into(vh.tvDesc);
        mDemoPopup = new DemoPopup(getContext());
        mDemoPopup.setPopupGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL)
                .setShowAnimation(SimpleAnimationUtils.getTranslateVerticalAnimation(-1f, 0, 500))
                .setDismissAnimation(SimpleAnimationUtils.getTranslateVerticalAnimation(0, -1f, 500));
        vh.popupShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDemoPopup.setAlignBackground(vh.checkAlignBackground.isChecked());
                int gravity = Gravity.NO_GRAVITY;
                if (vh.checkGravityLeft.isChecked()) {
                    gravity |= Gravity.LEFT;
                }
                if (vh.checkGravityTop.isChecked()) {
                    gravity |= Gravity.TOP;
                }
                if (vh.checkGravityRight.isChecked()) {
                    gravity |= Gravity.RIGHT;
                }
                if (vh.checkGravityBottom.isChecked()) {
                    gravity |= Gravity.BOTTOM;
                }
                if (vh.checkAlignBackground.isChecked()){
                    mDemoPopup.setAlignBackgroundGravity(gravity);
                }
                mDemoPopup.showPopupWindow(v);
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
        return mInflater.inflate(R.layout.frag_demo_align_background, container, false);
    }

    public static class ViewHolder {
        public View rootView;
        public TextView tvDesc;
        public Button popupShow;
        public AppCompatCheckBox checkAlignBackground;
        private AppCompatCheckBox checkGravityLeft;
        private AppCompatCheckBox checkGravityTop;
        private AppCompatCheckBox checkGravityRight;
        private AppCompatCheckBox checkGravityBottom;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.tvDesc = (TextView) rootView.findViewById(R.id.tv_desc);
            this.popupShow = (Button) rootView.findViewById(R.id.popup_show);
            this.checkAlignBackground = (AppCompatCheckBox) rootView.findViewById(R.id.check_align_background);
            this.checkGravityLeft = (AppCompatCheckBox) rootView.findViewById(R.id.check_gravity_left);
            this.checkGravityTop = (AppCompatCheckBox) rootView.findViewById(R.id.check_gravity_top);
            this.checkGravityRight = (AppCompatCheckBox) rootView.findViewById(R.id.check_gravity_right);
            this.checkGravityBottom = (AppCompatCheckBox) rootView.findViewById(R.id.check_gravity_bottom);
        }

    }
}