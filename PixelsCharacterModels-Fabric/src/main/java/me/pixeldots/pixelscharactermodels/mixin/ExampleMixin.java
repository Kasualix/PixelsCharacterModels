package me.pixeldots.pixelscharactermodels.mixin;

import me.pixeldots.pixelscharactermodels.PixelsCharacterModels;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class PixelsCharacterModels {
	@Inject(at = @At("HEAD"), method = "init()V")
	private void init(CallbackInfo info) {
		PixelsCharacterModels.LOGGER.info("This line is printed by an example mod mixin!");
	}
}