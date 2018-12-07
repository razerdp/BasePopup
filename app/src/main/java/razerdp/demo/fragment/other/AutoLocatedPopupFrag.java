package razerdp.demo.fragment.other;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.popup.AutoLocatedPopup;
import razerdp.demo.popup.AutoLocatedRecyclerViewPopup;

/**
 * Created by 大灯泡 on 2016/11/23.
 */
public class AutoLocatedPopupFrag extends SimpleBaseFrag implements View.OnClickListener {
    private Button popup_show;
    private Button popup_show1;
    private Button popup_show2;
    private Button popup_show3;
    private AppCompatCheckBox mUseRcv;

    private AutoLocatedRecyclerViewPopup mAutoLocatedRecyclerViewPopup;
    private AutoLocatedPopup mAutoLocatedPopup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAutoLocatedRecyclerViewPopup = new AutoLocatedRecyclerViewPopup(getActivity());
        mAutoLocatedPopup = new AutoLocatedPopup(getContext());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void bindEvent() {
        popup_show = (Button) findViewById(R.id.popup_show);
        popup_show1 = (Button) findViewById(R.id.popup_show1);
        popup_show2 = (Button) findViewById(R.id.popup_show2);
        popup_show3 = (Button) findViewById(R.id.popup_show3);
        mUseRcv = (AppCompatCheckBox) findViewById(R.id.check_use_rcv);

        setViewsClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUseRcv.isChecked()) {
                    mAutoLocatedRecyclerViewPopup.showPopupWindow(v);
                } else {
                    mAutoLocatedPopup.showPopupWindow(v);
                }
            }
        }, popup_show, popup_show1, popup_show2, popup_show3);

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
        return mInflater.inflate(R.layout.frag_auto_located_popup, container, false);
    }

    public static void setViewsClickListener(@Nullable View.OnClickListener listener, View... views) {
        for (View view : views) {
            if (view != null) {
                view.setOnClickListener(listener);
            }
        }
    }
}
