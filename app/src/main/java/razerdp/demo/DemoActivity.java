package razerdp.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.BasePopupDemoHooker;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.QuickPopupBuilder;
import razerdp.basepopup.QuickPopupConfig;
import razerdp.basepopup.R;
import razerdp.demo.fragment.basedemo.AlignPopupFrag;
import razerdp.demo.fragment.basedemo.AnimatePopupFrag;
import razerdp.demo.fragment.basedemo.AnyPosPopupFrag;
import razerdp.demo.fragment.basedemo.AutoLocatedPopupFrag;
import razerdp.demo.fragment.basedemo.BackPressEnablePopupFrag;
import razerdp.demo.fragment.basedemo.BackgroundPopupFrag;
import razerdp.demo.fragment.basedemo.BlurPopupFrag;
import razerdp.demo.fragment.basedemo.ClipChildrenPopupFrag;
import razerdp.demo.fragment.basedemo.ClipToScreenPopupFrag;
import razerdp.demo.fragment.basedemo.GravityPopupFrag;
import razerdp.demo.fragment.basedemo.InputPopupFrag;
import razerdp.demo.fragment.basedemo.OffsetPopupFrag;
import razerdp.demo.fragment.basedemo.OutSideTouchPopupFrag;
import razerdp.demo.fragment.basedemo.OutsideDismissPopupFrag;
import razerdp.demo.fragment.basedemo.ServicePopupFrag;
import razerdp.demo.fragment.basedemo.ShowPopupFrag;
import razerdp.demo.fragment.basedemo.UpdatePopupFrag;
import razerdp.demo.fragment.other.SimpleBaseFrag;
import razerdp.util.SimpleAnimationUtils;

public class DemoActivity extends AppCompatActivity {
    private FragmentManager mFragmentManager;

    private Map<Integer, SimpleBaseFrag> fragMap;

    private Fragment lastFragment;
    private Fragment currentFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        mFragmentManager = getSupportFragmentManager();

        fragMap = new HashMap<>();

        fragMap.put(R.id.id_gravity, new GravityPopupFrag());
        fragMap.put(R.id.id_showpopup, new ShowPopupFrag());
        fragMap.put(R.id.id_animate, new AnimatePopupFrag());
        fragMap.put(R.id.id_blur, new BlurPopupFrag());
        fragMap.put(R.id.id_background, new BackgroundPopupFrag());
        fragMap.put(R.id.id_input, new InputPopupFrag());
        fragMap.put(R.id.id_align, new AlignPopupFrag());
        fragMap.put(R.id.id_outside_dismiss, new OutsideDismissPopupFrag());
        fragMap.put(R.id.id_touch_intercept, new OutSideTouchPopupFrag());
        fragMap.put(R.id.id_backpress_enable, new BackPressEnablePopupFrag());
        fragMap.put(R.id.id_auto_located_popup, new AutoLocatedPopupFrag());
        fragMap.put(R.id.id_clip_children, new ClipChildrenPopupFrag());
        fragMap.put(R.id.id_clip_screen, new ClipToScreenPopupFrag());
        fragMap.put(R.id.id_offset, new OffsetPopupFrag());
        fragMap.put(R.id.id_any_position, new AnyPosPopupFrag());
        fragMap.put(R.id.id_update, new UpdatePopupFrag());
        fragMap.put(R.id.id_service, new ServicePopupFrag());
    }

    private void showGuide() {
        final ActionBar actionBar = getSupportActionBar();
        final View actionMenu = BasePopupDemoHooker.getToolBarActionMenuView(actionBar);
        if (actionMenu != null) {
            actionMenu.postDelayed(new Runnable() {
                @Override
                public void run() {
                    QuickPopupBuilder.with(DemoActivity.this)
                            .contentView(R.layout.popup_guide)
                            .config(new QuickPopupConfig()
                                    .withShowAnimation(SimpleAnimationUtils.getScaleAnimation(0f, 1f, 0f, 1f,
                                            Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f))
                                    .withDismissAnimation(SimpleAnimationUtils.getScaleAnimation(1f, 0f, 1f, 0f,
                                            Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f))
                                    .background(null)
                                    .blurBackground(true)
                                    .gravity(Gravity.BOTTOM | Gravity.LEFT)
                                    //12px为箭头与边缘的距离
                                    .offsetX((actionMenu.getWidth() / 2) + 12)
                                    //10px为actionMenu的内边距。。。
                                    .offsetY(-20)
                                    .dismissListener(new BasePopupWindow.OnDismissListener() {
                                        @Override
                                        public void onDismiss() {
                                            View menuBar = getWindow().getDecorView().findViewById(R.id.action_bar);
                                            if (menuBar instanceof Toolbar) {
                                                ((Toolbar) menuBar).showOverflowMenu();
                                            } else {
                                                openOptionsMenu();
                                            }
                                        }
                                    }))
                            .show(actionMenu);
                }
            }, 200);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_base, menu);
        showGuide();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SimpleBaseFrag simpleBaseFrag = fragMap.get(item.getItemId());
        if (simpleBaseFrag == null) {
            Intent intent = null;
            if (item.getItemId() == R.id.id_other_demo) {
                intent = new Intent(this, OtherDemoActivity.class);
            }
            if (intent != null) {
                startActivity(intent);
            }
            return false;
        }
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        if (simpleBaseFrag == lastFragment) return false;

        lastFragment = currentFragment;
        if (lastFragment != null && lastFragment.isAdded()) {
            fragmentTransaction.hide(lastFragment);
        }

        if (simpleBaseFrag.isAdded()) {
            fragmentTransaction.show(simpleBaseFrag);
        } else {
            fragmentTransaction.add(R.id.popup_fragment, simpleBaseFrag, simpleBaseFrag.getClass().getSimpleName());
        }
        fragmentTransaction.commitAllowingStateLoss();
        currentFragment = simpleBaseFrag;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(item.getTitle());
        }
        return super.onOptionsItemSelected(item);
    }

    private long lastClickBackTime;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastClickBackTime > 2000) { // 后退阻断
            Toast.makeText(this, "再点一次退出", Toast.LENGTH_LONG).show();
            lastClickBackTime = System.currentTimeMillis();
        } else { // 关掉app
            super.onBackPressed();
        }
    }

}
