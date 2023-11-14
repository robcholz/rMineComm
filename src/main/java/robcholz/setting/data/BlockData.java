package robcholz.setting.data;

import java.util.Collection;

public class BlockData {
    private String name;
    private int dimension;
    private int type;
    private int blockID;
    private Collection<Integer> pos;

    public BlockData() {
    }

    public BlockData(String name, int dimension, int type,int blockID, Collection<Integer> pos) {
        this.name = name;
        this.dimension = dimension;
        this.type = type;
        this.blockID=blockID;
        this.pos = pos;
    }

    public String getName(){
        return name;
    }

    public int getDimension() {
        return dimension;
    }

    public int getType() {
        return type;
    }

    public int getID(){
        return blockID;
    }

    public int getPosX() {
        return (Integer) pos.toArray()[0];
    }

    public int getPosY() {
        return (Integer) pos.toArray()[1];
    }

    public int getPosZ() {
        return (Integer) pos.toArray()[2];
    }
}
