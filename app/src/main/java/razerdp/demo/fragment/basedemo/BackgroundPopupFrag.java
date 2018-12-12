package razerdp.demo.fragment.basedemo;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Random;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.fragment.other.SimpleBaseFrag;
import razerdp.demo.popup.DemoPopup;
import razerdp.demo.utils.DimensUtils;
import razerdp.demo.utils.MultiSpanUtil;
import razerdp.demo.utils.OnClickListenerWrapper;
import razerdp.util.SimpleAnimationUtils;

/**
 * Created by 大灯泡 on 2018/12/10.
 */
public class BackgroundPopupFrag extends SimpleBaseFrag {
    private ViewHolder vh;
    private static final String DESC = "· 设置BasePopup背景颜色。\n" +
            "· 设置BasePopup背景图片（Drawable）。\n" +
            "· 默认背景色：#8f000000。\n";
    private DemoPopup mDemoPopup;
    private int color = Color.parseColor("#8f000000");

    @Override
    public void onInitView(View rootView) {
        vh = new ViewHolder(rootView);
        MultiSpanUtil.create(DESC)
                .append("Drawable").setTextColor(Color.RED)
                .append("#8f000000").setTextColor(Color.RED)
                .into(vh.tvDesc);
        vh.tvAlpha.setText("透明度：" + String.valueOf(vh.seekAlpha.getProgress()) + "%");
        mDemoPopup = new DemoPopup(getContext());
        mDemoPopup.setPopupGravity(Gravity.CENTER)
                .setShowAnimation(SimpleAnimationUtils.getDefaultScaleAnimation(true))
                .setDismissAnimation(SimpleAnimationUtils.getDefaultScaleAnimation(false));
        refreshColor();

        vh.btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshColor();
            }
        });

        vh.seekAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    int alpha = (int) (255 * (float) progress / 100);
                    color = Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
                    vh.selectedView.setBackgroundColor(color);
                    vh.tvAlpha.setText("透明度：" + String.valueOf(progress) + "%");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        vh.popupShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vh.checkOpenBackgroundColor.isChecked()) {
                    mDemoPopup.setBackground(0);
                } else {
                    if (vh.checkOpenDrawableBackground.isChecked()) {
                        Drawable drawable = getResources().getDrawable(R.drawable.popup_bg_drawable);
                        drawable.setAlpha((int) (255 * (float) vh.seekAlpha.getProgress() / 100));
                        mDemoPopup.setBackground(drawable);
                    } else {
                        mDemoPopup.setBackgroundColor(color);
                    }
                }
                mDemoPopup.showPopupWindow();
            }
        });
    }

    private int randomColor() {
        Random random = new Random();
        return 0xff000000 | random.nextInt(0x00ffffff);
    }

    private void refreshColor() {
        final int childCount = vh.layoutColorContaienr.getChildCount();
        if (childCount == 5) {
            for (int i = 0; i < childCount; i++) {
                View v = vh.layoutColorContaienr.getChildAt(i);
                int randomColor = randomColor();
                checkAndSetViewListener(v, randomColor);
                v.setBackgroundColor(randomColor);
            }
        } else {
            for (int i = 0; i < 5; i++) {
                View v = new View(getContext());
                int randomColor = randomColor();
                checkAndSetViewListener(v, randomColor);
                v.setBackgroundColor(randomColor);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(DimensUtils.dipToPx(getContext(), 30), DimensUtils.dipToPx(getContext(), 30));
                p.leftMargin = DimensUtils.dipToPx(getContext(), 8);
                vh.layoutColorContaienr.addView(v, p);
            }
        }
    }

    private void checkAndSetViewListener(View v, int color) {
        if (v == null) return;
        if (v.getTag() instanceof OnClickListenerWrapper) {
            ((OnClickListenerWrapper) v.getTag()).setData(color);
        } else {
            OnClickListenerWrapper<Integer> mListenerWrapper = new OnClickListenerWrapper<Integer>() {
                @Override
                public void onClick(View v) {
                    int alpha = (int) (255 * (float) vh.seekAlpha.getProgress() / 100);
                    int color = getData();
                    int combineColor = Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
                    vh.selectedView.setBackgroundColor(combineColor);
                    BackgroundPopupFrag.this.color = combineColor;
                }
            };
            mListenerWrapper.setData(color);
            v.setOnClickListener(mListenerWrapper);
            v.setTag(mListenerWrapper);
        }
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
        return mInflater.inflate(R.layout.frag_demo_background_color, container, false);
    }

    public static class ViewHolder {
        public View rootView;
        public TextView tvDesc;
        public Button popupShow;
        public AppCompatCheckBox checkOpenBackgroundColor;
        public AppCompatCheckBox checkOpenDrawableBackground;
        public Button btnChange;
        public SeekBar seekAlpha;
        public LinearLayout layoutColorContaienr;
        public View selectedView;
        public TextView tvAlpha;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.tvDesc = (TextView) rootView.findViewById(R.id.tv_desc);
            this.popupShow = (Button) rootView.findViewById(R.id.popup_show);
            this.checkOpenBackgroundColor = (AppCompatCheckBox) rootView.findViewById(R.id.check_open_background_color);
            this.checkOpenDrawableBackground = (AppCompatCheckBox) rootView.findViewById(R.id.check_open_drawable_background);
            this.btnChange = (Button) rootView.findViewById(R.id.btn_change);
            this.seekAlpha = (SeekBar) rootView.findViewById(R.id.seek_alpha);
            this.layoutColorContaienr = (LinearLayout) rootView.findViewById(R.id.layout_color_contaienr);
            this.selectedView = rootView.findViewById(R.id.view_selected_color);
            this.tvAlpha = rootView.findViewById(R.id.tv_alpha);
        }

    }
}
