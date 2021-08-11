package razerdp.basepopup;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 大灯泡 on 2021/6/24
 * <p>
 * Description：
 */
class FirstOpenActivityLiveData<T> extends MutableLiveData<T> {
    List<Observer<? super T>> observers;

    @Override
    public void observeForever(@NonNull Observer<? super T> observer) {
        super.observeForever(observer);
        if (observers == null) {
            observers = new ArrayList<>();
        }
        observers.add(observer);
    }

    void clear() {
        if (observers != null) {
            for (Observer<? super T> observer : observers) {
                removeObserver(observer);
            }
            observers.clear();
        }
        observers = null;
    }
}
