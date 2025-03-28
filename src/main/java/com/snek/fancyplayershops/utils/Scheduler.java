package com.snek.fancyplayershops.utils;

import java.util.Comparator;
import java.util.PriorityQueue;

import net.minecraft.server.MinecraftServer;








public class Scheduler {
    public Scheduler() { throw new UnsupportedOperationException("Utility class \"FocusFeatures\" cannot be instantiated"); }
    private static long tickNum = 0;
    private static final PriorityQueue<TaskHandler> taskQueue = new PriorityQueue<>(Comparator.comparingLong(e -> e.getTargetTick()));





    /*
     * The tick function of the scheduler.
     * Must be called exactly one time at the end of every server tick.
     */
    public static void tick(MinecraftServer server) {
        while(taskQueue.peek() != null && taskQueue.peek().getTargetTick() <= tickNum) {
            TaskHandler handler = taskQueue.poll();
            handler.compute();

            if(handler instanceof LoopTaskHandler h && !h.cancelled) {
                h.setTargetTick(tickNum + h.getInterval());
                taskQueue.add(h);
            }
        }

        tickNum++;
    }




    /**
     * Runs a task on the main thread every <interval> ticks after a specified delay.
     * @param delay The initial delay, expressed in server ticks.
     * @param delay The time interval between calls, expressed in server ticks.
     * @param task The task to run.
     * @return The handler of the newly created task schedule.
     */
    public static TaskHandler loop(long delay, long interval, Runnable task) {
        TaskHandler handler = new LoopTaskHandler(tickNum + delay, interval, task);
        taskQueue.add(handler);
        return handler;
    }




    /**
     * Runs a task on the main thread after a specified delay.
     * @param delay The delay, expressed in server ticks.
     * @param task The task to run.
     * @return The handler of the newly created task schedule.
     */
    public static TaskHandler schedule(long delay, Runnable task) {
        TaskHandler handler = new TaskHandler(tickNum + delay, task);
        taskQueue.add(handler);
        return handler;
    }




    /**
     * Runs a task on the main thread at the end of the current tick. Equivalent to calling TaskHandler.schedule with delay 0.
     * @param task The task to run.
     * @return The handler of the newly created task schedule.
     */
    public static TaskHandler run(Runnable task) {
        return schedule(0, task);
    }
}
