package razerdp.util.animation;

import android.util.SparseArray;

import androidx.annotation.NonNull;

import razerdp.util.log.PopupLog;

@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class AnimationApi<T> {
    SparseArray<BaseAnimationConfig> configs;


    //-----------alpha-------------
    public T withAlpha(@NonNull AlphaConfig config) {
        appendConfigs(config);
        return (T) this;
    }

    //-----------scale-------------
    public T withScale(@NonNull ScaleConfig config) {
        appendConfigs(config);
        return (T) this;
    }

    //-----------translation-------------
    public T withTranslation(@NonNull TranslationConfig config) {
        appendConfigs(config);
        return (T) this;
    }

    //-----------rotation-------------
    public T withRotation(@NonNull RotationConfig config) {
        appendConfigs(config);
        return (T) this;
    }

    void appendConfigs(@NonNull BaseAnimationConfig config) {
        if (configs == null) {
            configs = new SparseArray<>();
        }
        configs.delete(config.key());
        configs.append(config.key(), config);
    }
}
