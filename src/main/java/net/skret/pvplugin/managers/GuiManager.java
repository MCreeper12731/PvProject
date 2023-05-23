package net.skret.pvplugin.managers;

import lombok.Getter;
import net.skret.pvplugin.gui.Gui;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GuiManager {

    @Getter
    private final Map<String, Gui> inventories;

    @Getter
    private final Map<UUID, Gui> openInventories;

    public GuiManager() {
        inventories = new HashMap<>();
        openInventories = new HashMap<>();
    }

    public Gui getGui(String key) {
        return inventories.get(key);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        Gui gui = openInventories.get(player.getUniqueId());
        if (gui == null) return;
        event.setCancelled(true);
        if (event.getClickedInventory() == null) return;
        if (event.getClickedInventory().getType() == InventoryType.PLAYER) return;
        Gui.Action action = gui.getActions().get(event.getSlot());
        if (action == null) return;
        action.click(player);
        player.closeInventory();
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        openInventories.remove(player.getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        openInventories.remove(player.getUniqueId());
    }

}
