package razerdp.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
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
import razerdp.demo.fragment.SlideFromBottomPopupFrag;

public class DemoActivity extends FragmentActivity {
    private FragmentManager mFragmentManager;
    private ScalePopupFrag mNormalPopupFrag;
    private SlideFromBottomPopupFrag mSlideFromBottomPopupFrag;
    private CommentPopupFrag mCommentPopupFrag;
    private InputPopupFrag mInputPopupFrag;
    private ListPopupFrag mListPopupFrag;
    private MenuPopupFrag mMenuPopupFrag;
    private DialogPopupFrag mDialogPopupFrag;
    private CustomInterpolatorPopupFrag mInterpolatorPopupFrag;
    private FullScreenPopupFrag mFullScreenPopupFrag;
    private AutoLocatedPopupFrag mAutoLocatedPopupFrag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        mFragmentManager = getSupportFragmentManager();

        mNormalPopupFrag = new ScalePopupFrag();
        mSlideFromBottomPopupFrag = new SlideFromBottomPopupFrag();
        mCommentPopupFrag = new CommentPopupFrag();
        mInputPopupFrag = new InputPopupFrag();
        mListPopupFrag = new ListPopupFrag();
        mMenuPopupFrag = new MenuPopupFrag();
        mDialogPopupFrag = new DialogPopupFrag();
        mInterpolatorPopupFrag = new CustomInterpolatorPopupFrag();
        mFullScreenPopupFrag = new FullScreenPopupFrag();
        mAutoLocatedPopupFrag=new AutoLocatedPopupFrag();
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
        switch (item.getItemId()) {
            case R.id.id_scale_popup:
                mFragmentManager.beginTransaction().replace(R.id.parent, mNormalPopupFrag).commit();
                break;
            case R.id.id_slide_from_bottom_popup:
                mFragmentManager.beginTransaction().replace(R.id.parent, mSlideFromBottomPopupFrag).commit();
                break;
            case R.id.id_comment_popup:
                mFragmentManager.beginTransaction().replace(R.id.parent, mCommentPopupFrag).commit();
                break;
            case R.id.id_input_popup:
                mFragmentManager.beginTransaction().replace(R.id.parent, mInputPopupFrag).commit();
                break;
            case R.id.id_list_popup:
                mFragmentManager.beginTransaction().replace(R.id.parent, mListPopupFrag).commit();
                break;
            case R.id.id_menu_popup:
                mFragmentManager.beginTransaction().replace(R.id.parent, mMenuPopupFrag).commit();
                break;
            case R.id.id_dialog_popup:
                mFragmentManager.beginTransaction().replace(R.id.parent, mDialogPopupFrag).commit();
                break;
            case R.id.id_custom_interpolator_popup:
                mFragmentManager.beginTransaction().replace(R.id.parent, mInterpolatorPopupFrag).commit();
                break;
            case R.id.id_full_screen_popup:
                mFragmentManager.beginTransaction().replace(R.id.parent, mFullScreenPopupFrag).commit();
                break;
            case R.id.id_auto_located_popup:
                mFragmentManager.beginTransaction().replace(R.id.parent, mAutoLocatedPopupFrag).commit();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
