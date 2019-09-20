package razerdp.demo.base.baseadapter;

import android.view.View;

/**
 * Created by 大灯泡 on 2019/4/10.
 */

public interface OnItemClickListener<T> {

    void onItemClick(View v, int position, T data);
}
