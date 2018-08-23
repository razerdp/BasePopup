package razerdp.basepopup;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

/**
 * Created by 大灯泡 on 2018/8/23.
 * <p>
 * 快速建立Popup的builder
 */
public class QuickPopupBuilder {

    private WeakReference<Context> mContextWeakReference;
    QuickPopupConfig mConfig;
    View mContentView;

    int width = ViewGroup.LayoutParams.MATCH_PARENT;
    int height = ViewGroup.LayoutParams.MATCH_PARENT;


    public QuickPopupBuilder(Context context) {
        mContextWeakReference = new WeakReference<>(context);
        mConfig = new QuickPopupConfig();
    }

    public static QuickPopupBuilder with(Context context) {
        return new QuickPopupBuilder(context);
    }

    public QuickPopupBuilder contentView(int resId) {
        return contentView(View.inflate(getContext(), resId, null));
    }

    public QuickPopupBuilder contentView(View contentView) {
        mContentView = contentView;
        return this;
    }

    public QuickPopupBuilder width(int width) {
        this.width = width;
        return this;
    }

    public QuickPopupBuilder height(int height) {
        this.height = height;
        return this;
    }


    //-----------------------------------------tools-----------------------------------------
    private Context getContext() {
        return mContextWeakReference == null ? null : mContextWeakReference.get();
    }
}
