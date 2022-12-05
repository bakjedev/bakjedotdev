package me.bakje.bakjedev.bakjedev.module.misc;

import me.bakje.bakjedev.bakjedev.Bakjedev;
import me.bakje.bakjedev.bakjedev.module.Mod;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Optional;


public class MCF extends Mod {
    private boolean buttonHeld = false;
    public MCF() {
        super("MCF", "fren (:", Mod.Category.MISC, true);
    }

    @Override
    public void onTick() {
        int button = GLFW.GLFW_MOUSE_BUTTON_MIDDLE;

        if (GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), button) == GLFW.GLFW_PRESS && !buttonHeld) {
            buttonHeld = true;

            Optional<Entity> lookingAt = DebugRenderer.getTargetedEntity(mc.player, 200);

            if (lookingAt.isPresent()) {
                Entity e = lookingAt.get();

                if (e instanceof PlayerEntity) {
                    if (Bakjedev.friendMang.has(e)) {
                        Bakjedev.friendMang.remove(e);
                    } else {
                        Bakjedev.friendMang.add(e);
                    }
                }
            }
        } else if (GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), button) == GLFW.GLFW_RELEASE) {
            buttonHeld = false;
        }
    }
}