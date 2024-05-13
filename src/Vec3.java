public class Vec3 {
    double x, y, z;

    public Vec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3 add(Vec3 other) {
        return new Vec3(x + other.x, y + other.y, z + other.z);
    }

    public Vec3 subtract(Vec3 other) {
        return new Vec3(x - other.x, y - other.y, z - other.z);
    }

    public Vec3 scale(double s) {
        return new Vec3(x * s, y * s, z * s);
    }

    public Vec3 normalize() {
        double length = Math.sqrt(x * x + y * y + z * z);
        if (length == 0) return new Vec3(0, 0, 0); // handle zero vector
        return new Vec3(x / length, y / length, z / length);
    }

    public double dot(Vec3 other) {
        return x * other.x + y * other.y + z * other.z;
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public double distance(Vec3 other) {
        return subtract(other).length();
    }
}
