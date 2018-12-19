package razerdp.demo.popup;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;

/**
 * Created by 大灯泡 on 2018/12/19.
 */
public class BubblePopup extends BasePopupWindow {
    public TextView tvContent;

    public BubblePopup(Context context) {
        super(context);
        this.tvContent = findViewById(R.id.tv_content);

    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_bubble);
    }

    public void setContent(CharSequence text) {
        tvContent.setText(text);
    }
}
