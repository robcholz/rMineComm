package robcholz.manager;

import robcholz.hardwarecomm.comm.DeviceException;
import robcholz.hardwarecomm.device.AbstractCommDevice;
import robcholz.hardwarecomm.device.CommDeviceFactory;
import robcholz.hardwarecomm.device.CommDeviceInterface;
import robcholz.setting.SettingLoader;
import robcholz.setting.data.DeviceData;

import java.util.*;

public class CommDeviceManager extends AbstractCommManager<AbstractCommDevice> {
    private static CommDeviceManager instance;
    private final Map<String, CommDeviceInterface> cachedDeviceSearchMap;
    private final List<AbstractCommDevice> connectionList;

    public static CommDeviceManager getInstance() {
        if (instance == null)
            instance = new CommDeviceManager();
        return instance;
    }

    CommDeviceManager() {
        this.cachedDeviceSearchMap = new LinkedHashMap<>();
        this.connectionList = new LinkedList<>();
    }

    public void addToCacheSearchMap(List<CommDeviceInterface> devices) {
        for (CommDeviceInterface device : devices)
            cachedDeviceSearchMap.put(device.getName(), device);
    }

    public void rename(String name, String previousName) throws NullPointerException {
        getDevice(previousName).setName(name);
        try {
            SettingLoader.getInstance().saveDevices();
        } catch (Exception ignored) {
        }
    }

    public void connect(String deviceID) throws DeviceException.DeviceUnopenedException, DeviceException.BadDeviceConfigException, DeviceException.DeviceInUseException {
        AbstractCommDevice device = getDevice(deviceID);
        if (device != null) {
            device.connect();
            connectionList.add(device);
        }
    }

    public void connect(DeviceData deviceData) throws DeviceException.DeviceUnopenedException, DeviceException.BadDeviceConfigException, DeviceException.DeviceInUseException {
        AbstractCommDevice device = CommDeviceFactory.getInstance().createDevice(deviceData.getName(), deviceData.getID(), deviceData.getType());
        device.connect();
        connectionList.add(device);
    }

    public void disconnect(String deviceID) throws DeviceException.DeviceUnclosedException {
        getDevice(deviceID).disconnect();
        connectionList.remove(getDevice(deviceID));
    }

    public boolean isAvailable(String deviceID) {
        return getDevice(deviceID).isAvailable();
    }

    @Override
    public boolean add(AbstractCommDevice element) {
        for (AbstractCommDevice device : getMarkedElements()) {
            if (device.getName().equals(element.getName()))
                return false;
        }
        boolean flag = super.add(element);
        if (flag)
            try {
                SettingLoader.getInstance().saveDevices();
            } catch (Exception ignored) {
            }
        return flag;
    }

    @Override
    public boolean remove(AbstractCommDevice element) {
        boolean flag = super.remove(element);
        if (flag)
            try {
                SettingLoader.getInstance().saveDevices();
            } catch (Exception ignored) {
            }
        return flag;
    }

    public boolean add(String deviceID) {
        try {
            AbstractCommDevice device = CommDeviceManager.getInstance().deviceBuilder(deviceID);
            return add(device);
        } catch (NullPointerException e) {
            return false;
        }
    }

    public boolean remove(String name) {
        for (AbstractCommDevice device : getMarkedElements()) {
            if (name.equals(device.getName())) {
                return remove(device);
            }
        }
        return false;
    }

    public List<AbstractCommDevice> getConnectionList() {
        return Collections.unmodifiableList(connectionList);
    }

    private AbstractCommDevice getDevice(String name) throws NullPointerException {
        for (AbstractCommDevice device : getMarkedElements()) {
            if (device.getName().equals(name)||device.getID().equals(name)) {
                return device;
            }
        }
        throw new NullPointerException();
    }

    private AbstractCommDevice deviceBuilder(String name) throws NullPointerException {
        if (!cachedDeviceSearchMap.containsKey(name))
            throw new NullPointerException();
        CommDeviceInterface device = cachedDeviceSearchMap.get(name);
        return CommDeviceFactory.getInstance().createDevice(name, device.getID(), device.getDeviceType());
    }

    @Override
    public void update() {
        for (AbstractCommDevice device : connectionList) {
            device.onUpdate();
        }
    }
}
