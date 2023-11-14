package robcholz.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface SelectAction<E> {
    void onSelected(E listWidget);
}
