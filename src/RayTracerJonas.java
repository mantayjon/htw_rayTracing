import java.awt.*;
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
        //spheres.add(sphere2);
        //spheres.add(sphere3);


        Vec3 camPos = new Vec3(0.0, 0.0, 3.0);
        Camera camera = new Camera(camPos, (double) resX / resY);

        Vec3 lightPos1 = new Vec3(5.0, 0.0, 10.0);
        Light light1 = new Light(lightPos1, 0xFFFFFF, 1.0);

        Vec3 lightPos2 = new Vec3(-4.0, 2.0, 20.0);
        Light light2 = new Light(lightPos2, 0xFF0000, 1.0);

        // Add them to a list
        List<Light> lights = new ArrayList<>();
        lights.add(light1);
       // lights.add(light2);

        for (int y = 0; y < resY; ++y) {
            for (int x = 0; x < resX; ++x) {
                Vec3 rayDirection = Camera.getRayDirection(x, y, resX, resY);
                int pixelColor = getColorForPixel(camPos, rayDirection, spheres, lights);
                pixels[y * resX + x] = pixelColor;
            }
        }
        mis.newPixels();
    }



    private static int getColorForPixel(Vec3 camPos, Vec3 direction, List<Sphere> spheres, List<Light> lights) {
        Sphere closestSphere = null;
        double minS = Double.POSITIVE_INFINITY;
        Vec3 hitPoint = null;
        Vec3 normal = null;

        int background = 0x000000;

        // Find the closest sphere and its hit point
        for (Sphere sphere : spheres) {
            Vec3 toSphere = sphere.centerPoint.subtract(camPos);
            double a = direction.dot(direction);
            double b = 2.0 * direction.dot(toSphere);
            double c = toSphere.dot(toSphere) - sphere.radius * sphere.radius;
            double discriminant = b * b - 4 * a * c;

            if (discriminant >= 0) {
                double t1 = (-b - Math.sqrt(discriminant)) / (2.0 * a);
                double t2 = (-b + Math.sqrt(discriminant)) / (2.0 * a);
                double s = Math.min(t1, t2) > 0 ? Math.min(t1, t2) : Math.max(t1, t2);

                if (s < minS && s > 0) {
                    minS = s;
                    closestSphere = sphere;
                    hitPoint = camPos.add(direction.scale(s));
                    normal = hitPoint.subtract(sphere.centerPoint).normalize();
                }
            }
        }

        if (closestSphere == null) {
            return background;  // Background color
        }

        double red = 0;
        double green = 0;
        double blue = 0;

        for (Light light : lights) {
            Vec3 lightDir = light.position.subtract(hitPoint).normalize();

            System.out.println("LightDir: " + lightDir.x + " " + lightDir.y + " " + lightDir.z);
            System.out.println("Light Position: " + light.position.x + " " + light.position.y + " " + light.position.z);
            System.out.println("Hit Point: " + hitPoint.x + " " + hitPoint.y + " " + hitPoint.z);
            System.out.println("Normal: " + normal.x + " " + normal.y + " " + normal.z);
            System.out.println("");

            double diffuse = Math.max(normal.dot(lightDir), 0) * light.intensity;

            System.out.println("Diffuse: " + diffuse);

            red += (closestSphere.color >> 16 & 0xFF) * diffuse;
            green += (closestSphere.color >> 8 & 0xFF) * diffuse;
            blue += (closestSphere.color & 0xFF) * diffuse;
        }

        red = Math.min(255, red);
        green = Math.min(255, green);
        blue = Math.min(255, blue);

        int color = ((int) red << 16) + ((int) green << 8) + ((int) blue);
        return color;
    }


}