package tech.clearistic.mccapturetheflag.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.clearistic.mccapturetheflag.GameContext;
import tech.clearistic.mccapturetheflag.config.Team;

import java.util.List;

public class InventoryClickListener implements Listener {
    private final GameContext context;

    public InventoryClickListener(GameContext context) {
        this.context = context;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack currentItem = event.getCurrentItem();

        if (currentItem == null) {
            return;
        }

        if (currentItem.getType() != this.context.getConfig().getPrizeType()) {
            return;
        }

        Inventory inventory = event.getInventory();
        if (inventory.getType() != InventoryType.CHEST) {
            return;
        }

        // does this interaction involve a team's chest?
        List<Team> teams = this.context.getConfig()
                .getTeams()
                .stream()
                .filter(team -> team.getChest().equals(inventory.getLocation()))
                .toList();

        if (teams.size() == 0) {
            return;
        }

        // we have a score, get the scoring team
        Team scoringTeam = teams.get(0);

        HumanEntity whoClicked = event.getWhoClicked();

        // destroy the item in the user's inventory
        whoClicked.getInventory().remove(this.context.getConfig().getPrizeType());

        this.context.incrementScoreFor(scoringTeam.getName());
        this.context.resetPrizeChest();

        TextComponent text = Component.text(whoClicked.getName() + " scored a point for team " + scoringTeam.getName());
        Bukkit.getServer().broadcast(text);

        // cancel so the prize never makes it to the chest
        event.setCancelled(true);
    }
}
