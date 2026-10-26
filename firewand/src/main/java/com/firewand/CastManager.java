package com.firewand;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

/** Creates a new WandCast per right-click and ticks all active casts. No cooldown. */
public final class CastManager {
	private static final List<WandCast> ACTIVE = new ArrayList<>();

	private CastManager() {}

	public static void init() {
		UseItemCallback.EVENT.register((player, level, hand) -> {
			if (!(player instanceof ServerPlayer) || !(level instanceof ServerLevel serverLevel)) {
				return InteractionResult.PASS;
			}

			ItemStack stack = player.getItemInHand(hand);
			if (!WandItems.isFireWand(stack)) {
				return InteractionResult.PASS;
			}

			// Actual view direction of the player.
			Vec3 direction = player.getLookAngle();
			Vec3 origin = player.getEyePosition().add(direction.scale(WandConfig.START_OFFSET));
			ACTIVE.add(new WandCast(serverLevel, player.getUUID(), origin, direction));

			return InteractionResult.SUCCESS;
		});

		ServerTickEvents.END_SERVER_TICK.register(server -> ACTIVE.removeIf(WandCast::tick));
		ServerLifecycleEvents.SERVER_STOPPING.register(server -> ACTIVE.clear());
	}
}
