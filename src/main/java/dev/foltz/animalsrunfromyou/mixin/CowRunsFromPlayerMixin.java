package dev.foltz.animalsrunfromyou.mixin;

import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.foltz.animalsrunfromyou.Refs.*;

@Mixin(CowEntity.class)
public abstract class CowRunsFromPlayerMixin extends MobEntity {
    protected CowRunsFromPlayerMixin(World world) {
        super(null, null);
    }

    @Inject(method="initGoals", at=@At("RETURN"))
    private void initGoalsMixin(CallbackInfo info) {
        if (this.getWorld() instanceof ServerWorld) {
            this.goalSelector.add(3, new FleeEntityGoal<>((CowEntity) (Object) this, PlayerEntity.class, COW_FEAR_RANGE, COW_SPEED_SLOW, COW_SPEED_FAST));
        }
    }
}
