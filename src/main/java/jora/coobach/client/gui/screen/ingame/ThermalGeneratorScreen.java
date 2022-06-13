package jora.coobach.client.gui.screen.ingame;

import com.mojang.blaze3d.systems.RenderSystem;

import jora.coobach.screen.ThermalGeneratorScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ThermalGeneratorScreen extends HandledScreen<ThermalGeneratorScreenHandler> {
    private static final Identifier TEXTURE = new Identifier("coobach", "textures/gui/container/thermal_generator.png");
    private final ThermalGeneratorScreenHandler _screenHandler;
    
    public ThermalGeneratorScreen(ThermalGeneratorScreenHandler handler, PlayerInventory playerInventory, Text title) {
        super(handler, playerInventory, title);
        _screenHandler = handler;
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
        int mapTo50 = 50 * _screenHandler.getEnergy() / _screenHandler.getMaxEnergy();
        drawTexture(matrices, x + 99, y + 68 - mapTo50, 179, 15, 7, mapTo50);
        drawTexture(matrices, x + 99, y + 18, 176, 15, 3, 50);
        textRenderer.draw(matrices, Integer.toString(_screenHandler.getEnergy()) + "/" + Integer.toString(_screenHandler.getMaxEnergy()), x + 98, y + 70, 0xe92727);
        //at 99, 18
        //from 176, 14
    }
 
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }
 
    @Override
    protected void init() {
        super.init();
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }
}
