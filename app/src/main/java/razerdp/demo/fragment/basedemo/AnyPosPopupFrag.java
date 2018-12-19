package razerdp.demo.fragment.basedemo;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
public class AnyPosPopupFrag extends SimpleBaseFrag {
    private ViewHolder vh;
    private static final String DESC = "· BasePopup在任意位置弹出。";
    private DemoPopup mDemoPopup;
    float x, y;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onInitView(View rootView) {
        vh = new ViewHolder(rootView);
        MultiSpanUtil.create(DESC)
                .append("任意位置").setTextColor(Color.RED)
                .into(vh.tvDesc);
        mDemoPopup = new DemoPopup(getContext());
        mDemoPopup.setPopupGravity(Gravity.CENTER)
                .setShowAnimation(SimpleAnimationUtils.getDefaultScaleAnimation(true))
                .setDismissAnimation(SimpleAnimationUtils.getDefaultScaleAnimation(false));


        vh.layoutTouchContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    x = event.getRawX();
                    y = event.getRawY();
                }
                return false;
            }
        });
        vh.layoutTouchContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mDemoPopup.showPopupWindow((int) x, (int) y);
                return true;
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
        return mInflater.inflate(R.layout.frag_demo_any_pos, container, false);
    }

    public static class ViewHolder {
        public View rootView;
        public TextView tvDesc;
        public LinearLayout layoutTouchContainer;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.tvDesc = (TextView) rootView.findViewById(R.id.tv_desc);
            this.layoutTouchContainer = (LinearLayout) rootView.findViewById(R.id.layout_touch_container);
        }

    }
}