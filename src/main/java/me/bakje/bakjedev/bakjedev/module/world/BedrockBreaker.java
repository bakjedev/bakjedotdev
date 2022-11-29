package me.bakje.bakjedev.bakjedev.module.world;

import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.util.bedrockUtil.BreakingFlowController;


public class BedrockBreaker extends Mod {

    public BedrockBreaker() {
        super("BedrockBreaker", "chinese bedrock breaker mod", Category.WORLD, true);
    }

    @Override
    public void onEnable() {
        BreakingFlowController.switchOnOff();
    }

    @Override
    public void onDisable() {
        BreakingFlowController.switchOnOff();
    }
}
