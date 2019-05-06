package razerdp.demo.fragment.other;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.popup.AutoLocatedRecyclerViewPopup;
import razerdp.demo.popup.RecyclerViewPopup;

/**
 * Created by 大灯泡 on 2019/5/6.
 */
public class OtherPopupFrag2 extends SimpleBaseFrag {

    RecyclerViewPopup mPopup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onInitView(View rootView) {

        findViewById(R.id.create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopup == null) {
                    mPopup = new RecyclerViewPopup(getContext(),50);
                }
                mPopup.setAutoLocatePopup(false);
                mPopup.setPopupGravity(Gravity.TOP);
                mPopup.showPopupWindow(v);
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
        return mInflater.inflate(R.layout.frag_test_popup_other_2, container, false);
    }
}
