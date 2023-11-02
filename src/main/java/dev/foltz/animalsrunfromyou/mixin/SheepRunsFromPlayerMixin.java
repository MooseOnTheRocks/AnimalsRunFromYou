package dev.foltz.animalsrunfromyou.mixin;

import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.foltz.animalsrunfromyou.Refs.*;

@Mixin(SheepEntity.class)
public abstract class SheepRunsFromPlayerMixin extends MobEntity {
    protected SheepRunsFromPlayerMixin(World world) {
        super(null, null);
    }

    @Inject(method="initGoals", at=@At("RETURN"))
    private void initGoalsMixin(CallbackInfo info) {
        if (this.getWorld() instanceof ServerWorld) {
            this.goalSelector.add(3, new FleeEntityGoal<>((SheepEntity) (Object) this, PlayerEntity.class, SHEEP_FEAR_RANGE, SHEEP_SPEED_SLOW, SHEEP_SPEED_FAST));
        }
    }
}
