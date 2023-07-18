package cn.timer.ultra.utils.jello;

public class Circle {

    public double x;
    public double y;
    public double topRadius;
    public double speed;

    public int keyCode;
    private final CircleManager.KEY_TYPE type;

    public double progress;
    public double lastProgress;
    public boolean complete;


    public Circle(double x, double y, double rad, double speed, int key, CircleManager.KEY_TYPE type) {
        this.x = x;
        this.y = y;
        topRadius = rad;
        this.speed = speed;
        keyCode = key;
        this.type = type;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getTopRadius() {
        return topRadius;
    }

    public void setTopRadius(double topRadius) {
        this.topRadius = topRadius;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public CircleManager.KEY_TYPE getType() {
        return type;
    }
}
