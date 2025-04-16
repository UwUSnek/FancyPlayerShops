package com.snek.framework.utils.scheduler;








/**
 * A class that lets you control scheduled loop tasks.
 * Instances of this class are returned by the Scheduler's methods.
 */
public class LoopTaskHandler extends TaskHandler {
    private long interval;
    public long getInterval() { return interval; }


    /**
     * Creates a new LoopTaskHandler.
     * @param _targetTick The tick the first iteration of this task is scheduled for, measures in ticks.
     * @param _interval The interval between iterations, measures in ticks.
     * @param _task The task to execute.
     */
    public LoopTaskHandler(long _targetTick, long _interval, Runnable _task) {
        super(_targetTick, _task);
        interval = _interval;
    }
}
