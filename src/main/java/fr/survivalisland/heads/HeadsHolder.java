package fr.survivalisland.heads;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class HeadsHolder implements InventoryHolder {

    private Inventory inventory;

    @Override
    public Inventory getInventory() {
        return inventory;
    }

}
