package robcholz.monitor;

import net.minecraft.block.*;
import net.minecraft.state.property.Properties;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import robcholz.accessor.AccessibleBlock;

public class MonitoredBlockState {
    private final World world;
    private final AccessibleBlock.BlockType blockType;
    private final BlockPos blockPos;
    private boolean existed; // true if block existed
    private boolean activated; // for others, this is true if block exists
    private int luminance; // light level
    private int lightColor;
    private int power; // for chargeable block, this is larger than 0 if charged. for others, this keeps 0

    public MonitoredBlockState (World world, BlockPos blockPos) {
        this.world=world;
        this.blockPos = blockPos;
        this.blockType = new AccessibleBlock.BlockType(getBlockState());
    }

    public void onUpdate () {
        BlockState blockState=getBlockState();
        if (blockState.isAir()) {
            this.existed = false;
            this.power = 0;
            this.activated = false;
            this.luminance = 0;
            this.lightColor = 0;
            return;
        }
        this.existed = true;
        this.luminance = blockState.getLuminance();
        if (blockType.isComplex()) {
            // daylight detector, redstone wire, pressure plate
            this.power = blockState.get(Properties.POWER);
            this.activated = this.power > 0;
            if (isBlock(Blocks.REDSTONE_WIRE))
                this.lightColor = RedstoneWireBlock.getWireColor(power);
            else
                this.lightColor = 0;
            return;
        }
        if (blockType.isSimple()) {
            if (isBlock(Blocks.REDSTONE_LAMP)) {
                this.activated = luminance > 0;
                this.power = 0;
                this.lightColor = 0;
                return;
            }
            if (isBlock(Blocks.REDSTONE_TORCH) || isBlock(Blocks.REDSTONE_WALL_TORCH)) {
                this.activated = blockState.get(Properties.LIT);
                this.power = activated ? 15 : 0;
                this.lightColor = 0;
                return;
            }
            // pressure plate, button, tripwire,
            this.activated = blockState.get(TripwireBlock.POWERED);
            this.power = activated ? 15 : 0;
            this.lightColor = 0;
            return;
        }
        // unsorted
        this.power = 0;
        this.activated = false;
        this.lightColor = 0;
    }

    public BlockState getBlockState () {
        return world.getBlockState(blockPos);
    }

    public AccessibleBlock.BlockType getBlockType () {
        return blockType;
    }

    public BlockPos getBlockPos () {
        return blockPos;
    }

    public int getLightColor () {
        return lightColor;
    }

    public Text getBlockName () {
        return new TranslatableText(getBlockState().getBlock().getName().getString());
    }

    public Text getInternalType () {
        return new LiteralText(blockType.toString()).setStyle(new Style().setItalic(true).setColor(Formatting.GRAY));
    }

    public int getLuminance () {
        return luminance;
    }

    public int getPower () {
        return power;
    }

    public boolean isActivated () {
        return activated;
    }

    public boolean isExisted () {
        return existed;
    }

    private boolean isBlock (Block block) {
        return getBlockState().getBlock().equals(block);
    }

    public boolean isInConsistentDimension(){
        // TODO
        return true;
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return blockPos.equals(((MonitoredBlockState) o).blockPos);
    }

    @Override
    public int hashCode () {
        return blockPos.hashCode();
    }
}
