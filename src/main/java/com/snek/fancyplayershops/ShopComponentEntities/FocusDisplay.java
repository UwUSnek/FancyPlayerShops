package com.snek.fancyplayershops.ShopComponentEntities;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4i;

import com.snek.fancyplayershops.FancyPlayerShops;
import com.snek.fancyplayershops.Shop;
import com.snek.fancyplayershops.CustomDisplays.CustomTextDisplay;
import com.snek.fancyplayershops.CustomDisplays.DisplayAnimation;
import com.snek.fancyplayershops.CustomDisplays.TransformTransition;
import com.snek.fancyplayershops.utils.Scheduler;
import com.snek.fancyplayershops.utils.Utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.decoration.DisplayEntity.BillboardMode;
import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.AffineTransformation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;








public class FocusDisplay extends CustomTextDisplay {

    // Used to avoid purges. Stray displays won't be in here
    public static final HashSet<UUID> activeFocusDisplays = new HashSet<>();

    // Style data
    public  static final int TRANSITION_DURATION_SPAWN   = 4; // Measured in ticks. MUST BE EVEN
    public  static final int TRANSITION_DURATION_DESPAWN = 8; // Measured in ticks. MUST BE EVEN
    private static final Vector4i BG_FOCUSED             = new Vector4i(200, 20, 20, 20);
    private static final Vector4i BG_UNFOCUSED           = new Vector4i(0,  0, 0, 0);
    private static final Vector4i BG_AVG                 = new Vector4i(BG_FOCUSED).add(BG_UNFOCUSED).div(2);




    public FocusDisplay(ServerWorld world, BlockPos pos, ItemStack item, double price, int stock){
        super(
            world,
            Text.empty()
                .append(item.getItem() != Items.BARRIER ? Utils.getItemName(item) : Text.literal("[Empty shop]").setStyle(Style.EMPTY.withItalic(true)))
                .append(Text.literal("\nPrice: ")).append(Text.literal(Utils.formatPrice (price)).setStyle(Style.EMPTY.withColor(Formatting.GOLD).withBold(true)))
                .append(Text.literal("\nStock: ")).append(Text.literal(Utils.formatAmount(stock)).setStyle(Style.EMPTY.withColor(Formatting.DARK_AQUA).withBold(true)))
            ,
            new Vec3d(pos.getX() + 0.5, pos.getY() + 0.6, pos.getZ() + 0.5),
            BillboardMode.VERTICAL,
            false,
            new DisplayAnimation(
                List.of(new TransformTransition(
                    new AffineTransformation(
                        new Vector3f(0, 0.05f, 0),
                        new Quaternionf(),
                        new Vector3f(1.02f, 1.02f, 1.02f),
                        new Quaternionf()
                    ),
                    TRANSITION_DURATION_SPAWN
                )),
                null,
                List.of(new TransformTransition(
                    new AffineTransformation(
                        new Vector3f(0, 0, 0),
                        new Quaternionf(),
                        new Vector3f(1.0f, 1.0f, 1.0f),
                        new Quaternionf()
                    ),
                    TRANSITION_DURATION_DESPAWN
                ))
            )
        );

        activeFocusDisplays.add(getRawDisplay().getUuid()); //! Must be added before spawning the entity into the world to stop it from instantly getting purged
        setTextOpacity(0);
        setBackground(BG_UNFOCUSED);
    }




    @Override
    public void spawn(World world){
        super.spawn(world);


        // Transition first half
        setTextOpacity(127);
        setBackground(BG_AVG);
        apply(TRANSITION_DURATION_SPAWN / 2);


        // Transition second half
        Scheduler.schedule(TRANSITION_DURATION_SPAWN / 2, () -> {
            setTextOpacity(128);
        });
        Scheduler.schedule(TRANSITION_DURATION_SPAWN / 2 + 1, () -> {
            setTextOpacity(255);
            setBackground(BG_FOCUSED);
            apply(TRANSITION_DURATION_SPAWN / 2);
        });
    }




    @Override
    public void despawn(){
        super.despawn();
        activeFocusDisplays.remove(getRawDisplay().getUuid());


        // Transition first half
        setTextOpacity(128);
        setBackground(BG_AVG);
        apply(TRANSITION_DURATION_DESPAWN);


        // Transition second half
        Scheduler.schedule(TRANSITION_DURATION_DESPAWN / 2, () -> {
            setTextOpacity(127);
        });
        Scheduler.schedule(TRANSITION_DURATION_DESPAWN / 2 + 1, () -> {
            setTextOpacity(0);
            setBackground(BG_UNFOCUSED);
            apply(TRANSITION_DURATION_DESPAWN / 2);
        });
    }




    /**
     * Checks for stray focus displays and purges them.
     * Any TextDisplayEntity not registered as active display and in the same block as a shop is considered a stray display.
     * Must be called on entity load event.
     * @param entity The entity.
     */
    public static void onEntityLoad(Entity entity) {
        if (entity instanceof TextDisplayEntity) {
            World world = entity.getWorld();
            if(
                world != null &&
                !FocusDisplay.activeFocusDisplays.contains(entity.getUuid()) &&
                Shop.findShop(entity.getBlockPos(), world.getRegistryKey().getValue().toString()) != null
            ) {
                entity.remove(RemovalReason.KILLED);
            }
        }
    }
}
