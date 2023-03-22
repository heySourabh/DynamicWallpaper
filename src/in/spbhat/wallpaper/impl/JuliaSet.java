package in.spbhat.wallpaper.impl;

import in.spbhat.wallpaper.WallpaperGenerator;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

import static java.lang.Math.*;

public class JuliaSet implements WallpaperGenerator {
    @Override
    public void createWallpaper(BufferedImage wallpaper) {
        int width = wallpaper.getWidth();
        int height = wallpaper.getHeight();
        double heightToWidthRatio = 1.0 * height / width;
        double realRange = 3.0;
        double realMin = -realRange / 2;
        double imaginaryRange = realRange * heightToWidthRatio;
        double imaginaryMin = -imaginaryRange / 2;
        double delta_re = realRange / (width - 1);
        double delta_im = imaginaryRange / (height - 1);
        Random rnd = new Random();
        double hueStart = rnd.nextDouble(0, 0.3);
        double hueEnd = rnd.nextDouble(0.4, 1.0);
        Complex c = nextJuliaSetConstant(rnd);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double re = realMin + x * delta_re;
                double im = imaginaryMin + (height - y - 1) * delta_im;
                Complex z = new Complex(re, im);
                int rgb = getRGB(z, c, hueStart, hueEnd);
                wallpaper.setRGB(x, y, rgb);
            }
        }
    }

    private static Complex nextJuliaSetConstant(Random rnd) {
        double a = rnd.nextDouble() * 2 * PI;
        return new Complex(0.7885 * cos(a), 0.7885 * sin(a));
    }

    private static int getRGB(Complex z, Complex c, double hueStart, double hueEnd) {
        double maxMagSqr = 20 * 20;
        int maxIterations = 100;
        int iteration = 0;
        for (; iteration < maxIterations; iteration++) {
            z = f(z, c);
            if (z.magSqr() > maxMagSqr) {
                break;
            }
        }
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