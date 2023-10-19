package robcholz.monitor;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class BlockStateMonitor {
    private static BlockStateMonitor instance;
    private final Set<MonitoredBlockState> monitoredBlocks;
    // private final Set<MonitoredDevices> monitoredDevices;

    public static BlockStateMonitor getInstance () {
        if (instance == null)
            instance = new BlockStateMonitor();
        return instance;
    }

    public BlockStateMonitor () {
        this.monitoredBlocks = new HashSet<>();
    }

    public Set<MonitoredBlockState> getMonitoredBlocks () {
       return Collections.unmodifiableSet(monitoredBlocks);
    }

    public void update () {
        for (MonitoredBlockState block : monitoredBlocks) {
            block.onUpdate();
        }
    }

    public boolean addBlock (MonitoredBlockState blockState) {
        if(monitoredBlocks.contains(blockState))
            return false;
        return monitoredBlocks.add(blockState);
    }

    public boolean removeBlock(MonitoredBlockState blockState){
        if(!monitoredBlocks.contains(blockState))
            return false;
        return monitoredBlocks.remove(blockState);
    }
}
