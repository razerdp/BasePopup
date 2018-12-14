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
import razerdp.demo.utils.UIHelper;
import razerdp.util.SimpleAnimationUtils;

/**
 * Created by 大灯泡 on 2018/12/11.
 */
public class TouchInterceptPopupFrag extends SimpleBaseFrag {
    private ViewHolder vh;
    private static final String DESC = "· 设置是否允许BasePopup拦截事件，默认拦截。\n" +
            "· 【注】：如果不拦截事件，则背景层将会被去除（如果存在背景层又可以点击外部，这在交互和逻辑上是冲突的），去除背景层自2.1.0版本开始生效。";
    private DemoPopup mDemoPopup;

    @Override
    public void onInitView(View rootView) {
        vh = new ViewHolder(rootView);
        MultiSpanUtil.create(DESC)
                .append("默认拦截").setTextColor(Color.RED)
                .into(vh.tvDesc);
        mDemoPopup = new DemoPopup(getContext());
        mDemoPopup.setPopupGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL)
                .setShowAnimation(SimpleAnimationUtils.getTranslateVerticalAnimation(-1f, 0, 500))
                .setDismissAnimation(SimpleAnimationUtils.getTranslateVerticalAnimation(0, -1f, 500));
        vh.popupShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDemoPopup.setAllowInterceptTouchEvent(vh.checkTouchIntercept.isChecked());
                mDemoPopup.showPopupWindow(v);
            }
        });

        vh.btnClickMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.toast("点我~嗯啊");
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
        return mInflater.inflate(R.layout.frag_demo_touch_intercept, container, false);
    }

    public static class ViewHolder {
        public View rootView;
        public TextView tvDesc;
        public Button btnClickMe;
        public Button popupShow;
        public AppCompatCheckBox checkTouchIntercept;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.tvDesc = (TextView) rootView.findViewById(R.id.tv_desc);
            this.btnClickMe = (Button) rootView.findViewById(R.id.btn_click_me);
            this.popupShow = (Button) rootView.findViewById(R.id.popup_show);
            this.checkTouchIntercept = (AppCompatCheckBox) rootView.findViewById(R.id.check_touch_intercept);
        }

    }
}