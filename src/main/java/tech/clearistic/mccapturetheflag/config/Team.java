package tech.clearistic.mccapturetheflag.config;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SerializableAs("Team")
public class Team implements ConfigurationSerializable {
    private String name;
    private NamedTextColor color;
    private Location spawn;
    private Location chest;
    private List<String> playerNames;

    public NamedTextColor getColor() {
        return color;
    }

    public void setColor(NamedTextColor color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getSpawn() {
        return spawn;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    public Location getChest() {
        return chest;
    }

    public void setChest(Location chest) {
        this.chest = chest;
    }

    public List<String> getPlayerNames() {
        return playerNames;
    }

    public void setPlayerNames(List<String> playerNames) {
        this.playerNames = playerNames;
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
        Map<String, Object> m = new HashMap<>();

        m.put("name", this.name);
        m.put("color", this.color.value());
        m.put("spawn", this.spawn);
        m.put("chest", this.chest);
        m.put("players", this.playerNames);

        return m;
    }

    public static Team deserialize(Map<String, Object> m) {
        Team t = new Team();

        t.name = m.get("name").toString();
        t.color = NamedTextColor.ofExact((Integer) m.get("color"));
        t.spawn = (Location) m.get("spawn");
        t.chest = (Location) m.get("chest");
        t.playerNames = (List<String>) m.get("players");

        return t;
    }

    public boolean includesPlayer(String playerName) {
        return this.playerNames.stream().anyMatch(s -> s.equalsIgnoreCase(playerName));
    }
}
