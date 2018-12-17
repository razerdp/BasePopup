package razerdp.demo.fragment.basedemo;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.fragment.other.SimpleBaseFrag;
import razerdp.demo.popup.DemoPopup;
import razerdp.demo.utils.MultiSpanUtil;
import razerdp.util.SimpleAnimationUtils;

/**
 * Created by 大灯泡 on 2018/12/11.
 */
public class OffsetPopupFrag extends SimpleBaseFrag {
    private ViewHolder vh;
    private static final String DESC = "· 设置BasePopup在x，y上偏移量。\n" +
            "· 除了偏移量外，您也可以在您的xml根布局设置margin，前提是您的BasePopup必须使用createPopupById方法解析xml";
    private DemoPopup mDemoPopup;

    @Override
    public void onInitView(View rootView) {
        vh = new ViewHolder(rootView);
        MultiSpanUtil.create(DESC)
                .append("createPopupById").setTextColor(Color.RED)
                .into(vh.tvDesc);

        mDemoPopup = new DemoPopup(getContext());
        mDemoPopup.setPopupGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL)
                .setShowAnimation(SimpleAnimationUtils.getTranslateVerticalAnimation(-1f, 0, 500))
                .setDismissAnimation(SimpleAnimationUtils.getTranslateVerticalAnimation(0, -1f, 500));
        vh.popupShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int prePostX = vh.checkOffsetxNegative.isChecked() ? -1 : 1;
                int prePostY = vh.checkOffsetyNegative.isChecked() ? -1 : 1;

                mDemoPopup.setOffsetX(prePostX * vh.seekOffsetx.getProgress());
                mDemoPopup.setOffsetY(prePostY * vh.seekOffsety.getProgress());
                mDemoPopup.showPopupWindow(v);
            }
        });

        vh.seekOffsetx.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    int prePostX = vh.checkOffsetxNegative.isChecked() ? -1 : 1;
                    vh.tvOffsetX.setText("offsetX：" + prePostX * progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        vh.seekOffsety.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    int prePostY = vh.checkOffsetyNegative.isChecked() ? -1 : 1;
                    vh.tvOffsetY.setText("offsetY：" + prePostY * progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        vh.checkOffsetxNegative.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                vh.tvOffsetX.setText("offsetX：" + (isChecked ? -1 : 1) * vh.seekOffsetx.getProgress());
            }
        });
        vh.checkOffsetyNegative.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                vh.tvOffsetY.setText("offsetY：" + (isChecked ? -1 : 1) * vh.seekOffsety.getProgress());
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
        return mInflater.inflate(R.layout.frag_demo_offset, container, false);
    }

    public static class ViewHolder {
        public View rootView;
        public TextView tvDesc;
        public Button popupShow;
        public TextView tvOffsetX;
        public SeekBar seekOffsetx;
        public CheckBox checkOffsetxNegative;
        public TextView tvOffsetY;
        public SeekBar seekOffsety;
        public CheckBox checkOffsetyNegative;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.tvDesc = (TextView) rootView.findViewById(R.id.tv_desc);
            this.popupShow = (Button) rootView.findViewById(R.id.popup_show);
            this.tvOffsetX = (TextView) rootView.findViewById(R.id.tv_offset_x);
            this.seekOffsetx = (SeekBar) rootView.findViewById(R.id.seek_offsetx);
            this.checkOffsetxNegative = (CheckBox) rootView.findViewById(R.id.check_offsetx_negative);
            this.tvOffsetY = (TextView) rootView.findViewById(R.id.tv_offset_y);
            this.seekOffsety = (SeekBar) rootView.findViewById(R.id.seek_offsety);
            this.checkOffsetyNegative = (CheckBox) rootView.findViewById(R.id.check_offsety_negative);
        }

    }
}