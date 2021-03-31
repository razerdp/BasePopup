package razerdp.demo.ui.photobrowser;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.SharedElementCallback;

import java.io.File;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import razerdp.basepopup.R;
import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.utils.ToolUtil;
import razerdp.demo.utils.TransitionUtil;
import razerdp.demo.utils.UIHelper;
import razerdp.demo.utils.ViewUtil;
import razerdp.demo.widget.bigimageviewer.indicator.CircleProgressIndicator;
import razerdp.demo.widget.bigimageviewer.loader.ImageLoader;
import razerdp.demo.widget.bigimageviewer.view.ImageViewer;
import razerdp.demo.widget.viewpager.BaseCachedViewPagerAdapter;
import razerdp.demo.widget.viewpager.HackyViewPager;
import razerdp.demo.widget.viewpager.IndicatorContainer;
import razerdp.util.log.PopupLog;

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
                //大图
                if (mAdapter.curView instanceof ImageViewer) {
                    ImageViewer imageViewer = (ImageViewer) mAdapter.curView;
                    if (imageViewer.isLarge()) {
                        PopupLog.i(TAG, "is large", mAdapter.curView);
                        if (TransitionUtil.ENABLE) {
                            if (!onFinish) {
                                Transition enter = TransitionInflater.from(self())
                                        .inflateTransition(R.transition.photo_enter_large);
                                getWindow().setEnterTransition(enter);
                            } else {
                                Transition exit = TransitionInflater.from(self())
                                        .inflateTransition(R.transition.photo_exit_large);
                                getWindow().setExitTransition(exit);
                            }
                        }
                    }
                    sharedElements.clear();
                    sharedElements.put(PhotoBrowserProcessor.TRANSITION_NAME, imageViewer.getShowingImageView());
                } else {
                    sharedElements.clear();
                    sharedElements.put(PhotoBrowserProcessor.TRANSITION_NAME, mAdapter.curView);
                }
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

        private ImageLoader.Callback callback = new ImageLoaderCallbackAdapter() {
            @Override
            public void onCacheHit(int imageType, File image) {
                super.onCacheHit(imageType, image);
                supportStartPostponedEnterTransition();
            }

            @Override
            public void onCacheMiss(int imageType, File image) {
                super.onCacheMiss(imageType, image);
                supportStartPostponedEnterTransition();
            }

            @Override
            public void onStart() {
                super.onStart();
                supportStartPostponedEnterTransition();
            }

            @Override
            public void onProgress(int progress) {
                super.onProgress(progress);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                supportStartPostponedEnterTransition();
            }

            @Override
            public void onSuccess(File image) {
                super.onSuccess(image);
                supportStartPostponedEnterTransition();
            }

            @Override
            public void onFail(Exception error) {
                super.onFail(error);
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
            result.setFailureImage(UIHelper.getDrawable(R.drawable.ic_error));
            result.setImageViewLoader(new AppImageLoader());
            result.setProgressIndicator(new CircleProgressIndicator());
            result.setImageLoaderCallback(callback);
            result.setOnClickListener(v -> {
                curView = v;
                supportFinishAfterTransition();
            });
            return result;
        }


        @Override
        public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            super.setPrimaryItem(container, position, object);
            if (object instanceof View) {
                curView = (View) object;
            }
        }

        @Override
        protected void onBindData(final ImageViewer view, int position) {
            String photo = data.photos.get(position).getPhoto();
            String thumb_url = data.photos.get(position).getThumb();
            Uri original = TextUtils.isEmpty(photo) ? Uri.EMPTY : Uri.parse(photo);
            Uri thumb = TextUtils.isEmpty(thumb_url) ? original : Uri.parse(thumb_url);
            view.showImage(thumb, original);
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
