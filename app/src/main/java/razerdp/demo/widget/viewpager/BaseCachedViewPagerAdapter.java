package razerdp.demo.widget.viewpager;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import razerdp.demo.utils.SimplePool;
import razerdp.demo.utils.ViewUtil;


/**
 * Created by 大灯泡 on 2019/5/16
 * <p>
 * Description：
 */
public abstract class BaseCachedViewPagerAdapter<V extends View> extends PagerAdapter {

    private SimplePool<V> mPools;

    public BaseCachedViewPagerAdapter() {
        this(3);
    }

    public BaseCachedViewPagerAdapter(int poolSize) {
        mPools = new SimplePool<>(poolSize);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        V v = mPools.acquire();
        if (v == null) {
            v = onCreateView(container, position);
        }
        onBindData(v, position);
        container.addView(ViewUtil.removeFromParent(v), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        try {
            if (object instanceof View) {
                container.removeView((View) object);
                mPools.release((V) object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    protected abstract V onCreateView(ViewGroup container, int position);

    protected abstract void onBindData(V v, int position);


}
