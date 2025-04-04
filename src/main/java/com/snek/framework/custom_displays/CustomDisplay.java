package com.snek.framework.custom_displays;

import java.lang.reflect.Method;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import com.snek.framework.utils.Utils;

import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.decoration.Brightness;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.decoration.DisplayEntity.BillboardMode;
import net.minecraft.text.Text;
import net.minecraft.util.math.AffineTransformation;
import net.minecraft.world.World;








public abstract class CustomDisplay {
    protected @NotNull DisplayEntity heldEntity;


    static private Method method_setTransformation;
    static private Method method_setInterpolationDuration;
    static private Method method_setStartInterpolation;
    static private Method method_setBillboardMode;
    static private Method method_getBillboardMode;
    static private Method method_setViewRange;
    static private Method method_getViewRange;
    static private Method method_setBrightness;
    static private Method method_getBrightness;
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




    public CustomDisplay(@NotNull DisplayEntity _heldEntity) {
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




    public void spawn(@NotNull World world, Vector3d pos) {
        world.spawnEntity(heldEntity);
        heldEntity.setPosition(pos.x, pos.y, pos.z);
    }




    public void despawn() {
        heldEntity.remove(RemovalReason.KILLED);
    }




    public void setTransformation(@NotNull AffineTransformation transformation) {
        Utils.invokeSafe(method_setTransformation, heldEntity, transformation);
    }




    /**
     * @param duration The duration in ticks
     */
    public void setInterpolationDuration(int duration) {
        Utils.invokeSafe(method_setInterpolationDuration, heldEntity, duration);
    }


    public void setStartInterpolation() {
        Utils.invokeSafe(method_setStartInterpolation, heldEntity, 0);
    }


    public void setBillboardMode(@NotNull BillboardMode billboardMode) {
        Utils.invokeSafe(method_setBillboardMode, heldEntity, billboardMode);
    }


    public BillboardMode setBillboardMode() {
        return (BillboardMode)Utils.invokeSafe(method_getBillboardMode, heldEntity);
    }


    public void setViewRange(float viewRange) {
        Utils.invokeSafe(method_setViewRange, heldEntity, viewRange);
    }


    public float setViewRange() {
        return (float)Utils.invokeSafe(method_getViewRange, heldEntity);
    }


    public void setBrightness(@NotNull Brightness brightness) {
        Utils.invokeSafe(method_setBrightness, heldEntity, brightness);
    }


    public Brightness setBrightness() {
        return (Brightness)Utils.invokeSafe(method_getBrightness, heldEntity);
    }


    public void setCustomName(@NotNull Text name) {
        heldEntity.setCustomName(name);
    }


    public void setCustomNameVisible(boolean nameVisible) {
        heldEntity.setCustomNameVisible(nameVisible);
    }


    public void setGlowing(boolean glowing) {
        heldEntity.setGlowing(glowing);
    }
}
