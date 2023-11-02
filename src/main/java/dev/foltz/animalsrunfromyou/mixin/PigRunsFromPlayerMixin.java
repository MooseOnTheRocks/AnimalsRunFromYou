package dev.foltz.animalsrunfromyou.mixin;

import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.foltz.animalsrunfromyou.Refs.*;

@Mixin(PigEntity.class)
public abstract class PigRunsFromPlayerMixin extends MobEntity {
    protected PigRunsFromPlayerMixin(World world) {
        super(null, null);
    }

    @Inject(method="initGoals", at=@At("RETURN"))
    private void initGoalsMixin(CallbackInfo info) {
        if (this.getWorld() instanceof ServerWorld) {
            this.goalSelector.add(4, new FleeEntityGoal<>((PigEntity) (Object) this, PlayerEntity.class, PIG_FEAR_RANGE, PIG_SPEED_SLOW, PIG_SPEED_FAST));
        }
    }
}