package cn.chloeprime.gunsmithlib_std_ammo.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PrimedTnt.class)
public interface PrimedTntAccessor {
    @Accessor("owner") void gunsmithlib_std_ammo$setOwner(@Nullable LivingEntity owner);
}
