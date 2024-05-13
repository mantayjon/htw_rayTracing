public class Camera {
    Vec3 position;
    double aspectRatioX;

    public Camera(Vec3 position, double aspectRatioX) {
        this.position = position;
        this.aspectRatioX = aspectRatioX;
    }
    public Vec3 getRayDirection(int x, int y, int resX, int resY) {
        double rNorm = (x / (double) resX) * 2.0 * aspectRatioX - aspectRatioX;
        double uNorm = (y / (double) resY) * 2.0 - 1.0;
        Vec3 rayDirection = new Vec3(rNorm, uNorm, 1.0);
        return rayDirection.normalize();
    }
}