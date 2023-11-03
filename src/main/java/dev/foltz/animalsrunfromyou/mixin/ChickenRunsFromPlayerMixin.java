package dev.foltz.animalsrunfromyou.mixin;

import dev.foltz.animalsrunfromyou.ARFYModConfig;
import dev.foltz.animalsrunfromyou.RunAwayGoal;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.ChickenEntity;
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

@Mixin(ChickenEntity.class)
public abstract class ChickenRunsFromPlayerMixin extends MobEntity {
    protected ChickenRunsFromPlayerMixin(World world) {
        super(null, null);
    }

    @Inject(method="initGoals", at=@At("RETURN"))
    private void initGoalsMixin(CallbackInfo info) {
        if (this.getWorld() instanceof ServerWorld) {
            if (ARFYModConfig.getConfig().chicken.runFromWolves) {
                this.goalSelector.add(3, new RunAwayGoal<>((ChickenEntity) (Object) this, WolfEntity.class, Ingredient.ofItems(Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS, Items.TORCHFLOWER_SEEDS, Items.PITCHER_POD), () -> ARFYModConfig.getConfig().chicken));
            }
            this.goalSelector.add(3, new RunAwayGoal<>((ChickenEntity) (Object) this, PlayerEntity.class, Ingredient.ofItems(Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS, Items.TORCHFLOWER_SEEDS, Items.PITCHER_POD), () -> ARFYModConfig.getConfig().chicken));
        }
    }
}
