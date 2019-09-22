package razerdp.demo.base.livedata;


import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import razerdp.demo.utils.ToolUtil;

/**
 * Created by 大灯泡 on 2019/4/9.
 */
@SuppressWarnings("ALL")
public class DPLiveData<T> extends MutableLiveData<T> {
    private static final String TAG = "DPLiveData";
    private int mVersion = -1;

    public void send(T value) {
        if (ToolUtil.isMainThread()) {
            setValue(value);
        } else {
            postValue(value);
        }
    }

    @Override
    public void setValue(T value) {
        mVersion++;
        super.setValue(value);
    }

    @Override
    public void postValue(T value) {
        mVersion++;
        super.postValue(value);
    }

    int getVersion() {
        return mVersion;
    }

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        super.observe(owner, new LiveDataObserverWrapper(observer, this));
    }

    public void observeSticky(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer) {
        super.observe(owner, observer);
    }

    @Override
    public void observeForever(@NonNull Observer<? super T> observer) {
        super.observeForever(new LiveDataObserverWrapper(observer, this));
    }


    public void observeForeverSticky(@NonNull Observer<T> observer) {
        super.observeForever(observer);
    }
}
