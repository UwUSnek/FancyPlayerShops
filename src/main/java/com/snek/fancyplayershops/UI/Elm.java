package com.snek.fancyplayershops.UI;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.snek.fancyplayershops.Shop;

import net.minecraft.server.world.ServerWorld;








public class Elm {
    @Nullable private Elm parent = null;
    @NotNull  private List<@NotNull Elm> children = new ArrayList<>();
    @NotNull  private Shop shop;
    @NotNull  private ServerWorld world;


    public Elm(@NotNull Shop _shop) {
        shop = _shop;
        world = _shop.getWorld();
    }


    public void addChild(Elm child) {
        children.add(child);
        child.parent = this;
    }


    public List<Elm> getChildren() {
        return children;
    }


    public void   spawn(){}
    public void despawn(){}
}
