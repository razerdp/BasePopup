package razerdp.demo.model;

import android.view.View;

import razerdp.demo.base.baseadapter.MultiType;
import razerdp.demo.popup.PopupSource;

/**
 * Created by 大灯泡 on 2019/9/20
 * <p>
 * Description：
 */
public abstract class DemoCommonUsageInfo implements MultiType {
    public String title;
    public String desc;
    public String option;
    public boolean sourceVisible = true;
    protected PopupSource source;

    protected String name;
    protected String javaUrl;
    protected String resUrl;

    public DemoCommonUsageInfo() {
    }


    @Override
    public int getItemType() {
        return 1;
    }

    public abstract void toShow(View v);

    public abstract void toOption(View v);

    public void toSource(View v) {
        if (source == null) {
            source = new PopupSource(v.getContext(), name, javaUrl, resUrl);
        }
        source.showPopupWindow();
    }

}
