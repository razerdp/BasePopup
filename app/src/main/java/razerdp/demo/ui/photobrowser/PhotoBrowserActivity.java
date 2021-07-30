package razerdp.demo.ui.photobrowser;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.SharedElementCallback;
import androidx.core.view.ViewCompat;
import androidx.viewbinding.ViewBinding;
import razerdp.basepopup.R;
import razerdp.basepopup.databinding.ActivityImageBrowserBinding;
import razerdp.demo.base.TestData;
import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.utils.ToolUtil;
import razerdp.demo.utils.UIHelper;
import razerdp.demo.utils.ViewUtil;
import razerdp.demo.widget.bigimageviewer.view.ImageLoadCallback;
import razerdp.demo.widget.bigimageviewer.view.ImageViewer;
import razerdp.demo.widget.viewpager.BaseCachedViewPagerAdapter;
import razerdp.demo.widget.viewpager.HackyViewPager;
import razerdp.demo.widget.viewpager.IndicatorContainer;

/**
 * Created by 大灯泡 on 2019/8/22
 * <p>
 * Description：图片浏览
 */
public class PhotoBrowserActivity extends BaseActivity<PhotoBrowserActivity.Data> {


    View viewBackground;
    HackyViewPager viewPager;
    IndicatorContainer viewIndicator;
    InnerAdapter mAdapter;
    Data data;

    boolean isLoaded;

    @Override
    protected void onHandleIntent(Intent intent) {
        data = getActivityData();
        if (data == null || ToolUtil.isEmpty(data.photos)) {
            finish();
        }
    }

    @Override
    protected void onApplyStatusBarConfig(@NonNull StatusBarConfig config) {
        config.setDarkMode(false)
                .setFitsSystemWindows(false)
                .setTranslucentStatus(true);
    }

    @Override
    public ViewBinding onCreateViewBinding(LayoutInflater layoutInflater) {
        return ActivityImageBrowserBinding.inflate(layoutInflater);
    }

    @Override
    protected void onInitView(View decorView) {
        supportPostponeEnterTransition();
        isLoaded = false;
        mAdapter = new InnerAdapter();
        viewPager.setAdapter(mAdapter);
        viewIndicator.attachViewPager(viewPager);
        viewPager.setCurrentItem(Math.max(data.startPosition, 0));

        ActivityCompat.setEnterSharedElementCallback(this, new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                super.onMapSharedElements(names, sharedElements);
                if (mAdapter != null && mAdapter.curView != null) {
                    sharedElements.clear();
                    sharedElements.put(PhotoBrowserProcessor.TRANSITION_NAME, mAdapter.curView);
                }
            }

            @Override
            public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                super.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots);
                viewBackground.animate().alpha(1f).start();
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
        setResult(RESULT_OK, PhotoBrowserProcessor.setIndex(viewPager == null ? data.startPosition : viewPager
                .getCurrentItem()));
        super.supportFinishAfterTransition();
    }

    @Override
    public boolean onBackPressedInternal() {
        supportFinishAfterTransition();
        return true;
    }


    class InnerAdapter extends BaseCachedViewPagerAdapter<ImageViewer> {
        View curView;

        private ImageLoadCallback callback = new ImageLoadCallback() {
            @Override
            public void onShowThumbnail() {
                supportStartPostponedEnterTransition();
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess() {
                supportStartPostponedEnterTransition();
            }

            @Override
            public void onError() {
                supportStartPostponedEnterTransition();
            }

            @Override
            public void onFinish() {
                supportStartPostponedEnterTransition();
            }
        };

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return ToolUtil.isEmpty(data.photos) ? 0 : data.photos.size();
        }

        @Override
        protected ImageViewer onCreateView(ViewGroup container, int position) {
            ImageViewer result = (ImageViewer) ViewUtil.inflate(self(), R.layout.item_image_browser, container, false);
            result.setErrorDrawable(UIHelper.getDrawable(R.drawable.ic_error));
            result.setImageLoadCallback(callback);
            result.setOnClickListener(v -> {
                supportFinishAfterTransition();
            });
            return result;
        }


        @Override
        public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            super.setPrimaryItem(container, position, object);
            if (object instanceof View) {
                setCurView((View) object);
            }
        }

        void setCurView(View v) {
            ViewCompat.setTransitionName(v, PhotoBrowserProcessor.TRANSITION_NAME);
            this.curView = v;
        }

        @Override
        protected void onBindData(final ImageViewer view, int position) {
//            String photo = data.photos.get(position).getPhoto();
            String photo = TestData.TestResult.getThumb(data.photos.get(position)
                    .getPhoto(), UIHelper.getScreenWidth(), UIHelper.getScreenHeight());
            String thumb_url = data.photos.get(position).getThumb();
            Uri original = TextUtils.isEmpty(photo) ? Uri.EMPTY : Uri.parse(photo);
            Uri thumb = TextUtils.isEmpty(thumb_url) ? original : Uri.parse(thumb_url);
            view.loadImage(original, thumb);
        }

    }

    public static class Data extends BaseActivity.IntentData {
        private List<IPhotoBrowserProvider> photos;
        private int startPosition;

        public Data setPhotos(List<IPhotoBrowserProvider> photos) {
            this.photos = photos;
            return this;
        }

        public Data setStartPosition(int startPosition) {
            this.startPosition = startPosition;
            return this;
        }
    }
}
