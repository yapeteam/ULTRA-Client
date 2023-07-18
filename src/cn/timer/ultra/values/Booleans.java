package cn.timer.ultra.values;

import cn.timer.ultra.values.runnables.BooleansChangeRunnable;

public class Booleans extends Value<Boolean> {
    private BooleansChangeRunnable runnable = null;

    public Booleans(String name, Boolean value, BooleansChangeRunnable runnable) {
        super(name);
        this.runnable = runnable;
        this.setValue(value);
    }

    public Booleans(String name, Boolean value) {
        super(name);
        this.setValue(value);
    }

    @Override
    public void setValue(Boolean value) {
        if (this.value != value) {
            if (runnable != null && this.value != null)
                runnable.run(this.value, value);
            this.value = value;
        }
    }
}
