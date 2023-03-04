package fr.survivalisland.heads;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.UUID;

public class HeadsInventory {

    static public int page;

    public Player player;

    public HeadsInventory(int pPage, Player pPlayer) {

        page = pPage;

        this.player = pPlayer;

        this.ShowInventory();

    }


    public void ShowInventory() {

        // Récupération des têtes
        File file = new File("plugins/Heads", "Heads.yml");
        YamlConfiguration data = YamlConfiguration.loadConfiguration(file);

        // Création d'un inventaire à partir de HeadsHolder pour verifier les clics dans cet inventaire
        Inventory inventory = Bukkit.createInventory(new HeadsHolder(), 54, Component.text("Liste des têtes custom"));

        int nbrTete = data.getConfigurationSection("Heads").getKeys(false).size();

        int i = 0;

        // On affiche toutes les tetes pour remplir l'inventaire à cette page
        for (String tete : data.getConfigurationSection("Heads").getKeys(false)) {

            if (i >= (page - 1) * 45 && i < page * 45 && i < nbrTete) {

                ItemStack head = CreateCustomHead(data.getString("Heads." + tete).split("Value:\"")[1].split("\"}")[0]);

                SetItemMeta(head, Component.text(tete), Component.text(""), Component.text("Prix : " + Main.getConfiguration().getInt("HeadPrice") + "¢"), Component.text(""), Component.text("Clique pour acheter"));

                inventory.setItem(i%45, head);

            }

            i++;

        }

        // Si ce n'est pas la derniere page, ajout du bouton suivant
        if (page * 45 < nbrTete) {

            ItemStack head = CreateCustomHead("ewogICJ0aW1lc3RhbXAiIDogMTY3Nzk2Mzc5MTQ0MSwKICAicHJvZmlsZUlkIiA6ICIwMGZiNTRiOWI4NDA0YTA0YTViMmJhMzBlYzBlYTAxMiIsCiAgInByb2ZpbGVOYW1lIiA6ICJrbGxveWQ3MCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS80NTUzOTA4MzA1NTZjOWYwMThkMDVmMGM0Nzc3MDZjNGNiODg3Mzg3NWM5Zjk1ZGZmYTk1MDkxZGRmMTA2NDhkIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=");

            SetItemMeta(head, Component.text("Suivant (" + (page + 1) + ")"));

            inventory.setItem(53, head);

        }

        // Si ce n'est pas la premiere page, ajout du bouton precedent
        if (page > 1) {

            ItemStack head = CreateCustomHead("ewogICJ0aW1lc3RhbXAiIDogMTY3Nzk2MzgzNzAwOCwKICAicHJvZmlsZUlkIiA6ICJmMjc0YzRkNjI1MDQ0ZTQxOGVmYmYwNmM3NWIyMDIxMyIsCiAgInByb2ZpbGVOYW1lIiA6ICJIeXBpZ3NlbCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS84NmUxMTIzMDgwNTc3ZTk4ODE1NzZiMDQ2OGI4MTNmM2RkNTdmMWZiZTExZTQ3YjlhZmVlZTQxZjYxOTA5OTdjIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=");

            SetItemMeta(head, Component.text("Précédent (" + (page - 1) + ")"));

            inventory.setItem(45, head);

        }

        this.player.openInventory(inventory);

    }

    /**
     *
     * @param texture Texture a appliquer a la tete
     * @return Un ItemStack de la tete custom avec la texture donnée en paramètre
     */
    private ItemStack CreateCustomHead(String texture) {

        // Stack représentant la tête
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);

        // Modification de la texture de la tête d'un skin aléatoire
        GameProfile newSkinProfile = new GameProfile(UUID.randomUUID(), null);
        newSkinProfile.getProperties().put("textures", new Property("textures", texture));

        // Attribution du skin a la tête custom
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        Field profileField;
        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, newSkinProfile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        head.setItemMeta(headMeta);

        return head;

    }

    /**
     *
     * @param head ItemStack sur lequel appliquer les changements
     * @param displayName Premiere ligne du nom de l'objet
     * @param lore Lignes optionnelles de description de l'objet
     */
    private void SetItemMeta(ItemStack head, Component displayName, Component... lore) {

        ItemMeta meta = head.getItemMeta();

        meta.displayName(displayName);

        meta.lore(Arrays.asList(lore));

        head.setItemMeta(meta);

    }

}
