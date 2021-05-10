package me.architetto.fwriv.particles;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.CircleEffect;
import me.architetto.fwriv.FWRiv;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;

public class ParticlesManager {

    private static ParticlesManager instance;

    private EffectManager effectManager;

    private ParticlesManager() {
        if(instance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }

        this.effectManager = new EffectManager(FWRiv.getPlugin(FWRiv.class));

    }

    public static ParticlesManager getInstance() {
        if(instance == null) {
            instance = new ParticlesManager();
        }
        return instance;
    }

    public void arenaPointEffect(Location loc, int iterations) {
        CircleEffect circleEffect = new CircleEffect(this.effectManager);
        circleEffect.particle = Particle.REDSTONE;
        circleEffect.color = Color.YELLOW;
        circleEffect.radius = 0.4f;
        circleEffect.particles = 10;
        circleEffect.wholeCircle = true;
        circleEffect.enableRotation = false;
        circleEffect.iterations = iterations;
        circleEffect.setLocation(loc);
        circleEffect.start();
    }

    public EffectManager getEffectManager() {
        return effectManager;
    }
}
