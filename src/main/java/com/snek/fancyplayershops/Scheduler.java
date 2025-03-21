package com.snek.fancyplayershops;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;

import net.minecraft.server.MinecraftServer;








public class Scheduler {
    public Scheduler() { throw new UnsupportedOperationException("Utility class \"FocusFeatures\" cannot be instantiated"); }
    private static long tickNum = 0;
    private static final PriorityQueue<Map.Entry<Long, Runnable>> taskQueue = new PriorityQueue<>(Comparator.comparingLong(Map.Entry::getKey));





    /*
     * The tick function of the scheduler.
     * Must be called at the end of every server tick.
     */
    public static void tick(MinecraftServer server) {
        while(taskQueue.peek() != null && taskQueue.peek().getKey() <= tickNum) {
            taskQueue.poll().getValue().run();
        }

        tickNum++;
    }




    /**
     * Runs a task on the main thread after a specified delay.
     * @param server The server instance.
     * @param delay The delay, expressed in server ticks.
     * @param task The task to run.
     */
    public static void schedule(int delay, Runnable task) {
        assert delay > 0 : "Delay must be greater than 0";
        taskQueue.add(new AbstractMap.SimpleEntry<>(tickNum + delay, task));
    }
}
