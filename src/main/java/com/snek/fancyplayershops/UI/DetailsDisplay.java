package com.snek.fancyplayershops.UI;

import java.util.HashSet;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector4i;

import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.CustomDisplays.CustomTextDisplay;
import com.snek.fancyplayershops.CustomDisplays.Animation;
import com.snek.fancyplayershops.CustomDisplays.AnimationData;
import com.snek.fancyplayershops.CustomDisplays.Transform;
import com.snek.fancyplayershops.CustomDisplays.Transition;
import com.snek.fancyplayershops.utils.Txt;
import com.snek.fancyplayershops.utils.Utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.decoration.DisplayEntity.BillboardMode;
import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;








public class DetailsDisplay extends CustomTextDisplay {
    Shop targetShop;


    // Used to avoid purges. Stray displays won't be in here
    public static final HashSet<UUID> activeFocusDisplays = new HashSet<>();


    // Style data
    private static final Transform DEFAULT_TRANSFORM = new Transform().scale(0.5f);

    public  static final float T_SCALE          = 1.02f;
    private static final float T_HEIGHT         = 0.05f;

    public  static final int DURATION_SPAWN   = 4; // Measured in ticks. MUST BE EVEN
    public  static final int DURATION_DESPAWN = 8; // Measured in ticks. MUST BE EVEN

    private static final Vector4i BG_FOCUSED    = new Vector4i(200, 20, 20, 20);
    private static final Vector4i BG_UNFOCUSED  = new Vector4i(0,  0, 0, 0);




    public DetailsDisplay(@NotNull Shop targetShop){
        super(
            targetShop.getWorld(),
            new Txt()
                .cat(new Txt(Utils.getItemName(targetShop.getItem())))
                .cat(new Txt("\nPrice: ")).cat(new Txt(Utils.formatPrice (targetShop.getPrice())).bold().gold())
                .cat(new Txt("\nStock: ")).cat(new Txt(Utils.formatAmount(targetShop.getStock())).bold().aqua())
            .get(),
            targetShop.calcDisplayPos().add(0, 0.3d, 0),
            DEFAULT_TRANSFORM,
            BillboardMode.VERTICAL,
            false,
            new AnimationData(
                new Animation(new Transition(DEFAULT_TRANSFORM.clone().moveY(T_HEIGHT).scale(T_SCALE), DURATION_SPAWN)),
                null,
                new Animation(new Transition(DEFAULT_TRANSFORM, DURATION_DESPAWN))
            )
        );

        activeFocusDisplays.add(rawDisplay.getUuid()); //! Must be added before spawning the entity into the world to stop it from instantly getting purged
        setTextOpacity(128);
        setBackground(BG_UNFOCUSED);
    }




    @Override
    public void spawn(@NotNull World world){
        super.spawn(world);

        // Transition
        setTextOpacity(255);
        setBackground(BG_FOCUSED);
        apply(DURATION_SPAWN);
    }




    @Override
    public void despawn(){
        super.despawn();
        activeFocusDisplays.remove(rawDisplay.getUuid());

        // Transition
        setTextOpacity(128);
        setBackground(BG_UNFOCUSED);
        apply(DURATION_DESPAWN);
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
                !DetailsDisplay.activeFocusDisplays.contains(entity.getUuid()) &&
                Shop.findShop(entity.getBlockPos(), world.getRegistryKey().getValue().toString()) != null
            ) {
                entity.remove(RemovalReason.KILLED);
            }
        }
    }
}
