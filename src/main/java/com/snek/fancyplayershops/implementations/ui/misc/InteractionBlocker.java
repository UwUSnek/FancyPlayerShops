package com.snek.fancyplayershops.implementations.ui.misc;

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








/**
 * A special interaction entity that is used by shops to block unwanted players interactions.
 *     This stops client-side clicks on blocks and entities behind the shop block
 *     and client-side item use events, preventing annoying UI flashes and visual artifacts.
 */
public class InteractionBlocker {

    // Private methods of InteractionEntity
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


    // Entity names and command source identifier
    public static final String ENTITY_CUSTOM_NAME               = FancyPlayerShops.MOD_ID + ".ui.interactionblocker";
    public static final String ENTITY_CUSTOM_NAME_UNINITIALIZED = ENTITY_CUSTOM_NAME + ".uninitialized";
    public static final String COMMAND_SOURCE_NAME              = ENTITY_CUSTOM_NAME + ".patch";


    // In-world data
    private final InteractionEntity entity;
    private final Shop shop;




    /**
     * Creates a new InteractionBlocker.
     * @param _shop The target shop.
     */
    public InteractionBlocker(Shop _shop) {
        shop = _shop;
        entity = new InteractionEntity(EntityType.INTERACTION, shop.getWorld());
        Utils.invokeSafe(method_setHeight, entity, 1.01f);
        Utils.invokeSafe(method_setWidth,  entity, 1.01f);
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




    /**
     * Spawns the interaction entity into the world.
     * @param pos The coordinates at which to spawn the entity.
     */
    public void spawn(Vector3d pos) {

        // Spawn the entity, move it to the specified coords and set a temporary name to allow the command to recognize it
        shop.getWorld().spawnEntity(entity);
        entity.setPos(pos.x, pos.y, pos.z);
        entity.setCustomNameVisible(false);
        entity.setCustomName(new Txt(ENTITY_CUSTOM_NAME_UNINITIALIZED).get());


        //!  ╭────────────────────────────────────────────────────────────────────────────────────────────────────────────────╮
        //!  │                                                                                                              //!
        //!  │                        Manually update the entity using a vanilla data modify command.                       //!
        //!  │                                Use a custom source to silence command feedback.                              //!
        //!  │                                                                                                              //!
        //!  │            This workaround is used to fix a 1.20.1 issue that makes interactions spawned by the mod          //!
        //!  │                        unable to detect player clicks until their nbt data is modified.                      //!
        //!  │                                                                                                              //!
        //!  │                                                                                                              //!
        /*!  │    // Create the custom command source and use DUMMY as output to silence it                                 //!
        /*!  │  */MinecraftServer server = entity.getServer();                                                              //!
        /*!  │  */ServerWorld world = (ServerWorld)entity.getWorld();                                                       //!
        /*!  │  */ServerCommandSource source = new ServerCommandSource(                                                     //!
        /*!  │  */    CommandOutput.DUMMY, Vec3d.ZERO, Vec2f.ZERO, world,                                                   //!
        /*!  │  */    4, COMMAND_SOURCE_NAME, new Txt(COMMAND_SOURCE_NAME).get(), server, (Entity)null                      //!
        /*!  │  */);                                                                                                        //!
        //!  │                                                                                                              //!
        //!  │                                                                                                              //!
        /*!  │    // Execute the command using the custom command source                                                    //!
        /*!  │  */try {                                                                                                     //!
        /*!  │  */    server.getCommandManager().getDispatcher().execute(                                                   //!
        /*!  │  */        "execute as @e[type=minecraft:interaction,name=" + ENTITY_CUSTOM_NAME_UNINITIALIZED + "] " +      //!
        /*!  │  */        "run data modify entity @s Air set value 1000",                                                   //!
        /*!  │  */        source                                                                                            //!
        /*!  │  */    );                                                                                                    //!
        /*!  │  */} catch (CommandSyntaxException e) {                                                                      //!
        /*!  │  */    e.printStackTrace();                                                                                  //!
        /*!  │  */}                                                                                                         //!
        //!  │                                                                                                              //!
        //!  ╰────────────────────────────────────────────────────────────────────────────────────────────────────────────────╯


        // Replace the temporary name with the actual custom name. This name is the one that will be used to purge stray interactions
        entity.setCustomName(new Txt(ENTITY_CUSTOM_NAME).get());
    }




    /**
     * Removes the interaction entity from the world
     */
    public void despawn() {
        entity.remove(RemovalReason.KILLED);
    }
}
