package com.snek.framework.ui;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector3d;

import com.snek.framework.data_types.animations.Animation;
import com.snek.framework.data_types.ui.AlignmentX;
import com.snek.framework.data_types.ui.AlignmentY;
import com.snek.framework.ui.elements.Elm;
import com.snek.framework.ui.interfaces.Clickable;
import com.snek.framework.ui.interfaces.Hoverable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ClickType;
















/**
 * The most basic UI element. It can contain and manage any amount of UI elements.
 * By default, divs are invisible and don't exist in the minecraft world nor on the client.
 * They have a 2D size, a 2D position and alignment options.
 */
public class Div {


    // Tree data
    protected @Nullable Div parent = null;
    protected final @NotNull List<Div> children = new ArrayList<>();
    public void setParent(@Nullable Div _parent) { parent = _parent; }
    public @Nullable Div getLastChild() {
        int s = children.size();
        return s < 0 ? null : children.get(s - 1);
    }




    // UI data
    private @NotNull Vector2f   localSize  = new Vector2f(1, 1);
    private @NotNull Vector2f   localPos   = new Vector2f(0, 0);

    private @NotNull Vector2f   absSize    = new Vector2f(1, 1);
    private @NotNull Vector2f   absPos     = new Vector2f(0, 0);

    private @NotNull AlignmentX alignmentX = AlignmentX.NONE;
    private @NotNull AlignmentY alignmentY = AlignmentY.NONE;

    private int zIndex = 0;







    public Div() {
        // Empty
    }





    /**
     * Adds a child to this Div.
     * @param elm The new element.
     * @return elm
     */
    public Div addChild(Div elm) {
        elm.parent = this;
        elm.updateAbsPos();
        elm.updateZIndex();
        children.add(elm);
        return elm;
    }

    /**
     * Removes a child from this Div.
     * @param elm The removed element.
     * @return elm
     */
    public Div removeChild(Div elm) {
        elm.parent = null;
        elm.updateAbsPos();
        elm.updateZIndex();
        children.remove(elm);
        return elm;
    }

    /**
     * Removes all children from this Div.
     */
    public void clearChildren() {
        for(Div elm : children) {
            elm.parent = null;
            elm.updateAbsPos();
            elm.updateZIndex();
        }
        children.clear();
    }

    /**
     * Returns the list of children.
     * @return The list of children.
     */
    public List<Div> getChildren() {
        return children;
    }




    /**
     * Applies an animation to this element.
     * ! Partial steps at the end of the animation are expanded to cover the entire step.
     * @param animation The animation to apply.
     */
    public void applyAnimation(@NotNull Animation animation) {
        // for (Div elm : children) {
        //     elm.applyAnimation(animation);
        // }
    }




    /**
     * Instantly applies an animation to this element, ignoring transition times and easings.
     * ! Partial steps at the end of the animation are expanded to cover the entire step.
     * @param animation The animation to apply.
     */
    public void applyAnimationNow(@NotNull Animation animation) {
        // for (Div elm : children) {
        //     elm.applyAnimationNow(animation);
        // }
    }




    /**
     * Applies an animation to this element and all of its children.
     * ! Partial steps at the end of the animation are expanded to cover the entire step.
     * @param animation The animation to apply.
     */
    public void applyAnimationRecursive(@NotNull Animation animation) {
        applyAnimation(animation);
        for (Div elm : children) {
            elm.applyAnimation(animation);
            elm.applyAnimationRecursive(animation);
        }
    }




    /**
     * Instantly applies an animation to this element and all of its children, ignoring transition times and easings.
     * ! Partial steps at the end of the animation are expanded to cover the entire step.
     * @param animation The animation to apply.
     */
    public void applyAnimationNowRecursive(@NotNull Animation animation) {
        applyAnimationNow(animation);
        for (Div elm : children) {
            elm.applyAnimationNow(animation);
            elm.applyAnimationNowRecursive(animation);
        }
    }




    /**
     * Forwards a click to all the clickable elements of this canvas.
     * This method stops at the first element that consumes a click.
     * @param player The player that clicked.
     * @param clickType The type of click.
     */
    public void forwardClick(PlayerEntity player, ClickType clickType) {
        for (Div elm : children) {
            if(elm instanceof Clickable e) {
                if(e.onClick(player, clickType)) return;
                else elm.forwardClick(player, clickType);
            }
            else elm.forwardClick(player, clickType);
        }
    }




    /**
     * Forwards a hover event to all the hoverable elements of this canvas.
     * @param player The player that clicked.
     */
    public void forwardHover(PlayerEntity player) {
        for (Div elm : children) {
            if(elm instanceof Hoverable hoverableElm && hoverableElm instanceof Elm e) {
                e.updateHoverStatus(player);
            }
            elm.forwardHover(player);
        }
    }




