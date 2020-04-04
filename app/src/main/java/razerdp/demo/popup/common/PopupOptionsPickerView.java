package razerdp.demo.popup.common;

import android.view.View;

import com.bigkoo.pickerview.configure.PickerOptions;
import com.bigkoo.pickerview.view.OptionsPickerView;

/**
 * Created by 大灯泡 on 2019/8/14
 * <p>
 * Description：用于popup的picker，主要是防止select之后立刻dismiss导致消失的问题
 */
public class PopupOptionsPickerView<T> extends OptionsPickerView<T> {
    public PopupOptionsPickerView(PickerOptions pickerOptions) {
        super(pickerOptions);
    }

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        if (tag.equals("submit")) {
            returnData();
        } else if (tag.equals("cancel")) {
            if (mPickerOptions.cancelListener != null) {
                mPickerOptions.cancelListener.onClick(v);
            }
        }
    }
}
