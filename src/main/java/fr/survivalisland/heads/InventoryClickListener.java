package fr.survivalisland.heads;

import java.io.File;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.event.inventory.InventoryDragEvent;

public class InventoryClickListener implements Listener {

    @EventHandler
    private void inventoryClick(InventoryClickEvent e) {

        // Clic sur un autre type d'inventaire
        if (!(e.getView().getTopInventory().getHolder() instanceof HeadsHolder)) {
            return;
        }

        // On cancel l'event tout de suite pour éviter les glitchs
        e.setCancelled(true);

        // Si l'item cliqué n'est pas valide
        if ((e.getCurrentItem() == null) || (e.getCurrentItem().getType().equals(Material.AIR))) {
            return;
        }

        Player player = (Player) e.getWhoClicked();

        // Clic sûr suivant
        if (e.getSlot() == 53) {

            new HeadsInventory(Integer.parseInt(e.getCurrentItem().getItemMeta().getDisplayName().split("\\(")[1].split("\\)")[0]), player);
            return;

        }

        // Clic sur précédent
        if (e.getSlot() == 45) {

            new HeadsInventory(Integer.parseInt(e.getCurrentItem().getItemMeta().getDisplayName().split("\\(")[1].split("\\)")[0]), player);
            return;

        }

        File file = new File("plugins/Heads", "Heads.yml");
        YamlConfiguration data = YamlConfiguration.loadConfiguration(file);

        // Clic sur une tete
        if (data.isSet("Heads." + e.getCurrentItem().getItemMeta().getDisplayName())) {

            // Verification de la place dans l'inventaire
            if (player.getInventory().firstEmpty() == -1) {
                player.sendMessage(Component.text(ChatColor.RED + "Vous n'avez pas assez de place dans votre inventaire !"));
                return;
            }

            // Verification du porte-monnaie
            Economy econ = Main.getEconomy();

            if (econ.getBalance(player) < Main.getConfiguration().getInt("HeadPrice")) {
                player.sendMessage(ChatColor.RED + "Vous n'avez pas assez d'argent pour acheter une tête custom !");
                return;
            }

            // Retiration de l'argent
            EconomyResponse r = econ.withdrawPlayer(player, Main.getConfiguration().getInt("HeadPrice"));

            if (!r.transactionSuccess()) {
                player.sendMessage(ChatColor.RED + String.format("Une erreur est survenue : %s.", r.errorMessage));
            }
            // Give de la tete
            else {
                Bukkit.dispatchCommand( Bukkit.getConsoleSender(), "minecraft:give " + player.getName() + " " + data.getString("Heads." + e.getCurrentItem().getItemMeta().getDisplayName() ));
            }

        }

    }


    @EventHandler
    private void inventoryClick(InventoryDragEvent e) {

        if (e.getView().getTopInventory().getHolder() instanceof HeadsHolder) {

            e.setCancelled(true);

        }

    }

}
