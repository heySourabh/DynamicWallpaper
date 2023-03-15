import in.spbhat.wallpaper.WallpaperGenerator;
import in.spbhat.wallpaper.impl.MandelbrotSet;
import in.spbhat.wallpaper.impl.RandomGradient;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.locks.LockSupport;

import static java.time.Duration.ofMinutes;

public class Main {
    final static WallpaperGenerator[] generators = {new RandomGradient(), new MandelbrotSet(),};

    final static Random rnd = new Random();

    public static void main(String[] args) throws IOException {
        boolean running = true;
        while (running) {
            System.out.println("Changing background...");
            setWallpaper(generators[rnd.nextInt(generators.length)]);
            sleepFor(ofMinutes(1));
        }
    }

    private static void sleepFor(Duration duration) {
        LockSupport.parkNanos(duration.toNanos());
    }

    private static void setWallpaper(WallpaperGenerator wallpaperGenerator) throws IOException {
        Properties myEnv = new Properties();
        try (var envFile = Main.class.getResourceAsStream("in/spbhat/.env")) {
            myEnv.load(envFile);
        }

        String wallpaperFolderPath = myEnv.getProperty("WALL_PAPER_FOLDER");
        File wallpaperFile = new File(wallpaperFolderPath, "wallpaper.png");
        var wallpaper = wallpaperGenerator.createWallpaper(1920, 1200);
        ImageIO.write(wallpaper, "PNG", wallpaperFile);
    }
}
