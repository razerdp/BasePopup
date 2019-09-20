package razerdp.demo.base.baseadapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Space;


import java.util.ArrayList;
import java.util.List;

import razerdp.demo.utils.ToolUtil;
import razerdp.util.log.PopupLog;

/**
 * Created by 大灯泡 on 2019/4/10.
 * <p>
 * 请注意，adapter内部持有datas，与外部不是同一个对象
 */

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewHolder<T>> {

    private static final String TAG = "BaseRecyclerViewAdapter";
    protected Context context;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    protected int headerViewCount;
    protected int footerViewCount;

    private OnItemClickListener<T> mOnItemClickListener;
    private OnItemLongClickListener<T> mOnItemLongClickListener;

    public BaseRecyclerViewAdapter(@NonNull Context context) {
        this(context, new ArrayList<T>());
    }

    public BaseRecyclerViewAdapter(@NonNull Context context, @NonNull List<T> mDatas) {
        this.context = context;
        this.mDatas = new ArrayList<>();
        if (mDatas != null) {
            this.mDatas.addAll(mDatas);
        }
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        PopupLog.i(TAG, "onAttachedToRecyclerView");
        if (recyclerView.getAdapter() instanceof HeaderViewWrapperAdapter) {
            headerViewCount = ((HeaderViewWrapperAdapter) recyclerView.getAdapter()).getHeadersCount();
            footerViewCount = ((HeaderViewWrapperAdapter) recyclerView.getAdapter()).getFootersCount();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return getViewType(position, mDatas.get(position));
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseRecyclerViewHolder holder = null;
        int layout = getLayoutResId(viewType);
        if (layout != 0) {
            holder = getViewHolder(parent,
                    mInflater.inflate(layout, parent, false),
                    viewType);
            setupClickEvent(holder);
            return holder;
        }
        return createEmptyHolder();
    }

    protected void setupClickEvent(final BaseRecyclerViewHolder holder) {
        if (holder == null) return;
        if (mOnItemClickListener != null) {
            holder.itemView.setClickable(true);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getAdapterPosition();
                    pos -= headerViewCount;
                    if (pos < 0) return;
                    mOnItemClickListener.onItemClick(v, pos, mDatas.get(pos));
                }
            });
        }
        if (mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getAdapterPosition();
                    pos -= headerViewCount;
                    if (pos < 0) return false;
                    return mOnItemLongClickListener.onItemLongClick(v, pos, mDatas.get(pos));
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        T data = mDatas.get(position);
        holder.bindData(data, position);
        onBindData(holder, data, position);
    }

    @Override
    public void onViewRecycled(@NonNull BaseRecyclerViewHolder<T> holder) {
        super.onViewRecycled(holder);
        holder.recycle();
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public void updateData(List<T> datas) {
        updateData(-1, datas);
    }

    /**
     * @param position 当position为-1时，添加到list的末尾
     * @param datas
     */
    public void updateData(int position, List<T> datas) {
        if (!ToolUtil.isEmpty(datas)) {
            if (position == -1) {
                mDatas.clear();
                mDatas.addAll(datas);
            } else {
                mDatas.addAll(position, datas);
            }
        } else {
            //清空
            mDatas.clear();
        }
        notifyDataSetChanged();
    }


    public void clearData() {
        if (this.mDatas != null) {
            this.mDatas.clear();
            notifyDataSetChanged();
        }
    }

    public void addMore(List<T> datas) {
        if (!ToolUtil.isEmpty(datas)) {
            int lastDataSize = this.mDatas.size();
            this.mDatas.addAll(datas);
            notifyItemRangeChanged(lastDataSize, datas.size());
        }
    }

    public void remove(T data) {
        if (data != null) {
            int pos = mDatas.indexOf(data);
            if (pos < 0) return;
            if (mDatas.remove(data)) {
                notifyItemRemoved(pos);
            }
        }
    }


    public void remove(int position) {
        if (!ToolUtil.indexInList(mDatas, position)) return;
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    public List<T> getDatas() {
        return mDatas;
    }

    public void addData(int pos, @NonNull T data) {
        if (mDatas != null) {
            if (pos >= mDatas.size()) {
                mDatas.add(data);
            } else {
                mDatas.add(pos, data);
            }
            notifyItemInserted(pos);
        }
    }

    public void addData(@NonNull T data) {
        if (mDatas != null) {
            mDatas.add(data);
            notifyItemInserted(mDatas.size() - 1);
        }
    }

    public void addData(@NonNull List<T> data) {
        addData(-1, data);
    }

    public void addData(int pos, @NonNull List<T> data) {
        if (mDatas != null) {
            int curPos = mDatas.size();
            if (pos < 0) {
                mDatas.addAll(data);
            } else {
                mDatas.addAll(pos, data);
            }
            notifyItemRangeInserted(curPos, data.size());
        }
    }

    protected abstract int getViewType(int position, @NonNull T data);

    protected abstract int getLayoutResId(int viewType);

    protected abstract BaseRecyclerViewHolder getViewHolder(ViewGroup parent, View itemView, int viewType);

    protected void onBindData(BaseRecyclerViewHolder<T> holder, T data, int position) {

    }

    public BaseRecyclerViewAdapter<T> setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
        return this;
    }

    public BaseRecyclerViewAdapter<T> setOnItemLongClickListener(OnItemLongClickListener<T> onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
        return this;
    }

    public int getHeaderViewCount() {
        return headerViewCount;
    }

    public BaseRecyclerViewAdapter<T> setHeaderViewCount(int headerViewCount) {
        this.headerViewCount = headerViewCount;
        return this;
    }

    public int getFooterViewCount() {
        return footerViewCount;
    }

    public BaseRecyclerViewAdapter<T> setFooterViewCount(int footerViewCount) {
        this.footerViewCount = footerViewCount;
        return this;
    }

    public final Context getContext() {
        return context;
    }


    protected BaseRecyclerViewHolder createEmptyHolder() {
        return new EmptyHolder(new Space(getContext()));
    }

    private class EmptyHolder extends BaseRecyclerViewHolder {

        EmptyHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onBindData(Object data, int position) {

        }
    }
}