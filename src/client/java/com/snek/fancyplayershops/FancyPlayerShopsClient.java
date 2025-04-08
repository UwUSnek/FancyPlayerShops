package com.snek.fancyplayershops;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import java.util.Objects;




public class FancyPlayerShopsClient implements ClientModInitializer {
    private boolean isGenerated = false;

    public boolean getIsGenerated() {
        return isGenerated;
    }

    public void setIsGenerated(boolean isGenerated) {
        this.isGenerated = isGenerated;
    }



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