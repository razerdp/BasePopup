package razerdp.demo.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by 大灯泡 on 2019/7/17
 * <p>
 * Description：unsafe操作类
 */
public enum UnSafeUtil {
    INSTANCE;


    private Object unSafe;


    private Method field_objectFieldOffset;
    private Method field_getObject;
    private Method field_putObject;
    private Method field_arrayBaseOffset;
    private Method field_addressSize;
    private Method field_getInt;
    private Method field_getInt2;
    private Method field_getLong;
    private Method field_getLong2;
    private Method field_getObjectVolatile;
    private Method field_allocateInstance;

    private void checkUnSafe() {
        if (unSafe != null) return;
        try {
            Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            Field f = unsafeClass.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            //获取UnSafe
            unSafe = f.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public long objectFieldOffset(Field field) throws Exception {
        checkUnSafe();
        if (field_objectFieldOffset == null) {
            field_objectFieldOffset = unSafe.getClass().getMethod("objectFieldOffset", Field.class);
        }
        return (long) field_objectFieldOffset.invoke(unSafe, field);
    }

    public Object getObject(Object var1, long var2) throws Exception {
        checkUnSafe();

        if (field_getObject == null) {
            field_getObject = unSafe.getClass().getMethod("getObject", Object.class, long.class);
        }
        return field_getObject.invoke(unSafe, var1, var2);
    }

    public void putObject(Object var1, long var2, Object var4) throws Exception {
        checkUnSafe();

        if (field_putObject == null) {
            field_putObject = unSafe.getClass().getMethod("putObject", Object.class, long.class, Object.class);
        }
        field_putObject.invoke(unSafe, var1, var2, var4);
    }


    public int arrayBaseOffset(Class<?> var1) throws Exception {
        checkUnSafe();

        if (field_arrayBaseOffset == null) {
            field_arrayBaseOffset = unSafe.getClass().getMethod("arrayBaseOffset", Class.class);
        }
        return (int) field_arrayBaseOffset.invoke(unSafe, var1);
    }

    public int addressSize() throws Exception {
        checkUnSafe();

        if (field_addressSize == null) {
            field_addressSize = unSafe.getClass().getMethod("addressSize");
        }

        return (int) field_addressSize.invoke(unSafe);
    }

    public int getInt(long var1) throws Exception {
        checkUnSafe();

        if (field_getInt == null) {
            field_getInt = unSafe.getClass().getMethod("getInt", long.class);
        }
        return (int) field_getInt.invoke(unSafe, var1);
    }

    public int getInt(Object var1, long var2) throws Exception {
        checkUnSafe();

        if (field_getInt2 == null) {
            field_getInt2 = unSafe.getClass().getMethod("getInt", Object.class, long.class);
        }
        return (int) field_getInt2.invoke(unSafe, var1, var2);
    }

    public int getLong(long var1) throws Exception {
        checkUnSafe();

        if (field_getLong == null) {
            field_getLong = unSafe.getClass().getMethod("getLong", long.class);
        }
        return (int) field_getLong.invoke(unSafe, var1);
    }

    public int getLong(Object var1, long var2) throws Exception {
        checkUnSafe();

        if (field_getLong2 == null) {
            field_getLong2 = unSafe.getClass().getMethod("getLong", Object.class, long.class);
        }
        return (int) field_getLong2.invoke(unSafe, var1, var2);
    }

    public long addressOf(Object o) throws Exception {
        checkUnSafe();

        Object[] array = new Object[]{o};

        long baseOffset = arrayBaseOffset(Object[].class);
        int addressSize = addressSize();
        long result;
        switch (addressSize) {
            case 4:
                result = getInt(array, baseOffset);
                break;
            case 8:
                result = getLong(array, baseOffset);
                break;
            default:
                throw new Error("unsupported address size: " + addressSize);
        }
        return result;
    }


    public Object getObjectVolatile(Object var1, long var2) throws Exception {
        checkUnSafe();

        final Method field_getObjectVolatile = unSafe.getClass().getMethod("getObjectVolatile", Object.class, long.class);
        return field_getObjectVolatile.invoke(unSafe, var1, var2);
    }

    public void putObjectVolatile(Object var1, long var2, Object var4) throws Exception {
        checkUnSafe();

        if (field_getObjectVolatile == null) {
            field_getObjectVolatile = unSafe.getClass().getMethod("putObjectVolatile", Object.class, long.class, Object.class);
        }
        field_getObjectVolatile.invoke(unSafe, var1, var2, var4);
    }

    public Object allocateInstance(Class<?> clazz) throws Exception {
        checkUnSafe();
        if (field_allocateInstance == null) {
            field_allocateInstance = unSafe.getClass().getMethod("allocateInstance", Class.class);
        }
        return field_allocateInstance.invoke(unSafe, clazz);
    }

    public <T> T newInstance(Class<?> clazz) {
        try {
            return (T) allocateInstance(clazz);
        } catch (Exception e) {
            throw new UnsupportedOperationException("create instance for " + clazz + " failed. " + e.getMessage(), e);
        }
    }

}
