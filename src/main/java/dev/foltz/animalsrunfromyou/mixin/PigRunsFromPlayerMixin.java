package dev.foltz.animalsrunfromyou.mixin;

import dev.foltz.animalsrunfromyou.ARFYModConfig;
import dev.foltz.animalsrunfromyou.RunAwayGoal;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PigEntity.class)
public abstract class PigRunsFromPlayerMixin extends MobEntity {
    protected PigRunsFromPlayerMixin(World world) {
        super(null, null);
    }

    @Inject(method="initGoals", at=@At("RETURN"))
    private void initGoalsMixin(CallbackInfo info) {
        if (this.getWorld() instanceof ServerWorld) {
            if (ARFYModConfig.getConfig().pig.runFromWolves) {
                this.goalSelector.add(4, new RunAwayGoal<>((PigEntity) (Object) this, WolfEntity.class, Ingredient.ofItems(Items.CARROT, Items.POTATO, Items.BEETROOT), () -> ARFYModConfig.getConfig().pig));
            }
            this.goalSelector.add(4, new RunAwayGoal<>((PigEntity) (Object) this, PlayerEntity.class, Ingredient.ofItems(Items.CARROT, Items.POTATO, Items.BEETROOT), () -> ARFYModConfig.getConfig().pig));
        }
    }
}
