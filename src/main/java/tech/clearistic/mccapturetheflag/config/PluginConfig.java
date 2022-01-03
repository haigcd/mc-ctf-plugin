package tech.clearistic.mccapturetheflag.config;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SerializableAs("Config")
public class PluginConfig implements ConfigurationSerializable {
    private boolean allowBeds;
    private Material prizeType;
    private List<Team> teams;
    private Location prizeChest;
    private GameMode playerMode;

    public Location getPrizeChest() {
        return prizeChest;
    }

    public void setPrizeChest(Location prizeChest) {
        this.prizeChest = prizeChest;
    }

    public boolean isAllowBeds() {
        return allowBeds;
    }

    public void setAllowBeds(boolean allowBeds) {
        this.allowBeds = allowBeds;
    }

    public Material getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(Material prizeType) {
        this.prizeType = prizeType;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public GameMode getPlayerMode() {
        return playerMode;
    }

    public void setPlayerMode(GameMode playerMode) {
        this.playerMode = playerMode;
    }

    /**
     * Creates a Map representation of this class.
     * <p>
     * This class must provide a method to restore this class, as defined in
     * the {@link ConfigurationSerializable} interface javadocs.
     *
     * @return Map containing the current state of this class
     */
    @Override
    public @NotNull Map<String, Object> serialize() {
        HashMap<String, Object> m = new HashMap<>();

        m.put("allowBeds", this.allowBeds);
        m.put("prize", this.prizeType.name());
        m.put("teams", this.teams);
        m.put("prizeChest", this.prizeChest);
        m.put("playerMode", this.playerMode.name());

        return m;
    }

    public static PluginConfig deserialize(Map<String, Object> m) {
        PluginConfig config = new PluginConfig();

        config.allowBeds = (boolean) m.get("allowBeds");
        config.prizeType = Material.getMaterial(m.get("prize").toString());
        config.teams = (List<Team>) m.get("teams");
        config.prizeChest = (Location) m.get("prizeChest");
        config.playerMode = GameMode.valueOf(m.get("playerMode").toString());

        return config;
    }
}
