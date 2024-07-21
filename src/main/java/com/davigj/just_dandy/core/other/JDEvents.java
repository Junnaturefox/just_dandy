package com.davigj.just_dandy.core.other;

import codyhuh.worldofwonder.common.entity.DandeLionEntity;
import codyhuh.worldofwonder.core.WonderItems;
import com.davigj.just_dandy.core.JustDandy;
import com.davigj.just_dandy.core.registry.JDBlocks;
import com.teamabnormals.blueprint.core.util.NetworkUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import static com.davigj.just_dandy.core.other.JDConstants.FLUFF_TREE;

@Mod.EventBusSubscriber(modid = JustDandy.MOD_ID)
public class JDEvents {
    @SubscribeEvent
    public static void feelingDandy(BonemealEvent event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        if (level.getBlockState(pos).is(Blocks.DANDELION)) {
            level.setBlock(pos, JDBlocks.FLUFFY_DANDELION.get().defaultBlockState(), 1);
            event.setResult(Event.Result.ALLOW);
        }
    }

    @SubscribeEvent
    public static void whatInTheBloomMeal(PlayerInteractEvent.RightClickBlock event) {
        if (ModList.get().isLoaded("worldofwonder")) {
            Level level = event.getLevel();
            ItemStack stack = event.getItemStack();
            BlockPos pos = event.getPos();
            BlockState state = level.getBlockState(pos);
            Player player = event.getEntity();
            InteractionHand hand = event.getHand();
            bloomMealShenanigans(level, stack, pos, state, player, hand, event);
        }
    }

    public static void bloomMealShenanigans(Level level, ItemStack stack, BlockPos pos, BlockState state, Player player, InteractionHand hand, PlayerInteractEvent.RightClickBlock event) {
        if (stack.getItem() == WonderItems.BLOOM_MEAL.get() && state.getBlock() == JDBlocks.FLUFFY_DANDELION.get()) {
            if (!level.isClientSide) {
                RandomSource random = level.random;
                level.levelEvent(2005, pos, 0);
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                if (random.nextInt(3) == 0) {
                    FLUFF_TREE.growTree((ServerLevel) level, ((ServerLevel) level).getChunkSource().getGenerator(), pos, state, random);
                }
            } else {
                if (hand != null) {
                    player.swing(hand);
                }
            }
            if (event != null) {
                event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
            }
        }
    }

    @SubscribeEvent
    public static void interactDandelion(PlayerInteractEvent.EntityInteract event) {
        if (ModList.get().isLoaded("worldofwonder")) {
            if (event.getTarget() instanceof DandeLionEntity lion && !lion.isSheared() && event.getItemStack().is(Tags.Items.SHEARS)) {
                Vec3 mane = lion.getEyePosition();
                for (int i = 1; i < 5; i++) {
                    RandomSource random = lion.getRandom();
                    double d0 = mane.x() + random.nextGaussian() * 0.3D;
                    double d1 = mane.y() + random.nextGaussian() * 0.4D - 0.2;
                    double d2 = mane.z() + random.nextGaussian() * 0.3D;
                    double d3 = random.nextGaussian() * 0.02D;
                    double d4 = random.nextGaussian() * 0.02D;
                    double d5 = random.nextGaussian() * 0.02D;
                    NetworkUtil.spawnParticle("just_dandy:dandelion_fluff", d0, d1, d2, d3, d4, d5);
                }
            }
        }
    }
}
