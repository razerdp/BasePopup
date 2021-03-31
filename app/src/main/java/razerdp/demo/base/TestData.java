package razerdp.demo.base;


import android.text.TextUtils;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;
import razerdp.demo.app.AppContext;
import razerdp.demo.base.interfaces.ExtSimpleCallback;
import razerdp.demo.utils.FileUtil;
import razerdp.demo.utils.RandomUtil;
import razerdp.demo.utils.gson.GsonUtil;
import razerdp.demo.utils.rx.RxCall;
import razerdp.demo.utils.rx.RxHelper;
import razerdp.demo.utils.rx.RxTaskCall;
import razerdp.util.log.PopupLog;
import rxhttp.RxHttp;

/**
 * Created by 大灯泡 on 2019/7/16
 * <p>
 * Description：
 */
@SuppressWarnings("all")
public class TestData {

    static final List<String> textHost;
    static final List<TestResult> localCache = new ArrayList<>();

    static final String fileName = FileUtil.checkFileSeparator(AppContext.getAppContext()
            .getFilesDir()
            .getAbsolutePath()) + "test_data_cache";


    static {
        textHost = new ArrayList<>();
        //https://api.ixiaowai.cn/
        textHost.add("https://api.ixiaowai.cn/ylapi/index.php?code=json");
        textHost.add("https://api.ixiaowai.cn/tgrj/index.php?code=json");
    }


    public static void init() {
        RxHelper.runOnBackground(data -> {
            String json = FileUtil.readFile(fileName);
            if (!TextUtils.isEmpty(json)) {
                List<TestResult> ret = GsonUtil.INSTANCE.toList(json, TestResult.class);
                if (ret != null) {
                    localCache.addAll(ret);
                }
            }
        });
    }

    public static void getTestData(int count, ExtSimpleCallback<List<TestResult>> cb) {
        if (cb == null) return;
        cb.onStart();
        if (localCache != null) {
            cb.onCall(randomPick(count));
            fetchInBackground(count, null);
        } else {
            fetchInBackground(count, cb);
        }
    }

    static void fetchInBackground(int count, ExtSimpleCallback<List<TestResult>> cb) {
        RxHelper.runOnBackground(new RxTaskCall<List<TestResult>>() {
            @Override
            public List<TestResult> doInBackground() {
                List<TestResult> results = new ArrayList<>();
                doRequest(count, results);
                return results;
            }

            @Override
            public void onResult(List<TestResult> result) {
                saveResult(result);
                cb.onCall(randomPick(count));
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                cb.onError(0, e.getMessage());
            }
        });
    }

    static void updateData() {

    }

    static synchronized void saveResult(List<TestResult> results) {
        if (results != null && results.size() > 0) {
            localCache.addAll(results);
            RxHelper.runOnBackground(new RxCall<Void>() {
                @Override
                public void onCall(Void data) {
                    FileUtil.writeToFile(fileName, GsonUtil.INSTANCE.toString(localCache));
                }
            });
        }
    }

    static List<TestResult> randomPick(int count) {
        List<TestResult> ret = new ArrayList<>();
        for (int i = 0; i < Math.min(count, localCache.size()); i++) {
            ret.add(localCache.get(RandomUtil.randomInt(0, localCache.size() - 1)));
        }
        return ret;
    }


    static void doRequest(int count, List<TestResult> ret) {
        for (int i = 0; i < count; i++) {
            TestResult result = new TestResult();
            requestInternal(result);
            ret.add(result);
        }
    }

    static void requestInternal(TestResult ret) {
        RxHttp.get("https://picsum.photos/v2/list?page=%s&limit=100", RandomUtil.randomIntString(1, 10))
                .setSync()
                .asList(RequestInfo.class)
                .subscribe(new Consumer<List<RequestInfo>>() {
                    @Override
                    public void accept(List<RequestInfo> requestInfos) throws Exception {
                        int len = requestInfos.size();
                        for (int i = 0; i < RandomUtil.randomInt(1, 9); i++) {
                            ret.addPic(requestInfos.get(RandomUtil.randomInt(0, len - 1)).download_url);
                        }
                        ret.avatar = ret.getThumb(requestInfos.get(RandomUtil.randomInt(0, len - 1)).download_url, 200, 200);
                    }
                }, throwable -> PopupLog.e(throwable));

        String textUrl = textHost.get(RandomUtil.randomInt(0, textHost.size() - 1));
        RxHttp.get(textUrl)
                .setSync()
                .asString()
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Map<String, Object> map = GsonUtil.INSTANCE.toHashMap(s, String.class, Object.class);
                        if (map != null) {
                            ret.text = (String) map.get("msg");
                        }
                    }
                }, throwable -> PopupLog.e(throwable));
    }


    static class RequestInfo {
//        {
//            "id": "0",
//                "author": "Alejandro Escamilla",
//                "width": 5616,
//                "height": 3744,
//                "url": "https://unsplash.com/photos/yC-Yzbqy7PY",
//                "download_url": "https://picsum.photos/id/0/5616/3744"
//        }

        public int id;
        public String author;
        public String download_url;
    }

    public static class TestResult {
        public List<Pair<String, String>> pics;
        public String text;
        public String avatar;

        public TestResult() {
            pics = new ArrayList<>();
        }

        void addPic(String url) {
            if (TextUtils.isEmpty(url)) return;
            pics.add(Pair.create(url, getThumb(url, 200, 200)));
        }

        public static String getThumb(String url, int w, int h) {
            String[] params = url.split("/");
            try {
                params[params.length - 1] = String.valueOf(w);
                params[params.length - 2] = String.valueOf(h);
                return String.join("/", params);
            } catch (Exception e) {
                return url;
            }
        }
    }
}
