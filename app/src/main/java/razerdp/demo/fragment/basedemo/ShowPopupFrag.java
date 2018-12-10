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

/**
 * Created by 大灯泡 on 2018/12/10.
 */
public class ShowPopupFrag extends SimpleBaseFrag {
    private ViewHolder vh;
    private static final String DESC = "· 弹出BasePopup，无参传入，此时不与anchorView关联。\n" +
            "· 弹出BasePopup，无参传入，此时跟传入的anchorView关联，默认弹出在anchorView下方，并尝试左侧对齐。";
    private DemoPopup mDemoPopup;

    @Override
    public void onInitView(View rootView) {
        vh = new ViewHolder(rootView);
        MultiSpanUtil.create(DESC)
                .append("默认弹出在anchorView下方，并尝试左侧对齐。").setTextColor(Color.RED)
                .into(vh.tvDesc);
        mDemoPopup = new DemoPopup(getContext());
        mDemoPopup.setPopupGravity(Gravity.NO_GRAVITY);
        vh.popupShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vh.checkCombineAnchor.isChecked()) {
                    mDemoPopup.showPopupWindow(v);
                } else {
                    mDemoPopup.showPopupWindow();
                }
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
        return mInflater.inflate(R.layout.frag_demo_showpopup, container, false);
    }

    public static class ViewHolder {
        public View rootView;
        public TextView tvDesc;
        public Button popupShow;
        public AppCompatCheckBox checkCombineAnchor;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.tvDesc = (TextView) rootView.findViewById(R.id.tv_desc);
            this.popupShow = (Button) rootView.findViewById(R.id.popup_show);
            this.checkCombineAnchor = (AppCompatCheckBox) rootView.findViewById(R.id.check_combine_anchor);
        }

    }
}
