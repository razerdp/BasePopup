package razerdp.demo.fragment.basedemo;

import android.graphics.Color;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
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
public class ClipToScreenPopupFrag extends SimpleBaseFrag {
    private ViewHolder vh;
    private static final String DESC = "· 设置BasePopup是否禁止布局到屏幕外，默认为true。\n" +
            "· 如果传入false，当contentView宽高大于屏幕宽高，在允许范围内会自动进行位移以满足限制条件，这可能会导致setAutoLocatePopup失效";
    private DemoPopup mDemoPopup;

    @Override
    public void onInitView(View rootView) {
        vh = new ViewHolder(rootView);
        MultiSpanUtil.create(DESC)
                .append("true").setTextColor(Color.RED)
                .append("这可能会导致setAutoLocatePopup失效").setTextColor(Color.RED)
                .into(vh.tvDesc);
        mDemoPopup = new DemoPopup(getContext());
        mDemoPopup.setText("这是填充宽度的字 这是填充宽度的字 这是填充宽度的字 这是填充宽度的字 这是填充宽度的字");
        mDemoPopup.setPopupGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL)
                .setShowAnimation(createAnimation(true))
                .setDismissAnimation(createAnimation(false));
        vh.popupShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDemoPopup.setClipToScreen(!vh.checkClipToScreen.isChecked());
                mDemoPopup.showPopupWindow(v);
            }
        });

    }

    private Animation createAnimation(boolean in) {
        Animation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, in ? -1f : 0, Animation.RELATIVE_TO_PARENT, in ? 0 : -1f, Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0);
        translateAnimation.setDuration(500);
        return translateAnimation;
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
        return mInflater.inflate(R.layout.frag_demo_clip_to_screen, container, false);
    }

    public static class ViewHolder {
        public View rootView;
        public TextView tvDesc;
        public Button popupShow;
        public AppCompatCheckBox checkClipToScreen;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.tvDesc = (TextView) rootView.findViewById(R.id.tv_desc);
            this.popupShow = (Button) rootView.findViewById(R.id.popup_show);
            this.checkClipToScreen = (AppCompatCheckBox) rootView.findViewById(R.id.check_clip_to_screen);
        }

    }
}