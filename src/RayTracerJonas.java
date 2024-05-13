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
        Camera camera = new Camera(camPos, (double) resX / resY);
        Light light = new Light(new Vec3(5.0, 5.0, 10.0), 0xFFFFFF, 1.0);

        for (int y = 0; y < resY; ++y) {
            for (int x = 0; x < resX; ++x) {
                Vec3 ur = camera.getRayDirection(x, y, resX, resY);
                Vec3 cameraToSphere = sphere.centerPoint.subtract(camera.position);
                double a = ur.dot(ur);
                double b = 2.0 * ur.dot(cameraToSphere);
                double c = cameraToSphere.dot(cameraToSphere) - sRad * sRad;
                double discriminant = b * b - 4 * a * c;

                if (discriminant >= 0) {
                    double root1 = (-b + Math.sqrt(discriminant)) / (2.0 * a);
                    double root2 = (-b - Math.sqrt(discriminant)) / (2.0 * a);
                    double s = Math.min(root1, root2);
                    if (s > 0) {
                        Vec3 hitPoint = camera.position.add(ur.scale(s));
                        Vec3 normal = hitPoint.subtract(spherePos).normalize();
                        Vec3 lightDir = light.position.subtract(hitPoint).normalize();
                        double diffuse = Math.max(normal.dot(lightDir), 0) * light.intensity;
                        int red = (int) ((sphereColor >> 16 & 0xFF) * diffuse);
                        int green = (int) ((sphereColor >> 8 & 0xFF) * diffuse);
                        int blue = (int) ((sphereColor & 0xFF) * diffuse);
                        int rgb = (red << 16) + (green << 8) + blue;
                        pixels[y * resX + x] = rgb;
                    } else {
                        pixels[y * resX + x] = 0x112231; // Default color if no hit because of negative s
                    }
                } else {
                    pixels[y * resX + x] = 0x112231; //
            }
        }
        mis.newPixels();


    }

}
}