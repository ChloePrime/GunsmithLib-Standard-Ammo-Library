package cn.chloeprime.gunsmithlib_std_ammo.data;

import cn.chloeprime.gunsmithlib_std_ammo.GunsmithLibStdAmmoMod;
import cn.chloeprime.gunsmithlib_std_ammo.common.item.GSAItemTags;
import cn.chloeprime.gunsmithlib_std_ammo.common.util.DatagenRegistryHelper;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static cn.chloeprime.gunsmithlib_std_ammo.common.item.GSAItems.*;

public class GSARecipeProvider extends RecipeProvider implements DatagenRegistryHelper {
    public GSARecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(@Nonnull Consumer<FinishedRecipe> output) {
        // 炼钢
        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(Tags.Items.INGOTS_IRON),
                        RecipeCategory.MISC,
                        STEEL_INGOT.get(),
                        6.4F,
                        6400)
                .unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
                .save(output, GunsmithLibStdAmmoMod.loc("steel_ingot_from_blasting"));
        // 硝酸混酸
        ShapelessRecipeBuilder
                .shapeless(RecipeCategory.MISC, NITRATION_MIXTURE.get(), 4)
                .requires(Tags.Items.GUNPOWDER)
                .requires(Tags.Items.GUNPOWDER)
                .requires(Items.BLAZE_POWDER)
                .requires(Items.BLAZE_POWDER)
                .requires(Items.GLASS_BOTTLE)
                .requires(Items.GLASS_BOTTLE)
                .requires(Items.GLASS_BOTTLE)
                .requires(Items.GLASS_BOTTLE)
                .requires(Items.WATER_BUCKET)
                .unlockedBy("has_blaze_powder", has(Items.BLAZE_POWDER))
                .save(output, getKey(NITRATION_MIXTURE.get()));
        // 硝酸混酸处理钨矿
        ShapelessRecipeBuilder
                .shapeless(RecipeCategory.BUILDING_BLOCKS, PURIFIED_DEBRIS.get())
                .requires(NITRATION_MIXTURE.get())
                .requires(Items.ANCIENT_DEBRIS)
                .unlockedBy("has_nitration_mixture", has(NITRATION_MIXTURE.get()))
                .save(output, getKey(PURIFIED_DEBRIS.get()));
        // 烧钨
        gsaOreSmelting(output, List.of(PURIFIED_DEBRIS.get(), RAW_TUNGSTEN.get()), RecipeCategory.MISC, TUNGSTEN_INGOT.get(), 1.5F, 200, "tungsten");
        gsaOreBlasting(output, List.of(PURIFIED_DEBRIS.get(), RAW_TUNGSTEN.get()), RecipeCategory.MISC, TUNGSTEN_INGOT.get(), 1.5F, 100, "tungsten");
        // 钨刀片
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TUNGSTEN_BLADE.get())
                .define('#', Tags.Items.INGOTS_IRON)
                .define('X', GSAItemTags.INGOTS_TUNGSTEN)
                .pattern("X")
                .pattern("X")
                .pattern("#")
                .unlockedBy("has_tungsten_ingot", has(GSAItemTags.INGOTS_TUNGSTEN))
                .save(output);
        // 烧泰伯利亚矿系列
        var greenTibOres = Stream.of(TIBERIUM_ORE, DEEPSLATE_TIBERIUM_ORE, NETHER_TIBERIUM_ORE).map(Supplier::get).toList();
        gsaOreSmelting(output, greenTibOres, RecipeCategory.MISC, GREEN_TIBERIUM_CRYSTAL.get(), 0.5F, 200, "green_tiberium_crystal_ore");
        gsaOreBlasting(output, greenTibOres, RecipeCategory.MISC, GREEN_TIBERIUM_CRYSTAL.get(), 0.5F, 100, "green_tiberium_crystal_ore");
        gsaOreSmelting(output, List.of(END_TIBERIUM_SEED_ORE.get()), RecipeCategory.MISC, TIBERIUM_SEED.get(), 25, 200, "tiberium_seed");
        gsaOreBlasting(output, List.of(END_TIBERIUM_SEED_ORE.get()), RecipeCategory.MISC, TIBERIUM_SEED.get(), 25, 100, "tiberium_seed");

        // N2 炸药
        ShapelessRecipeBuilder
                .shapeless(RecipeCategory.MISC, N2_DYNAMITE.get())
                .requires(Tags.Items.GUNPOWDER)
                .requires(Items.BLAZE_POWDER)
                .requires(GSAItemTags.GEMS_TIB_GREEN)
                .unlockedBy("has_green_tiberiuum_crystal", has(GREEN_TIBERIUM_CRYSTAL.get()))
                .save(output, getKey(N2_DYNAMITE.get()));

        // 泰伯利亚混合物，泰伯利亚合金
        ShapelessRecipeBuilder
                .shapeless(RecipeCategory.MISC, TIBERIUM_COMPOUND.get())
                .requires(GSAItemTags.GEMS_TIB_GREEN)
                .requires(GSAItemTags.GEMS_TIB_GREEN)
                .requires(GSAItemTags.GEMS_TIB_GREEN)
                .requires(GSAItemTags.GEMS_TIB_BLUE)
                .requires(Items.CLAY_BALL)
                .requires(Items.CLAY_BALL)
                .requires(Items.CLAY_BALL)
                .requires(Items.CLAY_BALL)
                .unlockedBy("has_blue_tiberiuum_crystal", has(BLUE_TIBERIUM_CRYSTAL.get()))
                .save(output, getKey(TIBERIUM_COMPOUND.get()));
        gsaOreSmelting(output, List.of(TIBERIUM_COMPOUND.get()), RecipeCategory.MISC, TIBERIUM_ALLOY_INGOT.get(), 0.5F, 200, "tiberium_alloy");
        gsaOreBlasting(output, List.of(TIBERIUM_COMPOUND.get()), RecipeCategory.MISC, TIBERIUM_ALLOY_INGOT.get(), 0.5F, 100, "tiberium_alloy");
    }

    @ParametersAreNonnullByDefault
    protected static void gsaOreSmelting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<? extends ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTIme, String pGroup) {
        gsaOreCooking(pFinishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    @ParametersAreNonnullByDefault
    protected static void gsaOreBlasting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<? extends ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        gsaOreCooking(pFinishedRecipeConsumer, RecipeSerializer.BLASTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    @ParametersAreNonnullByDefault
    protected static void gsaOreCooking(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer, List<? extends ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer).group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike)).save(pFinishedRecipeConsumer, GunsmithLibStdAmmoMod.loc(getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike)));
        }
    }
}
