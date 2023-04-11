package me.opkarol.jobs.inventories;

import me.opkarol.OpJobs;
import me.opkarol.opc.api.gui.OpInventory;
import me.opkarol.opc.api.gui.holder.IInventoryHolder;
import me.opkarol.opc.api.gui.inventory.InventoryFactory;
import me.opkarol.opc.api.gui.items.HeadInventoryItem;
import me.opkarol.opc.api.gui.items.InventoryItem;
import me.opkarol.opc.api.utils.FormatUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static me.opkarol.jobs.inventories.JobExperienceInventory.openExperienceInventory;
import static me.opkarol.opc.api.tools.HeadManager.updatePlayerHeadInInventory;

public class JobMainInventory implements IInventoryHolder {

    private final OpInventory inventory;

    public JobMainInventory(@NotNull Player player) {
        this.inventory = new OpInventory(new InventoryFactory(27, "#<5389FD>&lPrace"));

        InventoryItem mainProfile = new InventoryItem(Material.PLAYER_HEAD, event -> {
            event.setCancelled(true);
            openExperienceInventory(player, OpJobs.getInstance().getInventoriesHolder());
        });
        mainProfile.setName("&eWCZYTYWANIE...");

        InventoryItem questionMark = new HeadInventoryItem("2705fd94a0c431927fb4e639b0fcfb49717e412285a02b439e0112da22b2e2ec",
                event -> event.setCancelled(true));
        questionMark.setLore("&7Wybierając jeden z sześciu dostępnych zawodów:", "#<5389FD>• Górnika,", "#<5389FD>• Rybaka,", "#<5389FD>• Hutnika,", "#<5389FD>• Drwala,", "#<5389FD>• Farmera,", "#<5389FD>• Rzemieślnika.", "&7otrzymasz zadanie, składające się z bloków, które trzeba", "#<5389FD>zebrać, wykopać &7lub #<5389FD>wykonać &7w zależności od wybranej pracy.", "#<5389FD>Zadania składają się z etapów&7,", "&7a każdy etap wymaga zebrania innej ilości bloków.", "", "&7Gdy ukończysz wszystkie etapy zadania,", "#<5389FD>otrzymasz dukaty jako nagrodę&7.", "&7Dukaty można wykorzystać do kupowania różnych przedmiotów.")
                .setName("#<5389FD>&lO co chodzi?");

        InventoryItem book = new HeadInventoryItem("aff5818d72309a5cd5abe8e3308287021b94685c2e59f9b6fcf655f99bfab109",
                event -> event.setCancelled(true));
        book.setLore("&7Aby wybrać pracę, należy wpisać komendę:", "#<5389FD>• /praca wybierz &7(otwiera okienko gui z dostępnymi pracami,", "&7wybierz pracę naciskając na główkę postaci)", "#<5389FD>• /praca losowa &7(wybiera losową pracę)", "#<5389FD>• /praca poziomy &7(wyświetla okienko z poziomami każdego rodzaju pracy)", "&7Po wybraniu pracy, gracz otrzymuje zadanie,", "&7które musi ukończyć, wykonując odpowiednie akcje na blokach.", "", "&7Aby zobaczyć postęp zadania, należy wpisać komendę:", "#<5389FD>• /praca postep &7(wyświetla postęp, jeśli ma aktywną pracę).")
                .setName("#<5389FD>&lJak używać?");

        inventory.set(mainProfile, 10);
        inventory.set(questionMark, 13);
        inventory.set(book, 16);
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
        updatePlayerHeadInInventory(player, inventory, 10, itemStack -> {
            ItemMeta meta = itemStack.getItemMeta();
            if (meta == null) {
                return;
            }

            meta.setDisplayName(FormatUtils.formatMessage("&7Profil: #<5389FD>&l" + player.getName()));
            meta.setLore(FormatUtils.formatList(List.of("&8&o(&7&oNaciśnij #<5389FD>&oLPM &7&oaby", "&7&owyświetlić swój poziom!&8&o)")));
            itemStack.setItemMeta(meta);
        });

    }
}
