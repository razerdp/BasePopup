package razerdp.demo.base.interfaces;

import android.view.View;

/**
 * Created by 大灯泡 on 2019/4/9.
 */
public abstract class OnClickListenerWrapper<T> implements View.OnClickListener {
    T data;

    public T getData() {
        return data;
    }

    public OnClickListenerWrapper<T> setData(T data) {
        this.data = data;
        return this;
    }
}
