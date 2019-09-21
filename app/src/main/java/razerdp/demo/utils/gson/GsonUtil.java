package razerdp.demo.utils.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by 大灯泡 on 2017/4/18.
 * <p>
 * gson单例
 */

public enum GsonUtil {
    INSTANCE;

    Gson gson = new GsonBuilder()
            .setLenient()
            .serializeNulls()
            .registerTypeAdapter(Integer.class, new IntegerDefaultAdapter())
            .registerTypeAdapter(int.class, new IntegerDefaultAdapter())
            .registerTypeAdapter(Double.class, new DoubleDefaultAdapter())
            .registerTypeAdapter(double.class, new DoubleDefaultAdapter())
            .registerTypeAdapter(Long.class, new LongDefaultAdapter())
            .registerTypeAdapter(long.class, new LongDefaultAdapter())
            .create();

    public String toString(Object obj) {
        return gson.toJson(obj);
    }

    public <T> T toObject(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> toList(String json, Class<T> clazz) {
        return gson.fromJson(json, TypeList(clazz));
    }

    @SuppressWarnings("unchecked")
    public <T> ArrayList<T> toArrayList(String json, Class<T> clazz) {
        return gson.fromJson(json, TypeArrayList(clazz));
    }

    @SuppressWarnings("unchecked")
    public <T> Set<T> toSet(String json, Class<T> clazz) {
        return (Set<T>) gson.fromJson(json, TypeSet(clazz));
    }

    @SuppressWarnings("unchecked")
    public <K, V> HashMap<K, V> toHashMap(String json, Class<K> keyClazz, Class<V> valueClazz) {
        return gson.fromJson(json, TypeHashMap(keyClazz, valueClazz));
    }

    public Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .setLenient()
                    .serializeNulls()
                    .registerTypeAdapter(Integer.class, new IntegerDefaultAdapter())
                    .registerTypeAdapter(int.class, new IntegerDefaultAdapter())
                    .registerTypeAdapter(Double.class, new DoubleDefaultAdapter())
                    .registerTypeAdapter(double.class, new DoubleDefaultAdapter())
                    .registerTypeAdapter(Long.class, new LongDefaultAdapter())
                    .registerTypeAdapter(long.class, new LongDefaultAdapter())
                    .create();
        }
        return gson;
    }


    public static Type TypeList(Type type) {
        return $Gson$Types.newParameterizedTypeWithOwner(null, List.class, type);
    }

    public static Type TypeArrayList(Type type) {
        return $Gson$Types.newParameterizedTypeWithOwner(null, ArrayList.class, type);
    }

    public static Type TypeSet(Type type) {
        return $Gson$Types.newParameterizedTypeWithOwner(null, Set.class, type);
    }

    public static Type TypeHashMap(Type type, Type type2) {
        return $Gson$Types.newParameterizedTypeWithOwner(null, HashMap.class, type, type2);
    }

    public static Type TypeMap(Type type, Type type2) {
        return $Gson$Types.newParameterizedTypeWithOwner(null, Map.class, type, type2);
    }

    public static Type TypeParameterized(Type ownerType, Type rawType, Type... typeArguments) {
        return $Gson$Types.newParameterizedTypeWithOwner(ownerType, rawType, typeArguments);
    }

    public static Type TypeArray(Type type) {
        return $Gson$Types.arrayOf(type);
    }

    public static Type TypeSubtypeOf(Type type) {
        return $Gson$Types.subtypeOf(type);
    }

    public static Type TypeSupertypeOf(Type type) {
        return $Gson$Types.supertypeOf(type);
    }


    public String formatJson(String strJson) {
        // 计数tab的个数
        int tabNum = 0;
        StringBuffer jsonFormat = new StringBuffer();
        int length = strJson.length();

        char last = 0;
        for (int i = 0; i < length; i++) {
            char c = strJson.charAt(i);
            if (c == '{') {
                tabNum++;
                jsonFormat.append(c + "\n");
                jsonFormat.append(getSpaceOrTab(tabNum));
            } else if (c == '}') {
                tabNum--;
                jsonFormat.append("\n");
                jsonFormat.append(getSpaceOrTab(tabNum));
                jsonFormat.append(c);
            } else if (c == ',') {
                jsonFormat.append(c + "\n");
                jsonFormat.append(getSpaceOrTab(tabNum));
            } else if (c == ':') {
                jsonFormat.append(c + " ");
            } else if (c == '[') {
                tabNum++;
                char next = strJson.charAt(i + 1);
                if (next == ']') {
                    jsonFormat.append(c);
                } else {
                    jsonFormat.append(c + "\n");
                    jsonFormat.append(getSpaceOrTab(tabNum));
                }
            } else if (c == ']') {
                tabNum--;
                if (last == '[') {
                    jsonFormat.append(c);
                } else {
                    jsonFormat.append("\n" + getSpaceOrTab(tabNum) + c);
                }
            } else {
                jsonFormat.append(c);
            }
            last = c;
        }
        return jsonFormat.toString();
    }

    // 是空格还是tab
    private String getSpaceOrTab(int tabNum) {
        StringBuffer sbTab = new StringBuffer();
        for (int i = 0; i < tabNum; i++) {
            sbTab.append('\t');
        }
        return sbTab.toString();
    }
}
