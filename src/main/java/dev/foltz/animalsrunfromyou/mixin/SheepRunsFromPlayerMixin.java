package dev.foltz.animalsrunfromyou.mixin;

import dev.foltz.animalsrunfromyou.ARFYModConfig;
import dev.foltz.animalsrunfromyou.RunAwayGoal;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SheepEntity.class)
public abstract class SheepRunsFromPlayerMixin extends MobEntity {
    protected SheepRunsFromPlayerMixin(World world) {
        super(null, null);
    }

    @Inject(method="initGoals", at=@At("RETURN"))
    private void initGoalsMixin(CallbackInfo info) {
        if (this.getWorld() instanceof ServerWorld) {
            if (ARFYModConfig.getConfig().sheep.runFromWolves) {
                this.goalSelector.add(3, new RunAwayGoal<>((SheepEntity) (Object) this, WolfEntity.class, Ingredient.ofItems(Items.WHEAT), () -> ARFYModConfig.getConfig().sheep));
            }
            this.goalSelector.add(3, new RunAwayGoal<>((SheepEntity) (Object) this, PlayerEntity.class, Ingredient.ofItems(Items.WHEAT), () -> ARFYModConfig.getConfig().sheep));
        }
    }
}
