package cn.timer.ultra.values;

import cn.timer.ultra.values.runnables.NumbersChangeRunnable;

import java.util.Objects;

public class Numbers<V extends Number> extends Value<V> {
    private V min;
    private V max;
    private NumbersChangeRunnable runnable = null;

    public Numbers(String name, V minimum, V maximum, V value) {
        super(name);
        if (minimum.doubleValue() > maximum.doubleValue()) {
            minimum = maximum;
        }
        this.min = minimum;
        this.max = maximum;
        this.setValue(value);
    }

    public Numbers(String name, V minimum, V maximum, V value, NumbersChangeRunnable onChanged) {
        super(name);
        if (minimum.doubleValue() > maximum.doubleValue()) {
            minimum = maximum;
        }
        this.min = minimum;
        this.max = maximum;
        this.runnable = onChanged;
        this.setValue(value);
    }

    @Override
    public void setValue(V value) {
        if (!Objects.equals(value, this.value)) {
            if (runnable != null)
                runnable.run(this.floatValue(), Float.parseFloat(value.toString()));
            this.value = value;
        }
    }

    public void setMin(V min) {
        this.min = min;
    }

    public void setMax(V max) {
        this.max = max;
    }

    public V getMin() {
        return min;
    }

    public V getMax() {
        return max;
    }

    public float floatValue() {
        if (value == null) return 0;
        if (value instanceof Float) {
            return Float.parseFloat(String.valueOf(value));
        } else if (value instanceof Integer)
            return Integer.parseInt(String.valueOf(value));
        return value.floatValue();
    }

    public float floatMax() {
        if (max == null) return 0;
        if (max instanceof Float) {
            return Float.parseFloat(String.valueOf(max));
        } else if (max instanceof Integer)
            return Integer.parseInt(String.valueOf(max));
        return max.floatValue();
    }

    public float floatMin() {
        if (min == null) return 0;
        if (min instanceof Float) {
            return Float.parseFloat(String.valueOf(min));
        } else if (min instanceof Integer)
            return Integer.parseInt(String.valueOf(min));
        return min.floatValue();
    }

    public int intValue() {
        if (value == null) return 0;
        if (value instanceof Float) {
            return (int) Float.parseFloat(String.valueOf(value));
        } else if (value instanceof Integer)
            return Integer.parseInt(String.valueOf(value));
        return (int) value.floatValue();
    }

    public int intMax() {
        if (max == null) return 0;
        if (max instanceof Float) {
            return (int) Float.parseFloat(String.valueOf(max));
        } else if (max instanceof Integer)
            return Integer.parseInt(String.valueOf(max));
        return (int) max.floatValue();
    }

    public int intMin() {
        if (min == null) return 0;
        if (min instanceof Float) {
            return (int) Float.parseFloat(String.valueOf(min));
        } else if (min instanceof Integer)
            return Integer.parseInt(String.valueOf(min));
        return (int) min.floatValue();
    }
}