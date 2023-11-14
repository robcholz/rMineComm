package robcholz.gui.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class ConnectScreen extends Screen {

    protected ConnectScreen(Text title) {
        super(title);
    }
}
