package razerdp.basepopup;

import android.util.Log;

import java.lang.reflect.Field;

import razerdp.util.UnsafeHelper;

/**
 * Created by 大灯泡 on 2018/10/12.
 * <p>
 * 针对android p 的反射绕过
 */
final class PopupReflectionHelper {

    private String i ="123";

    public PopupReflectionHelper() {
        try {
            Field test = getClass().getDeclaredField("i");
            long offset = UnsafeHelper.objectFieldOffset(test);


            Object[] tempArray = new Object[1];
            tempArray[0] = this;

            long address = UnsafeHelper.addressOf(this);

            Object obj = UnsafeHelper.getObject(this,address+offset);

            int d = UnsafeHelper.getInt(this, offset);
            Log.d("ddd", "PopupReflectionHelper: ");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
