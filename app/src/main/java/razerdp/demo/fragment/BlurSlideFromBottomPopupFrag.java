package razerdp.demo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.popup.BlurSlideFromBottomPopup;

/**
 * Created by 大灯泡 on 2017/12/27.
 */
public class BlurSlideFromBottomPopupFrag extends SimpleBaseFrag {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void bindEvent() {

    }

    @Override
    public BasePopupWindow getPopup() {
        BlurSlideFromBottomPopup popup = new BlurSlideFromBottomPopup(mContext);
        popup.setBlurBackground(true);
        return popup;
    }

    @Override
    public Button getButton() {
        return (Button) mFragment.findViewById(R.id.popup_show);
    }

    @Override
    public View getFragment() {
        return mInflater.inflate(R.layout.frag_blur_slide_from_bottom_popup, container, false);
    }
}
