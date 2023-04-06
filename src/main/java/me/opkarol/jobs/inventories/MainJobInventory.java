package me.opkarol.jobs.inventories;

import me.opkarol.OpJobs;
import me.opkarol.opc.api.gui.OpInventory;
import me.opkarol.opc.api.gui.holder.IInventoryHolder;
import me.opkarol.opc.api.gui.inventory.InventoryFactory;
import me.opkarol.opc.api.gui.items.InventoryItem;
import me.opkarol.opc.api.utils.FormatUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static me.opkarol.jobs.inventories.JobExperienceInventory.openExperienceInventory;
import static me.opkarol.opc.api.tools.HeadManager.updatePlayerHeadInInventory;

public class MainJobInventory implements IInventoryHolder {

    private final OpInventory inventory;

    public MainJobInventory(@NotNull Player player) {
        this.inventory = new OpInventory(new InventoryFactory(27, "#<5389FD>&lPrace"));

        InventoryItem item = new InventoryItem(Material.PLAYER_HEAD, event -> {
            event.setCancelled(true);
            openExperienceInventory(player, OpJobs.getInstance().getInventoriesHolder());
        });
        item.setName("&eWCZYTYWANIE...");

        inventory.set(item, 11);
        inventory.setAllUnused(0, getBlankItem());
        inventory.build();

        inventory.openInventory(player);
        updateHead(player);
    }

    @Override
    public OpInventory getInventory() {
        return inventory;
    }

    public void updateHead(Player player) {
        updatePlayerHeadInInventory(player, inventory, 11, itemStack -> {
            ItemMeta meta = itemStack.getItemMeta();
            if (meta == null) {
                return;
            }

            meta.setDisplayName(FormatUtils.formatMessage("&7Profil: #<5389FD>" + player.getName()));
            meta.setLore(FormatUtils.formatList(List.of("&7Naciśnij #<5389FD>LPM &7aby", "&7wyświetlić swój poziom!")));
            itemStack.setItemMeta(meta);
        });
    }
}
