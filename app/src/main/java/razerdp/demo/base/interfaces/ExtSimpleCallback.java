package razerdp.demo.base.interfaces;

/**
 * Created by 大灯泡 on 2019/4/8.
 * 升级版simpleCallback
 */
public abstract class ExtSimpleCallback<T> implements SimpleCallback<T> {

    public void onStart() {
    }

    public void onError(int code, String errorMessage) {
    }

    public void onFinish() {
    }
}
