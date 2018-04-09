package razerdp.demo.base.baseadapter;

import android.view.View;

/**
 * Created by 大灯泡 on 2017/4/27.
 */

public abstract class OnBaseRecyclerViewLongItemClickListener<T, V extends BaseRecyclerViewHolder<T>> {
    public boolean onItemLongClick(V holder, View v, int position, T data) {
        return onItemLongClick(v, position, data);
    }

    public abstract boolean onItemLongClick(View v, int position, T data);

}
