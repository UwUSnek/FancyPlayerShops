package com.snek.fancyplayershops.implementations.ui;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;

import com.snek.fancyplayershops.FancyPlayerShops;
import com.snek.fancyplayershops.Shop;
import com.snek.framework.ui.TextElm;
import com.snek.framework.ui.styles.TextElmStyle;
import com.snek.framework.utils.Txt;
import com.snek.framework.utils.Utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.world.World;








public class DetailsDisplay extends TextElm {
    private static final String ENTITY_CUSTOM_NAME = FancyPlayerShops.MOD_ID + ".ui.displayentity";
    Shop targetShop;

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


        // Empty shop case
        final ItemStack _item = targetShop.getItem();
        if(_item.getItem() == Items.AIR) {
            text.set(new Txt()
            .cat(Shop.EMPTY_SHOP_NAME)
            .cat(new Txt("\nPrice: ")).cat(new Txt(Utils.formatPrice (targetShop.getPrice())).bold().color(C_RGB_PRICE))
            .cat(new Txt("\nStock: ")).cat(new Txt(Utils.formatAmount(targetShop.getStock())).bold().color((int)col.x, (int)col.y, (int)col.z))
            .get());
        }

        // Configured shop case
        else {
            text.set(new Txt()
                .cat(new Txt(Utils.getItemName(_item)).get())
                .cat(new Txt("\nPrice: ")).cat(new Txt(Utils.formatPrice (targetShop.getPrice())).bold().color(C_RGB_PRICE))
                .cat(new Txt("\nStock: ")).cat(new Txt(Utils.formatAmount(targetShop.getStock())).bold().color((int)col.x, (int)col.y, (int)col.z))
            .get());
        }

        // Flush style
        flushStyle();
    }




    @Override
    public void spawn(Vector3d pos){
        super.spawn(pos);
        entity.setCustomName(new Txt(ENTITY_CUSTOM_NAME).get());
        entity.setCustomNameVisible(false);

        // // Transition
        // setTextOpacity(255);
        // setBackground(S_BG);
        // apply(S_TIME);
    }




    @Override
    public void despawn(){
        super.despawn();
        // activeFocusDisplays.remove(rawDisplay.getUuid());

        // // Transition
        // setTextOpacity(128);
        // setBackground(D_BG);
        // apply(D_TIME);
    }




    /**
     * Checks for stray focus displays and purges them.
     * Any TextDisplayEntity not registered as active display and in the same block as a shop is considered a stray display.
     * Must be called on entity load event.
     * @param entity The entity.
     */
    public static void onEntityLoad(@NotNull Entity entity) {
        if (entity instanceof TextDisplayEntity) {
            World world = entity.getWorld();
            if(
                world != null &&
                entity.getCustomName() != null &&
                entity.getCustomName().getString() == ENTITY_CUSTOM_NAME &&
                Shop.findShop(entity.getBlockPos(), world) != null
            ) {
                entity.remove(RemovalReason.KILLED);
            }
        }
    }
}
