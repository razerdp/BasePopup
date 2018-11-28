package razerdp.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.HashMap;
import java.util.Map;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.fragment.other.AutoLocatedPopupFrag;
import razerdp.demo.fragment.other.BlurSlideFromBottomPopupFrag;
import razerdp.demo.fragment.other.BottomInputFragment;
import razerdp.demo.fragment.other.CommentPopupFrag;
import razerdp.demo.fragment.other.DialogPopupFrag;
import razerdp.demo.fragment.other.DismissControlPopupFrag;
import razerdp.demo.fragment.other.InputPopupFrag;
import razerdp.demo.fragment.other.ListPopupFrag;
import razerdp.demo.fragment.other.LocatePopupFrag;
import razerdp.demo.fragment.other.MenuPopupFrag;
import razerdp.demo.fragment.other.ScalePopupFrag;
import razerdp.demo.fragment.other.SimpleBaseFrag;
import razerdp.demo.fragment.other.SlideFromBottomPopupFrag;
import razerdp.demo.fragment.other.SlideFromTopPopupFrag;
import razerdp.demo.fragment.other.SlideFromTopPopupFrag2;

public class OtherDemoActivity extends AppCompatActivity {
    private FragmentManager mFragmentManager;

    private Map<Integer, SimpleBaseFrag> fragMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_demo);
        BasePopupWindow.setDebugLogEnable(true);
        mFragmentManager = getSupportFragmentManager();

        fragMap = new HashMap<>();

        fragMap.put(R.id.id_scale_popup, new ScalePopupFrag());
        fragMap.put(R.id.id_slide_from_bottom_popup, new SlideFromBottomPopupFrag());
        fragMap.put(R.id.id_comment_popup, new CommentPopupFrag());
        fragMap.put(R.id.id_input_popup, new InputPopupFrag());
        fragMap.put(R.id.id_list_popup, new ListPopupFrag());
        fragMap.put(R.id.id_menu_popup, new MenuPopupFrag());
        fragMap.put(R.id.id_dialog_popup, new DialogPopupFrag());
        fragMap.put(R.id.id_auto_located_popup, new AutoLocatedPopupFrag());
        fragMap.put(R.id.id_slide_from_top_popup, new SlideFromTopPopupFrag());
        fragMap.put(R.id.id_slide_from_top_popup2, new SlideFromTopPopupFrag2());
        fragMap.put(R.id.id_dismiss_control_popup, new DismissControlPopupFrag());
        fragMap.put(R.id.id_blur_slide_from_bottom_popup, new BlurSlideFromBottomPopupFrag());
        fragMap.put(R.id.id_locate_with_view, new LocatePopupFrag());
        fragMap.put(R.id.id_slide_from_bottom_input_popup, new BottomInputFragment());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_demo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SimpleBaseFrag simpleBaseFrag = fragMap.get(item.getItemId());
        if (simpleBaseFrag == null) {
            Intent intent = null;
            if (item.getItemId() == R.id.id_full_activity) {
                intent = new Intent(this, DemoFullScreenActivity.class);
            }
            if (intent != null) {
                startActivity(intent);
            }
            return false;
        }
        mFragmentManager.beginTransaction().replace(R.id.popup_fragment, simpleBaseFrag).commit();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(item.getTitle());
        }
        return super.onOptionsItemSelected(item);
    }

}
