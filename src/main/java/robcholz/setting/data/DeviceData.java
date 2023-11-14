package robcholz.setting.data;

public class DeviceData {
    private String name;
    private int type;
    private String ID;

    public DeviceData(){}

    public DeviceData(String name, int type, String ID) {
        this.name = name;
        this.type = type;
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public String getID() {
        return ID;
    }
}
