package cn.chloeprime.gunsmithlib_std_ammo.mixin;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PotionBrewing.class)
public interface PotionBrewingAccessor {
    @Invoker
    static void invokeAddMix(Potion base, Item ingredient, Potion result) {
        throw new AbstractMethodError();
    }
}
