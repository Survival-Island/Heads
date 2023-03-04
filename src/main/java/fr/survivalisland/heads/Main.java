package fr.survivalisland.heads;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;

public class Main extends JavaPlugin {

    private static Economy econ = null;

    private static FileConfiguration config;

    public void onEnable() {

        // Lien des commandes avec les classes
        this.getCommand("heads").setExecutor(new HeadsCommand());

        // Lien des events avec les classes
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);


        // Setup Vault economy
        if (!setupEconomy() ) {
            this.getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }


        // Création du fichier de configuration s'il n'existe pas
        config = getConfig();

        // Initialisation des valeurs par défaut
        config.addDefault("HeadPrice", 2000);
        config.addDefault("RandomHeadPrice", 200);

        // Enregistrement du fichier de configuration
        config.options().copyDefaults(true);
        saveConfig();

        config.setComments("HeadPrice", Collections.singletonList("Prix d'une tête"));
        config.setComments("RandomHeadPrice", Collections.singletonList("Prix d'une tête aléatoire"));
        saveConfig();

    }


    /**
     * Setup Vault economy
     * @return The economy instance
     */
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    /**
     * Get the economy instance
     * @return The economy instance
     */
    public static Economy getEconomy() {
        return econ;
    }


    /**
     * Get the config
     * @return config
     */
    public static FileConfiguration getConfiguration() {
        return config;
    }

}