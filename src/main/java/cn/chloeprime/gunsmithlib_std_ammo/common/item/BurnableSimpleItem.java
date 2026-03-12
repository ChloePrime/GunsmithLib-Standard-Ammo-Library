package cn.chloeprime.gunsmithlib_std_ammo.common.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;

public class BurnableSimpleItem extends SimpleItem {
    private final int burnTime;

    public BurnableSimpleItem(int burnTime, Properties properties) {
        super(properties);
        this.burnTime = burnTime;
    }

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return burnTime;
    }
}
