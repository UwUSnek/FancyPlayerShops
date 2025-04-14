package com.snek.fancyplayershops.implementations.ui;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

import com.snek.fancyplayershops.FancyPlayerShops;
import com.snek.fancyplayershops.Shop;
import com.snek.framework.ui.elements.FancyTextElm;
import com.snek.framework.ui.elements.PanelElm;
import com.snek.framework.utils.Txt;

import net.minecraft.entity.Entity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity;
import net.minecraft.world.World;








//FIXME unify entity purging and move the methods to Elm
//FIXME unify entity purging and move the methods to Elm
//FIXME unify entity purging and move the methods to Elm
//FIXME unify entity purging and move the methods to Elm

public class ShopPanelElm extends PanelElm {
    // private static final String ENTITY_CUSTOM_NAME = FancyPlayerShops.MOD_ID + ".ui.panel";
    protected final @NotNull Shop shop;


    protected ShopPanelElm(@NotNull Shop _shop) {
        super(_shop.getWorld());
        shop = _shop;
        // style.editTransform().moveZ(-0.0001f);
        // flushStyle();
    }


    // @Override
    // public void spawn(Vector3d pos){
    //     super.spawn(pos);
    //     entity.setCustomNameVisible(false);
    //     entity.setCustomName(new Txt(ENTITY_CUSTOM_NAME).get());
    // }


    // /**
    //  * Checks for stray tracked panels and purges them.
    //  * Must be called on entity load event.
    //  * @param entity The entity.
    //  */
    // public static void onEntityLoad(@NotNull Entity entity) {
    //     if (entity instanceof TextDisplayEntity) {
    //         World world = entity.getWorld();
    //         if(
    //             world != null &&
    //             entity.getCustomName() != null &&
    //             entity.getCustomName().getString().equals(ENTITY_CUSTOM_NAME)
    //         ) {
    //             entity.remove(RemovalReason.KILLED);
    //         }
    //     }
    // }
}
