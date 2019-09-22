package razerdp.demo.ui.photobrowser;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.OnViewTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.SharedElementCallback;
import butterknife.BindView;
import razerdp.basepopup.R;
import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.base.imageloader.GlideApp;
import razerdp.demo.utils.ToolUtil;
import razerdp.demo.widget.viewpager.BaseCachedViewPagerAdapter;
import razerdp.demo.widget.viewpager.HackyViewPager;
import razerdp.demo.widget.viewpager.IndicatorContainer;

/**
 * Created by 大灯泡 on 2019/8/22
 * <p>
 * Description：图片浏览
 */
public class PhotoBrowserActivity extends BaseActivity<PhotoBrowserActivity.Data> {


    @BindView(R.id.view_background)
    View viewBackground;
    @BindView(R.id.view_pager)
    HackyViewPager viewPager;
    @BindView(R.id.view_indicator)
    IndicatorContainer viewIndicator;
    InnerAdapter mAdapter;
    Data data;

    boolean onFinish;
    boolean isLoaded;

    @Override
    protected void onHandleIntent(Intent intent) {
        data = getActivityData();
        if (data == null || ToolUtil.isEmpty(data.photos)) {
            finish();
            return;
        }
    }

    @Override
    protected void onApplyStatusBarConfig(@NonNull StatusBarConfig config) {
        config.setDarkMode(false)
                .setFitsSystemWindows(false)
                .setTranslucentStatus(true);
    }

    @Override
    public int contentViewLayoutId() {
        return R.layout.activity_image_browser;
    }

    @Override
    protected void onInitView(View decorView) {
        supportPostponeEnterTransition();
        isLoaded = false;
        mAdapter = new InnerAdapter();
        viewPager.setAdapter(mAdapter);
        viewIndicator.attachViewPager(viewPager);
        viewPager.setCurrentItem(Math.max(data.startPosition, 0));
        onFinish = false;
        ActivityCompat.setEnterSharedElementCallback(this, new SharedElementCallback() {
            @Override
            public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                super.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots);
                viewBackground.animate().alpha(1f).start();
            }

            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                super.onMapSharedElements(names, sharedElements);
                sharedElements.clear();
                sharedElements.put(PhotoBrowserProcessor.TRANSITION_NAME, mAdapter.curView);
            }
        });
    }

    @Override
    public void supportStartPostponedEnterTransition() {
        if (!isLoaded) {
            super.supportStartPostponedEnterTransition();
            isLoaded = true;
        }
    }

    @Override
    public void supportFinishAfterTransition() {
        onFinish = true;
        setResult(RESULT_OK, PhotoBrowserProcessor.setIndex(viewPager == null ? data.startPosition : viewPager.getCurrentItem()));
        super.supportFinishAfterTransition();
    }

    @Override
    public boolean onBackPressedInternal() {
        supportFinishAfterTransition();
        viewBackground.animate().alpha(0f).start();
        return true;
    }


    class InnerAdapter extends BaseCachedViewPagerAdapter<PhotoView> {
        View curView;

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return ToolUtil.isEmpty(data.photos) ? 0 : data.photos.size();
        }

        @Override
        protected PhotoView onCreateView(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            photoView.setMaximumScale(4f);
            photoView.setZoomTransitionDuration(350);
            photoView.setOnViewTapListener(new OnViewTapListener() {
                @Override
                public void onViewTap(View view, float x, float y) {
                    curView = view;
                    supportFinishAfterTransition();
                }
            });
            return photoView;
        }


        @Override
        public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            super.setPrimaryItem(container, position, object);
            if (object instanceof View) {
                curView = (View) object;
            }
        }

        @Override
        protected void onBindData(final PhotoView view, int position) {
            GlideApp.with(view)
                    .load(data.photos.get(position))
                    .dontAnimate()
                    .addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            supportStartPostponedEnterTransition();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            supportStartPostponedEnterTransition();
                            return false;
                        }
                    })
                    .into(view);
        }

    }

    public static class Data extends BaseActivity.IntentData {
        private List<String> photos;
        private int startPosition;

        public Data setPhotos(List<String> photos) {
            this.photos = photos;
            return this;
        }

        public Data setStartPosition(int startPosition) {
            this.startPosition = startPosition;
            return this;
        }
    }
}
