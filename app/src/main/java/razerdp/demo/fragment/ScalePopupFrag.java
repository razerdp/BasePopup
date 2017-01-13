package razerdp.demo.fragment;

import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.popup.ScalePopup;

/**
 * Created by 大灯泡 on 2016/1/15.
 */
public class ScalePopupFrag extends SimpleBaseFrag {

    private ScalePopup scalePopup;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void bindEvent() {
        scalePopup=new ScalePopup(mContext);
        scalePopup.setOnDismissListener(new BasePopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Toast.makeText(mContext,"dismiss",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public BasePopupWindow getPopup() {
        return scalePopup;
    }

    @Override
    public Button getButton() {
        return (Button) mFragment.findViewById(R.id.popup_show);
    }

    @Override
    public View getFragment() {
        return mInflater.inflate(R.layout.frag_scale_popup, container, false);
    }
}
