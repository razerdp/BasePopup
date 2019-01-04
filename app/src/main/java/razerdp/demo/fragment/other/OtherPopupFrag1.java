package razerdp.demo.fragment.other;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.QuickPopupBuilder;
import razerdp.basepopup.QuickPopupConfig;
import razerdp.basepopup.R;

/**
 * Created by 大灯泡 on 2016/1/15.
 */
public class OtherPopupFrag1 extends SimpleBaseFrag {


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
                QuickPopupBuilder.with(getContext())
                        .contentView(R.layout.popup_test_other_1)
                        .config(new QuickPopupConfig().gravity(Gravity.BOTTOM | Gravity.LEFT)
                                .offsetX((int) (v.getWidth() * 0.8f))
                                .backgroundColor(0)
                                .allowInterceptTouchEvent(false)
                                .withClick(R.id.post, this, true)
                                .withClick(R.id.audio, this, true)
                                .withClick(R.id.picture, this, true)
                                .withClick(R.id.article, this, true))
                        .show(v);
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
        return mInflater.inflate(R.layout.frag_test_popup_other_1, container, false);
    }
}
