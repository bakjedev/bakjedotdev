package me.bakje.bakjedev.bakjedev.module.Render;

import me.bakje.bakjedev.bakjedev.event.events.ParticleEvent;
import me.bakje.bakjedev.bakjedev.eventbus.BakjeSubscribe;
import me.bakje.bakjedev.bakjedev.module.Mod;
import me.bakje.bakjedev.bakjedev.module.ModuleManager;
import me.bakje.bakjedev.bakjedev.module.Settings.BooleanSetting;
import net.minecraft.client.particle.CampfireSmokeParticle;
import net.minecraft.client.particle.ElderGuardianAppearanceParticle;
import net.minecraft.client.particle.ExplosionLargeParticle;
import net.minecraft.client.particle.FireworksSparkParticle;
import net.minecraft.particle.ParticleTypes;

public class NoRender extends Mod {
    public BooleanSetting particles = new BooleanSetting("Particles", true);
    public BooleanSetting bossbar = new BooleanSetting("Bossbar", true);
    public BooleanSetting fog = new BooleanSetting("Fog", true);
    public BooleanSetting effecthud = new BooleanSetting("EffectHud", true);
    public BooleanSetting hurtcam = new BooleanSetting("Hurtcam", true);
    public BooleanSetting overlay = new BooleanSetting("Overlay", true);



    public NoRender() {
        super("NoRender", "removes bunch of things", Category.RENDER, true);
        addSettings(fog,particles,bossbar,effecthud,hurtcam, overlay);
    }

    @BakjeSubscribe
    public void onParticle(ParticleEvent.Normal event) {
        if (this.particles.isEnabled() && event.getParticle() instanceof ExplosionLargeParticle) event.setCancelled(true);
    }

    @BakjeSubscribe
    public void onParticleEmitter(ParticleEvent.Emitter event) {
        if (this.particles.isEnabled() && event.getEffect().getType() == ParticleTypes.TOTEM_OF_UNDYING) event.setCancelled(true);
    }
}
