package com.snek.fancyplayershops.utils;

import java.beans.SimpleBeanInfo;
import java.util.AbstractMap.SimpleEntry;




/**
 * A class that lets you control scheduled tasks.
 * Instances of this class are returned by the Scheduler's methods.
 */
public class TaskHandler {
    public final Long targetTick;
    private final Runnable task;
    private boolean cancelled = false;


    public TaskHandler(Long _targetTick, Runnable _task) {
        targetTick = _targetTick;
        task = _task;
    }


    /**
     * Marks the task as cancelled.
     * Calling .exec() on cancelled task doesn't run them.
     */
    public void cancel(){
        cancelled = true;
    }


    /**
     * Marks the task as scheduled.
     * This undos any previous calls to .cancel()
     */
    public void schedule(){
        cancelled = false;
    }


    /**
     * Immediately runs the task if it hasn't been cancelled.
     */
    public void exec(){
        if(!cancelled) task.run();
    }
}
