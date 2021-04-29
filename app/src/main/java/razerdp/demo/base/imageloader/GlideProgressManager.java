
package razerdp.demo.base.imageloader;

import android.net.Uri;
import android.os.SystemClock;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;
import razerdp.demo.app.AppContext;
import razerdp.demo.utils.RandomUtil;
import razerdp.demo.widget.bigimageviewer.loader.glide.ImageDownloadTarget;
import razerdp.util.log.PopupLog;

/**
 * Created by 大灯泡 on 2019/5/6.
 * <p>
 * Glide加载进度
 */
public class GlideProgressManager {
    private static volatile GlideProgressManager sInstance;
    private final ConcurrentHashMap<Integer, ImageDownloadTarget> mRequestTargetMap = new ConcurrentHashMap<>();
    protected final RequestManager mRequestManager;

    GlideProgressManager(RequestManager mRequestManager) {
        this.mRequestManager = mRequestManager;
    }


    private static Interceptor createInterceptor(final ResponseProgressListener listener) {
        return chain -> {
            Request request = chain.request();
            Response originalResponse = chain.proceed(request);
            String url = String.valueOf(request.url());
            String location = originalResponse.headers().get("Location");
            DispatchingProgressListener.replaceLocation(url, location);
            if (!DispatchingProgressListener.contains(url)) {
                return originalResponse;
            }

            return originalResponse.newBuilder()
                    .body(new OkHttpProgressResponseBody(request.url(), originalResponse.body(),
                            listener))
                    .build();
        };
    }

    public static GlideProgressManager init(Glide glide, OkHttpClient okHttpClient) {
        if (sInstance == null) {

        }
        return sInstance;
    }

