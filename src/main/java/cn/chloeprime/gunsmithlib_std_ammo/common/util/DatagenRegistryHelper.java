package cn.chloeprime.gunsmithlib_std_ammo.common.util;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nonnull;
import java.util.Objects;

@SuppressWarnings("deprecation")
public interface DatagenRegistryHelper {
    default <T> @Nonnull ResourceLocation getKey(Registry<T> reg, T instance) {
        return Objects.requireNonNull(reg.getKey(instance));
    }

    default @Nonnull ResourceLocation getKey(Block block) {
        return getKey(BuiltInRegistries.BLOCK, block);
    }

    default @Nonnull ResourceLocation getKey(Item item) {
        return getKey(BuiltInRegistries.ITEM, item);
    }
}
