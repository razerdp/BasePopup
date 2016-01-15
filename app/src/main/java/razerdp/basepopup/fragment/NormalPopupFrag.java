package razerdp.basepopup.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import razerdp.basepopup.R;
import razerdp.basepopup.basepopup.BasePopupWindow;
import razerdp.basepopup.widget.NormalPopup;

/**
 * Created by 大灯泡 on 2016/1/15.
 */
public class NormalPopupFrag extends SimpleBaseFrag {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public BasePopupWindow getPopup() {
        return new NormalPopup(mContext);
    }

    @Override
    public Button getButton() {
        return (Button) mFragment.findViewById(R.id.popup_show);
    }

    @Override
    public View getFragment() {
        return mInflater.inflate(R.layout.frag_normal_popup,container,false);
    }
}
