package org.example.ataraxiawarmup.player;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.example.ataraxiawarmup.Main;

public class PlayerChatListener implements Listener {

    private Main plugin;

    public PlayerChatListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerChats(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        CustomPlayer customPlayer = CustomPlayer.fromPlayer(player);
        Chat chat = customPlayer.getChat();
        switch (chat) {
            case PARTY:
                event.setCancelled(true);
                if (customPlayer.getParty() == null) {
                    player.sendMessage(ChatColor.RED + "You are not in a party and are now in the ALL chat.");
                    customPlayer.setChat(Chat.ALL);
                } else {
                    for (CustomPlayer customPlayer1 : customPlayer.getParty().getAllPlayers()) {
                        customPlayer1.getPlayer().sendMessage(chat.getPrefix() + ChatColor.WHITE + player.getName() + ": " + ChatColor.stripColor(event.getMessage()));
                    }
                }
                break;
            case INPUT:
                event.setCancelled(true);
                customPlayer.setRecentInput(event.getMessage());
                break;
        }
    }

}
