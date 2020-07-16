package razerdp.util.animation;

import android.util.Property;

public abstract class FloatPropertyCompat<T> extends Property<T, Float> {

    public FloatPropertyCompat(String name) {
        super(Float.class, name);
    }

    /**
     * A type-specific variant of {@link #set(Object, Float)} that is faster when dealing
     * with fields of type <code>float</code>.
     */
    public abstract void setValue(T object, float value);

    @Override
    final public void set(T object, Float value) {
        setValue(object, value);
    }

}
