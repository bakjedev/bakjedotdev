package me.bakje.bakjedev.bakjedev.mixin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.Proxy;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {

    @Shadow @Final private boolean doBackgroundFade;

    @Inject(method = "render", at = @At("RETURN"), cancellable = true)
    private void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        Text drawText = Text.literal("Logged in as " + mc.session.getUsername());
        int x = (mc.getWindow().getWidth()/4) - ((mc.textRenderer.getWidth(drawText) / 2));
        int y = ((mc.currentScreen.height)-mc.textRenderer.fontHeight)-2;
        mc.textRenderer.draw(matrices, drawText, x, y, 0xFFAA00);

    }
}
