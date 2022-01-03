package tech.clearistic.mccapturetheflag;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;
import tech.clearistic.mccapturetheflag.config.PluginConfig;
import tech.clearistic.mccapturetheflag.config.Team;
import tech.clearistic.mccapturetheflag.listeners.InventoryClickListener;
import tech.clearistic.mccapturetheflag.listeners.InventoryMoveItemListener;
import tech.clearistic.mccapturetheflag.listeners.PlayerJoinListener;
import tech.clearistic.mccapturetheflag.listeners.PlayerRespawnListener;

import java.util.ArrayList;
import java.util.List;

public class CaptureTheFlag extends JavaPlugin {
    private GameContext context;

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onEnable() {
        super.onEnable();

        ConfigurationSerialization.registerClass(Location.class);
        ConfigurationSerialization.registerClass(Team.class, "Team");
        ConfigurationSerialization.registerClass(PluginConfig.class, "Config");

//        this.saveSampleConfig();

        this.context = new GameContext();

        FileConfiguration fileConfig = this.configure();
        this.context.setConfig((PluginConfig) fileConfig.get("config"));

        setupGameContext();

        Bukkit.getLogger().info(this.getName() + " is enabled");
    }

    private void setupGameContext() {
        setupScoreboard();
        setupTeams();
        setupChests();
        setupListeners();
    }

    private void setupChests() {
        this.context.getConfig().getTeams().forEach(t -> {
            Location chestLoc = t.getChest();

            World chestWorld = chestLoc.getWorld();
            Block currBlock = chestWorld.getBlockAt(chestLoc);

            if (currBlock.getType() != Material.CHEST) {
                Bukkit.getLogger().info("Creating goal chest for team " + t.getName());
                currBlock.setType(Material.CHEST);
            }
        });

        this.context.resetPrizeChest();
    }

    private void setupTeams() {
        this.context.getMainScoreboard().getTeams().forEach(org.bukkit.scoreboard.Team::unregister);

        this.context.getConfig().getTeams().forEach(t -> {
            org.bukkit.scoreboard.Team team = this.context.getMainScoreboard().getTeam(t.getName());
            if (team == null) {
                Bukkit.getLogger().info("Creating team " + t.getName());
                team = this.context.getMainScoreboard().registerNewTeam(t.getName());
            }
            team.color(t.getColor());
            team.setOption(org.bukkit.scoreboard.Team.Option.NAME_TAG_VISIBILITY, org.bukkit.scoreboard.Team.OptionStatus.FOR_OWN_TEAM);
        });
    }

    private void setupScoreboard() {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        this.context.setMainScoreboard(scoreboardManager.getMainScoreboard());

        this.context.getMainScoreboard().clearSlot(DisplaySlot.SIDEBAR);

        Objective captureObjective = this.context.getMainScoreboard().getObjective("Captures");
        if (captureObjective != null) {
            Bukkit.getLogger().info("Unregistering existing objective");
            captureObjective.unregister();
        }
        captureObjective = this.context.getMainScoreboard()
                .registerNewObjective("Captures", "dummy", Component.text("Captures"), RenderType.INTEGER);
        captureObjective.setDisplaySlot(DisplaySlot.SIDEBAR);

        this.context.setCaptureObjective(captureObjective);

        Objective finalCaptureObjective = captureObjective;
        context.getConfig().getTeams().forEach(t -> {
            Score score = finalCaptureObjective.getScore(t.getName());
            score.setScore(0);
        });
    }

    private void setupListeners() {
        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new PlayerJoinListener(this.context), this);
        pluginManager.registerEvents(new PlayerRespawnListener(this.context), this);
        pluginManager.registerEvents(new InventoryMoveItemListener(this.context), this);
        pluginManager.registerEvents(new InventoryClickListener(this.context), this);
    }

    private FileConfiguration configure() {
        // only saves if the file doesn't already exist
        this.saveDefaultConfig();

        return this.getConfig();
    }

    private void saveSampleConfig() {
        FileConfiguration config = this.getConfig();

        PluginConfig pluginConfig = new PluginConfig();

        pluginConfig.setAllowBeds(false);
        pluginConfig.setPrizeType(Material.DRAGON_HEAD);
        pluginConfig.setPlayerMode(GameMode.SURVIVAL);

        Team t1 = new Team();
        t1.setName("Team1");
        t1.setColor(NamedTextColor.RED);
        t1.setSpawn(new Location(Bukkit.getWorld("world"), 0, 0, 0));
        t1.setChest(new Location(Bukkit.getWorld("world"), 0, 0, 0));

        List<String> players = new ArrayList<>();
        players.add("Player1");
        players.add("Player2");

        t1.setPlayerNames(players);

        List<Team> teams = new ArrayList<>();
        teams.add(t1);

        pluginConfig.setTeams(teams);

        config.set("config", pluginConfig);

        this.saveConfig();
    }
}
