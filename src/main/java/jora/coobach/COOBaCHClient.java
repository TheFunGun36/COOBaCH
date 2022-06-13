package jora.coobach;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

@Environment(EnvType.CLIENT)
public class COOBaCHClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(COOBaCH.THERMAL_GENERATOR_SCREEN_HANDLER, ThermalGeneratorScreen::new);
    }
}
