package com.snek.fancyplayershops.ShopComponentEntities;

import java.util.HashSet;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4i;

import com.snek.fancyplayershops.Shop;
import com.snek.framework.data_types.Animation;
import com.snek.framework.custom_displays.CustomTextDisplay;
import com.snek.framework.data_types.Transform;
import com.snek.framework.ui.TextElm;
import com.snek.framework.ui.styles.TextElmStyle;
import com.snek.framework.data_types.TargetTransition;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.decoration.DisplayEntity.BillboardMode;
import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity;
import net.minecraft.world.World;








public class DetailsDisplay extends TextElm {
    Shop targetShop;


    // // Used to avoid purges. Stray displays won't be in here
    // public static final HashSet<UUID> activeFocusDisplays = new HashSet<>();

    private static final Vector3i C_RGB_PRICE      = new Vector3i(243, 255, 0);
    private static final Vector3f C_HSV_STOCK_HIGH = Utils.RGBtoHSV(new Vector3f(0, 223, 0)); //! Float instead of int for more precision
    private static final Vector3f C_HSV_STOCK_LOW  = Utils.RGBtoHSV(new Vector3f(200, 0, 0)); //! Float instead of int for more precision




    public DetailsDisplay(@NotNull Shop _targetShop){
        super(_targetShop.getWorld());
        // super(
        //     _targetShop.getWorld(),
        //     new Txt("Something went wrong :c").red().get(),
        //     _targetShop.calcDisplayPos().add(0, 0.3d, 0),
        //     DEFAULT_TRANSFORM,
        //     BillboardMode.VERTICAL,
        //     false,
        //     new com.snek.framework.ui.styles.AnimationData(
        //         new Animation(new TargetTransition(DEFAULT_TRANSFORM.clone().moveY(S_HEIGHT).scale(S_SCALE), S_TIME)),
        //         null,
        //         new Animation(new TargetTransition(DEFAULT_TRANSFORM, D_TIME))
        //     )
        // );
        targetShop = _targetShop;

        // activeFocusDisplays.add(rawDisplay.getUuid()); //! Must be added before spawning the entity into the world to stop it from instantly getting purged
        // setTextOpacity(128);
        // setBackground(D_BG);
        updateDisplay();
    }




    /**
     * Updates the displayed values using the current item name, price and stock.
     */
    public void updateDisplay(){
        float factor = 1.0f - (float)targetShop.getStock() / 1000f;
        Vector3f col = Utils.HSVtoRGB(new Vector3f(C_HSV_STOCK_LOW).add(new Vector3f(C_HSV_STOCK_HIGH).sub(C_HSV_STOCK_LOW).mul(1.0f - (factor * factor))));
        ((TextElmStyle)style).setText(new Txt()
            .cat(new Txt(Utils.getItemName(targetShop.getItem())))
            .cat(new Txt("\nPrice: ")).cat(new Txt(Utils.formatPrice (targetShop.getPrice())).bold().color(C_RGB_PRICE))
            .cat(new Txt("\nStock: ")).cat(new Txt(Utils.formatAmount(targetShop.getStock())).bold().color((int)col.x, (int)col.y, (int)col.z))
        .get());
    }




    // @Override
    // public void spawn(@NotNull World world){
    //     super.spawn(world);

    //     // Transition
    //     setTextOpacity(255);
    //     setBackground(S_BG);
    //     apply(S_TIME);
    // }




    // @Override
    // public void despawn(){
    //     super.despawn();
    //     activeFocusDisplays.remove(rawDisplay.getUuid());

    //     // Transition
    //     setTextOpacity(128);
    //     setBackground(D_BG);
    //     apply(D_TIME);
    // }




    /**
     * Checks for stray focus displays and purges them.
     * Any TextDisplayEntity not registered as active display and in the same block as a shop is considered a stray display.
     * Must be called on entity load event.
     * @param entity The entity.
     */
    public static void onEntityLoad(@NotNull Entity entity) {
        //FIXME ONLY PURGE DISPLAYS FROM THIS MOD
        //FIXME ONLY PURGE DISPLAYS FROM THIS MOD
        //FIXME ONLY PURGE DISPLAYS FROM THIS MOD
        //FIXME ONLY PURGE DISPLAYS FROM THIS MOD
        // if (entity instanceof TextDisplayEntity) {
        //     World world = entity.getWorld();
        //     if(
        //         world != null &&
        //         !DetailsDisplay.activeFocusDisplays.contains(entity.getUuid()) &&
        //         Shop.findShop(entity.getBlockPos(), world) != null
        //     ) {
        //         entity.remove(RemovalReason.KILLED);
        //     }
        // }
    }
}
