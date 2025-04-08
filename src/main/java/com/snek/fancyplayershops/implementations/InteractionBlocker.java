package com.snek.fancyplayershops.implementations;

import java.lang.reflect.Method;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.snek.fancyplayershops.FancyPlayerShops;
import com.snek.fancyplayershops.Shop;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.decoration.InteractionEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;








public class InteractionBlocker {
    private static Method method_setWidth;
    private static Method method_setHeight;
    static {
        try {
            method_setWidth  = InteractionEntity.class.getDeclaredMethod("setInteractionWidth",  float.class);
            method_setHeight = InteractionEntity.class.getDeclaredMethod("setInteractionHeight", float.class);
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        method_setWidth.setAccessible(true);
        method_setHeight.setAccessible(true);
    }


    public static final String ENTITY_CUSTOM_NAME               = FancyPlayerShops.MOD_ID + ".ui.interactionblocker";
    public static final String ENTITY_CUSTOM_NAME_UNINITIALIZED = ENTITY_CUSTOM_NAME + ".uninitialized";
    public static final String COMMAND_SOURCE_NAME              = ENTITY_CUSTOM_NAME + ".patch";
    private final InteractionEntity entity;
    private final Shop shop;




    public InteractionBlocker(Shop _shop) {
        shop = _shop;
        entity = new InteractionEntity(EntityType.INTERACTION, shop.getWorld());
        Utils.invokeSafe(method_setHeight, entity, 0.9f);
        Utils.invokeSafe(method_setWidth, entity, 0.9f);
    }




    /**
     * Checks for stray interaction entities and purges them.
     * Must be called on entity load event.
     * @param entity The entity.
     */
    public static void onEntityLoad(@NotNull Entity entity) {
        if (entity instanceof InteractionEntity) {
            World world = entity.getWorld();
            if(
                world != null &&
                entity.getCustomName() != null &&
                entity.getCustomName().getString().equals(ENTITY_CUSTOM_NAME)
            ) {
                entity.remove(RemovalReason.KILLED);
            }
        }
    }



    public void spawn(Vector3d pos) {
        shop.getWorld().spawnEntity(entity);
        entity.setPos(pos.x, pos.y, pos.z);
        entity.setCustomNameVisible(false);
        entity.setCustomName(new Txt(ENTITY_CUSTOM_NAME_UNINITIALIZED).get());


        //!  ╭────────────────────────────────────────────────────────────────────────────────────────────────────────────────╮
        //!  │                                                                                                                │
        //!  │                        Manually update the entity using a vanilla data modify command.                         │
        //!  │                                Use a custom source to silence command feedback.                                │
        //!  │                                                                                                                │
        //!  │            This workaround is used to fix a 1.20.1 issue that makes interactions spawned by the mod            │
        //!  │                        unable to detect player clicks until their nbt data is modified.                        │
        //!  │                                                                                                                │
        //!  │                                                                                                                │
        /*!  │  */try {                                                                                                  //!  │
        /*!  │  */    MinecraftServer server = entity.getServer();                                                       //!  │
        /*!  │  */    ServerWorld world = (ServerWorld)entity.getWorld();                                                //!  │
        /*!  │  */    ServerCommandSource source = new ServerCommandSource(                                              //!  │
        /*!  │  */        CommandOutput.DUMMY, Vec3d.ZERO, Vec2f.ZERO, world,                                            //!  │
        /*!  │  */        4, COMMAND_SOURCE_NAME, new Txt(COMMAND_SOURCE_NAME).get(), server, (Entity)null               //!  │
        /*!  │  */    );                                                                                                 //!  │
        /*!  │  */    server.getCommandManager().getDispatcher().execute(                                                //!  │
        /*!  │  */        "execute as @e[type=minecraft:interaction,name=" + ENTITY_CUSTOM_NAME_UNINITIALIZED + "] " +   //!  │
        /*!  │  */        "run data modify entity @s Air set value 1000",                                                //!  │
        /*!  │  */        source                                                                                         //!  │
        /*!  │  */    );                                                                                                 //!  │
        /*!  │  */    entity.setCustomName(new Txt(ENTITY_CUSTOM_NAME).get());                                           //!  │
        /*!  │  */} catch (CommandSyntaxException e) {                                                                   //!  │
        /*!  │  */    e.printStackTrace();                                                                               //!  │
        /*!  │  */}                                                                                                      //!  │
        //!  │                                                                                                                │
        //!  ╰────────────────────────────────────────────────────────────────────────────────────────────────────────────────╯
    }



    public void despawn() {
        entity.remove(RemovalReason.KILLED);
    }
}
