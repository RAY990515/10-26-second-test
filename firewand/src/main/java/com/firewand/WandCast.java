package com.firewand;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * One independent cast (one right-click). Owns its own start position, direction,
 * age, travelled distance and set of already-hit entities.
 */
public final class WandCast {
	private final ServerLevel level;
	private final UUID casterId;
	private final Vec3 origin;
	private final Vec3 direction;
	private final Set<UUID> hitEntities = new HashSet<>();

	private int age = 0;
	private double traveled = 0.0;

	public WandCast(ServerLevel level, UUID casterId, Vec3 origin, Vec3 direction) {
		this.level = level;
		this.casterId = casterId;
		this.origin = origin;
		this.direction = direction;
	}

	/** Advances the cast by one tick. Returns true when the cast has ended. */
	public boolean tick() {
		double next = Math.min(this.traveled + WandConfig.SPEED, WandConfig.RANGE);
		Vec3 from = this.origin.add(this.direction.scale(this.traveled));
		Vec3 to = this.origin.add(this.direction.scale(next));

		spawnParticles(this.traveled, next);
		hitEntities(from, to);

		this.traveled = next;
		this.age++;
		return this.age >= WandConfig.LIFETIME || this.traveled >= WandConfig.RANGE;
	}

	/** Thin, single-line stream of vanilla FLAME particles between two distances along the beam. */
	private void spawnParticles(double startDist, double endDist) {
		for (double d = startDist; d < endDist; d += WandConfig.PARTICLE_STEP) {
			Vec3 p = this.origin.add(this.direction.scale(d));
			this.level.sendParticles(ParticleTypes.FLAME, p.x, p.y, p.z, 1, 0.0, 0.0, 0.0, 0.0);
		}
	}

	/** Sweeps the beam segment for living entities; each entity is affected once per cast. */
	private void hitEntities(Vec3 from, Vec3 to) {
		AABB sweep = new AABB(from, to).inflate(WandConfig.HIT_MARGIN);
		List<LivingEntity> candidates = this.level.getEntitiesOfClass(
			LivingEntity.class,
			sweep,
			e -> e.isAlive()
				&& !e.isSpectator()
				&& !e.getUUID().equals(this.casterId)
				&& !this.hitEntities.contains(e.getUUID())
		);

		for (LivingEntity target : candidates) {
			AABB box = target.getBoundingBox().inflate(WandConfig.HIT_MARGIN);
			if (box.contains(from) || box.clip(from, to).isPresent()) {
				applyHit(target);
			}
		}
	}

	private void applyHit(LivingEntity target) {
		this.hitEntities.add(target.getUUID());

		Entity caster = this.level.getEntity(this.casterId);
		target.hurtServer(
			this.level,
			this.level.damageSources().indirectMagic(null, caster),
			(float) WandConfig.DAMAGE
		);

		// knockback(strength, x, z) pushes AWAY from the (x, z) direction, so pass the inverse
		// of the cast direction to push the target along the cast direction.
		target.knockback(WandConfig.KNOCKBACK, -this.direction.x, -this.direction.z);
	}
}
