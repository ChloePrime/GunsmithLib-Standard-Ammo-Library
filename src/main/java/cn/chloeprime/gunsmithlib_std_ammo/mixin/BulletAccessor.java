package cn.chloeprime.gunsmithlib_std_ammo.mixin;

import com.tacz.guns.entity.EntityKineticBullet;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = EntityKineticBullet.class, remap = false)
public interface BulletAccessor {
    @Accessor ResourceLocation getGunId();
    @Accessor ResourceLocation getAmmoId();
    @Accessor void setGunId(ResourceLocation value);
    @Accessor void setAmmoId(ResourceLocation value);
}
