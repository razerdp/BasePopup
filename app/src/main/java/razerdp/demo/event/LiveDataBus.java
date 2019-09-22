package razerdp.demo.event;

import android.content.Intent;
import android.util.Pair;

import razerdp.demo.base.livedata.DPLiveData;

/**
 * Created by 大灯泡 on 2019/8/13
 * <p>
 * Description：事件总线~livedata
 */
public enum LiveDataBus {
    INSTANCE;

    private DPLiveData<Pair<Integer, Intent>> activityReenterLiveData;


    public DPLiveData<Pair<Integer, Intent>> getActivityReenterLiveData() {
        if (activityReenterLiveData == null) {
            activityReenterLiveData = new DPLiveData<>();
        }
        return activityReenterLiveData;
    }
}
