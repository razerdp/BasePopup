package razerdp.demo.fragment.basedemo;

import android.graphics.Color;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.TextView;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.fragment.other.SimpleBaseFrag;
import razerdp.demo.popup.DemoPopup;
import razerdp.demo.utils.MultiSpanUtil;

/**
 * Created by 大灯泡 on 2018/12/11.
 */
public class ClipChildrenPopupFrag extends SimpleBaseFrag {
    private ViewHolder vh;
    private static final String DESC = "· 设置BasePopup是否裁剪contentView，默认为true，该方法常与动画配置有关。\n" +
            "· 传入true，contentView则会被裁剪至其绘制边界内，动画如放大动画会受到影响而无法突破其绘制范围。\n" +
            "· 传入false，contentView允许突破绘制范围。\n";
    private DemoPopup mDemoPopup;

    @Override
    public void onInitView(View rootView) {
        vh = new ViewHolder(rootView);
        MultiSpanUtil.create(DESC)
                .append("动画配置").setTextColor(Color.RED)
                .into(vh.tvDesc);
        mDemoPopup = new DemoPopup(getContext());
        mDemoPopup.setPopupGravity(Gravity.CENTER)
                .setShowAnimation(createAnimation(true))
                .setDismissAnimation(createAnimation(false));
        vh.popupShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDemoPopup.setClipChildren(vh.checkClipchildren.isChecked());
                mDemoPopup.showPopupWindow();
            }
        });

    }

    private Animation createAnimation(boolean in) {
        RotateAnimation result = new RotateAnimation(in ? 0 : 360f, in ? 360f : 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        result.setInterpolator(new OvershootInterpolator(in ? 4 : -4));
        result.setDuration(1500);
        return result;
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
        return mInflater.inflate(R.layout.frag_demo_clip_children, container, false);
    }

    public static class ViewHolder {
        public View rootView;
        public TextView tvDesc;
        public Button popupShow;
        public AppCompatCheckBox checkClipchildren;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.tvDesc = (TextView) rootView.findViewById(R.id.tv_desc);
            this.popupShow = (Button) rootView.findViewById(R.id.popup_show);
            this.checkClipchildren = (AppCompatCheckBox) rootView.findViewById(R.id.check_clipchildren);
        }

    }
}