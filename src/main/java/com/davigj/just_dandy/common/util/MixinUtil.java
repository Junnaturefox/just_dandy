package com.davigj.just_dandy.common.util;

import codyhuh.worldofwonder.common.block.DandelionFluffBlock;
import codyhuh.worldofwonder.core.WonderBlocks;
import com.davigj.just_dandy.common.block.FluffyDandelionBlock;
import com.davigj.just_dandy.core.registry.JDParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.ModList;

public class MixinUtil {
    // Util class that handles compatibility-based particle fx because mixins not being able to hotswap wastes my time
    public static void bellowsPuff(BlockPos startingPos, Direction facing, Level level) {
        for (int i = 1; i <= 5; i++) {
            BlockPos facingPos = startingPos.relative(facing, i);
            BlockState frontState = level.getBlockState(facingPos);
            if ((frontState.getBlock() instanceof FluffyDandelionBlock) ||
                    (ModList.get().isLoaded("worldofwonder") && frontState.getBlock() instanceof DandelionFluffBlock)) {
                double xo = (double) facing.getStepX();
                double yo = (double) facing.getStepY();
                double zo = (double) facing.getStepZ();
                RandomSource random = level.getRandom();
                double x = xo * 0.5 + (double) facingPos.getX() + 0.5 + ((double) random.nextFloat() - 0.5) / 3.0;
                double y = yo * 0.5 + (double) facingPos.getY() + 0.5 + ((double) random.nextFloat() - 0.5) / 3.0;
                double z = zo * 0.5 + (double) facingPos.getZ() + 0.5 + ((double) random.nextFloat() - 0.5) / 3.0;
                double vel = (double) (0.125F + level.random.nextFloat() * 0.2F);
                double velX = xo * vel;
                double velY = yo * vel;
                double velZ = zo * vel;
                level.addParticle(JDParticleTypes.DANDELION_FLUFF.get(), x, y, z, velX * 0.8, velY * 0.8, velZ * 0.8);
            }
        }
    }

    public static void currentPuff(BlockPos startingPos, Direction facing, Level level, float intensity, int distance) {
        RandomSource random = level.getRandom();
        double xo = (double) (Math.abs(facing.getStepX()) * intensity) + (random.nextGaussian() * 0.01);
        double yo = (double) Math.abs(facing.getStepY()) * intensity;
        double zo = (double) (Math.abs(facing.getStepZ()) * intensity) + (random.nextGaussian() * 0.01);
        for (int i = 1; i <= distance + 1; i++) {
            if (random.nextInt(i + 1) == i) {
                BlockPos facingPos = startingPos.relative(facing, i);
                BlockState frontState = level.getBlockState(facingPos);
                if (frontState.getBlock() instanceof FluffyDandelionBlock) {
                    level.addParticle(JDParticleTypes.DANDELION_FLUFF.get(),
                            facingPos.getX() + 0.5, facingPos.getY() + 0.5, facingPos.getZ() + 0.5, xo, yo, zo);
                } else if (ModList.get().isLoaded("worldofwonder") && frontState.is(WonderBlocks.DANDELION_FLUFF.get())) {
                    level.addParticle(JDParticleTypes.DANDELION_FLUFF.get(),
                            facingPos.getX() + 0.5 + (random.nextGaussian() * 0.01),
                            facingPos.getY() + 0.5,
                            facingPos.getZ()+ 0.5 + (random.nextGaussian() * 0.01), xo, yo, zo);
                }
            }
        }
    }


    public static void brushUpFluff(Level level, BlockHitResult hitResult, Vec3 viewVector, HumanoidArm arm) {
        double d0 = 3.0D;
        int i = arm == HumanoidArm.RIGHT ? 1 : -1;
        int j = level.getRandom().nextInt(3, 6);
        Direction direction = hitResult.getDirection();
        FluffDelta fluffDelta = FluffDelta.fromDirection(viewVector, direction);
        Vec3 vec3 = hitResult.getLocation();
        RandomSource random = level.getRandom();
        for(int k = 0; k < j; ++k) {
            level.addParticle(JDParticleTypes.DANDELION_FLUFF.get(),
                    vec3.x - (double)(direction == Direction.WEST ? 1.0E-6F : 0.0F),
                    vec3.y,
                    vec3.z - (double)(direction == Direction.NORTH ? 1.0E-6F : 0.0F),
                    fluffDelta.xd() * (double)i * d0 * random.nextDouble() * 0.01 ,
                    -0.01 - (0.025 * random.nextGaussian()),
                    fluffDelta.zd() * (double)i * d0 * random.nextDouble() * 0.01);
        }
    }

    record FluffDelta(double xd, double yd, double zd) {
        public static FluffDelta fromDirection(Vec3 p_273421_, Direction p_272987_) {
            double d0 = 0.0D;
            FluffDelta dustDelta;
            switch (p_272987_) {
                case DOWN:
                case UP:
                    dustDelta = new FluffDelta(p_273421_.z(), 0.0D, -p_273421_.x());
                    break;
                case NORTH:
                    dustDelta = new FluffDelta(1.0D, 0.0D, -0.1D);
                    break;
                case SOUTH:
                    dustDelta = new FluffDelta(-1.0D, 0.0D, 0.1D);
                    break;
                case WEST:
                    dustDelta = new FluffDelta(-0.1D, 0.0D, -1.0D);
                    break;
                case EAST:
                    dustDelta = new FluffDelta(0.1D, 0.0D, 1.0D);
                    break;
                default:
                    throw new IncompatibleClassChangeError();
            }

            return dustDelta;
        }
    }
}
