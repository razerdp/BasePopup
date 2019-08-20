package razerdp.demo;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import razerdp.basepopup.R;
import razerdp.demo.popup.SlideFromBottomPopup;

/**
 * Created by 大灯泡 on 2019/2/18.
 */
public class DemoDialogFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_fragment);
        findViewById(R.id.popup_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestDialogFragment updateAppDialog = new TestDialogFragment();
                updateAppDialog.show(getSupportFragmentManager(), "TestDialogFragment");
            }
        });
    }


    public static class TestDialogFragment extends DialogFragment implements DialogInterface.OnKeyListener {
        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
            getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            if (getDialog().getWindow() != null) {
                getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            getDialog().setCanceledOnTouchOutside(false);
            getDialog().setOnKeyListener(this);
            View rootView = inflater.inflate(R.layout.dialogfragment_test, container, false);
            initView(rootView);
            return rootView;
        }

        private void initView(View rootView) {
            TextView tv = rootView.findViewById(R.id.tv_test);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new SlideFromBottomPopup(getContext())
                            .inject(TestDialogFragment.this)
                            .showPopupWindow();
                }
            });
        }

        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dismiss();
                return true;
            }
            return false;
        }
    }
}
