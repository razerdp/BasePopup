package razerdp.demo.fragment.basedemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.fragment.other.SimpleBaseFrag;
import razerdp.demo.popup.GravityPopup;

/**
 * Created by 大灯泡 on 2018/11/28.
 */
public class GravityPopupFrag extends SimpleBaseFrag implements View.OnClickListener {

    private GravityPopup mPopupWindow;
    private AppCompatCheckBox checkGravityLeft;
    private AppCompatCheckBox checkGravityTop;
    private AppCompatCheckBox checkGravityRight;
    private AppCompatCheckBox checkGravityBottom;
    private AppCompatCheckBox checkGravityCenterVertical;
    private AppCompatCheckBox checkGravityCenterHorizontal;
    private AppCompatCheckBox checkGravityCenter;
    private Button popupShow;
    private AppCompatCheckBox checkCombineAnchor;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void bindEvent() {
        initView();
        mPopupWindow = new GravityPopup(mContext);

        popupShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int gravity = Gravity.NO_GRAVITY;
                if (checkGravityLeft.isChecked()) {
                    gravity |= Gravity.LEFT;
                }
                if (checkGravityTop.isChecked()) {
                    gravity |= Gravity.TOP;
                }
                if (checkGravityRight.isChecked()) {
                    gravity |= Gravity.RIGHT;
                }
                if (checkGravityBottom.isChecked()) {
                    gravity |= Gravity.BOTTOM;
                }
                if (checkGravityCenterVertical.isChecked()) {
                    gravity |= Gravity.CENTER_VERTICAL;
                }
                if (checkGravityCenterHorizontal.isChecked()) {
                    gravity |= Gravity.CENTER_HORIZONTAL;
                }
                if (checkGravityCenter.isChecked()) {
                    gravity |= Gravity.CENTER;
                }
                mPopupWindow.setPopupGravity(gravity);
                if (checkCombineAnchor.isChecked()) {
                    mPopupWindow.showPopupWindow(v);
                } else {
                    mPopupWindow.showPopupWindow();
                }
            }
        });


    }

    private void initView() {
        this.checkGravityLeft = (AppCompatCheckBox) findViewById(R.id.check_gravity_left);
        this.checkGravityTop = (AppCompatCheckBox) findViewById(R.id.check_gravity_top);
        this.checkGravityRight = (AppCompatCheckBox) findViewById(R.id.check_gravity_right);
        this.checkGravityBottom = (AppCompatCheckBox) findViewById(R.id.check_gravity_bottom);
        this.checkGravityCenterVertical = (AppCompatCheckBox) findViewById(R.id.check_gravity_center_vertical);
        this.checkGravityCenterHorizontal = (AppCompatCheckBox) findViewById(R.id.check_gravity_center_horizontal);
        this.checkGravityCenter = (AppCompatCheckBox) findViewById(R.id.check_gravity_center);
        this.checkCombineAnchor = (AppCompatCheckBox) findViewById(R.id.check_combine_anchor);
        this.popupShow = (Button) findViewById(R.id.popup_show);
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
        return mInflater.inflate(R.layout.frag_demo_gravity, container, false);
    }

}
