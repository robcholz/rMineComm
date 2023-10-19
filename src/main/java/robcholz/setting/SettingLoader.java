package robcholz.setting;

public class SettingLoader {
    private static SettingLoader settingLoader;

    public static SettingLoader getInstance () {
        if (settingLoader == null)
            settingLoader = new SettingLoader();
        return settingLoader;
    }

    public SettingLoader () {

    }
}
