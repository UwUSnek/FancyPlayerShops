package com.snek.fancyplayershops;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class FancyPlayerShops implements ModInitializer {
	public static final String MOD_ID = "fancyplayershops";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
        TestCommand.register();

		LOGGER.info("FancyPlayerShops initialized. :3");
	}
}