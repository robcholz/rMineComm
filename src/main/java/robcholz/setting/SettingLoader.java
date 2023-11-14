package robcholz.setting;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;
import robcholz.hardwarecomm.device.CommDeviceFactory;
import robcholz.hardwarecomm.device.CommDeviceInterface;
import robcholz.manager.CommBlockManager;
import robcholz.manager.CommDeviceManager;
import robcholz.setting.data.BlockData;
import robcholz.setting.data.DeviceData;
import robcholz.state.CommBlockInterface;
import robcholz.state.CommBlockState;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class SettingLoader {
    private static final String BLOCK_FILE_NAME = "rminecomm_blocks.json";
    private static final String DEVICE_FILE_NAME = "rminecomm_devices.json";
    private static SettingLoader settingLoader;
    private final CommDeviceManager deviceManager;
    private final CommBlockManager blockManager;
    private final Gson gson;

    @NotNull
    public static SettingLoader getInstance() {
        if (settingLoader == null)
            settingLoader = new SettingLoader();
        return settingLoader;
    }

    public SettingLoader() {
        this.gson = new Gson();
        this.deviceManager = CommDeviceManager.getInstance();
        this.blockManager = CommBlockManager.getInstance();
    }

    public void load() {
        loadBlocks();
        loadDevices();
    }

    public void loadBlocks() {
        try {
            String content = readFile(BLOCK_FILE_NAME, getBlockFilePath());
            List<BlockData> list = gson.fromJson(content, new TypeToken<List<BlockData>>() {
            }.getType());
            for (BlockData raw : list) {
                blockManager.add(new CommBlockState(raw.getName(), raw.getDimension(),
                        new BlockPos(raw.getPosX(), raw.getPosY(), raw.getPosZ())
                ));
            }
        } catch (Exception ignored) {
        }
    }

    public void loadDevices() {
        try {
            String content = readFile(DEVICE_FILE_NAME, getDeviceFilePath());
            List<DeviceData> list = gson.fromJson(content, new TypeToken<List<DeviceData>>() {
            }.getType());
            for (DeviceData raw : list) {
                deviceManager.add(CommDeviceFactory.getInstance().createDevice(raw.getName(), raw.getID(), raw.getType()));
            }
        } catch (Exception ignored) {
        }
    }

    public void saveDevices() throws IOException {
        List<DeviceData> list = new LinkedList<>();
        for (CommDeviceInterface device : deviceManager.getMarkedElements()) {
            list.add(new DeviceData(device.getName(), device.getDeviceType(), device.getID()));
        }
        writeFile(DEVICE_FILE_NAME, getDeviceFilePath(), gson.toJson(list));
    }

    public void saveBlocks() throws IOException {
        List<BlockData> list = new LinkedList<>();
        for (CommBlockInterface block : blockManager.getMarkedElements()) {
            list.add(new BlockData(
                    block.getName(),
                    block.getWorldID(),
                    block.getBlockType().getRawID(),
                    Registry.BLOCK.getRawId(block.getBlockState().getBlock()),
                    Arrays.asList(block.getBlockPos().getX(),
                            block.getBlockPos().getY(),
                            block.getBlockPos().getZ())));
        }
        writeFile(BLOCK_FILE_NAME, getBlockFilePath(), gson.toJson(list));
    }

    private Path getDeviceFilePath() {
        return FabricLoader.getInstance().getConfigDir();
    }

    private Path getBlockFilePath() {
        String levelName = Objects.requireNonNull(MinecraftClient.getInstance().getServer()).getLevelName();
        return FabricLoader.getInstance().getGameDir().resolve("saves").resolve(levelName);
    }

    private void writeFile(String fileName, Path path, String content) {
        try {
            FileWriter fileWriter = new FileWriter(path.resolve(fileName).toFile());
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.write(content);
            fileWriter.flush();
        } catch (IOException ignored) {
        }
    }

    private String readFile(String fileName, Path path) throws IOException {
        try {
            Path p = path.resolve(fileName);
            if (!Files.exists(p))
                Files.createFile(p);
            return new String(Files.readAllBytes(p));
        } catch (IOException e) {
            throw e;
        }
    }
}
