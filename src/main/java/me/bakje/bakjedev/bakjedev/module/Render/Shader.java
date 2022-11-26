package me.bakje.bakjedev.bakjedev.module.Render;

import com.google.gson.JsonSyntaxException;
import me.bakje.bakjedev.bakjedev.event.events.EntityRenderEvent;
import me.bakje.bakjedev.bakjedev.event.events.WorldRenderEvent;
import me.bakje.bakjedev.bakjedev.eventbus.BakjeSubscribe;
import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.Settings.BooleanSetting;
import me.bakje.bakjedev.bakjedev.module.Settings.NumberSetting;
import me.bakje.bakjedev.bakjedev.util.shader.ColorVertexConsumerProvider;
import me.bakje.bakjedev.bakjedev.util.shader.CoreShaders;
import me.bakje.bakjedev.bakjedev.util.shader.ShaderEffectWrapper;
import me.bakje.bakjedev.bakjedev.util.shader.ShaderLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.OreBlock;
import net.minecraft.block.RedstoneOreBlock;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.IOException;
import java.util.ArrayList;

public class Shader extends Mod {
    private ShaderEffectWrapper shader;
    private ColorVertexConsumerProvider colorVertexer;

    public NumberSetting fill = new NumberSetting("Fill", 1, 255, 50, 1);
    public BooleanSetting players = new BooleanSetting("Players", true);
    public Shader() {
        super("Shader", "fancy esp", Category.RENDER, true);
        try {
            shader = new ShaderEffectWrapper(
                    ShaderLoader.loadEffect(mc.getFramebuffer(), new Identifier("bakje", "shaders/post/entity_outline.json")));

            colorVertexer = new ColorVertexConsumerProvider(shader.getFramebuffer("main"), CoreShaders::getColorOverlayShader);
        } catch (JsonSyntaxException | IOException e) {
            throw new RuntimeException("Failed to initialize ESP Shader! loaded too early?", e);
        }
    }

    @BakjeSubscribe
    public void onWorldRender(WorldRenderEvent.Pre event) {
        shader.prepare();
        shader.clearFramebuffer("main");
    }

    @BakjeSubscribe
    public void onEntityRender(EntityRenderEvent.Single.Pre event) {
        int[] color = {0, 0, 0};
        if (event.getEntity()==mc.player) {
            event.setVertex(colorVertexer.createDualProvider(event.getVertex(), color[0], color[1], color[2], this.fill.getValueInt()));
        } else {
            color = new int[]{255, 0, 0};
        }
    }

    @BakjeSubscribe
    public void onWorldRender(WorldRenderEvent.Post event) {
        colorVertexer.draw();
        shader.render();
        shader.drawFramebufferToMain("main");
    }


}