    public void spawn(Vector3d pos){
        for (Div elm : children) {
            elm.spawn(pos);
        }
    }




    public void despawn(){
        for (Div elm : children) {
            elm.despawn();
        }
    }




    public void despawnNow(){
        for (Div elm : children) {
            elm.despawnNow();
        }
    }









    protected void updateAbsSize() {
        absSize.set(localSize);
        for (Div e : children) e.updateAbsSize();
        // size.set(parent == null ? localSize : new Vector2f(localSize).mul(parent.getSize()));
        //TODO check if an "absolute size" is needed. remove this function if not
    }

    public void setSize(@NotNull Vector2f _size) {
        localSize.set(_size);
        // for (Div c : children) c.setSize(_size);
        updateAbsSize();
    }

    public void setSizeX(float x) {
        localSize.x = x;
        // for (Div c : children) c.setSizeX(x);
        updateAbsSize();
    }

    public void setSizeY(float y) {
        localSize.y = y;
        // for (Div c : children) c.setSizeY(y);
        updateAbsSize();
    }

    public void scale(@NotNull Vector2f _size) {
        localSize.mul(_size);
        // for (Div c : children) c.scale(_size);
        updateAbsSize();
    }

    public void scaleX(float x) {
        localSize.x *= x;
        // for (Div c : children) c.scaleX(x);
        updateAbsSize();
    }

    public void scaleY(float y) {
        localSize.y *= y;
        // for (Div c : children) c.scaleY(y);
        updateAbsSize();
    }




    protected void updateZIndex() {
        zIndex = parent == null ? 0 : parent.zIndex + parent.getLayerCount();
        for (Div c : children) c.updateZIndex();
    }
    public int getZIndex() {
        return zIndex;
    }
    /**
     * Returns the amount of Z-Layers occupied by this element.
     * @return The amount of Z-Layers.
     */
    public int getLayerCount() {
        return 0;
    }




    protected void updateAbsPos() {
        // Calculate unrestricted position
        Vector2f p = parent == null ? new Vector2f(0, 0) : parent.getAbsPos();
        Vector2f s = parent == null ? new Vector2f(1, 1) : parent.getAbsSize();

        // Apply horizontal alignment
        float x = switch(alignmentX) {
            case LEFT   -> p.x - (s.x - localSize.x) / 2;
            case RIGHT  -> p.x + (s.x - localSize.x) / 2;
            case CENTER -> p.x;
            case NONE   -> p.x + localPos.x;
        };

        // Apply vertical alignment
        float y = switch(alignmentY) {
            case TOP    -> p.y - (s.y - localSize.y) / 2;
            case BOTTOM -> p.y + (s.y - localSize.y) / 2;
            case CENTER -> p.y;
            case NONE   -> p.y + localPos.y;
        };

        // Update the value and run the function on all children
        absPos.set(x, y);
        for (Div c : children) c.updateAbsPos();
    }


    public void setPos(@NotNull Vector2f _pos) {
        localPos.set(_pos);
        updateAbsPos();
        // for (Div c : children) c.setPos(_pos);
    }

    public void setPosX(float x) {
        localPos.x = x;
        updateAbsPos();
        // for (Div c : children) c.setPosX(x);
    }

    public void setPosY(float y) {
        localPos.y = y;
        updateAbsPos();
        // for (Div c : children) c.setPosY(y);
    }

    public void move(@NotNull Vector2f _pos) {
        localPos.add(_pos);
        updateAbsPos();
        // for (Div c : children) c.move(_pos);
    }

    public void moveX(float x) {
        localPos.x += x;
        updateAbsPos();
        // for (Div c : children) c.moveX(x);
    }

    public void moveY(float y) {
        localPos.y += y;
        updateAbsPos();
        // for (Div c : children) c.moveY(y);
    }




    public void setAlignmentX(@NotNull AlignmentX _alignmentX) {
        alignmentX = _alignmentX;
        updateAbsPos();
    }

    public void setAlignmentY(@NotNull AlignmentY _alignmentY) {
        alignmentY = _alignmentY;
        updateAbsPos();
    }




    public @NotNull Vector2f   getLocalSize () { return localSize;  }
    public @NotNull Vector2f   getLocalPos  () { return localPos;   }

    public @NotNull Vector2f   getAbsSize   () { return absSize;  }
    public @NotNull Vector2f   getAbsPos    () { return absPos;   }

    public @NotNull AlignmentX getAlignmentX() { return alignmentX; }
    public @NotNull AlignmentY getAlignmentY() { return alignmentY; }
}
