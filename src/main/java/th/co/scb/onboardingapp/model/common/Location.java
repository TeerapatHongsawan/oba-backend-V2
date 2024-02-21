package th.co.scb.onboardingapp.model.common;

public class Location {

    private float x = 0;
    private float y = 0;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Location(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float offsetX(float x) {
        this.x = this.x + x;
        return this.x;
    }

    public float offsetY(float y) {
        this.y = this.y + y;
        return this.y;
    }
}
