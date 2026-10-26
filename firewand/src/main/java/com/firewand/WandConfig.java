package com.firewand;

/** All tunable values for the Fire Wand live here. */
public final class WandConfig {
	private WandConfig() {}

	// ---- Core beam parameters ----
	/** Blocks the beam front travels per tick. */
	public static final double SPEED = 1.5;
	/** Maximum cast lifetime in ticks (30 ticks = 1.5 seconds). */
	public static final int LIFETIME = 30;
	/** Maximum distance of one cast, in blocks. */
	public static final double RANGE = 20.0;
	/** Damage dealt to each entity hit by a cast. */
	public static final double DAMAGE = 99.0;
	/** Knockback strength applied to each entity hit by a cast. */
	public static final double KNOCKBACK = 3.0;

	// ---- Supporting values ----
	/** The chat input is "///wand"; the leading "/" is stripped by the client, so the command literal is "//wand". */
	public static final String COMMAND_LITERAL = "//wand";
	/** Distance in front of the eyes where the beam starts. */
	public static final double START_OFFSET = 0.5;
	/** Distance between FLAME particles along the beam (one narrow stream). */
	public static final double PARTICLE_STEP = 0.25;
	/** Extra hitbox margin around entities for beam collision. */
	public static final double HIT_MARGIN = 0.3;
	/** Custom-data key identifying the Fire Wand. */
	public static final String WAND_TAG = "fire_wand";
}
