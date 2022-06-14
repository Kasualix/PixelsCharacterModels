package me.pixeldots.pixelscharactermodels.other;

import java.nio.file.Paths;

import org.lwjgl.glfw.GLFW;

import me.pixeldots.pixelscharactermodels.PCMClient;
import me.pixeldots.pixelscharactermodels.files.AnimationHelper;
import me.pixeldots.pixelscharactermodels.skin.SkinHelper;
import me.pixeldots.scriptedmodels.platform.PlatformUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.LivingEntity;

public class KeyBindings {
	
	private static KeyBinding openGUI = KeyBindingHelper.registerKeyBinding(new KeyBinding(
		"key.pixelscharactermodels.opengui", // The translation key of the keybinding's name
		InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
		GLFW.GLFW_KEY_R, // The keycode of the key
		"category.pixelscharactermodels" // The translation key of the keybinding's category.
	));
	private static KeyBinding reloadKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
		"key.pixelscharactermodels.reload", // The translation key of the keybinding's name
		InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
		GLFW.GLFW_KEY_KP_1, // The keycode of the key
		"category.pixelscharactermodels" // The translation key of the keybinding's category.
	));
	
	private static KeyBinding Anim1 = KeyBindingHelper.registerKeyBinding(
			new KeyBinding("key.pixelscharactermodels.anim1", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_KP_1, "category.PixelsCharacterModels"));
	/*private static KeyBinding Anim2 = KeyBindingHelper.registerKeyBinding(
			new KeyBinding("key.pixelscharactermodels.anim2", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_KP_2, "category.PixelsCharacterModels"));
	private static KeyBinding Anim3 = KeyBindingHelper.registerKeyBinding(
			new KeyBinding("key.pixelscharactermodels.anim3", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_KP_3, "category.PixelsCharacterModels"));
	private static KeyBinding Anim4 = KeyBindingHelper.registerKeyBinding(
			new KeyBinding("key.pixelscharactermodels.anim4", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_KP_4, "category.PixelsCharacterModels"));
	private static KeyBinding Anim5 = KeyBindingHelper.registerKeyBinding(
			new KeyBinding("key.pixelscharactermodels.anim5", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_KP_5, "category.PixelsCharacterModels"));*/
	
	public static void registerKeyBindings() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
		    if (openGUI.wasPressed()) {
		    	PCMClient.OpenGUI();
		    }
			if (reloadKey.wasPressed()) {
				SkinHelper.reloadSkins();
			}
		    /*if (KeyBindings.reloadKey.wasPressed()) {
            	PixelsCharacterModels.client.ReloadSkins();
            	PixelsCharacterModelsMain.clientHandler.requestModelData();
            }*/
		    
		    if (Anim1.wasPressed()) {
				LivingEntity entity = client.player;
				EntityModel<?> model = PlatformUtils.getModel(entity);
				String current = AnimationHelper.get_current(entity, model);
				if (current.equals("ScriptedModels.json"))
					AnimationHelper.stop(entity, model);
				else
					AnimationHelper.play(Paths.get(".", "PCM/Animations/ScriptedModels.json").toFile(), entity, model);
		    	//setAnimation(PixelsCharacterModels.localData.AnimationIDOne, PixelsCharacterModels.localData.AnimationOneisFrames);
		    }
		    /*if (Anim2.wasPressed()) {
		    	setAnimation(PixelsCharacterModels.localData.AnimationIDTwo, PixelsCharacterModels.localData.AnimationTwoisFrames);
		    }
		    if (Anim3.wasPressed()) {
		    	setAnimation(PixelsCharacterModels.localData.AnimationIDThree, PixelsCharacterModels.localData.AnimationThreeisFrames);
		    }
		    if (Anim4.wasPressed()) {
		    	setAnimation(PixelsCharacterModels.localData.AnimationIDFour, PixelsCharacterModels.localData.AnimationFourisFrames);
		    }
		    if (Anim5.wasPressed()) {
		    	setAnimation(PixelsCharacterModels.localData.AnimationIDFive, PixelsCharacterModels.localData.AnimationFiveisFrames);
		    }*/
		});
	}
	
	/*public static void setAnimation(String key, boolean isFrames) {
		if (!isFrames) {
			if (PixelsCharacterModels.playingAnimation != key) {
				PixelsCharacterModels.isPlayingFrames = false;
				PixelsCharacterModels.playingFramesData = null;
	    		if (PixelsCharacterModels.AnimationsData.loadAnimation(key, PixelsCharacterModels.thisPlayer, PixelsCharacterModels.PlayerDataList.get(PixelsCharacterModels.thisPlayer.getUuid()).model)) {
		    		PixelsCharacterModels.playingAnimation = key;
		    		PixelsCharacterModels.playingAnimationData = PixelsCharacterModels.client.currentStoredAnimation;
	    		}
	    	}
	    	else {
	    		PixelsCharacterModels.playingAnimation = "";
	    		PixelsCharacterModels.playingAnimationData = null;
	    	}
		} else {
			if (PixelsCharacterModels.playingFramesData == null || !PixelsCharacterModels.playingFramesData.name.equalsIgnoreCase(key)) {
	    		if (PixelsCharacterModels.FramesData.loadFrame(key, PixelsCharacterModels.thisPlayer, PixelsCharacterModels.PlayerDataList.get(PixelsCharacterModels.thisPlayer.getUuid()).model)) {
	    			PixelsCharacterModels.isPlayingFrames = true;
	    			PixelsCharacterModels.playingFramesData = PixelsCharacterModels.client.currentStoredFrames;
	    			PixelsCharacterModels.client.framesAnimationID = 0;
					//loadAnimationFrames(PixelsCharacterModels.EntityModelList.get(PixelsCharacterModels.thisPlayer), PixelsCharacterModels.thisPlayer);
				}
	    	}
	    	else if (PixelsCharacterModels.playingFramesData.name.equalsIgnoreCase(key)) {
	    		PixelsCharacterModels.isPlayingFrames = false;
	    		PixelsCharacterModels.playingFramesData = null;
	    		PixelsCharacterModels.playingAnimation = "";
	    		PixelsCharacterModels.playingAnimationData = null;
	    	}
		}
	}*/
	
	/*public static void loadAnimationFrames(PlayerEntityModel<?> model, PlayerEntity entity) {
		String s = PixelsCharacterModels.playingFramesData.frames.get(PixelsCharacterModels.PCMClient.framesAnimationID);
		PixelsCharacterModels.AnimationsData.loadAnimation(s, entity, model);
		PixelsCharacterModels.playingAnimation = s;
		PixelsCharacterModels.playingAnimationData = PixelsCharacterModels.PCMClient.currentStoredAnimation;
	}*/
}
