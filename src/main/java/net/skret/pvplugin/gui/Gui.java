package net.skret.pvplugin.gui;

import lombok.Getter;
import net.skret.pvplugin.managers.GuiManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class Gui {

    protected final GuiManager guiManager;
    @Getter
    protected Map<Integer, Action> actions;
    @Getter
    protected Inventory inventory;

    public Gui(GuiManager guiManager, int rows, String title) {
        this.guiManager = guiManager;
        actions = new HashMap<>();
        inventory = Bukkit.createInventory(null, rows * 9, title);
    }

    public void setItem(int slot, ItemStack item, Action action) {
        inventory.setItem(slot, item);
        if (action == null) return;
        actions.put(slot, action);
    }

    public void setItem(int slot, ItemStack item) {
        setItem(slot, item, null);
    }

    public interface Action {
        void click(Player player);
    }

    public void open(Player player) {
        player.openInventory(inventory);
        guiManager.getOpenInventories().put(player.getUniqueId(), this);
    }

}
