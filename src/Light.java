public class Light {
    public Vec3 position;
    public int color;
    public double intensity;

    public Light(Vec3 position, int color, double intensity) {
        this.position = position;
        this.color = color;
        this.intensity = intensity;
    }

    public Vec3 getPosition() {
        return position;
    }

    public void setPosition(Vec3 position) {
        this.position = position;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public double getIntensity() {
        return intensity;
    }

    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }
}