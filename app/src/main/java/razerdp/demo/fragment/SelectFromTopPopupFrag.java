package razerdp.demo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.popup.ScalePopup;
import razerdp.demo.popup.SelectFromTopPopup;

/**
 * Created by 大灯泡 on 2016/1/15.
 */
public class SelectFromTopPopupFrag extends SimpleBaseFrag {

    private SelectFromTopPopup mSelectFromTopPopup;
    private RelativeLayout titleView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void bindEvent() {
        titleView = (RelativeLayout) findViewById(R.id.rl_title);
        mSelectFromTopPopup = new SelectFromTopPopup(mContext);
        findViewById(R.id.tv_select_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectFromTopPopup.showPopupWindow(titleView);
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
        return mInflater.inflate(R.layout.frag_select_from_top_popup, container, false);
    }
}
