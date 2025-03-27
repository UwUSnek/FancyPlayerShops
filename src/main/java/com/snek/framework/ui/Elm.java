package com.snek.framework.ui;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.Shop;
import com.snek.framework.custom_displays.CustomDisplay;
import com.snek.framework.data_types.Animation;
import com.snek.framework.data_types.Flagged;
import com.snek.framework.data_types.Transform;
import com.snek.framework.ui.styles.ElmStyle;
import com.snek.framework.utils.Scheduler;

import net.minecraft.entity.decoration.DisplayEntity.BillboardMode;
import net.minecraft.server.world.ServerWorld;








public abstract class Elm {

    private @NotNull  Flagged<Transform>     transform;
    private @NotNull  Flagged<Float>         viewRange;
    private @NotNull  Flagged<BillboardMode> billboardMode;

    // private boolean isSpawningScheduled   = false;
    // private boolean isDespawningScheduled = false;


    // Element list used to update UIs
    private static final @NotNull List<Elm> elmList = new ArrayList<>();

    // Tree data
    private @Nullable Elm parent = null;
    private @NotNull  List<@NotNull Elm> children = new ArrayList<>();

    // In-world data
    protected @NotNull ServerWorld   world;
    protected @NotNull CustomDisplay entity;
    protected @NotNull ElmStyle      style;




    protected Elm(@NotNull ServerWorld _world, CustomDisplay _entity, @NotNull ElmStyle _style) {
        world  = _world;
        entity = _entity;
        style  = _style;
    }




    /**
     * Flushes changeable style values to the entity.
     * This does not start an interpolation.
     */
    public void flushStyle() {
        if(transform    .isFlagged()) entity.setTransformation(transform    .get().get()); transform    .unflag();
        if(viewRange    .isFlagged()) entity.setViewRange     (viewRange    .get()      ); viewRange    .unflag();
        if(billboardMode.isFlagged()) entity.setBillboardMode (billboardMode.get()      ); billboardMode.unflag();
    }




    public CustomDisplay getEntity() {
        return entity;
    }




    /**
     * Adds a child to this Elm, then sets it's parent to this.
     * @param child The new child.
     */
    public void addChild(Elm child) {
        children.add(child);
        child.parent = this;
    }


    /**
     * Returns the list of children of this Elm.
     * @return The list of children.
     */
    public List<Elm> getChildren() {
        return children;
    }




    /**
     * Spawns the element and its associated entities into the world.
     */
    public void spawn() {
        entity.spawn(world);
    }




    /**
     * Removes the element and its associated entities from the world.
     */
    public void despawn() {
        Scheduler.schedule(style.getDespawnAnimation().getTotalDuration(), () -> {
            entity.despawn();
        });
    }




    /**
     * Processes transitions and other tick features of this Elm and all of its children, recursively.
     * Must be called at the end of each tick.
     */
    public void tick() {
       flushStyle();
    }
}