    public static GlideProgressManager getsInstance() {
        if (sInstance == null) {
            synchronized (GlideProgressManager.class) {
                if (sInstance == null) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    builder.connectionPool(new ConnectionPool(20, 5, TimeUnit.MINUTES))
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(10, TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS)
                            .addNetworkInterceptor(createInterceptor(new DispatchingProgressListener()));
                    OkHttpClient client = builder.build();
                    client.dispatcher().setMaxRequestsPerHost(20);
                    Glide.get(AppContext.getAppContext()).getRegistry()
                            .replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(client));
                    sInstance = new GlideProgressManager(Glide.with(AppContext.getAppContext()));
                }
            }
        }
        return sInstance;
    }

    public static void forget(String url) {
        DispatchingProgressListener.forget(url);
    }

    public static void expect(String url, ProgressListener listener) {
        DispatchingProgressListener.expect(url, listener);
    }

    public void loadImage(int requestId, Uri uri, ImageDownloadTarget target) {
        clearTarget(requestId);
        saveTarget(requestId, target);

        downloadImageInto(uri, target);
    }

    protected void downloadImageInto(Uri uri, Target<File> target) {
        PopupLog.i("downuri", uri);
        mRequestManager
                .downloadOnly()
                .load(uri)
                .into(target);
    }

    private void saveTarget(int requestId, ImageDownloadTarget target) {
        mRequestTargetMap.put(requestId, target);
    }

    private void clearTarget(int requestId) {
        ImageDownloadTarget target = mRequestTargetMap.remove(requestId);
        if (target != null) {
            mRequestManager.clear(target);
        }
    }

    public interface ProgressListener {
        void onDownloadStart();

        void onProgress(int progress);

        void onDownloadFinish();

        abstract class ProgressListenerAdapter implements ProgressListener {

            @Override
            public void onDownloadStart() {

            }

            @Override
            public void onProgress(int progress) {

            }

            @Override
            public void onDownloadFinish() {

            }
        }
    }

    public interface ProgressListenerEx extends ProgressListener {

        void setProgressListener(ProgressListener mListener);

        void setUrl(String url);

        String getUrl();

    }

    private interface ResponseProgressListener {
        void update(HttpUrl url, long bytesRead, long contentLength);
    }

    private static class DispatchingProgressListener implements ResponseProgressListener {
        private static final WeakHashMap<String, ProgressListener> LISTENERS = new WeakHashMap<>();
        private static final Map<String, Integer> PROGRESSES = new HashMap<>();
        private static final String URL_QUERY_PARAM_START = "\\?";


        static void forget(String url) {
            LISTENERS.remove(getRawKey(url));
            PROGRESSES.remove(getRawKey(url));
        }

        static void expect(String url, ProgressListener listener) {
            LISTENERS.put(getRawKey(url), listener);
        }

        static boolean contains(String url) {
            return LISTENERS.containsKey(getRawKey(url));
        }

        static void replaceLocation(String url, String location) {
            if (TextUtils.isEmpty(location)) return;
            ProgressListener p = LISTENERS.remove(getRawKey(url));
            if (p == null) return;
            LISTENERS.put(getRawKey(location), p);
        }

        @Override
        public void update(HttpUrl url, final long bytesRead, final long contentLength) {
            String key = getRawKey(url.toString());
            final ProgressListener listener = LISTENERS.get(key);
            if (listener == null) {
                return;
            }

            Integer lastProgress = PROGRESSES.get(key);
            if (lastProgress == null) {
                // ensure `onStart` is called before `onProgress` and `onFinish`
                listener.onDownloadStart();
            }
            if (contentLength > 0 && bytesRead >= contentLength) {
                listener.onDownloadFinish();
                forget(key);
                return;
            }
            int progress = 0;
            if (contentLength > 0) {
                progress = (int) ((float) bytesRead / contentLength * 100);
            }
            if (lastProgress == null || progress != lastProgress) {
                PROGRESSES.put(key, progress);
                listener.onProgress(progress);
            }
        }

        private static String getRawKey(String formerKey) {
            return formerKey.split(URL_QUERY_PARAM_START)[0];
        }
    }

    private static class OkHttpProgressResponseBody extends ResponseBody {
        private final HttpUrl mUrl;
        private final ResponseBody mResponseBody;
        private final ResponseProgressListener mProgressListener;
        private BufferedSource mBufferedSource;
        private long fullLengthRecord = 0;

        OkHttpProgressResponseBody(HttpUrl url, ResponseBody responseBody,
                                   ResponseProgressListener progressListener) {
            this.mUrl = url;
            this.mResponseBody = responseBody;
            this.mProgressListener = progressListener;
        }

        @Override
        public MediaType contentType() {
            return mResponseBody.contentType();
        }

        @Override
        public long contentLength() {
            return mResponseBody.contentLength();
        }

        @Override
        public BufferedSource source() {
            if (mBufferedSource == null) {
                mBufferedSource = Okio.buffer(source(mResponseBody.source()));
            }
            return mBufferedSource;
        }


        private Source source(Source source) {
            return new ForwardingSource(source) {
                private long mTotalBytesRead = 0L;
                private List<Long> progressSimulate = null;

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long fullLength = mResponseBody.contentLength();
                    if (fullLength > 0) {
                        return readWithContentLength(sink, byteCount);
                    } else {
                        return readWithOutContentLength(sink, byteCount);
                    }
                }

                long readWithContentLength(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    long fullLength = mResponseBody.contentLength();
                    if (bytesRead == -1) { // this source is exhausted
                        mTotalBytesRead = fullLength;
                    } else {
                        mTotalBytesRead += bytesRead;
                    }
                    mProgressListener.update(mUrl, mTotalBytesRead, fullLength);
                    return bytesRead;
                }

                long readWithOutContentLength(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = 0;
                    long fullLength = 0;
                    if (fullLengthRecord <= 0) {
                        mProgressListener.update(mUrl, 0, 100);
                        if (progressSimulate == null) {
                            progressSimulate = new ArrayList<>();
                        }
                        while ((bytesRead = super.read(sink, byteCount)) > 0) {
                            progressSimulate.add(bytesRead);
                            fullLengthRecord += bytesRead;
                        }
                    }
                    fullLength = fullLengthRecord;
                    while (progressSimulate != null && progressSimulate.size() > 0) {
                        bytesRead = progressSimulate.remove(0);
                        if (bytesRead == -1) { // this source is exhausted
                            mTotalBytesRead = fullLength;
                        } else {
                            mTotalBytesRead += bytesRead;
                        }
                        SystemClock.sleep(RandomUtil.randomInt(1, 20));
                        mProgressListener.update(mUrl, mTotalBytesRead, fullLength);
                    }
                    mProgressListener.update(mUrl, fullLength, fullLength);
                    return fullLength;
                }

                @Override
                public void close() throws IOException {
                    super.close();
                    progressSimulate = null;
                }
            };
        }
    }
}
