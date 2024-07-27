package me.lorenzo0111.rocketbans.managers;

import me.lorenzo0111.rocketbans.RocketBans;
import me.lorenzo0111.rocketbans.data.Mute;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MuteManager implements Listener {
    private final RocketBans plugin;
    private final Map<UUID, Mute> activeMutes = new HashMap<>();

    public MuteManager(RocketBans plugin) {
        this.plugin = plugin;
    }

    public void reload() {
        plugin.getDatabase().getActiveMutes().thenAccept(mutes -> {
            for (Mute mute : mutes) {
                activeMutes.put(mute.uuid(), mute);
            }
        });
    }

    public void addMute(Mute mute) {
        activeMutes.put(mute.uuid(), mute);
    }

    public void removeMute(Mute mute) {
        if (!activeMutes.containsKey(mute.uuid())) return;
        if (activeMutes.get(mute.uuid()).id() != mute.id()) return;

        activeMutes.remove(mute.uuid());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (!activeMutes.containsKey(event.getPlayer().getUniqueId())) return;

        event.setCancelled(true);
        event.getPlayer().sendMessage(plugin.getPrefixed("mute.deny"));
    }
}