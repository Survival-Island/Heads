package fr.survivalisland.heads;

import net.kyori.adventure.text.Component;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class HeadsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Commande pour afficher l'interface de toutes les tetes
        if (args[0].equals("list")) {

            if (sender instanceof Player player) {

                new HeadsInventory(1, player);

            }

        }

        // Commande pour se give une tete aléatoire
        if (args[0].equals("giverandom") && sender.hasPermission("heads.giverandom")) {

            if (!(sender instanceof Player player)) {
                return true;
            }

            // Verification de la place dans l'inventaire
            if (player.getInventory().firstEmpty() == -1) {
                player.sendMessage(Component.text(ChatColor.RED + "Vous n'avez pas assez de place dans votre inventaire !"));
                return true;
            }

            // Verification du porte-monnaie
            Economy econ = Main.getEconomy();

            if (econ.getBalance(player) < Main.getConfiguration().getInt("HeadPrice")) {
                player.sendMessage(ChatColor.RED + "Vous n'avez pas assez d'argent pour acheter une tête custom !");
                return true;
            }

            // Retiration de l'argent
            EconomyResponse r = econ.withdrawPlayer(player, Main.getConfiguration().getInt("HeadPrice"));

            if (!r.transactionSuccess()) {
                player.sendMessage(ChatColor.RED + String.format("Une erreur est survenue : %s.", r.errorMessage));
            }
            // Give de la tete
            else {
                File file = new File("plugins/Heads", "Heads.yml");
                YamlConfiguration data = YamlConfiguration.loadConfiguration(file);

                int total = data.getConfigurationSection("Heads").getKeys(false).size();

                Bukkit.dispatchCommand( Bukkit.getConsoleSender(), "minecraft:give " + sender.getName() + " " + data.getString("Heads." + data.getConfigurationSection("Heads").getKeys(false).toArray()[(int)(Math.random() * total)] ));
            }

            return true;

        }

        return false;
    }

}