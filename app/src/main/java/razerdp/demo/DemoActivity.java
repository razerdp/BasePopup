package razerdp.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

import java.util.HashMap;
import java.util.Map;

import razerdp.basepopup.R;
import razerdp.demo.fragment.AutoLocatedPopupFrag;
import razerdp.demo.fragment.CommentPopupFrag;
import razerdp.demo.fragment.CustomInterpolatorPopupFrag;
import razerdp.demo.fragment.DialogPopupFrag;
import razerdp.demo.fragment.FullScreenPopupFrag;
import razerdp.demo.fragment.InputPopupFrag;
import razerdp.demo.fragment.ListPopupFrag;
import razerdp.demo.fragment.MenuPopupFrag;
import razerdp.demo.fragment.ScalePopupFrag;
import razerdp.demo.fragment.SlideFromTopPopupFrag;
import razerdp.demo.fragment.SimpleBaseFrag;
import razerdp.demo.fragment.SlideFromBottomPopupFrag;

public class DemoActivity extends FragmentActivity {
    private FragmentManager mFragmentManager;

    private Map<Integer, SimpleBaseFrag> fragMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        mFragmentManager = getSupportFragmentManager();

        fragMap = new HashMap<>();

        fragMap.put(R.id.id_scale_popup, new ScalePopupFrag());
        fragMap.put(R.id.id_slide_from_bottom_popup, new SlideFromBottomPopupFrag());
        fragMap.put(R.id.id_comment_popup, new CommentPopupFrag());
        fragMap.put(R.id.id_input_popup, new InputPopupFrag());
        fragMap.put(R.id.id_list_popup, new ListPopupFrag());
        fragMap.put(R.id.id_menu_popup, new MenuPopupFrag());
        fragMap.put(R.id.id_dialog_popup, new DialogPopupFrag());
        fragMap.put(R.id.id_custom_interpolator_popup, new CustomInterpolatorPopupFrag());
        fragMap.put(R.id.id_full_screen_popup, new FullScreenPopupFrag());
        fragMap.put(R.id.id_auto_located_popup, new AutoLocatedPopupFrag());
        fragMap.put(R.id.id_slide_from_top_popup, new SlideFromTopPopupFrag());


        SimpleBaseFrag firstShowFrag = fragMap.get(R.id.id_scale_popup);
        mFragmentManager.beginTransaction().replace(R.id.popup_fragment, firstShowFrag).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_demo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        SimpleBaseFrag simpleBaseFrag = fragMap.get(item.getItemId());
        mFragmentManager.beginTransaction().replace(R.id.popup_fragment, simpleBaseFrag).commit();
        return super.onOptionsItemSelected(item);
    }

}
