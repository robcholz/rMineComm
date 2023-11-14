package robcholz.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import robcholz.Reference;

public class ModItem {
    public static final Item MOD_ICON_ITEM = new Item(new FabricItemSettings());

    public static void registerItem (Item item, String itemID) {
        Registry.register(Registry.ITEM, new Identifier(Reference.MOD_ID, itemID), item);
    }

    public static void register () {
        registerItem(MOD_ICON_ITEM, "mod_icon");
    }
}
