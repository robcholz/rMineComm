package robcholz.manager;


import robcholz.setting.SettingLoader;
import robcholz.state.CommBlockInterface;

import java.util.LinkedHashMap;
import java.util.Map;

public class CommBlockManager extends AbstractCommManager<CommBlockInterface> {
    private static CommBlockManager instance;
    private final Map<String, CommBlockInterface> cachedPosMap;

    public static CommBlockManager getInstance() {
        if (instance == null)
            instance = new CommBlockManager();
        return instance;
    }

    public CommBlockManager() {
        this.cachedPosMap = new LinkedHashMap<>();
    }

    @Override
    public boolean add(CommBlockInterface element) {
        boolean flag = super.add(element);
        if (flag)
            try {
                SettingLoader.getInstance().saveBlocks();
            } catch (Exception ignored) {
            }
        return flag;
    }

    @Override
    public boolean remove(CommBlockInterface element) {
        boolean flag = super.remove(element);
        if (flag)
            try {
                SettingLoader.getInstance().saveBlocks();
            } catch (Exception ignored) {
            }
        return flag;
    }

    public boolean remove(String name) {
        for (CommBlockInterface block : getMarkedElements()) {
            if (name.equals(block.getName())) {
                return super.remove(block);
            }
        }
        return false;
    }

    @Override
    public void update() {
        for (CommBlockInterface block : getMarkedElements()) {
            block.onUpdate();
        }
    }

    public CommBlockInterface getPos(String name) throws NullPointerException {
        if (cachedPosMap.containsKey(name))
            return cachedPosMap.get(name);
        throw new NullPointerException();
    }
}
