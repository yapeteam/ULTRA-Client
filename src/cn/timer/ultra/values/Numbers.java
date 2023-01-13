package cn.timer.ultra.values;

public class Numbers<V extends Number> extends Value<V> {
    private V min, max, distance;

    public Numbers(String name, V minimum, V maximum, V distance, V value) {
        super(name);
        if (minimum.doubleValue() > maximum.doubleValue()) {
            minimum = maximum;
        }
        this.min = minimum;
        this.max = maximum;
        this.distance = distance;
        this.setValue(value);
    }

    public V getMin() {
        return min;
    }
    public float getMinF(){
        return Float.parseFloat(String.valueOf(min));
    }

    public V getMax() {
        return max;
    }
    public float getMaxF() {
        return Float.parseFloat(String.valueOf(max));
    }
    public float getValueF(){
        return Float.parseFloat(String.valueOf(value));
    }

    public void setMin(V min) {
        this.min = min;
    }

    public void setMax(V max) {
        this.max = max;
    }

    public void setDistance(V distance) {
        this.distance = distance;
    }

    public V getDistance() {
        return distance;
    }
}
