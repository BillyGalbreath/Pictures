package net.pl3x.bukkit.pictures.listener;

import net.pl3x.bukkit.pictures.picture.PictureManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class BukkitListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        PictureManager.INSTANCE.sendAllMaps(event.getPlayer());
    }
}
