package dev.foltz.animalsrunfromyou;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;
import java.util.function.Supplier;

public class RunAwayGoal<T extends LivingEntity> extends Goal {
    public static final int FLEE_RANGE_HORIZONTAL = 16;
    public static final int FLEE_RANGE_VERTICAL = 7;

    private final PathAwareEntity mob;
    private final Supplier<ARFYModConfig.AnimalStats> getStats;
    private final TargetPredicate withinRangePredicate;
    private T targetEntity;
    private final Class<T> classToRunFrom;
    private final EntityNavigation fleeingEntityNavigation;
    private Path fleePath;
    private final Ingredient breedingIngredient;

    public RunAwayGoal(PathAwareEntity mob, Class<T> classToRunFrom, Ingredient breedingIngredient, Supplier<ARFYModConfig.AnimalStats> getStats) {
        this.mob = mob;
        this.classToRunFrom = classToRunFrom;
        this.breedingIngredient = breedingIngredient;
        this.getStats = getStats;
        this.withinRangePredicate = TargetPredicate
            .createAttackable()
            .setPredicate(EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR::test);
        this.fleeingEntityNavigation = mob.getNavigation();
        this.setControls(EnumSet.of(Control.MOVE));
    }

    private T targetNearestEntityToRunFrom() {
        var gatherEntities = mob.getWorld().getEntitiesByClass(classToRunFrom,
            mob.getBoundingBox().expand(getDistance(), (getDistance() + 1f) / 2f, getDistance()), (livingEntity) -> true);
        return mob.getWorld()
            .getClosestEntity(gatherEntities, withinRangePredicate.setBaseMaxDistance(getDistance()), mob, mob.getX(), mob.getY(), mob.getZ());
    }

    @Override
    public boolean canStart() {
        if (getDistance() == 0) {
            return false;
        }

        targetEntity = targetNearestEntityToRunFrom();
        if (targetEntity == null) {
            return false;
        }
        for (net.minecraft.item.ItemStack itemStack : targetEntity.getHandItems()) {
            if (breedingIngredient.test(itemStack)) {
                System.out.println("Noticed breeding item!");
                return false;
            }
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
        var maxDist = getDistance();
        var distSquared = Math.min(mob.squaredDistanceTo(targetEntity), maxDist * maxDist);
        var dist = Math.sqrt(distSquared);
        var delta = 1 - dist / maxDist;
        float speed;
        if (getRatio() == 0) {
            speed = distSquared == 0
                ? getFastSpeed()
                : (float) MathHelper.lerp(delta, getSlowSpeed(), getFastSpeed());
        }
        else {
            speed = delta >= getRatio() ? getFastSpeed() : getSlowSpeed();
        }
        mob.getNavigation().setSpeed(speed);
    }

    private float getDistance() {
        return getStats.get().distance;
    }

    private float getSlowSpeed() {
        return getStats.get().farSpeed;
    }

    private float getFastSpeed() {
        return getStats.get().nearSpeed;
    }

    private float getRatio() {
        return getStats.get().ratio;
    }
}
