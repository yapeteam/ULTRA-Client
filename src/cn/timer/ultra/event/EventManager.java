package cn.timer.ultra.event;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventManager {//用于管理events
    private final ConcurrentHashMap<Class<? extends Event>, List<Handler>> events = new ConcurrentHashMap<>();
    private final MethodHandles.Lookup lookup = MethodHandles.lookup();
    public static EventManager instance = new EventManager();

    public <E extends Event> E call(E event) {//响应
        List<Handler> events = this.events.get(event.getClass());
        if (events != null && !events.isEmpty()) {
            for (Handler handler : events) {
                try {
                    handler.handler.invokeExact(handler.parent, event);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
        return event;
    }

    public void register(Object... objects) {//用于注册所需要响应的类/Module，目前我们只需要激活Modules和本类
        for (Object object : objects) {
            for (Method method : object.getClass().getDeclaredMethods()) {
                if (method.getParameterCount() == 1 && method.isAnnotationPresent(EventTarget.class)) {
                    Class<?> eventClass = method.getParameterTypes()[0];
                    if (!this.events.containsKey(eventClass)) {
                        this.events.put((Class<? extends Event>)eventClass, new CopyOnWriteArrayList<>());
                    }
                    this.events.get(eventClass).add(new Handler(method, object, method.getDeclaredAnnotation(EventTarget.class).priority()));
                    this.events.get(eventClass).sort(Comparator.comparingInt(e -> e.priority));
                }
            }
        }
    }

    public void unregister(Object... objects) {//用于取消注册所需要响应的类/Module，目前我们只需要激活Modules和本类
        for (Object object : objects) {
            for (List<Handler> events : this.events.values()) {
                for (Handler handler : events) {
                    if (handler.parent != object) continue;
                    events.remove(handler);
                }
            }
        }
    }

    private static class Handler {//用于注册
        private MethodHandle handler;
        private final Object parent;
        private final byte priority;

        public Handler(Method method, Object parent, byte priority) {
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            MethodHandle methodHandle = null;
            try {
                methodHandle = EventManager.instance.lookup.unreflect(method);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (methodHandle != null) {
                this.handler = methodHandle.asType(methodHandle.type().changeParameterType(0, Object.class).changeParameterType(1, Event.class));
            }
            this.parent = parent;
            this.priority = priority;
        }
    }
}
