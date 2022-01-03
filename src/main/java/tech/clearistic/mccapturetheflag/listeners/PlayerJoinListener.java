package tech.clearistic.mccapturetheflag.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import tech.clearistic.mccapturetheflag.GameContext;
import tech.clearistic.mccapturetheflag.config.Team;

public class PlayerJoinListener implements Listener {

    private final GameContext context;

    public PlayerJoinListener(GameContext context) {
        this.context = context;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();

        Team team = this.context.getTeamFor(playerName);
        if (team == null) {
            Bukkit.getLogger().info("Player " + playerName + " is not on any team, ignoring");
            return;
        }

        player.setGameMode(context.getConfig().getPlayerMode());

        org.bukkit.scoreboard.Team scoreboardTeam = this.context.getMainScoreboard().getTeam(team.getName());
        if (!scoreboardTeam.hasPlayer(player)) {
            Bukkit.getLogger().info("Adding player " + playerName + " to team " + scoreboardTeam.getName());
            scoreboardTeam.addEntity(player);
        }

        // teleport player to spawn location
        player.teleport(team.getSpawn());
    }
}
