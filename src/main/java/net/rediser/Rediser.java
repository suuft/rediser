package net.rediser;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import net.rediser.annotation.PrimaryField;
import net.rediser.util.JedisUtil;
import net.rediser.util.JsonUtil;
import net.rediser.annotation.Redis;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@UtilityClass
public class Rediser {


    private Map<String, Redis> REDIS_REGISTERED_MAP = new HashMap<>();
    private Map<String, String> KEY_REGISTERED_MAP = new HashMap<>();

    public void registerClass(Class<? extends RObject> objectClass) {

        String className = objectClass.getName();
        Redis redis = objectClass.getDeclaredAnnotation(Redis.class);
        if (redis != null) {
            REDIS_REGISTERED_MAP.put(className, redis);

            Arrays.stream(objectClass.getDeclaredFields()).forEach(f -> {

                PrimaryField annotation = f.getAnnotation(PrimaryField.class);
                if (annotation != null) {
                    KEY_REGISTERED_MAP.put(className, f.getName());
                }

            });
            if (!KEY_REGISTERED_MAP.containsKey(className)) {
                err("failed to register class, @KeyField annotation not found.");
            }
            info("everything went well. The \"" + objectClass.getName() + "\" class is registered, with the " + redis.hostName() + ":" + redis.port() + " redis, and the \"" + KEY_REGISTERED_MAP.get(className) + "\" key field.");
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> instanceClass, Object key) {
        if (!REDIS_REGISTERED_MAP.containsKey(instanceClass.getName())) return null;
        String json = JedisUtil.get(REDIS_REGISTERED_MAP.get(instanceClass.getName()), key.toString());
        if (json != null) {
            return JsonUtil.from(json, instanceClass);
        }
        return null;

    }

    @SuppressWarnings("unchecked")
    public <T> Collection<T> getAll(Class<T> instanceClass) {
        if (!REDIS_REGISTERED_MAP.containsKey(instanceClass.getName())) return new ArrayList<>();
        Collection<String> jsonCollection = JedisUtil.getAll(REDIS_REGISTERED_MAP.get(instanceClass.getName())).values();

        Collection<T> collection = new ArrayList<>();
        for (String json : jsonCollection) {
            if (json != null) {
                collection.add(JsonUtil.from(json, instanceClass));
            }
        }

        return collection;
    }

    @SuppressWarnings("unchecked")
    public <T> Collection<T> getAllByFilter(Class<T> instanceClass, Predicate<T> filter) {
        return getAll(instanceClass).stream().filter(filter).collect(Collectors.toList());
    }

    public void remove(Class<? extends RObject> instanceClass, Object key) {
        if (!REDIS_REGISTERED_MAP.containsKey(instanceClass.getName())) return;
        JedisUtil.remove(REDIS_REGISTERED_MAP.get(instanceClass.getName()), key.toString());
    }

    public void put(Object obj) throws NoSuchFieldException, IllegalAccessException {
        String className = obj.getClass().getName();
        Redis redis = REDIS_REGISTERED_MAP.get(obj.getClass().getName());
        if (redis == null) return;
        Field field = obj.getClass().getDeclaredField(KEY_REGISTERED_MAP.get(className));
        field.setAccessible(true);
        Object value = field.get(obj);

        JedisUtil.set(redis, value.toString(), obj);
    }

    private void err(@NonNull String string) {
        System.out.println("[REDISER] :: Houston, we have a problem: " + string);
        System.out.println("[REDISER] :: We'll have to stop the ship.");
    }

    private void info(@NonNull String string) {
        System.out.println("[REDISER] :: Houston, we want to say: " + string);
    }
}
