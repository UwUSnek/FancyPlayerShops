// package com.snek.fancyplayershops.mixin;

// import org.spongepowered.asm.mixin.Mixin;
// import org.spongepowered.asm.mixin.gen.Accessor;
// import org.spongepowered.asm.mixin.gen.Invoker;
// import org.spongepowered.asm.mixin.injection.At;
// import org.spongepowered.asm.mixin.injection.Inject;
// import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity;
// import net.minecraft.entity.data.TrackedData;
// import net.minecraft.text.Text;




// @Mixin(net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity.class)
// public interface TextDisplayAccessor{
//     @Invoker("setText")
//     public void invokeSetText(Text text);
// }
//TODO fix mixins