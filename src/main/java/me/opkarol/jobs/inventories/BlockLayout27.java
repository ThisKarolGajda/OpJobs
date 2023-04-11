package me.opkarol.jobs.inventories;

import me.opkarol.opc.api.gui.OpInventory;
import me.opkarol.opc.api.gui.items.InventoryItem;
import me.opkarol.opc.api.map.OpMap;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockLayout27 {
    private static final OpMap<Integer, List<Integer>> MAP = new OpMap<>();

    static {
        MAP.set(1, List.of(13));
        MAP.set(2, Arrays.asList(11, 15));
        MAP.set(3, Arrays.asList(10, 13, 16));
        MAP.set(4, Arrays.asList(10, 12, 14, 16));
        MAP.set(5, Arrays.asList(9, 11, 13, 15, 17));
        MAP.set(6, Arrays.asList(3, 5, 10, 16, 21, 23));
        MAP.set(7, Arrays.asList(2, 6, 10, 13, 16, 21, 23));
        MAP.set(8, Arrays.asList(1, 7, 9, 12, 14, 17, 20, 24));
        MAP.set(9, Arrays.asList(1, 4, 7, 11, 13, 15, 19, 22, 25));
        MAP.set(10, Arrays.asList(2, 4, 6, 9, 12, 14, 17, 19, 22, 25));
    }

    public static List<Integer> getBlocksLayout(int size) {
        return MAP.getOrDefault(size, new ArrayList<>());
    }

    public static void setInventoryBlocks(@NotNull OpInventory inventory, @NotNull List<InventoryItem> list) {
        int inventorySize = inventory.getInventoryHolder().getInventorySlots();
        int listSize = list.size();
        List<Integer> integerList = getBlocksLayout(listSize);
        for (int i = 0; i < listSize; i++) {
            if (i >= inventorySize) {
                return;
            }
            InventoryItem item = list.get(i);
            inventory.set(item, integerList.get(i));
        }
    }

    public static void setInventoryBlocks(@NotNull OpInventory inventory, @NotNull List<InventoryItem> list, int moveBy) {
        int inventorySize = inventory.getInventoryHolder().getInventorySlots();
        int listSize = list.size();
        List<Integer> integerList = getBlocksLayout(listSize);
        for (int i = 0; i < listSize; i++) {
            if (i >= inventorySize) {
                return;
            }
            InventoryItem item = list.get(i);
            inventory.set(item, integerList.get(i) + moveBy);
        }
    }
}