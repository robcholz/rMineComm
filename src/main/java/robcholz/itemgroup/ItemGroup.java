package robcholz.itemgroup;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import robcholz.Reference;
import robcholz.item.ModItem;

public class ItemGroup {
    public static final net.minecraft.item.ItemGroup RMINECOMM_ITEMGROUP = FabricItemGroupBuilder.create(
                    new Identifier(Reference.MOD_ID, "group"))
            .icon(() -> new ItemStack(ModItem.MOD_ICON_ITEM))
            .build().setName("rMineComm").setNoScrollbar();
}
