package razerdp.demo.utils.rx;


import io.reactivex.functions.Consumer;
import razerdp.util.log.PopupLog;

/**
 * Created by 大灯泡 on 2019/4/9.
 * 默认打印日志的错误consumer
 */
public final class DefaultLogThrowableConsumer implements Consumer<Throwable> {
    private String TAG = this.getClass().getSimpleName();

    public DefaultLogThrowableConsumer() {
    }

    public DefaultLogThrowableConsumer(String TAG) {
        this.TAG = TAG;
    }

    @Override
    public void accept(Throwable throwable) throws Exception {
        PopupLog.e(TAG, throwable);
    }
}
