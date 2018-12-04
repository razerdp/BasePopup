package razerdp.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import razerdp.basepopup.R;
import razerdp.demo.popup.ScalePopup;

/**
 * Created by 大灯泡 on 2018/9/6.
 */
public class DemoLeakcanaryActivity extends AppCompatActivity implements View.OnClickListener {

    private Button finish;
    private Button show;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leakcanary);
        ScalePopup scalePopup = new ScalePopup(this);
        scalePopup.showPopupWindow();
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
        new ScalePopup(this).showPopupWindow();
    }
}
