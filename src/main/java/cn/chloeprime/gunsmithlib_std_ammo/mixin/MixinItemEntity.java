package cn.chloeprime.gunsmithlib_std_ammo.mixin;

import cn.chloeprime.gunsmithlib_std_ammo.common.entity.SlicingWarhead;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public class MixinItemEntity {
    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void mercyItemsFromSlicerExplosion(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!source.is(DamageTypeTags.IS_EXPLOSION)) {
            return;
        }
        if (source.getDirectEntity() instanceof SlicingWarhead) {
            cir.setReturnValue(false);
        }
    }
}
