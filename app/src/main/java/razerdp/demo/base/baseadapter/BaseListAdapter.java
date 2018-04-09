package razerdp.demo.base.baseadapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by 大灯泡 on 2017/4/19.
 */

public abstract class BaseListAdapter<T, V extends BaseListViewHolder> extends BaseAdapter {
    protected List<T> datas;
    protected LayoutInflater mInflater;
    protected Context mContext;

    public BaseListAdapter(Context context, @Nullable List<T> datas) {
        mContext = context;
        this.datas = datas;
        mInflater = LayoutInflater.from(context);
    }

    public void updateData(@NonNull List<T> newDatas) {
        if (this.datas != null && this.datas != newDatas) {
            this.datas.clear();
            this.datas.addAll(newDatas);
        } else {
            this.datas = newDatas;
        }
        notifyDataSetChanged();
    }

    public void addMore(@NonNull List<T> newDatas) {
        datas.addAll(newDatas);
        notifyDataSetChanged();
    }

    public List<T> getDatas() {
        return datas;
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public T getItem(int position) {
        return datas.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseListViewHolder holder = null;
        final int itemType = getItemViewType(position);
        if (convertView == null) {
            convertView = mInflater.inflate(getLayoutRes(itemType), parent, false);
            holder = initViewHolder(itemType);
            holder.onInFlate(convertView);
            convertView.setTag(holder);
        } else {
            holder = (BaseListViewHolder) convertView.getTag();
        }
        onBindView(position, getItem(position), holder);
        return convertView;
    }

    public Context getContext() {
        return mContext;
    }

    public abstract int getLayoutRes(int viewType);

    public abstract BaseListViewHolder initViewHolder(int viewType);

    public abstract void onBindView(int position, T data, BaseListViewHolder holder);
}
