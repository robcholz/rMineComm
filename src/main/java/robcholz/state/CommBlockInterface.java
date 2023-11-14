package robcholz.state;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface CommBlockInterface {
    public static final int COMPLEX = 0;
    public static final int SIMPLE = 1;
    public static final int ACCESSIBLE = 2;
    public static final int UNACCESSIBLE = 3;
    String getName();
    void onUpdate ();

    BlockState getBlockState ();

    AccessibleBlock.BlockType getBlockType ();

    BlockPos getBlockPos ();

    World getWorld();

    int getWorldID();

    int getLightColor ();

    Text getBlockName ();

    Text getInternalType ();

    int getLuminance ();

    int getPower ();

    boolean isActivated ();

    boolean isExisted ();

    boolean isInConsistentDimension ();
}
