package tech.clearistic.mccapturetheflag.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.ItemStack;
import tech.clearistic.mccapturetheflag.GameContext;

public class InventoryMoveItemListener implements Listener {
    private final GameContext context;

    public InventoryMoveItemListener(GameContext context) {
        this.context = context;
    }

    @EventHandler
    public void onInventoryMoveItem(InventoryMoveItemEvent event) {
        Bukkit.getLogger().info("MOVE EVENT");

        ItemStack item = event.getItem();
        if (item.getType() == this.context.getConfig().getPrizeType()) {
            Bukkit.getLogger().info("Inventory movement involves prize");
        }
    }
}
