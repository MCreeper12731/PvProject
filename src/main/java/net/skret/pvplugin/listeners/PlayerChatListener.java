package net.skret.pvplugin.listeners;

import lombok.RequiredArgsConstructor;
import net.skret.pvplugin.managers.PlayerManager;
import net.skret.pvplugin.utils.Color;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@RequiredArgsConstructor
public class PlayerChatListener implements Listener {

    private final PlayerManager playerManager;

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();

        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1);

        event.setMessage(Color.color("&" + playerManager.getStats(event.getPlayer().getUniqueId()).getChatColor() + event.getMessage()));

    }

}
