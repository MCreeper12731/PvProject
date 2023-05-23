package net.skret.pvplugin.listeners;

import lombok.RequiredArgsConstructor;
import net.luckperms.api.LuckPerms;
import net.skret.pvplugin.managers.DatabaseManager;
import net.skret.pvplugin.managers.PlayerManager;
import net.skret.pvplugin.utils.CusItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@RequiredArgsConstructor
public class PlayerJoinListener implements Listener {

    private final PlayerManager playerManager;
    private final DatabaseManager databaseManager;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        databaseManager.loadData(player.getUniqueId(), player.getDisplayName());

        playerManager.updateRank(player.getUniqueId());

        if (!player.hasPlayedBefore() || player.getName().equals("MCreeper12731")) {

            ItemStack bat = CusItem.create(Material.STICK, "Wooden Bat");
            ItemMeta batMeta = bat.getItemMeta();
            batMeta.addEnchant(Enchantment.DAMAGE_ALL, 2, true);
            batMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);
            bat.setItemMeta(batMeta);

            ItemStack pickaxe = new ItemStack(Material.WOODEN_PICKAXE);
            ItemMeta pickaxeMeta = pickaxe.getItemMeta();
            pickaxeMeta.addEnchant(Enchantment.DIG_SPEED, 1, true);
            pickaxe.setItemMeta(pickaxeMeta);

            player.getInventory().addItem(bat, pickaxe, new ItemStack(Material.LEATHER_HELMET), new ItemStack(Material.LEATHER_CHESTPLATE), new ItemStack(Material.LEATHER_LEGGINGS), new ItemStack(Material.LEATHER_BOOTS));

        }

    }

}
