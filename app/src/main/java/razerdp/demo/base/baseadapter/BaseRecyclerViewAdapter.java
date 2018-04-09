package razerdp.demo.base.baseadapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.R;
import razerdp.demo.utils.ToolUtil;

/**
 * Created by 大灯泡 on 2017/4/18.
 * <p>
 * 请注意，adapter内部持有datas，与外部不是同一个对象
 */

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewHolder<T>> {

    private static final String TAG = "BaseRecyclerViewAdapter";
    protected Context context;
    protected List<T> datas;
    protected LayoutInflater mInflater;

    private OnBaseRecyclerViewItemClickListener<T, BaseRecyclerViewHolder<T>> onRecyclerViewItemClickListener;
    private OnBaseRecyclerViewLongItemClickListener<T, BaseRecyclerViewHolder<T>> onRecyclerViewLongItemClickListener;

    public BaseRecyclerViewAdapter(@NonNull Context context) {
        this(context, new ArrayList<T>());
    }

    public BaseRecyclerViewAdapter(@NonNull Context context, @NonNull List<T> datas) {
        this.context = context;
        this.datas = new ArrayList<>();
        if (datas != null) {
            this.datas.addAll(datas);
        }
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        return getViewType(position, datas.get(position));
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseRecyclerViewHolder holder = null;
        if (getLayoutResId(viewType) != 0) {
            View rootView = mInflater.inflate(getLayoutResId(viewType), parent, false);
            holder = getViewHolder(parent, rootView, viewType);
        } else {
            holder = getViewHolder(parent, null, viewType);
        }
        setUpItemEvent(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        T data = datas.get(position);
        holder.itemView.setTag(R.id.recycler_view_tag, data);
        holder.onBindData(data, position);
        onBindData(holder, data, position);
    }

    private void setUpItemEvent(final BaseRecyclerViewHolder holder) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRecyclerViewItemClickListener != null) {
                    //这个获取位置的方法，防止添加删除导致位置不变
                    int layoutPosition = holder.getAdapterPosition();
                    try {
                        onRecyclerViewItemClickListener.onItemClick(holder, holder.itemView, layoutPosition, datas
                                .get(layoutPosition));
                    } catch (ClassCastException e) {
                        onRecyclerViewItemClickListener.onItemClick(holder.itemView, layoutPosition, datas
                                .get(layoutPosition));
                    }
                }
            }
        });
        //longclick
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onRecyclerViewLongItemClickListener != null) {
                    int layoutPosition = holder.getAdapterPosition();
                    try {
                        return onRecyclerViewLongItemClickListener.onItemLongClick(holder, holder.itemView, layoutPosition, datas
                                .get(layoutPosition));
                    } catch (ClassCastException e) {
                        return onRecyclerViewLongItemClickListener.onItemLongClick(holder.itemView, layoutPosition, datas
                                .get(layoutPosition));
                    }
                } else {
                    return true;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public void updateData(List<T> datas) {
        if (this.datas != null && this.datas != datas) {
            this.datas.clear();
            this.datas.addAll(datas);
        } else {
            this.datas = datas;
        }
        notifyDataSetChanged();
    }

    public void clearData() {
        if (this.datas != null) {
            this.datas.clear();
            notifyDataSetChanged();
        }
    }

    public void addMore(List<T> datas) {
        if (!ToolUtil.isListEmpty(datas)) {
            this.datas.addAll(datas);
            notifyDataSetChanged();
        }
    }

    public void remove(T data) {
        if (data != null) {
            int pos = this.datas.indexOf(data);
            this.datas.remove(data);
            if (pos != -1) {
                notifyItemRemoved(pos);
            } else {
                notifyDataSetChanged();
            }
        }
    }


    public void remove(int position) {
        if (position != -1) {
            try {
                this.datas.remove(position);
                notifyItemRemoved(position);
            } catch (Exception e) {
                e.printStackTrace();
                notifyDataSetChanged();
            }
        }
    }

    public List<T> getDatas() {
        return datas;
    }

    public void addData(int pos, @NonNull T data) {
        if (datas != null) {
            datas.add(pos, data);
            notifyItemInserted(pos);
        }
    }

    public void addData(@NonNull T data) {
        if (datas != null) {
            datas.add(data);
            notifyItemInserted(datas.size() - 1);
        }
    }

    public void deleteData(int pos) {
        if (datas != null && datas.size() > pos) {
            datas.remove(pos);
            notifyItemRemoved(pos);
        }
    }

    public T findData(int pos) {
        if (pos < 0 || pos > datas.size()) {
            Log.e(TAG, "这个position他喵咪的太强大了，我hold不住");
            return null;
        }
        return datas.get(pos);
    }

    protected abstract int getViewType(int position, @NonNull T data);

    protected abstract int getLayoutResId(int viewType);

    protected abstract BaseRecyclerViewHolder getViewHolder(ViewGroup parent, View rootView, int viewType);

    protected void onBindData(BaseRecyclerViewHolder<T> holder, T data, int position) {

    }

    public OnBaseRecyclerViewItemClickListener getOnRecyclerViewItemClickListener() {
        return onRecyclerViewItemClickListener;
    }

    public void setOnRecyclerViewItemClickListener(OnBaseRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener<T> onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    public OnBaseRecyclerViewLongItemClickListener getOnRecyclerViewLongItemClickListener() {
        return onRecyclerViewLongItemClickListener;
    }

    public void setOnRecyclerViewLongItemClickListener(OnBaseRecyclerViewLongItemClickListener onRecyclerViewLongItemClickListener) {
        this.onRecyclerViewLongItemClickListener = onRecyclerViewLongItemClickListener;
    }

    public void setOnRecyclerViewLongItemClickListener(OnRecyclerViewLongItemClickListener<T> onRecyclerViewLongItemClickListener) {
        this.onRecyclerViewLongItemClickListener = onRecyclerViewLongItemClickListener;
    }
}