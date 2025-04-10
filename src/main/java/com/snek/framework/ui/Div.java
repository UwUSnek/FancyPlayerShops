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
    private @NotNull Vector2f   size       = new Vector2f(1, 1);
    private @NotNull Vector2f   pos        = new Vector2f(0, 0);
    private @NotNull AlignmentX alignmentX = AlignmentX.NONE;
    private @NotNull AlignmentY alignmentY = AlignmentY.NONE;

    public void setSize      (@NotNull Vector2f   _size      ) { size.set(_size);          }
    public void setSizeX     (         float      x          ) { size.x = x;               }
    public void setSizeY     (         float      y          ) { size.y = y;               }
    public void scale        (@NotNull Vector2f   _size      ) { size.mul(_size);          }
    public void scaleX       (         float      x          ) { size.x *= x;              }
    public void scaleY       (         float      y          ) { size.y *= y;              }

    public void setPos       (@NotNull Vector2f   _pos       ) { pos.set(_pos);            }
    public void setPosX      (         float      x          ) { pos.x = x;                }
    public void setPosY      (         float      y          ) { pos.y = y;                }
    public void move         (@NotNull Vector2f   _pos       ) { pos.add(_pos);            }
    public void moveX        (         float      x          ) { pos.x += x;               }
    public void moveY        (         float      y          ) { pos.y += y;               }

    public void setAlignmentX(@NotNull AlignmentX _alignmentX) { alignmentX = _alignmentX; }
    public void setAlignmentY(@NotNull AlignmentY _alignmentY) { alignmentY = _alignmentY; }


    public @NotNull Vector2f   getSize      () { return size;       }
    public @NotNull Vector2f   getPos       () { return pos;        }
    public @NotNull AlignmentX getAlignmentX() { return alignmentX; }
    public @NotNull AlignmentY getAlignmentY() { return alignmentY; }







    public Div() {}





    public void addChild(Div elm) {
        children.add(elm);
        elm.parent = this;
    }
    public void removeChild(Div elm) {
        children.remove(elm);
        elm.parent = null;
    }
    public void clearChildren() {
        for(Div elm : children) elm.parent = null;
        children.clear();
    }
    public List<Div> getChildren() {
        return children;
    }




    /**
     * Applies an animation to all the elements of this canvas.
     * Use right rotations to rotate around the center of the canvas.
     * ! Partial steps at the end of the animation are expanded to cover the entire step.
     * @param animation The animation to apply.
     */
    public void applyAnimation(@NotNull Animation animation) {
        for (Div elm : children) {
            elm.applyAnimation(animation);
        }
    }




    /**
     * Instantly applies an animation to all the elements of this canvas, ignoring transition times and easings.
     * Use right rotations to rotate around the center of the canvas.
     * ! Partial steps at the end of the animation are expanded to cover the entire step.
     * @param animation The animation to apply.
     */
    public void applyAnimationNow(@NotNull Animation animation) {
        for (Div elm : children) {
            elm.applyAnimationNow(animation);
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
}
