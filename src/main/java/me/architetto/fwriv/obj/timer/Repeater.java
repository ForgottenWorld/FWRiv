package me.architetto.fwriv.obj.timer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

public class Repeater implements Runnable {

    // Main class for bukkit scheduling
    private JavaPlugin plugin;

    // Our scheduled task's assigned id, needed for canceling
    private Integer assignedTaskId;

    // Seconds and shiz
    private int totalSeconds;
    private long delay;

    private boolean isStarted = false;

    // Actions to perform while counting down, before and after
    private Consumer<Repeater> everySecond;
    private Runnable beforeTimer;

    // Construct a timer, you could create multiple so for example if
    // you do not want these "actions"
    public Repeater(JavaPlugin plugin, long delay,
                    Runnable beforeTimer,
                    Consumer<Repeater> everySecond) {
        // Initializing fields
        this.plugin = plugin;

        this.delay = delay;
        this.totalSeconds = 0;

        this.beforeTimer = beforeTimer;
        this.everySecond = everySecond;
    }

    /**
     * Runs the timer once, decrements seconds etc...
     * Really wish we could make it protected/private so you couldn't access it
     */
    @Override
    public void run() {

        // Are we just starting?
        if (totalSeconds == 0) beforeTimer.run();

        // Do what's supposed to happen every second
        everySecond.accept(this);

        // Decrement the seconds left
        totalSeconds++;
    }

    /**
     * Gets the total seconds this timer is running
     *
     * @return Total seconds timer is running
     */
    public int getTotalSeconds() {
        return totalSeconds;
    }

    /**
     * Schedules this instance to "run" every second
     */
    public void scheduleTimer() {
        // Initialize our assigned task's id, for later use so we can cancel
        this.assignedTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, this.delay, 20L);
        this.isStarted = true;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void cancelTimer() {
        Bukkit.getScheduler().cancelTask(this.assignedTaskId);
        this.isStarted = false;
        this.totalSeconds = 0;
    }


}
