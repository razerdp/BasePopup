package razerdp.demo.base.baseadapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

import razerdp.demo.utils.ToolUtil;
import razerdp.demo.utils.UnSafeUtil;
import razerdp.demo.utils.joor.Reflect;
import razerdp.demo.utils.joor.ReflectException;
import razerdp.util.log.PopupLog;

/**
 * Created by 大灯泡 on 2019/7/30
 * <p>
 * Description：懒的写adapter的时候，请用这个（针对单个布局）
 */
public class SimpleRecyclerViewAdapter<T> extends BaseRecyclerViewAdapter<T> {
    private static final String TAG = "SimpleRecyclerViewAdapt";

    //部分viewholder内部类默认持有外部引用，此时需要传入该对象
    private WeakReference<Object> outher;
    private Class<? extends BaseSimpleRecyclerViewHolder> holderClass;
    private int itemViewLayoutId;
    private static HashMap<String, Integer> mViewHolderIdCache = new HashMap<>();

    public SimpleRecyclerViewAdapter(@NonNull Context context) {
        super(context);
    }

    public SimpleRecyclerViewAdapter(@NonNull Context context, @NonNull List<T> datas) {
        super(context, datas);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (outher != null) {
            outher.clear();
        }
        outher = null;
    }

    @Override
    protected int getViewType(int position, @NonNull T data) {
        return 0;
    }

    @Override
    protected int getLayoutResId(int viewType) {
        if (itemViewLayoutId == 0) {
            gaintId(holderClass);
        }
        return itemViewLayoutId;
    }

    public SimpleRecyclerViewAdapter<T> outher(Object object) {
        this.outher = new WeakReference<>(object);
        return this;
    }

    @Override
    protected BaseRecyclerViewHolder getViewHolder(ViewGroup parent, View itemView, int viewType) {
        try {
            return Reflect.onClass(holderClass).create(itemView).get();
        } catch (ReflectException e) {
            //考虑到非静态内部类的情况，创建viewholder的时候需要传入外部类的对象
            if (outher != null && outher.get() != null) {
                //如果是创建在内部类里面的viewholder，则需要绑定outher
                return Reflect.onClass(holderClass).create(outher.get(), itemView).get();
            }
            //其余情况皆是Context的内部类
            return Reflect.onClass(holderClass).create(getContext(), itemView).get();
        }
    }


    public SimpleRecyclerViewAdapter setHolder(Class<? extends BaseSimpleRecyclerViewHolder> clazz) {
        this.holderClass = clazz;
        return gaintId(clazz);
    }

    private SimpleRecyclerViewAdapter gaintId(Class<? extends BaseSimpleRecyclerViewHolder> clazz) {
        if (clazz == null) {
            throw new NullPointerException("holderclass不能为空");
        }
        String key = getContext().getClass().getName() + "$Type:" + clazz.getName();
        if (mViewHolderIdCache.containsKey(key)) {
            PopupLog.i("gaintId : read from cache");
            itemViewLayoutId = ToolUtil.cast(mViewHolderIdCache.get(key), Integer.class, 0);
            if (itemViewLayoutId != 0) return this;
        }
        BaseSimpleRecyclerViewHolder tempHolder = UnSafeUtil.INSTANCE.newInstance(clazz);
        itemViewLayoutId = tempHolder.inflateLayoutResourceId();
        if (itemViewLayoutId != 0) {
            mViewHolderIdCache.put(key, itemViewLayoutId);
        }
        tempHolder = null;
        return this;
    }
}
