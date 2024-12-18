package studio.dreamys.prometheus.util;

import java.lang.reflect.Field;

public class ReflectionUtils {
    public static void set(Object object, String field, Object value) {
        try {
            Field field_ = object.getClass().getDeclaredField(field);
            field_.setAccessible(true);
            field_.set(object, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.out.println("[Prometheus] Failed to set field " + field + " in " + object.getClass().getSimpleName() + " to " + value + ".");
            e.printStackTrace();
            System.out.println("[Prometheus] This is a bug, please report it to the developer.");
        }
    }
}
