import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class RayTracerJonas {

    public static void main(String[] args) {
        int resX = 1024;
        int resY = 768;

        int[] pixels = new int[resX * resY];

        MemoryImageSource mis = new MemoryImageSource(resX, resY, new DirectColorModel(24, 0xff0000,
                0xff00, 0xff), pixels, 0, resX);
        mis.setAnimated(true);
        Image image = Toolkit.getDefaultToolkit().createImage(mis);
        JFrame frame = new JFrame("");
        frame.add(new JLabel(new ImageIcon(image)));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);


        Vec3 spherePos = new Vec3(0.0, 0.0, 0.0);
        double sRad = 0.5;
        int sphereColor = 0x7B0323;
        Sphere sphere = new Sphere(spherePos, sRad, sphereColor);

        Vec3 spherePos2 = new Vec3(-1.0, 0.0, 1.0);
        double sRad2 = 0.2;
        int sphereColor2 = 0x316729;
        Sphere sphere2 = new Sphere(spherePos2, sRad2, sphereColor2);

        Vec3 spherePos3 = new Vec3(1.0, 2.0, -1.0);
        double sRad3 = 0.5;
        int sphereColor3 = 0xFFFFFF;
        Sphere sphere3 = new Sphere(spherePos3, sRad3, sphereColor3);

        List<Sphere> spheres = new ArrayList<>();
        spheres.add(sphere);
        spheres.add(sphere2);
        spheres.add(sphere3);


        Vec3 camPos = new Vec3(0.0, 0.0, 3.0);
        Camera camera = new Camera(camPos, (double) resX / resY);

        Vec3 lightPos1 = new Vec3(5.0, 0.0, 10.0);
        Light light1 = new Light(lightPos1, 0xFFFFFF, 1.0);

        Vec3 lightPos2 = new Vec3(0.0, 2.0, 10.0);
        Light light2 = new Light(lightPos2, 0xFF0000, 1.0);

        // Add them to a list
        List<Light> lights = new ArrayList<>();
        lights.add(light1);
        lights.add(light2);

        for (int y = 0; y < resY; ++y) {
            for (int x = 0; x < resX; ++x) {
                Vec3 ur = camera.getRayDirection(x, y, resX, resY);
                int pixelColor = getColorForPixel(camPos, ur, spheres, lights);
                pixels[y * resX + x] = pixelColor;
            }
        }
        mis.newPixels();
    }



    private static int getColorForPixel(Vec3 origin, Vec3 direction, List<Sphere> spheres, List<Light> lights) {
        int color = 0x112231; // Default background color

        for (Sphere sphere : spheres) {
            Vec3 toSphere = sphere.centerPoint.subtract(origin);
            double a = direction.dot(direction);
            double b = 2.0 * direction.dot(toSphere);
            double c = toSphere.dot(toSphere) - sphere.radius * sphere.radius;
            double discriminant = b * b - 4 * a * c;

            if (discriminant >= 0) {
                double sqrtDiscriminant = Math.sqrt(discriminant);
                double root1 = (-b - sqrtDiscriminant) / (2.0 * a);
                double root2 = (-b + sqrtDiscriminant) / (2.0 * a);
                double s = root1 < 0 ? root2 : root1;

                Vec3 hitPoint = origin.add(direction.scale(s));
                Vec3 normal = hitPoint.subtract(sphere.centerPoint).normalize();

                for (Light light : lights) {
                    Vec3 lightDir = light.position.subtract(hitPoint).normalize();
                    double diffuse = Math.max(normal.dot(lightDir), 0) * light.intensity;

                    int red = (int) ((sphere.color >> 16 & 0xFF) * diffuse);
                    int green = (int) ((sphere.color >> 8 & 0xFF) * diffuse);
                    int blue = (int) ((sphere.color & 0xFF) * diffuse);
                    color += (red << 16) + (green << 8) + blue; // Add color contribution from each light
                }
            }


        }
        return color;
    }
}