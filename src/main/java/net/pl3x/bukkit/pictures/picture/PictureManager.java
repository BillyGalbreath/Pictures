package net.pl3x.bukkit.pictures.picture;

import net.pl3x.bukkit.pictures.Pictures;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.map.MapView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class PictureManager {
    public static PictureManager INSTANCE = new PictureManager();

    private Set<Picture> pictures = new HashSet<>();

    public void addPicture(Picture picture) {
        pictures.add(picture);
    }

    public void sendAllMaps(Player player) {
        pictures.forEach(picture -> player.sendMap(picture.getMapView()));
    }

    public java.awt.Image downloadImage(String url) {
        try {
            BufferedImage image = ImageIO.read(new URL(url));
            return image == null ? null : image.getScaledInstance(128, 128, 1);
        } catch (Exception ignore) {
        }
        return null;
    }

    public java.awt.Image loadImage(File file) {
        try {
            BufferedImage image = ImageIO.read(file);
            return image == null ? null : image.getScaledInstance(128, 128, 1);
        } catch (Exception ignore) {
        }
        return null;
    }

    public boolean saveImage(Image image, int id) {
        try {
            File dir = new File(Pictures.getInstance().getDataFolder(), "images");
            if (!dir.exists() && !dir.mkdirs()) {
                return false;
            }
            ImageIO.write((RenderedImage) image, "png", new File(dir, id + ".png"));
            return true;
        } catch (Exception ignore) {
        }
        return false;
    }

    public void loadPictures() {
        pictures.clear();
        File dir = new File(Pictures.getInstance().getDataFolder(), "images");
        if (!dir.exists()) {
            dir.mkdirs();
            return;
        }
        if (!dir.isDirectory()) {
            return;
        }
        File[] files = dir.listFiles((dir1, name) -> name.endsWith(".png"));
        if (files == null) {
            return;
        }
        int count = 0;
        for (File file : files) {
            try {
                java.awt.Image image = loadImage(file);
                if (image == null) {
                    continue;
                }
                MapView mapView = Bukkit.getMap(Short.parseShort(file.getName().split(".png")[0]));
                if (mapView == null) {
                    continue;
                }
                addPicture(new Picture(image, mapView));
                count++;
            } catch (Exception ignore) {
            }
        }
        Pictures.getInstance().getLogger().info("Loaded " + count + " images from disk");
    }
}
