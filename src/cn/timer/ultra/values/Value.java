package cn.timer.ultra.values;

public class Value<V> {
    public V value;
    private final String name;
    public float optionAnim;
    public float optionAnimNow;

    public Value(String name) {
        this.name = name;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }
}
