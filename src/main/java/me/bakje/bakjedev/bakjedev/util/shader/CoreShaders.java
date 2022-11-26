package me.bakje.bakjedev.bakjedev.util.shader;

import net.minecraft.client.render.Shader;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

import java.io.IOException;

public class CoreShaders {
    private static final Shader COLOR_OVERLAY_SHADER;

    public static Shader getColorOverlayShader() {
        return COLOR_OVERLAY_SHADER;
    }

    static {
        try {
            COLOR_OVERLAY_SHADER = ShaderLoader.load(VertexFormats.POSITION_COLOR_TEXTURE, new Identifier("bakje", "color_overlay.json"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to initilize BleachHack core shaders", e);
        }
    }
}
