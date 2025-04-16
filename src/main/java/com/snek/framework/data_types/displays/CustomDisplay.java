package com.snek.framework.data_types.displays;

import java.lang.reflect.Method;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3f;

import com.snek.framework.utils.Utils;

import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.decoration.Brightness;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.decoration.DisplayEntity.BillboardMode;
import net.minecraft.text.Text;
import net.minecraft.util.math.AffineTransformation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;








/**
 * An abstract wrapper for Minecraft's Display Entities.
 * This class allows for better customization and more readable code.
 */
public abstract class CustomDisplay {
    protected @NotNull DisplayEntity heldEntity;


    // Private methods
    private static Method method_setTransformation;
    private static Method method_setInterpolationDuration;
    private static Method method_setStartInterpolation;
    private static Method method_setBillboardMode;
    private static Method method_getBillboardMode;
    private static Method method_setViewRange;
    private static Method method_getViewRange;
    private static Method method_setBrightness;
    private static Method method_getBrightness;
    static {
        try {
            method_setTransformation        = DisplayEntity.class.getDeclaredMethod("setTransformation", AffineTransformation.class);
            method_setInterpolationDuration = DisplayEntity.class.getDeclaredMethod("setInterpolationDuration",           int.class);
            method_setStartInterpolation    = DisplayEntity.class.getDeclaredMethod("setStartInterpolation",              int.class);
            method_setBillboardMode         = DisplayEntity.class.getDeclaredMethod("setBillboardMode",         BillboardMode.class);
            method_getBillboardMode         = DisplayEntity.class.getDeclaredMethod("getBillboardMode");
            method_setViewRange             = DisplayEntity.class.getDeclaredMethod("setViewRange",                     float.class);
            method_getViewRange             = DisplayEntity.class.getDeclaredMethod("getViewRange");
            method_setBrightness            = DisplayEntity.class.getDeclaredMethod("setBrightness",               Brightness.class);
            method_getBrightness            = DisplayEntity.class.getDeclaredMethod("getBrightnessUnpacked");
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        method_setTransformation.setAccessible(true);
        method_setInterpolationDuration.setAccessible(true);
        method_setStartInterpolation.setAccessible(true);
        method_setBillboardMode.setAccessible(true);
        method_getBillboardMode.setAccessible(true);
        method_setViewRange.setAccessible(true);
        method_getViewRange.setAccessible(true);
        method_setBrightness.setAccessible(true);
        method_getBrightness.setAccessible(true);
    }




    /**
     * Creates a new CustomDisplay using an existing DisplayEntity as in-world entity.
     * @param _heldEntity The display entity.
     */
    protected CustomDisplay(@NotNull DisplayEntity _heldEntity) {
        heldEntity = _heldEntity;
        setBrightness(new Brightness(15, 15));
    }




    /**
     * Returns the UUID of the raw display entity.
     * @return The UUID.
     */
    public UUID getUuid(){
        return heldEntity.getUuid();
    }




    /**
     * Spawns the entity into the world.
     * @param world The world to spawn the entity in.
     * @param pos The position of the spawned entity.
     */
    public void spawn(@NotNull World world, Vector3d pos) {
        heldEntity.setPosition(pos.x, pos.y, pos.z);
        world.spawnEntity(heldEntity);
    }


    /**
     * Removes the entity from the world.
     */
    public void despawn() {
        heldEntity.remove(RemovalReason.KILLED);
    }








    /**
     * Sets a new transformation value to the entity.
     * This is equivalent to changing the entity's "transformation" NBT.
     * @param transformation The new value.
     */
    public void setTransformation(@NotNull AffineTransformation transformation) {
        Utils.invokeSafe(method_setTransformation, heldEntity, transformation);
    }


    /**
     * Sets a new interpolation duration value to the entity.
     * This is equivalent to changing the entity's "interpolation_duration" NBT.
     * @param duration The new value, measured in ticks
     */
    public void setInterpolationDuration(int duration) {
        Utils.invokeSafe(method_setInterpolationDuration, heldEntity, duration);
    }


    /**
     * Starts the interpolation at the end of the current tick.
     * This is equivalent to setting the entity's "start_interpolation" NBT to 0.
     */
    public void setStartInterpolation() {
        Utils.invokeSafe(method_setStartInterpolation, heldEntity, 0);
    }


    /**
     * Sets a new billboard mode value to the entity.
     * This is equivalent to changing the entity's "billboard" NBT.
     * @param billboardMode The new value.
     */
    public void setBillboardMode(@NotNull BillboardMode billboardMode) {
        Utils.invokeSafe(method_setBillboardMode, heldEntity, billboardMode);
    }


    /**
     * Retrieves the entity's billboard mode.
     * @return The current billboard mode.
     */
    public BillboardMode getBillboardMode() {
        return (BillboardMode)Utils.invokeSafe(method_getBillboardMode, heldEntity);
    }


    /**
     * Sets a new view range value to the entity.
     * This is equivalent to changing the entity's "view_range" NBT.
     * The maximum distance the entity is visible at is (view_range * 64) blocks.
     * @param viewRange The new value.
     */
    public void setViewRange(float viewRange) {
        Utils.invokeSafe(method_setViewRange, heldEntity, viewRange);
    }


    /**
     * Retrieves the entity's view range.
     * The maximum distance the entity is visible at is (view_range * 64) blocks.
     * @return The current view range.
     */
    public float getViewRange() {
        return (float)Utils.invokeSafe(method_getViewRange, heldEntity);
    }


    /**
     * Sets a new brightness value to the entity.
     * This is equivalent to changing the entity's "brightness" NBT.
     * @param brightness
     */
    public void setBrightness(@NotNull Brightness brightness) {
        Utils.invokeSafe(method_setBrightness, heldEntity, brightness);
    }


    /**
     * Retrieves the entity's brightness.
     * @return The current brightness.
     */
    public Brightness getBrightness() {
        return (Brightness)Utils.invokeSafe(method_getBrightness, heldEntity);
    }


    /**
     * Sets a new custom name value to the entity.
     * This is equivalent to changing the entity's "custom_name" NBT.
     * @param name The new value.
     */
    public void setCustomName(@NotNull Text name) {
        heldEntity.setCustomName(name);
    }


    /**
     * Sets a new custom name visible value to the entity.
     * This is equivalent to changing the entity's "custom_name_visible" NBT.
     * @param name The new value.
     */
    public void setCustomNameVisible(boolean nameVisible) {
        heldEntity.setCustomNameVisible(nameVisible);
    }


    /**
     * Sets a new glowing value to the entity.
     * @param name The new value. True makes the entity glow. False makes it stop glowing.
     */
    public void setGlowing(boolean glowing) {
        heldEntity.setGlowing(glowing);
    }


    /**
     * Sets a new position to the entity.
     * @param pos The new position.
     */
    public void setPos(Vector3f pos) {
        heldEntity.setPosition(new Vec3d(pos));
    }


    /**
     * Retrieves the entity's position and returns a copy of it.
     * @return A copy of the current position.
     */
    public Vector3f getPosCopy() {
        return heldEntity.getPos().toVector3f();
    }
}
