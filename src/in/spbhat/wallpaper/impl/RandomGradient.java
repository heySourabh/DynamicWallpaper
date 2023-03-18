package in.spbhat.wallpaper.impl;

import in.spbhat.wallpaper.WallpaperGenerator;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class RandomGradient implements WallpaperGenerator {
    record Colors(Color[] collection) {
    }

    record Point(int x, int y) {
        double distance(Point other) {
            double dx = this.x - other.x;
            double dy = this.y - other.y;
            return Math.sqrt(dx * dx + dy * dy);
        }
    }

    Colors[] colorsArray = {
            new Colors(new Color[]{Color.decode("0xbfb190"), Color.decode("0xfff9f3")}),
            new Colors(new Color[]{Color.decode("0x3b8fa3"), Color.decode("0x563953")}),
            new Colors(new Color[]{Color.decode("0xa3483b"), Color.decode("0x3d4f66")}),
            new Colors(new Color[]{Color.decode("0x6f493f"), Color.decode("0x313e2f")}),
    };

    @Override
    public BufferedImage createWallpaper(int width, int height) {
        Random rnd = new Random();
        return rnd.nextDouble() < 0.5
                ? linearGradient(width, height)
                : radialGradient(width, height);
    }

    private BufferedImage linearGradient(int width, int height) {
        Point lower_left = new Point(0, height);
        Point lower_right = new Point(width, height);
        Point upper_left = new Point(0, 0);
        Point upper_right = new Point(width, 0);
        Point[] corners = {
                lower_left, lower_right, upper_left, upper_right
        };
        Collections.shuffle(Arrays.asList(corners));
        Point corner1 = corners[0];
        Point corner2 = corners[1];
        Random rnd = new Random();
        Colors colors = colorsArray[rnd.nextInt(colorsArray.length)];
        Collections.shuffle(Arrays.asList(colors.collection));
        Color color1 = colors.collection[0];
        Color color2 = colors.collection[1];
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setPaint(new GradientPaint(corner1.x, corner1.y, color1, corner2.x, corner2.y, color2));
        g.fillRect(0, 0, width, height);
        return image;
    }

    private BufferedImage radialGradient(int width, int height) {
        Point lower_left = new Point(0, height);
        Point lower_right = new Point(width, height);
        Point upper_left = new Point(0, 0);
        Point upper_right = new Point(width, 0);
        Point[] corners = {
                lower_left, lower_right, upper_left, upper_right
        };
        Random rnd = new Random();
        Point center = new Point(rnd.nextInt(width), rnd.nextInt(height));
        double radius = 0.0;
        for (Point corner : corners) {
            radius = Math.max(radius, center.distance(corner));
        }
        Colors colors = colorsArray[rnd.nextInt(colorsArray.length)];
        Collections.shuffle(Arrays.asList(colors.collection));
        Color color1 = colors.collection[0];
        Color color2 = colors.collection[1];
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setPaint(new RadialGradientPaint((float) center.x, (float) center.y, (float) radius, new float[]{0, 1}, new Color[]{color1, color2}));
        g.fillRect(0, 0, width, height);
        return image;
    }
}
