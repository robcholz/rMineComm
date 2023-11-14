package robcholz.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.io.File;

@Mixin(MinecraftServer.class)
public interface MinecraftServerAccessor {
    @Accessor
    File getGameDir();
}
