package razerdp.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import razerdp.basepopup.QuickPopupBuilder;
import razerdp.basepopup.QuickPopupConfig;
import razerdp.basepopup.R;
import razerdp.util.SimpleAnimationUtils;

/**
 * Created by 大灯泡 on 2018/9/6.
 */
public class DemoFullScreenActivity extends AppCompatActivity implements View.OnClickListener {

    private Button finish;
    private Button show;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        window.setAttributes(params);
        setContentView(R.layout.activity_full_screen);
        initView();
    }

    private void initView() {
        finish = (Button) findViewById(R.id.finish);
        show = (Button) findViewById(R.id.show);

        finish.setOnClickListener(this);
        show.setOnClickListener(this);
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
        }
    }

    private void showPopup() {
        QuickPopupBuilder.with(this)
                .contentView(R.layout.popup_normal)
                .config(new QuickPopupConfig()
                        .blurBackground(true)
                        .withShowAnimation(SimpleAnimationUtils.getDefaultScaleAnimation(true))
                        .withDismissAnimation(SimpleAnimationUtils.getDefaultScaleAnimation(false)))
                .show();
    }
}
