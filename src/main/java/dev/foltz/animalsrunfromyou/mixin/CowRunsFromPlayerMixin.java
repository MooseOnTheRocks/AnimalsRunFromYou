package dev.foltz.animalsrunfromyou.mixin;

import dev.foltz.animalsrunfromyou.ARFYModConfig;
import dev.foltz.animalsrunfromyou.RunAwayGoal;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CowEntity.class)
public abstract class CowRunsFromPlayerMixin extends MobEntity {
    protected CowRunsFromPlayerMixin(World world) {
        super(null, null);
    }

    @Inject(method="initGoals", at=@At("RETURN"))
    private void initGoalsMixin(CallbackInfo info) {
        if (this.getWorld() instanceof ServerWorld) {
            this.goalSelector.add(3, new RunAwayGoal<>((CowEntity) (Object) this, PlayerEntity.class, () -> ARFYModConfig.getConfig().cow));
        }
    }
}
