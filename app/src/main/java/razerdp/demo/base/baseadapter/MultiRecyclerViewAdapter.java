package razerdp.demo.base.baseadapter;

import android.content.Context;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;


import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

import razerdp.demo.utils.UnSafeUtil;
import razerdp.demo.utils.joor.Reflect;
import razerdp.demo.utils.joor.ReflectException;
import razerdp.util.log.PopupLog;

/**
 * Created by 大灯泡 on 2019/4/10.
 */
public class MultiRecyclerViewAdapter<T extends MultiType> extends BaseRecyclerViewAdapter<T> {

    private static final String TAG = "MultiRecyclerViewAdapte";

    private static HashMap<String, Integer> mViewHolderIdCache = new HashMap<>();
    private SparseArray<ViewHolderInfo> mHolderInfos = new SparseArray<>();

    private WeakReference<Object> outher;

    public MultiRecyclerViewAdapter(Context context) {
        super(context);
    }

    public MultiRecyclerViewAdapter(Context context, List<T> datas) {
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
        return data.getItemType();
    }

    @Override
    protected int getLayoutResId(int viewType) {
        ViewHolderInfo info = mHolderInfos.get(viewType);
        return info == null ? 0 : info.getLayoutId();
    }

    @Override
    protected BaseRecyclerViewHolder getViewHolder(ViewGroup parent, View itemView, int viewType) {
        if (mHolderInfos.size() < 0) {
            PopupLog.e(TAG, "holder没有注册，请调用#appendHolder添加holder信息");
            return createEmptyHolder();
        }
        ViewHolderInfo info = mHolderInfos.get(viewType);
        if (info == null) {
            PopupLog.e(TAG, "无法获取该viewType的holder信息", viewType);
            return createEmptyHolder();
        }
        final int id = info.getLayoutId();

        if (id == 0) {
            PopupLog.e(TAG, "id为0", info.getHolderClass());
            return createEmptyHolder();
        }

        if (itemView == null) {
            itemView = mInflater.inflate(id, parent, false);
        }

        try {
            return Reflect.onClass(info.getHolderClass()).create(itemView).get();
        } catch (ReflectException e) {
            //考虑到非静态内部类的情况，创建viewholder的时候需要传入外部类的对象
            if (outher != null && outher.get() != null) {
                //如果是创建在内部类里面的viewholder，则需要绑定outher
                return Reflect.onClass(info.getHolderClass()).create(outher.get(), itemView).get();
            }
            //其余情况皆是Context的内部类
            return Reflect.onClass(info.getHolderClass()).create(getContext(), itemView).get();
        }
    }

    public MultiRecyclerViewAdapter appendHolder(Class<? extends BaseMultiRecyclerViewHolder> holderClass) {
        return appendHolder(holderClass, holderClass.hashCode());
    }

    public MultiRecyclerViewAdapter appendHolder(Class<? extends BaseMultiRecyclerViewHolder> holderClass, int... viewType) {
        for (int i : viewType) {
            if (mHolderInfos.get(i) == null) {
                mHolderInfos.put(i, new ViewHolderInfo(holderClass, i));
            }
        }
        return this;
    }

    public MultiRecyclerViewAdapter replaceHolder(Class<? extends BaseMultiRecyclerViewHolder> which, int oldType, Class<? extends BaseMultiRecyclerViewHolder> holderClass, int... viewType) {
        ViewHolderInfo info = mHolderInfos.get(oldType);
        if (info != null && TextUtils.equals(which.getName(), info.mHolderClass.getName())) {
            mHolderInfos.remove(oldType);
            appendHolder(holderClass, viewType);
        }
        return this;
    }

    public MultiRecyclerViewAdapter outher(Object object) {
        this.outher = new WeakReference<>(object);
        return this;
    }


    private static class ViewHolderInfo {
        final Class<? extends BaseMultiRecyclerViewHolder> mHolderClass;
        final int viewType;

        int layoutId;

        public ViewHolderInfo(Class<? extends BaseMultiRecyclerViewHolder> holderClass, int viewType) {
            mHolderClass = holderClass;
            this.viewType = viewType;
        }

        public int getLayoutId() {
            if (layoutId == 0) {
                searchLayout();
            }
            return layoutId;
        }

        public Class<? extends BaseMultiRecyclerViewHolder> getHolderClass() {
            return mHolderClass;
        }

        public int getViewType() {
            return viewType;
        }

        static String generateKey(Class clazz, int viewType) {
            return clazz.getName() + "$Type:" + viewType;
        }

        void searchLayout() {
            if (layoutId != 0) return;
            if (mHolderClass != null) {
                final String key = generateKey(mHolderClass, viewType);
                if (mViewHolderIdCache.containsKey(key)) {
                    layoutId = mViewHolderIdCache.get(key);
                    if (layoutId != 0) {
                        PopupLog.i("ViewHolderInfo", "id from cache : " + layoutId);
                        return;
                    }
                }
                //利用unsafe绕过构造器创建对象，该对象仅用于获取layoutid
                BaseMultiRecyclerViewHolder holder = UnSafeUtil.INSTANCE.newInstance(mHolderClass);
                if (holder == null) return;
                layoutId = holder.inflateLayoutResourceId();
                if (layoutId != 0) {
                    mViewHolderIdCache.put(key, layoutId);
                }
                holder = null;
            }
        }
    }
}