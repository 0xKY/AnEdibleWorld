package me.kaloyankys.anedibleworld;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class AnEdibleWorld implements ModInitializer {
    public static final String MOD_ID = "anedibleworld";
    public static final Logger LOGGER =  LogManager.getLogger(MOD_ID);

    @Override
    public void onInitialize() {}

    private static Identifier id(String id) {
        return new Identifier(MOD_ID, id);
    }
}
