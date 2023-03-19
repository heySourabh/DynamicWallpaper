package in.spbhat.wallpaper.impl;

import in.spbhat.wallpaper.WallpaperGenerator;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

import static java.lang.Math.sqrt;

public class MandelbrotSet implements WallpaperGenerator {
    private static final Location[] interestingLocations = {
            new Location(new Complex(-0.651, 0.492), 0.02),
            new Location(new Complex(-0.6, 0), 2.8),
            new Location(new Complex(-0.748, 0.1), 0.01),
    };

    @Override
    public void createWallpaper(BufferedImage wallpaper) {
        int width = wallpaper.getWidth();
        int height = wallpaper.getHeight();
        double heightToWidthRatio = 1.0 * height / width;
        Random rnd = new Random();
        Location interesting_location = interestingLocations[rnd.nextInt(interestingLocations.length)];
        Complex center = interesting_location.center();
        double realRange = interesting_location.realRange();

        double realMin = center.re() - realRange / 2;
        double imaginaryRange = realRange * heightToWidthRatio;
        double imaginaryMin = center.im() - imaginaryRange / 2;
        double delta_re = realRange / (width - 1);
        double delta_im = imaginaryRange / (height - 1);
        double hueStart = rnd.nextDouble(0, 0.3);
        double hueEnd = rnd.nextDouble(0.4, 1.0);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double re = realMin + x * delta_re;
                double im = imaginaryMin + (height - y - 1) * delta_im;
                int rgb = getRGB(new Complex(re, im), hueStart, hueEnd);
                wallpaper.setRGB(x, y, rgb);
            }
        }
    }

    private static int getRGB(Complex c, double hueStart, double hueEnd) {
        Complex z = new Complex(0, 0);
        double maxMagSqr = 20 * 20;
        int maxIterations = 100;
        int iteration = 0;
        for (; iteration < maxIterations; iteration++) {
            z = f(z, c);
            if (z.magSqr() > maxMagSqr) {
                break;
            }
        }
//        if (iteration != maxIterations)
//            System.out.println(iteration);
        double r = sqrt(1.0 * iteration / maxIterations);
        float h = (float) (3 * (hueEnd - hueStart) * r + hueStart);
        float s = (float) (1 - r);
        float b = (float) (1 - r);
        return Color.getHSBColor(h, s, b).getRGB();
    }

    private static Complex f(Complex z, Complex c) {
        return z.sqr().plus(c);
    }
}

record Location(Complex center, double realRange) {
}

record Complex(double re, double im) {
    public Complex plus(Complex other) {
        return new Complex(this.re + other.re, this.im + other.im);
    }

    public Complex times(Complex other) {
        double re1 = this.re;
        double im1 = this.im;
        double re2 = other.re;
        double im2 = other.im;
        double re = re1 * re2 - im1 * im2;
        double im = re1 * im2 + re2 * im1;
        return new Complex(re, im);
    }

    public Complex sqr() {
        return times(this);
    }

    public double magSqr() {
        return re * re + im * im;
    }
}