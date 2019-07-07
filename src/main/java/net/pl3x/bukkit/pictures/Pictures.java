package net.pl3x.bukkit.pictures;

import net.pl3x.bukkit.pictures.command.CmdPictures;
import net.pl3x.bukkit.pictures.configuration.Config;
import net.pl3x.bukkit.pictures.configuration.Lang;
import net.pl3x.bukkit.pictures.picture.PictureManager;
import net.pl3x.bukkit.pictures.listener.BukkitListener;
import org.bukkit.plugin.java.JavaPlugin;

public class Pictures extends JavaPlugin {
    private static Pictures instance;

    public Pictures() {
        instance = this;
    }

    public void onEnable() {
        Config.reload(this);
        Lang.reload(this);

        PictureManager.INSTANCE.loadPictures();

        getServer().getPluginManager().registerEvents(new BukkitListener(), this);

        getCommand("pictures").setExecutor(new CmdPictures(this));
    }

    public static Pictures getInstance() {
        return instance;
    }
}
