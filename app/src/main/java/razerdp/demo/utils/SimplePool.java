package razerdp.demo.utils;

import androidx.annotation.NonNull;
import androidx.core.util.Pools;

import razerdp.util.log.PopupLog;


/**
 * Created by 大灯泡 on 2019/4/25
 * <p>
 * Description：
 *
 * @see Pools.SimplePool
 */
public class SimplePool<T> implements Pools.Pool<T> {
    private final Object[] mPool;
    private int mPoolSize;

    public SimplePool(int maxPoolSize) {
        if (maxPoolSize <= 0) {
            throw new IllegalArgumentException("The max pool size must be > 0");
        } else {
            this.mPool = new Object[maxPoolSize];
        }
    }

    public T acquire() {
        if (this.mPoolSize > 0) {
            int lastPooledIndex = this.mPoolSize - 1;
            T instance = (T) this.mPool[lastPooledIndex];
            this.mPool[lastPooledIndex] = null;
            --this.mPoolSize;
            return instance;
        } else {
            return null;
        }
    }

    public boolean release(@NonNull T instance) {
        if (this.isInPool(instance)) {
            PopupLog.i("Already in the pool!");
            return false;
        } else if (this.mPoolSize < this.mPool.length) {
            this.mPool[this.mPoolSize] = instance;
            ++this.mPoolSize;
            return true;
        } else {
            return false;
        }
    }

    public boolean isFull() {
        return this.mPoolSize == mPool.length;
    }

    public boolean isInPool(@NonNull T instance) {
        for (int i = 0; i < this.mPoolSize; ++i) {
            if (this.mPool[i] == instance) {
                return true;
            }
        }

        return false;
    }

    public void clearPool() {
        clearPool(null);
    }

    public void clearPool(OnClearListener<T> l) {
        for (int i = 0; i < mPool.length; i++) {
            if (l != null && mPool[i] != null) {
                l.onClear((T) mPool[i]);
            }
            mPool[i] = null;
        }
        mPoolSize = 0;
    }

    public int size() {
        return mPoolSize;
    }

    public interface OnClearListener<T> {
        void onClear(T cached);
    }
}
