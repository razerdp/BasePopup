package razerdp.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by 大灯泡 on 2018/9/28.
 */
public class UnsafeHelper {
    //unsafe本来就是单例，因此可以静态设置
    private static Object unSafe;

    static {
        creteUnSafe();
    }

    private UnsafeHelper() {

    }

    private static void creteUnSafe() {
        try {
            Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            Field f = unsafeClass.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            //获取UnSafe
            unSafe = f.get(unsafeClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void checkUnSafe() throws NullPointerException {
        if (unSafe == null) {
            throw new NullPointerException("unsafe对象为空");
        }
    }


    public static long objectFieldOffset(Field field) throws Exception {
        checkUnSafe();
        final Method field_objectFieldOffset = unSafe.getClass().getMethod("objectFieldOffset", Field.class);
        return (long) field_objectFieldOffset.invoke(unSafe, field);
    }

    public static Object getObject(Object var1, long var2) throws Exception {
        checkUnSafe();

        final Method field_getObject = unSafe.getClass().getMethod("getObject", Object.class, long.class);
        return field_getObject.invoke(unSafe, var1, var2);
    }

    public static void putObject(Object var1, long var2, Object var4) throws Exception {
        checkUnSafe();

        final Method field_putObject = unSafe.getClass().getMethod("putObject", Object.class, long.class, Object.class);
        field_putObject.invoke(unSafe, var1, var2, var4);
    }

    public static void putObjectVolatile(Object var1, long var2, Object var4) throws Exception {
        checkUnSafe();

        final Method field_putObjectVolatile = unSafe.getClass().getMethod("putObjectVolatile", Object.class, long.class, Object.class);
        field_putObjectVolatile.invoke(unSafe, var1, var2, var4);
    }

    public static Object getObjectVolatile(Object var1, long var2) throws Exception {
        checkUnSafe();

        final Method field_getObjectVolatile = unSafe.getClass().getMethod("getObjectVolatile", Object.class, long.class);
        return field_getObjectVolatile.invoke(unSafe, var1, var2);
    }
}