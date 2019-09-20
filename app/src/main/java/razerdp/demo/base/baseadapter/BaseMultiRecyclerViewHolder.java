package razerdp.demo.base.baseadapter;

import androidx.annotation.NonNull;
import android.view.View;


/**
 * Created by 大灯泡 on 2019/4/10.
 */
public abstract class BaseMultiRecyclerViewHolder<T extends MultiType> extends BaseRecyclerViewHolder<T> {


    public BaseMultiRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public abstract int inflateLayoutResourceId();

}
