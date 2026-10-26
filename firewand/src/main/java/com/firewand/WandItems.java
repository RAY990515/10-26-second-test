package com.firewand;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;

public final class WandItems {
	private WandItems() {}

	/** A vanilla stick named "Fire Wand", with enchantment glint and a custom-data marker. */
	public static ItemStack createFireWand() {
		ItemStack stack = new ItemStack(Items.STICK);
		stack.set(DataComponents.CUSTOM_NAME, Component.literal("Fire Wand"));
		stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);

		CompoundTag tag = new CompoundTag();
		tag.putBoolean(WandConfig.WAND_TAG, true);
		stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
		return stack;
	}

	/** True only for sticks carrying the Fire Wand marker. A normal stick returns false. */
	public static boolean isFireWand(ItemStack stack) {
		if (stack.isEmpty() || !stack.is(Items.STICK)) {
			return false;
		}
		CustomData data = stack.get(DataComponents.CUSTOM_DATA);
		return data != null && data.copyTag().getBooleanOr(WandConfig.WAND_TAG, false);
	}
}
