package razerdp.demo.base.baseadapter;

import android.content.Context;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * Created by 大灯泡 on 2017/4/18.
 * <p>
 * rvholder基类
 */

public abstract class BaseRecyclerViewHolder<T> extends RecyclerView.ViewHolder {
    private boolean isRecycled = false;
    protected T data;

    public BaseRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    final void bindData(T data, int position) {
        isRecycled = false;
        this.data = data;
        onBindData(data, position);
    }

    public abstract void onBindData(T data, int position);

    public final <V extends View> V findViewById(@IdRes int id) {
        return itemView.findViewById(id);
    }

    void recycle() {
        isRecycled = true;
        onRecycled();
    }

    protected void onRecycled() {

    }

    public T getData() {
        return data;
    }

    public Context getContext() {
        return itemView.getContext();
    }
}
