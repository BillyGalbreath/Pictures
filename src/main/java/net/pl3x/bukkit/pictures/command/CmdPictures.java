package net.pl3x.bukkit.pictures.command;

import net.pl3x.bukkit.pictures.Pictures;
import net.pl3x.bukkit.pictures.configuration.Config;
import net.pl3x.bukkit.pictures.configuration.Lang;
import net.pl3x.bukkit.pictures.picture.Picture;
import net.pl3x.bukkit.pictures.picture.PictureManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CmdPictures implements TabExecutor {
    private final Pictures plugin;

    public CmdPictures(Pictures plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && sender.hasPermission("command.pictures")) {
            return Stream.of("reload", "create")
                    .filter(arg -> arg.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("command.pictures")) {
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        if (args.length == 0) {
            return false; // show usage
        }

        if (args[0].equalsIgnoreCase("reload")) {
            Config.reload(plugin);
            Lang.reload(plugin);

            Lang.send(sender, "&d" + plugin.getName() + " v" + plugin.getDescription().getVersion() + " reloaded");
            return true;
        }

        if (args[0].equalsIgnoreCase("create") && args.length > 1) {
            Image image = PictureManager.INSTANCE.downloadImage(args[1]);
            if (image == null) {
                Lang.send(sender, Lang.ERROR);
                return true;
            }

            MapView mapView = Bukkit.createMap(Bukkit.getWorlds().get(0));
            Picture picture = new Picture(image, mapView);

            PictureManager.INSTANCE.addPicture(picture);

            if (sender instanceof Player) {
                ItemStack map = new ItemStack(Material.FILLED_MAP);
                MapMeta meta = (MapMeta) map.getItemMeta();
                meta.setMapView(picture.getMapView());
                map.setItemMeta(meta);

                Player player = ((Player) sender);
                player.getInventory().addItem(map).forEach((index, item) -> {
                    Item drop = player.getWorld().dropItem(player.getLocation(), item);
                    drop.setPickupDelay(0);
                    drop.setOwner(player.getUniqueId());
                });
            }

            Lang.send(sender, Lang.IMAGE_CREATED);
            return true;
        }

        return false;
    }
}
