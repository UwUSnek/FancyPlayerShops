package com.snek.fancyplayershops.utils;




/**
 * A class that lets you control scheduled loop tasks.
 * Instances of this class are returned by the Scheduler's methods.
 */
public class LoopTaskHandler extends TaskHandler {
    private long interval;
    public long getInterval() { return interval; }


    public LoopTaskHandler(long _targetTick, long _interval, Runnable _task) {
        super(_targetTick, _task);
        interval = _interval;
    }
}
