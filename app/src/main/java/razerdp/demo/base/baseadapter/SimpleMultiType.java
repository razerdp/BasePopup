package razerdp.demo.base.baseadapter;

import java.io.Serializable;

/**
 * Created by 大灯泡 on 2019/4/10.
 */
public class SimpleMultiType implements Serializable, MultiType {
    int type;

    public SimpleMultiType() {
    }

    public SimpleMultiType(int type) {
        this.type = type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
