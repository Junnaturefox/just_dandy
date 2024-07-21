package com.davigj.just_dandy.core.other;

import codyhuh.worldofwonder.common.block.trees.DandelionFluffTree;
import codyhuh.worldofwonder.core.WonderBlocks;
import com.davigj.just_dandy.core.registry.JDBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.grower.AzaleaTreeGrower;
import net.minecraftforge.fml.ModList;

public class JDConstants {
    public static final AbstractTreeGrower FLUFF_TREE;
    public static final Block FLUFF_BLOCK;

    public static final ResourceLocation PINE_BARRENS = new ResourceLocation("environmental", "pine_barrens");
    public static final ResourceLocation HAZE_MOUNTAIN = new ResourceLocation("terralith", "haze_mountain");
    public static final ResourceLocation CLOUD_FOREST = new ResourceLocation("terralith", "cloud_forest");
    public static final ResourceLocation DANDELION_FIELDS = new ResourceLocation("worldofwonder", "dandelion_fields");

    static {
        FLUFF_TREE = !ModList.get().isLoaded("worldofwonder") ? new AzaleaTreeGrower() : new DandelionFluffTree();
        FLUFF_BLOCK = !ModList.get().isLoaded("worldofwonder") ? JDBlocks.POTTED_FLUFFY_DANDELION.get() : WonderBlocks.DANDELION_FLUFF.get();
    }

}
