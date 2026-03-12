package cn.chloeprime.gunsmithlib_std_ammo.data;

import cn.chloeprime.gunsmithlib_std_ammo.GunsmithLibStdAmmoMod;
import cn.chloeprime.gunsmithlib_std_ammo.common.block.GSABlocks;
import cn.chloeprime.gunsmithlib_std_ammo.common.block.InfectiousLeavesBlock;
import cn.chloeprime.gunsmithlib_std_ammo.common.item.GSAItems;
import cn.chloeprime.gunsmithlib_std_ammo.common.util.DatagenRegistryHelper;
import com.google.common.base.Suppliers;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

public class GSALootProvider extends LootTableProvider {
    public GSALootProvider(PackOutput output) {
        super(output, Set.of(), entries());
    }

    private static List<LootTableProvider.SubProviderEntry> entries() {
        return List.of(
                new LootTableProvider.SubProviderEntry(BlockLoot::new, LootContextParamSets.BLOCK)
        );
    }

    private static class BlockLoot extends BlockLootSubProvider implements DatagenRegistryHelper {
        private static final Supplier<Set<Item>> EXPLOSION_RESISTANT = Suppliers.memoize(() -> Set.of(
                GSAItems.STEEL_INGOT.get(),
                GSAItems.PURIFIED_DEBRIS.get(),
                GSAItems.TUNGSTEN_INGOT.get()
        ));

        private BlockLoot() {
            super(EXPLOSION_RESISTANT.get(), FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        protected @Nonnull Iterable<Block> getKnownBlocks() {
            return StreamSupport.stream(super.getKnownBlocks().spliterator(), false)
                    .filter(block -> GunsmithLibStdAmmoMod.MOD_ID.equals(getKey(block).getNamespace()))
                    .toList();
        }

        @Override
        protected void generate() {
            add(GSABlocks.PURIFIED_DEBRIS.get(), blk -> createOreDrop(blk, GSAItems.RAW_TUNGSTEN.get()));
            add(GSABlocks.TIBERIUM_ORE.get(), blk -> createOreDrop(blk, GSAItems.GREEN_TIBERIUM_CRYSTAL.get()));
            add(GSABlocks.DEEPSLATE_TIBERIUM_ORE.get(), blk -> createOreDrop(blk, GSAItems.GREEN_TIBERIUM_CRYSTAL.get()));
            add(GSABlocks.NETHER_TIBERIUM_ORE.get(), createDenseTiberiumOreDrops(GSAItems.GREEN_TIBERIUM_CRYSTAL.get()));
            add(GSABlocks.END_TIBERIUM_SEED_ORE.get(), blk -> createOreDrop(blk, GSAItems.TIBERIUM_SEED.get()));
            add(GSABlocks.TIBERIUM_LEAVES.get(), this::tiberiumLeaves);
        }

        private static final float[] CRYSTAL_CHANCES = new float[]{0.2F, 0.22222223F, 0.25F, 0.33333335F, 0.5F};

        protected Function<Block, LootTable.Builder> createDenseTiberiumOreDrops(ItemLike drop) {
            return ore -> createSilkTouchDispatchTable(ore, this.applyExplosionDecay(ore, LootItem
                    .lootTableItem(drop)
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F)))
                    .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
        }

        private LootTable.Builder tiberiumLeaves(Block leaves) {
            var dropSelf = HAS_SHEARS.or(HAS_SILK_TOUCH);
            var dropStick = checkBlockState(leaves, InfectiousLeavesBlock.STAGE, 0, 1, 2, 3).and(dropSelf.invert());
            var dropGreen = checkBlockState(leaves, InfectiousLeavesBlock.STAGE, 0, 1, 2, 3, 4, 5, 6).and(dropSelf.invert());
            var dropBlue = checkBlockState(leaves, InfectiousLeavesBlock.STAGE, 7).and(dropSelf.invert());
            var crystal = new AlternativesEntry.Builder()
                    .when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, CRYSTAL_CHANCES))
                    .otherwise(LootItem.lootTableItem(GSAItems.GREEN_TIBERIUM_CRYSTAL.get()).when(dropGreen))
                    .otherwise(LootItem.lootTableItem(GSAItems.BLUE_TIBERIUM_CRYSTAL.get()).when(dropBlue));
            return createSilkTouchOrShearsDispatchTable(leaves, applyExplosionCondition(leaves, crystal))
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0F))
                            .when(dropStick)
                            .add(this.applyExplosionDecay(leaves, LootItem.lootTableItem(Items.STICK)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
                                    .when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, CRYSTAL_CHANCES))
                            )
                    );
        }

        private static
        LootItemCondition.Builder checkBlockState(Block block, Property<Integer> property, int... values) {
            var stateConditions = new ArrayList<StatePropertiesPredicate.Builder>();
            for (int value : values) {
                stateConditions.add(StatePropertiesPredicate.Builder.properties().hasProperty(property, value));
            }

            var conditions = stateConditions.stream()
                    .map(stateCondition -> LootItemBlockStatePropertyCondition
                                    .hasBlockStateProperties(block)
                                    .setProperties(stateCondition))
                    .toArray(LootItemCondition.Builder[]::new);
            if (conditions.length == 1) {
                return conditions[0];
            } else {
                return AnyOfCondition.anyOf(conditions);
            }
        }
    }
}
