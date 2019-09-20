package razerdp.demo.widget.span;

import android.view.View;

import razerdp.basepopup.R;
import razerdp.demo.utils.UIHelper;


/**
 * Created by 大灯泡 on 2019/4/9.
 */
public class ClickSpan extends ClickableSpanEx {

    private View.OnClickListener mOnClickListener;

    public ClickSpan(View.OnClickListener onClickListener) {
        super(-1, UIHelper.getColor(R.color.press_color), false);
        mOnClickListener = onClickListener;
    }

    @Override
    public void onClick(View widget) {
        if (mOnClickListener != null) {
            mOnClickListener.onClick(widget);
        }
    }
}
