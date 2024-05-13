import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;

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

        Vec3 camPos = new Vec3(0.0, 0.0, 3.0);

        Vec3 cameraToSphere = sphere.centerPoint.subtract(camPos);

        double aspectRatio = (double) resX / resY;


        for (int y = 0; y < resY; ++y) {
            double uNorm = (y / (double)resY) * 2.0 - 1.0;

            for (int x = 0; x < resX; ++x) {
                double rNorm = (x / (double) resX) * 2.0 * aspectRatio - aspectRatio;

                Vec3 ur = new Vec3(rNorm, uNorm, 1.0); // Assume camera looks towards z=1
                ur = ur.normalize(); // Normalize to make it a direction vector

                double a = ur.dot(ur); // Always 1, since ur is normalized
                double b = 2.0 * ur.dot(cameraToSphere);
                double c = cameraToSphere.dot(cameraToSphere) - sRad * sRad;

                double discriminant = b * b - 4 * a * c;

                if (discriminant >= 0) {
                    double root1 = (-b + Math.sqrt(discriminant)) / (2.0 * a);
                    double root2 = (-b - Math.sqrt(discriminant)) / (2.0 * a);
                    double s = Math.min(root1, root2);
                    if (s > 0) {
                        pixels[y * resX + x] = sphereColor; // Sphere color if hit
                    } else {
                        pixels[y * resX + x] = 0x112231; // Default color if no hit because of negative s
                    }
                } else {
                    pixels[y * resX + x] = 0x112231; // Default color if no hit
                }
            }
        }
        mis.newPixels();

    }

}