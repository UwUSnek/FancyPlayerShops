package com.snek.fancyplayershops.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import net.minecraft.text.Text;




@Mixin(net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity.class)
public interface TextDisplayAccessor{
    @Invoker("setText")
    public void invokeSetText(Text text);
}