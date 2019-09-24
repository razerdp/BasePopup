package razerdp.demo.utils.rx;

/**
 * Created by 大灯泡 on 2018/5/24.
 */
public abstract class RxCallImpl<T> implements RxCall<T> {
    T data;

    public RxCallImpl() {
    }

    public RxCallImpl(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public RxCallImpl<T> setData(T data) {
        this.data = data;
        return this;
    }
}
