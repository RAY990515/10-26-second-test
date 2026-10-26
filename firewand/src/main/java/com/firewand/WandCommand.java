package com.firewand;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public final class WandCommand {
	private WandCommand() {}

	public static void init() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
			dispatcher.register(
				Commands.literal(WandConfig.COMMAND_LITERAL).executes(context -> {
					ServerPlayer player = context.getSource().getPlayerOrException();
					ItemStack wand = WandItems.createFireWand();
					if (!player.getInventory().add(wand)) {
						player.drop(wand, false);
					}
					return 1;
				})
			)
		);
	}
}
