package robcholz.state;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.Registry;

import java.util.Hashtable;

public class AccessibleBlock {
    public static class BlockType {
        public static final int COMPLEX = 0;
        public static final int SIMPLE = 1;
        public static final int ACCESSIBLE = 2;
        private static final int UNACCESSIBLE = 3;

        private final int blockType;

        public BlockType (BlockState blockState) {
            int id=getIDFromBlock(blockState.getBlock());
            this.blockType = accessibleBlocks.getOrDefault(id, UNACCESSIBLE);
        }

        public int getRawID(){
            return blockType;
        }

        public boolean isAccessible () {
            return blockType == COMPLEX || blockType == SIMPLE;
        }

        public boolean isUnaccessible () {
            return !isAccessible();
        }

        public boolean isComplex () {
            return blockType == COMPLEX;
        }

        public boolean isSimple () {
            return blockType == SIMPLE;
        }

        public static boolean isAccessible (BlockState blockState) {
            return accessibleBlocks.containsKey(getIDFromBlock(blockState.getBlock()));
        }

        public static boolean isUnaccessible (BlockState blockState) {
            return !isAccessible(blockState);
        }

        public static boolean isComplex (BlockState blockState) {
            if (!accessibleBlocks.containsKey(getIDFromBlock(blockState.getBlock())))
                return false;
            return (accessibleBlocks.get(getIDFromBlock(blockState.getBlock())) == BlockType.COMPLEX);
        }

        public static boolean isSimple (BlockState blockState) {
            if (!accessibleBlocks.containsKey(getIDFromBlock(blockState.getBlock())))
                return false;
            return (accessibleBlocks.get(getIDFromBlock(blockState.getBlock())) == BlockType.SIMPLE);
        }

        @Override
        public String toString () {
            if (isSimple())
                return "Simple";
            if (isComplex())
                return "Complex";
            if (isAccessible())
                return "Accessible";
            return "Unaccessible";
        }
    }

    private static final Hashtable<Integer, Integer> accessibleBlocks = new Hashtable<>();

    static {
        // redstone related
        addBlock(Blocks.REDSTONE_LAMP, BlockType.SIMPLE);
        addBlock(Blocks.REDSTONE_WALL_TORCH, BlockType.SIMPLE);
        addBlock(Blocks.REDSTONE_TORCH, BlockType.SIMPLE);
        addBlock(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, BlockType.COMPLEX);
        addBlock(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, BlockType.COMPLEX);
        addBlock(Blocks.REDSTONE_WIRE, BlockType.COMPLEX);

        // what should i call them
        addBlock(Blocks.DAYLIGHT_DETECTOR, BlockType.COMPLEX);
        addBlock(Blocks.TRIPWIRE_HOOK, BlockType.SIMPLE);
        addBlock(Blocks.DETECTOR_RAIL, BlockType.SIMPLE);

        // buttons
        addBlock(Blocks.BIRCH_BUTTON, BlockType.SIMPLE);
        addBlock(Blocks.ACACIA_BUTTON, BlockType.SIMPLE);
        addBlock(Blocks.DARK_OAK_BUTTON, BlockType.SIMPLE);
        addBlock(Blocks.JUNGLE_BUTTON, BlockType.SIMPLE);
        addBlock(Blocks.OAK_BUTTON, BlockType.SIMPLE);

        // pressure plates
        addBlock(Blocks.ACACIA_PRESSURE_PLATE, BlockType.SIMPLE);
        addBlock(Blocks.BIRCH_PRESSURE_PLATE, BlockType.SIMPLE);
        addBlock(Blocks.DARK_OAK_PRESSURE_PLATE, BlockType.SIMPLE);
        addBlock(Blocks.JUNGLE_PRESSURE_PLATE, BlockType.SIMPLE);
        addBlock(Blocks.OAK_PRESSURE_PLATE, BlockType.SIMPLE);
        addBlock(Blocks.SPRUCE_PRESSURE_PLATE, BlockType.SIMPLE);
        addBlock(Blocks.STONE_PRESSURE_PLATE, BlockType.SIMPLE);
    }

    private static Integer getIDFromBlock(Block block){
        return Registry.BLOCK.getRawId(block);
    }

    private static void addBlock (Block block, int blockType) {
        accessibleBlocks.put(getIDFromBlock(block), blockType);
    }
}
