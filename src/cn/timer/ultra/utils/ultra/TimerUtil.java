package cn.timer.ultra.utils.ultra;

public final class TimerUtil {
    private long time;

    public TimerUtil() {
        this.time = System.nanoTime() / 1000000L;
    }

    public boolean hasTimeElapsed(final long time, final boolean reset) {
        if (this.time() >= time) {
            if (reset) {
                this.reset();
            }
            return true;
        }
        return false;
    }

    public boolean delay(final float milliSec) {
        return this.time() >= milliSec;
    }

    public long time() {
        return System.nanoTime() / 1000000L - this.time;
    }

    public void reset() {
        this.time = System.nanoTime() / 1000000L;
    }

}
