package razerdp.demo.fragment.basedemo;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.fragment.other.SimpleBaseFrag;
import razerdp.demo.services.DemoService;
import razerdp.demo.utils.MultiSpanUtil;

/**
 * Created by 大灯泡 on 2019/5/16
 * <p>
 * Description：
 */
public class ServicePopupFrag extends SimpleBaseFrag {
    TextView tvDesc;
    Button popupShow;

    private static final String DESC = "· 在Service里弹出PopupWindow，当context不是ActivityContext的时候，会取当前app顶级Activity作为windowToken";

    @Override
    public void onInitView(View rootView) {
        this.tvDesc = (TextView) rootView.findViewById(R.id.tv_desc);
        this.popupShow = (Button) rootView.findViewById(R.id.popup_show);

        MultiSpanUtil.create(DESC)
                .append("Service").setTextColor(Color.RED)
                .append("当前app顶级Activity").setTextColor(Color.RED)
                .into(tvDesc);

        popupShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startService(new Intent(getActivity(), DemoService.class));
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
        return mInflater.inflate(R.layout.frag_demo_service, container, false);
    }

}
