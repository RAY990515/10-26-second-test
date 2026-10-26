package com.firewand;

import net.fabricmc.api.ModInitializer;

public class FireWandMod implements ModInitializer {
	@Override
	public void onInitialize() {
		WandCommand.init();
		CastManager.init();
	}
}
