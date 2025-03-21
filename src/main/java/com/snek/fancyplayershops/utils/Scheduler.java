package com.snek.fancyplayershops.utils;

import java.util.Comparator;
import java.util.PriorityQueue;

import net.minecraft.server.MinecraftServer;








public class Scheduler {
    public Scheduler() { throw new UnsupportedOperationException("Utility class \"FocusFeatures\" cannot be instantiated"); }
    private static long tickNum = 0;
    private static final PriorityQueue<TaskHandler> taskQueue = new PriorityQueue<>(Comparator.comparingLong(e -> e.targetTick));





    /*
     * The tick function of the scheduler.
     * Must be called exactly one time at the end of every server tick.
     */
    public static void tick(MinecraftServer server) {
        while(taskQueue.peek() != null && taskQueue.peek().targetTick <= tickNum) {
            taskQueue.poll().exec();
        }

        tickNum++;
    }




    /**
     * Runs a task on the main thread after a specified delay.
     * @param delay The delay, expressed in server ticks.
     * @param task The task to run.
     * @return The handler of the newly created task schedule.
     */
    public static TaskHandler schedule(int delay, Runnable task) {
        assert delay > 0 : "Delay must be greater than 0";
        TaskHandler handler = new TaskHandler(tickNum + delay, task);
        taskQueue.add(handler);
        return handler;
    }



    //TODO add .repeat(). it repeates the task every n ticks after m ticks of delay until it gets explicitly cancelled
}
