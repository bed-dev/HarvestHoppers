package codes.bed.harvesthoppers.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "unused"})
public class Maps {

    public static Integer integer(java.util.Map<String, Object> map, String key, Integer defaultValue) {
        try {
            Object value = map.get(key);
            return value instanceof Integer || value == null ? (Integer) value : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static Integer sinteger(java.util.Map<String, Object> map, String key, Integer defaultValue) {
        try {
            Object value = map.get(key);
            return value instanceof Integer ? (Integer) value : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static String string(java.util.Map<String, Object> map, String key, String defaultValue) {
        try {
            Object value = map.get(key);
            return value instanceof String ? (String) value : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static String sstring(java.util.Map<String, Object> map, String key, String defaultValue) {
        try {
            Object value = map.get(key);
            return value instanceof String ? (String) value : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static List<String> list(java.util.Map<String, Object> map, List<String> keys, List<String> defaultValue) {
        try {
            for(String key : keys) {
                List<String> result = list(map, key, defaultValue);
                if (result != null) {
                    return result;
                }
            }
        } catch (Exception ignored) {
        }
        return defaultValue;
    }


    public static List<String> list(java.util.Map<String, Object> map, String key, List<String> defaultValue) {
        try {
            Object value = map.get(key);
            if (value instanceof List) {
                List<String> list = (List<String>) value;
                return list.isEmpty() || !(list.getFirst() instanceof String) ? defaultValue : list;
            } else if (value instanceof String) {
                return Collections.singletonList(value.toString());
            }
            return defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static List<Object> map(java.util.Map<String, Object> map, List<String> keys, List<Object> defaultValue) {
        try {
            for (String k : keys) {
                List<Object> result = map(map, k, defaultValue);
                if (result != null) {
                    return result;
                }
            }
        } catch (Exception ignored) {
        }
        return defaultValue;
    }

    public static List<Object> map(java.util.Map<String, Object> map, String key, List<Object> defaultValue) {
        try {
            Object value = map.get(key);
            if (value instanceof List) {
                List<Object> list = (List<Object>) value;
                return !list.isEmpty() && (list.getFirst() instanceof java.util.Map<?, ?> || list.getFirst() instanceof String)
                        ? new ArrayList<>(list) : defaultValue;
            }
            return defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static List<Object> smap(java.util.Map<String, Object> map, String key, List<Object> defaultValue) {
        try {
            Object value = map.get(key);
            if (value instanceof List) {
                List<Object> list = (List<Object>) value;
                return !list.isEmpty() && (list.getFirst() instanceof java.util.Map<?, ?> || list.getFirst() instanceof String)
                        ? new ArrayList<>(list) : defaultValue;
            }
            return defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static Boolean bool(java.util.Map<String, Object> map, String key, Boolean defaultValue) {
        try {
            Object value = map.get(key);
            return value instanceof Boolean ? (Boolean) value : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static Boolean sbool(java.util.Map<String, Object> map, String key, Boolean defaultValue) {
        try {
            Object value = map.get(key);
            return value instanceof Boolean ? (Boolean) value : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
