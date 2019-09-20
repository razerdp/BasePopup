package razerdp.demo.base.interfaces;

import androidx.annotation.Keep;

/**
 * Created by 大灯泡 on 2019/4/9.
 */
@Keep
public interface SimpleCallback<T> {
    void onCall(T data);
}
