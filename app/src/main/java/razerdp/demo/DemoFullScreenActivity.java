package razerdp.demo;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import razerdp.basepopup.QuickPopupBuilder;
import razerdp.basepopup.QuickPopupConfig;
import razerdp.basepopup.R;
import razerdp.demo.popup.SlideFromBottomInputPopup;
import razerdp.util.SimpleAnimationUtils;

/**
 * Created by 大灯泡 on 2018/9/6.
 */
public class DemoFullScreenActivity extends AppCompatActivity implements View.OnClickListener {

    private Button finish;
    private Button show;
    private Button show2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams lp = this.getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            this.getWindow().setAttributes(lp);
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_full_screen);
        initView();
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
            }
        });
    }

    private void initView() {
        finish = (Button) findViewById(R.id.finish);
        show = (Button) findViewById(R.id.show);

        finish.setOnClickListener(this);
        show.setOnClickListener(this);
        show2 = (Button) findViewById(R.id.show2);
        show2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.finish:
                finish();
                break;
            case R.id.show:
                showPopup();
                break;
            case R.id.show2:
                showPopupBySystem();
                break;
        }
    }

    private void showPopup() {
        QuickPopupBuilder.with(this)
                .contentView(R.layout.popup_scale)
                .config(new QuickPopupConfig()
                        .blurBackground(true)
                        .withShowAnimation(SimpleAnimationUtils.getDefaultScaleAnimation(true))
                        .withDismissAnimation(SimpleAnimationUtils.getDefaultScaleAnimation(false)))
                .show();
    }

    private void showPopupBySystem() {
        PopupWindow window = new PopupWindow(View.inflate(this, R.layout.popup_scale, null), ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setOutsideTouchable(true);
        window.setTouchable(true);
        window.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

}
