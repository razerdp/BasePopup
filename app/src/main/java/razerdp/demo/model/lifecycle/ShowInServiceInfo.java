package razerdp.demo.model.lifecycle;

import android.content.Intent;
import android.view.View;

import razerdp.demo.model.DemoCommonUsageInfo;
import razerdp.demo.services.DemoService;

/**
 * Created by 大灯泡 on 2020/2/3.
 */
public class ShowInServiceInfo extends DemoCommonUsageInfo {
    public ShowInServiceInfo() {
        title = "在service（或非activity）中弹窗";
    }

    @Override
    public void toShow(View v) {
        Intent intent = new Intent(v.getContext(), DemoService.class);
        v.getContext().startService(intent);
    }

    @Override
    public void toOption(View v) {

    }
}
