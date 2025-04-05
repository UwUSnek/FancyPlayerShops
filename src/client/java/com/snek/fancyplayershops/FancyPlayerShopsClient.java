package com.snek.fancyplayershops;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;




public class FancyPlayerShopsClient implements ClientModInitializer {
    public static boolean isGenerated = false;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_WORLD_TICK.register(client -> {
            if(!isGenerated) {
                FontWidthGenerator.generate();
                isGenerated = true;
            }
        });
    }
}