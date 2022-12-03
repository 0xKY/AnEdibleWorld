package me.kaloyankys.anedibleworld.client;

import me.kaloyankys.anedibleworld.AnEdibleWorld;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class AnEdibleWorldClient implements ClientModInitializer {
    public static final KeyBinding R = createKeyBind("special_ability");

    @Override
    public void onInitializeClient() {
    }

    private static KeyBinding createKeyBind(String name) {
        return KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key." + AnEdibleWorld.MOD_ID + "." + name,
                InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R,
                "category." + AnEdibleWorld.MOD_ID + ".keybinds"
        ));
    }
}
