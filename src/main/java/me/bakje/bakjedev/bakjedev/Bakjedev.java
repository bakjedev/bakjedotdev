package me.bakje.bakjedev.bakjedev;


import me.bakje.bakjedev.bakjedev.ui.screens.clickgui.ClickGui;
import me.bakje.bakjedev.bakjedev.eventbus.BakjeEventBus;
import me.bakje.bakjedev.bakjedev.eventbus.handler.InexactEventHandler;
import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.ModuleManager;
import me.bakje.bakjedev.bakjedev.util.FriendManager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

public class Bakjedev implements ModInitializer {

    public static final Bakjedev INSTANCE = new Bakjedev();
    public static Logger logger = LogManager.getLogger(Bakjedev.class);
    public static final MinecraftClient mc = MinecraftClient.getInstance();
    public static final String VERSION = "1.2";
    public BakjeEventBus eventBus = new BakjeEventBus(new InexactEventHandler("bakjedev"), Bakjedev.logger);
    public static FriendManager friendMang;
    @Override
    public void onInitialize() {
        System.out.println("joe biden");
        friendMang = new FriendManager();
    }
    public void onKeyPress(int key, int action) {
        if (action == GLFW.GLFW_PRESS && mc.currentScreen==null) {
            for (Mod module : ModuleManager.INSTANCE.getModules()) {
                if (key== module.getKey()) module.toggle();
            }

            if (key==GLFW.GLFW_KEY_RIGHT_SHIFT) mc.setScreen(ClickGui.INSTANCE);
        }
    }

    public void onTick() {
        if (mc.player != null) {
            for (Mod module : ModuleManager.INSTANCE.getEnabledModules()) {
                module.onTick();
            }
        }
    }
}
