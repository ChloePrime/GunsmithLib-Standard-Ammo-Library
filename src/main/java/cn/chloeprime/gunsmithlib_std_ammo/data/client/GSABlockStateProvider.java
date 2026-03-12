package cn.chloeprime.gunsmithlib_std_ammo.data.client;

import cn.chloeprime.gunsmithlib_std_ammo.GunsmithLibStdAmmoMod;
import cn.chloeprime.gunsmithlib_std_ammo.common.util.DatagenRegistryHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

import static cn.chloeprime.gunsmithlib_std_ammo.common.block.GSABlocks.*;

public class GSABlockStateProvider extends BlockStateProvider implements DatagenRegistryHelper {
    public GSABlockStateProvider(
            PackOutput output,
            String modid,
            ExistingFileHelper exFileHelper
    ) {
        super(output, modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        axisBlock(PURIFIED_DEBRIS.get());
        simpleBlockItem(PURIFIED_DEBRIS);

        simpleBlockWithItem(TIBERIUM_ORE.get(), cubeAll(TIBERIUM_ORE.get()));
        simpleBlockWithItem(DEEPSLATE_TIBERIUM_ORE.get(), cubeAll(DEEPSLATE_TIBERIUM_ORE.get()));
        simpleBlockWithItem(NETHER_TIBERIUM_ORE.get(), cubeAll(NETHER_TIBERIUM_ORE.get()));
        simpleBlockWithItem(END_TIBERIUM_SEED_ORE.get(), cubeAll(END_TIBERIUM_SEED_ORE.get()));
        simpleBlock(TIBERIUM_LEAVES.get(), models().singleTexture(
                getKey(TIBERIUM_LEAVES.get()).toString(),
                GunsmithLibStdAmmoMod.loc("minecraft", "block/leaves"),
                "all",
                blockTexture(TIBERIUM_LEAVES.get())));
        simpleBlockItem(TIBERIUM_LEAVES);
    }

    @SuppressWarnings("deprecation")
    private void simpleBlockItem(Supplier<? extends Block> block) {
        simpleBlockItem(block.get(), models().getExistingFile(BuiltInRegistries.BLOCK.getKey(block.get())));
    }
}
