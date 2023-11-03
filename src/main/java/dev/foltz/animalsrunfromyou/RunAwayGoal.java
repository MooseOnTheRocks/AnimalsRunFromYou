package dev.foltz.animalsrunfromyou;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;
import java.util.function.Supplier;

public class RunAwayGoal<T extends LivingEntity> extends Goal {
    public static final int FLEE_RANGE_HORIZONTAL = 16;
    public static final int FLEE_RANGE_VERTICAL = 7;

    private final PathAwareEntity mob;
    private final Supplier<ARFYModConfig.Stats> getStats;
    private final TargetPredicate withinRangePredicate;
    private T targetEntity;
    private final Class<T> classToRunFrom;
    private final EntityNavigation fleeingEntityNavigation;
    private Path fleePath;

    public RunAwayGoal(PathAwareEntity mob, Class<T> classToRunFrom, Supplier<ARFYModConfig.Stats> getStats) {
        this.mob = mob;
        this.classToRunFrom = classToRunFrom;
        this.getStats = getStats;
        this.withinRangePredicate = TargetPredicate
            .createAttackable()
            .setPredicate(EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR::test);
        this.fleeingEntityNavigation = mob.getNavigation();
        this.setControls(EnumSet.of(Control.MOVE));
    }

    private T targetNearestEntityToRunFrom() {
        var gatherEntities = mob.getWorld().getEntitiesByClass(classToRunFrom,
            mob.getBoundingBox().expand(getDistance(), 3.0, getDistance()), (livingEntity) -> true);
        return mob.getWorld()
            .getClosestEntity(gatherEntities, withinRangePredicate.setBaseMaxDistance(getDistance()), mob, mob.getX(), mob.getY(), mob.getZ());
    }

    @Override
    public boolean canStart() {
        targetEntity = targetNearestEntityToRunFrom();
        if (targetEntity == null) {
            return false;
        }

        Vec3d vec3d = NoPenaltyTargeting.findFrom(mob, FLEE_RANGE_HORIZONTAL, FLEE_RANGE_VERTICAL, targetEntity.getPos());
        if (vec3d == null) {
            return false;
        }
        else if (targetEntity.squaredDistanceTo(vec3d.x, vec3d.y, vec3d.z) < targetEntity.squaredDistanceTo(mob)) {
            return false;
        }
        else {
            fleePath = fleeingEntityNavigation.findPathTo(vec3d.x, vec3d.y, vec3d.z, 0);
            return fleePath != null;
        }
    }

    @Override
    public boolean shouldContinue() {
        return !fleeingEntityNavigation.isIdle();
    }

    @Override
    public void start() {
        fleeingEntityNavigation.startMovingAlong(fleePath, getSlowSpeed());
    }

    @Override
    public void stop() {
        this.targetEntity = null;
    }

    @Override
    public void tick() {
        var maxDistSquared = getDistance() * getDistance();
        var distSquared = Math.max(mob.squaredDistanceTo(targetEntity), maxDistSquared);
        var delta = 1 - distSquared / maxDistSquared;
        var speed = distSquared == 0 ? getFastSpeed() : MathHelper.lerp(delta, getSlowSpeed(), getFastSpeed());
        System.out.println("DELTA = " + delta + " , SPEED = " + speed);
        mob.getNavigation().setSpeed(speed);
    }

    private float getDistance() {
        return getStats.get().distance;
    }

    private float getSlowSpeed() {
        return getStats.get().slowSpeed;
    }

    private float getFastSpeed() {
        return getStats.get().fastSpeed;
    }
}
