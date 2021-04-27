package me.architetto.fwriv.event.service;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.CircleEffect;
import me.architetto.fwriv.FWRiv;
import me.architetto.fwriv.config.SettingsHandler;
import me.architetto.fwriv.event.EventService;
import me.architetto.fwriv.localization.Message;
import me.architetto.fwriv.obj.timer.Repeater;
import me.architetto.fwriv.partecipant.PartecipantStatus;
import me.architetto.fwriv.partecipant.PartecipantsManager;
import org.bukkit.*;

import java.util.Objects;

public class AntiCamperService {

    private static AntiCamperService instance;

    private EffectManager effectManager;

    private CircleEffect circleEffect;
    private Location centeredLocation;

    private int acY;
    private int acYMax;
    private int acDamage;
    private int acFinalDamage;
    private int acGrowValue;
    private int acPeriod;

    private Repeater repeater;

    private AntiCamperService() {
        if(instance != null)
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");

        this.effectManager = new EffectManager(FWRiv.getPlugin(FWRiv.class));


    }

    public static AntiCamperService getInstance() {
        if(instance == null)
            instance = new AntiCamperService();

        return instance;
    }

    public void initializeAntiCamperSystem() {

        EventService eventService = EventService.getInstance();
        SettingsHandler settingsHandler = SettingsHandler.getInstance();

        this.centeredLocation = eventService.getArena().getCenteredLowestLocation();
        this.centeredLocation.add(0,5,0);
        this.acY = this.centeredLocation.getBlockY();

        this.acYMax = eventService.getArena().getTower().getBlockY() - settingsHandler.getAcMaxYDiff();
        this.acPeriod = settingsHandler.getAcGrowPeriod();
        this.acGrowValue = settingsHandler.getAcGrowValue();

        this.acDamage = settingsHandler.getAcDamage();
        this.acFinalDamage = settingsHandler.getAcFinalDamage();

        startAntiCamperSystem();

    }

    private void startAntiCamperSystem() {
        this.repeater = new Repeater(FWRiv.getPlugin(FWRiv.class),
                SettingsHandler.getInstance().getAcDelay(),
                () -> {
            //
                    this.circleEffect = new CircleEffect(this.effectManager);
                    circleEffect.particle = Particle.REDSTONE;
                    circleEffect.color = Color.RED;
                    circleEffect.particleSize = 15;
                    circleEffect.particleCount = 4;
                    circleEffect.iterations = -1;
                    circleEffect.particles = 50;
                    circleEffect.radius = 18;
                    circleEffect.visibleRange = 100;
                    circleEffect.enableRotation = false;
                    circleEffect.setLocation(this.centeredLocation);
                    circleEffect.start();

                    PartecipantsManager.getInstance().getPartecipantsUUID(PartecipantStatus.PLAYING).stream()
                            .map(Bukkit::getPlayer)
                            .filter(Objects::nonNull)
                            .forEach(p -> Message.ANTICAMPER_START.send(p,this.acY));

                },
                (s) -> {
            //
                    if (s.getTotalSeconds() != 0
                            && acY != acYMax
                            && s.getTotalSeconds() % acPeriod == 0) {
                        acY = Math.min(acYMax, acY + acGrowValue);
                        this.centeredLocation.setY(acY);
                        this.circleEffect.setLocation(this.centeredLocation);
                        if (acY == acYMax) {
                            this.acDamage = this.acFinalDamage;
                            this.circleEffect.color = Color.BLACK;
                            this.centeredLocation.getWorld().playSound(this.centeredLocation, Sound.ENTITY_LIGHTNING_BOLT_THUNDER,10,1);
                            PartecipantsManager.getInstance().getPartecipantsUUID(PartecipantStatus.PLAYING).stream()
                                    .map(Bukkit::getPlayer)
                                    .filter(Objects::nonNull)
                                    .forEach(p -> Message.ANTICAMPER_MAX_HEIGHT.send(p,this.acY));
                        } else
                            PartecipantsManager.getInstance().getPartecipantsUUID(PartecipantStatus.PLAYING).stream()
                                    .map(Bukkit::getPlayer)
                                    .filter(Objects::nonNull)
                                    .forEach(p -> Message.ANTICAMPER_HEIGHT_CHANGE.send(p,this.acY));
                    }

                    PartecipantsManager.getInstance().getPartecipantsUUID(PartecipantStatus.PLAYING).stream()
                            .map(Bukkit::getPlayer)
                            .filter(Objects::nonNull)
                            .filter(p -> p.getLocation().getBlockY() <= this.acY)
                            .forEach(p -> p.damage(this.acDamage));

                });
        this.repeater.scheduleTimer();
    }

    public void stopAntiCamperSystem() {
        if (this.repeater != null) {
            if (this.repeater.getTotalSeconds() != 0)
                this.circleEffect.cancel();
            if (this.repeater.isStarted())
                this.repeater.cancelTimer();
        }

        this.centeredLocation = null;
        this.circleEffect = null;
    }
}
