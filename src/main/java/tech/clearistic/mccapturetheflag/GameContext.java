package tech.clearistic.mccapturetheflag;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import tech.clearistic.mccapturetheflag.config.PluginConfig;
import tech.clearistic.mccapturetheflag.config.Team;

import java.util.List;

public class GameContext {
    private PluginConfig config;
    private Scoreboard mainScoreboard;
    private Objective captureObjective;

    public Objective getCaptureObjective() {
        return captureObjective;
    }

    public void setCaptureObjective(Objective captureObjective) {
        this.captureObjective = captureObjective;
    }

    public PluginConfig getConfig() {
        return config;
    }

    public void setConfig(PluginConfig config) {
        this.config = config;
    }

    public Scoreboard getMainScoreboard() {
        return mainScoreboard;
    }

    public void setMainScoreboard(Scoreboard mainScoreboard) {
        this.mainScoreboard = mainScoreboard;
    }

    public void resetPrizeChest() {
        Location prizeChestLoc = this.config.getPrizeChest();
        Block currBlock = prizeChestLoc.getWorld().getBlockAt(prizeChestLoc);

        if (currBlock.getType() != Material.CHEST) {
            Bukkit.getLogger().info("Creating prize chest");
            currBlock.setType(Material.CHEST);
        }

        Chest prizeChest = (Chest) currBlock.getState();
        Inventory inv = prizeChest.getBlockInventory();
        Material prize = this.config.getPrizeType();
        if (!inv.contains(prize)) {
            Bukkit.getLogger().info("Adding prize to chest");
            ItemStack stack = new ItemStack(prize);
            inv.addItem(stack);
        }
    }

    public void incrementScoreFor(String teamName) {
        Score score = this.captureObjective.getScore(teamName);
        score.setScore(score.getScore() + 1);
    }

    public Team getTeamFor(String playerName) {
        List<Team> teams = this.config.getTeams()
                .stream()
                .filter(team -> team.includesPlayer(playerName))
                .toList();

        if (teams.size() == 0) {
            return null;
        }

        return teams.get(0);
    }
}
