package com.snek.framework.utils.scheduler;




/**
 * A class that lets you control scheduled tasks.
 * Instances of this class are returned by the Scheduler's methods.
 */
public class TaskHandler {
    private long targetTick;
    public long getTargetTick() { return targetTick; }
    public void setTargetTick(long n) { targetTick = n; }
    protected final Runnable task;
    protected boolean cancelled = false;




    /**
     * Creates a new TaskHandler.
     * @param _targetTick The tick the task is scheduled for.
     * @param _task The task to execute.
     */
    public TaskHandler(long _targetTick, Runnable _task) {
        targetTick = _targetTick;
        task = _task;
    }


    /**
     * Marks the task as cancelled.
     * Calling .exec() on cancelled tasks doesn't run them.
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
    public void compute(){
        if(!cancelled) task.run();
    }
}
