package robcholz.block;


import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import robcholz.Reference;
import robcholz.itemgroup.ItemGroup;

public class ModBlock {
    public static final Block DAYLIGHT_DETECTOR = new DaylightDetectorBlock(FabricBlockSettings.of(Material.WOOD).strength(0.2F).sounds(BlockSoundGroup.WOOD));
    public static final Block LIGHT_SENSOR = new Block(FabricBlockSettings.of(Material.METAL).strength(4.0f));
    public static final Block REDSTONE_METER = new Block(FabricBlockSettings.of(Material.METAL).strength(4.0f));
    public static final Block WEATHER_SENSOR = new Block(FabricBlockSettings.of(Material.METAL).strength(4.0f));

    private static void registerBlockItem (Block block, String blockID) {
        Registry.register(Registry.ITEM, new Identifier(Reference.MOD_ID, blockID),
                new BlockItem(block, new FabricItemSettings().group(ItemGroup.RMINECOMM_ITEMGROUP)));
    }

    private static void registerBlock (Block block, String blockID) {
        registerBlockItem(block, blockID);
        Registry.register(Registry.BLOCK, new Identifier(Reference.MOD_ID, blockID), block);
    }

    public static void register () {
        registerBlock(DAYLIGHT_DETECTOR, "daylight_detector");
        registerBlock(LIGHT_SENSOR, "light_sensor");
        registerBlock(REDSTONE_METER, "redstone_meter");
        registerBlock(WEATHER_SENSOR, "weather_sensor");
    }
}
