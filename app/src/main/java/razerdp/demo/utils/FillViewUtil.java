package razerdp.demo.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.R;
import razerdp.demo.utils.joor.Reflect;
import razerdp.demo.utils.joor.ReflectException;

/**
 * Created by 大灯泡 on 2019/4/15
 * <p>
 * Description：
 */
@SuppressWarnings("unchecked")
public class FillViewUtil {
    private static final String TAG = "FillViewUtil";

    public static <V extends FillViewHolder> void fillView(@NonNull int count,
                                                           @NonNull ViewGroup target,
                                                           @LayoutRes int itemViewRes,
                                                           @NonNull OnFillViewsListener<Integer, V> onFillViewsListener) {
        List<Integer> datas = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            datas.add(i);
        }
        fillView(datas, target, itemViewRes, onFillViewsListener);
    }

    public static <T, V extends FillViewHolder> void fillView(@NonNull List<T> datas,
                                                              @NonNull ViewGroup target,
                                                              @LayoutRes int itemViewRes,
                                                              @NonNull OnFillViewsListener<T, V> onFillViewsListener) {
        if (datas == null || target == null || itemViewRes == 0 || onFillViewsListener == null) {
            return;
        }
        final int childCount = target.getChildCount();
        final int size = datas.size();
        int index = 0;
        if (childCount > 0) {
            //如果添加过
            for (; index < childCount && index < size; index++) {
                //复用原有的
                View child = target.getChildAt(index);
                Object obj = child.getTag(R.id.tag_fill_view);
                if (!(obj instanceof FillViewHolder)) continue;//可能是别的view
                V holder = (V) obj;
                holder.holderPosition = index;
                holder.bindData(datas.get(index), index, index == size - 1);
            }
        }

        //填充剩余的
        final int rest = size - childCount;
        SimplePool<V> pool = ToolUtil.cast(target.getTag(R.id.tag_fill_view_recycler_bin), SimplePool.class);
        if (pool == null) {
            pool = new SimplePool<>(16);
            target.setTag(R.id.tag_fill_view_recycler_bin, pool);
        }
        if (rest > 0) {
            for (int i = index; i < size; i++) {
                T data = datas.get(i);
                V viewHolder = null;
                viewHolder = pool.acquire();

                if (viewHolder == null) {
                    View childView = LayoutInflater.from(target.getContext()).inflate(itemViewRes, target, false);
                    viewHolder = onFillViewsListener.onCreateViewHolder(childView, data, i);
                }
                viewHolder.bindViewHolder();
                viewHolder.holderPosition = i;
                target.addView(ViewUtil.removeFromParent(viewHolder.itemView));
                viewHolder.bindData(data, i, i == size - 1);
            }
        } else {
            for (int i = index; i < index - rest; i++) {
                View child = target.getChildAt(i);
                Object obj = child.getTag(R.id.tag_fill_view);
                if (!(obj instanceof FillViewHolder)) continue;//可能是别的view
                V holder = (V) obj;
                pool.release(holder);
            }
            //无情移除~
            target.removeViewsInLayout(index, -rest);
            target.requestLayout();
        }
    }


    public static void clear(ViewGroup target) {
        if (target == null) return;
        final int childCount = target.getChildCount();
        SimplePool<FillViewHolder> pool = ToolUtil.cast(target.getTag(R.id.tag_fill_view_recycler_bin), SimplePool.class);
        if (pool == null) {
            pool = new SimplePool<>(16);
            target.setTag(R.id.tag_fill_view_recycler_bin, pool);
        }
        for (int i = 0; i < childCount; i++) {
            View child = target.getChildAt(i);
            Object obj = child.getTag(R.id.tag_fill_view);
            if (!(obj instanceof FillViewHolder)) continue;//可能是别的view
            FillViewHolder holder = (FillViewHolder) obj;
            pool.release(holder);
        }
        target.removeViews(0, childCount);
    }

    public static void destroy(ViewGroup target) {
        if (target == null) return;
        SimplePool<FillViewHolder> pool = ToolUtil.cast(target.getTag(R.id.tag_fill_view_recycler_bin), SimplePool.class);
        if (pool != null) {
            pool.clearPool();
        }
        target.removeAllViews();
    }

    public static FillViewHolder getHolder(View itemView) {
        if (itemView == null) return null;
        return (FillViewHolder) itemView.getTag(R.id.tag_fill_view);
    }

    public static abstract class FillViewHolder<T> {
        protected T data;
        protected final View itemView;
        int holderPosition = 0;

        public final <V extends View> V findViewById(int resid) {
            if (resid > 0 && itemView != null) {
                return itemView.findViewById(resid);
            }
            return null;
        }

        public FillViewHolder(View itemView) {
            this.itemView = itemView;
        }

        public int getHolderPosition() {
            return holderPosition;
        }

        public final void bindData(T data, int position, boolean isLast) {
            this.data = data;
            onBindData(data, position, isLast);
        }

        public abstract void onBindData(T data, int position, boolean isLast);

        void bindViewHolder() {
            itemView.setTag(R.id.tag_fill_view, this);
        }

        public T getData() {
            return data;
        }
    }

    public static <T, V extends FillViewHolder> OnFillViewsListener<T, V> simpleImpl(Class<V> holderClass, Object... outhers) {
        return new OnFillViewsListener<T, V>() {
            @Override
            public V onCreateViewHolder(View itemView, T data, int position) {
                try {
                    return Reflect.onClass(holderClass).create(itemView).get();
                } catch (ReflectException e) {
                    //考虑到非静态内部类的情况，创建viewholder的时候需要传入外部类的对象
                    if (outhers.length > 0) {
                        Object outher = outhers[0];
                        if (outher != null) {
                            //如果是创建在内部类里面的viewholder，则需要绑定outher
                            return Reflect.onClass(holderClass).create(outher, itemView).get();
                        }
                    }
                    return null;
                }
            }
        };
    }

    public interface OnFillViewsListener<T, V extends FillViewHolder> {
        V onCreateViewHolder(View itemView, T data, int position);
    }

}
