public class Sphere {

    public Vec3 centerPoint;
    public double radius;
    public int color;

    public Sphere(Vec3 centerPoint, double radius, int color){
        this.centerPoint = centerPoint;
        this.radius = radius;
        this.color = color;
    }

    public Vec3 getCenterPoint() {
        return centerPoint;
    }

    public void setCenterPoint(Vec3 centerPoint) {
        this.centerPoint = centerPoint;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}