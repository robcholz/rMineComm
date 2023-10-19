package robcholz.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ListWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class HomeScreen extends Screen {
    public ButtonWidget button1;
    public ButtonWidget button2;
    public ButtonWidget button3;
    private TextFieldWidget myDevicesText;
    private TextFieldWidget otherDevicesText;
    private ListWidget scannedDeviceList;
    private TextFieldWidget searchBox;

    protected HomeScreen (Text title) {
        super(title);
    }

    @Override
    protected void init () {
/*
        button1 = ButtonWidget.builder(Text.literal("Button 1"), button -> {
                    System.out.println("You clicked button1!");
                })
                .dimensions(width / 2 - 205, 20, 200, 20)
                .tooltip(Tooltip.of(Text.literal("Tooltip of button1")))
                .build();
        button2 = ButtonWidget.builder(Text.literal("Button 2"), button -> {
                    System.out.println("You clicked button2!");
                })
                .dimensions(width / 2 + 5, 20, 200, 20)
                .tooltip(Tooltip.of(Text.literal("Tooltip of button2")))
                .build();

        this.addButton(button1);
        this.addButton(button2);
        this.addButton(button3);
 */
    }
}
