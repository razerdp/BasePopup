package razerdp.demo.ui.photobrowser;

import android.app.Activity;
import android.content.Intent;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.app.ComponentActivity;
import androidx.core.app.SharedElementCallback;
import androidx.lifecycle.Observer;
import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.event.LiveDataBus;
import razerdp.demo.utils.LifeCycleHolder;
import razerdp.demo.utils.StringUtil;
import razerdp.demo.utils.ToolUtil;

/**
 * Created by 大灯泡 on 2019/8/22
 * <p>
 * Description：
 */
public class PhotoBrowserProcessor implements Observer<Pair<Integer, Intent>> {
    static final String TRANSITION_NAME = "PhotoBrowserShareView";
    static final String INTENT_KEY_INDEX = "index";

    final List<String> photos;
    int startPosition;
    ImageView from;
    int exitIndex;
    IPhotoBrowserExitViewProvider viewProvider;
    LifeCycleHolder mLifecycleHolder;

    boolean calledReenter;


    public PhotoBrowserProcessor(String photo) {
        this.photos = new ArrayList<>();
        if (StringUtil.noEmpty(photo)) {
            this.photos.add(photo);
        }
    }

    public PhotoBrowserProcessor(List<String> photos) {
        this.photos = new ArrayList<>();
        if (!ToolUtil.isEmpty(photos)) {
            this.photos.addAll(photos);
        }
    }

    public static PhotoBrowserProcessor with(List<String> photos) {
        return new PhotoBrowserProcessor(photos);
    }

    public static PhotoBrowserProcessor with(String url) {
        return new PhotoBrowserProcessor(url);
    }

    public PhotoBrowserProcessor setPhotos(List<String> photos) {
        this.photos.clear();
        if (!ToolUtil.isEmpty(photos)) {
            this.photos.addAll(photos);
        }
        return this;
    }

    public PhotoBrowserProcessor setPhoto(String url) {
        this.photos.clear();
        this.photos.add(url);
        return this;
    }

    public PhotoBrowserProcessor setStartPosition(int startPosition) {
        this.startPosition = startPosition;
        return this;
    }

    public PhotoBrowserProcessor fromView(ImageView from) {
        this.from = from;
        return this;
    }

    public PhotoBrowserProcessor setExitViewProvider(IPhotoBrowserExitViewProvider provider) {
        this.viewProvider = provider;
        return this;
    }

    public PhotoBrowserProcessor start(ComponentActivity act) {
        if (act == null) {
            return this;
        }
        Intent intent = new Intent(act, PhotoBrowserActivity.class);
        PhotoBrowserActivity.Data data = new PhotoBrowserActivity.Data();
        data.setPhotos(photos)
                .setStartPosition(startPosition);
        data.writeToIntent(intent);

        if (mLifecycleHolder == null) {
            mLifecycleHolder = LifeCycleHolder.handle(act, this, new LifeCycleHolder.Callback<PhotoBrowserProcessor>() {
                @Override
                public void onDestroy(@Nullable PhotoBrowserProcessor obj) {
                    from = null;
                    viewProvider = null;
                    ActivityCompat.setExitSharedElementCallback(act, null);
                    LiveDataBus.INSTANCE.getActivityReenterLiveData().removeObserver(PhotoBrowserProcessor.this);
                }
            });
        }
        if (act instanceof BaseActivity) {
            LiveDataBus.INSTANCE.getActivityReenterLiveData().removeObserver(this);
            LiveDataBus.INSTANCE.getActivityReenterLiveData().observe(act, this);
        }
        calledReenter = false;
        if (from != null) {
            if (viewProvider != null) {
                handleExit(act);
            }
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(act, from, TRANSITION_NAME);
            ActivityCompat.startActivity(act, intent, optionsCompat.toBundle());
        } else {
            act.startActivity(intent);
        }
        return this;
    }

    private void handleExit(Activity act) {
        ActivityCompat.setExitSharedElementCallback(act, new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                if (viewProvider == null || !calledReenter) return;
                ImageView imageView = viewProvider.onGetTransitionExitView(from, exitIndex);
                if (imageView == null) return;
                sharedElements.clear();
                sharedElements.put(TRANSITION_NAME, imageView);
            }
        });
    }

    public void handleActivityReenter(int requestCode, Intent data) {
        if (data == null) {
            exitIndex = startPosition;
            return;
        }
        this.exitIndex = data.getIntExtra(INTENT_KEY_INDEX, startPosition);
        calledReenter = true;
    }

    static Intent setIndex(int index) {
        index = Math.max(0, index);
        Intent result = new Intent();
        result.putExtra(INTENT_KEY_INDEX, index);
        return result;
    }


    @Override
    public void onChanged(Pair<Integer, Intent> integerIntentPair) {
        if (integerIntentPair == null) return;
        handleActivityReenter(integerIntentPair.first, integerIntentPair.second);
    }
}
