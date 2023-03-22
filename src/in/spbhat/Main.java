package in.spbhat;

import in.spbhat.wallpaper.WallpaperGenerator;
import in.spbhat.wallpaper.impl.JuliaSet;
import in.spbhat.wallpaper.impl.MandelbrotSet;
import in.spbhat.wallpaper.impl.RandomGradient;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.locks.LockSupport;

import static java.time.Duration.ofMinutes;

public class Main {
    private final static WallpaperGenerator[] generators = {
            new RandomGradient(),
            new MandelbrotSet(),
            new JuliaSet(),
    };
    private final static BufferedImage wallpaper = new BufferedImage(1920, 1200, BufferedImage.TYPE_INT_RGB);

    public static void main(String[] args) throws IOException {
        Random rnd = new Random();
        boolean running = true;
        while (running) {
            System.out.println("Changing background...");
            setWallpaper(generators[rnd.nextInt(generators.length)]);
            sleepFor(ofMinutes(2));
        }
    }

    private static void sleepFor(Duration duration) {
        LockSupport.parkNanos(duration.toNanos());
    }

    private static void setWallpaper(WallpaperGenerator wallpaperGenerator) throws IOException {
        Properties myEnv = new Properties();
        try (var envFile = Main.class.getResourceAsStream(".env")) {
            myEnv.load(envFile);
        }

        File wallpaperFolderPath = new File(myEnv.getProperty("WALL_PAPER_FOLDER"));
        File wallpaperFile = new File("background.jpg");
        wallpaperGenerator.createWallpaper(wallpaper);
        ImageIO.write(wallpaper, "JPEG", wallpaperFile);
        Files.move(wallpaperFile.toPath(),
                new File(wallpaperFolderPath, wallpaperFile.getName()).toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        wallpaper.flush();
    }
}
