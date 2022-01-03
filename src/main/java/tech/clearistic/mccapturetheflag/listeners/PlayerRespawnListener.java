package tech.clearistic.mccapturetheflag.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import tech.clearistic.mccapturetheflag.GameContext;
import tech.clearistic.mccapturetheflag.config.PluginConfig;
import tech.clearistic.mccapturetheflag.config.Team;

import java.util.List;

public class PlayerRespawnListener implements Listener {
    private final GameContext context;

    public PlayerRespawnListener(GameContext context) {
        this.context = context;
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();

        Team team = this.context.getTeamFor(playerName);
        if (team == null) {
            return;
        }

        event.setRespawnLocation(team.getSpawn());
        Bukkit.getLogger().info("Sending " + player + " to team spawn point");
    }
}
