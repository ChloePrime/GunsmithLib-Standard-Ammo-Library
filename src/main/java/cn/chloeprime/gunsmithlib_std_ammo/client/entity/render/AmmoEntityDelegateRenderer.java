package cn.chloeprime.gunsmithlib_std_ammo.client.entity.render;

import cn.chloeprime.gunsmithlib_std_ammo.common.util.AmmoIdHolder;
import cn.chloeprime.gunsmithlib_std_ammo.mixin.BulletAccessor;
import com.tacz.guns.init.ModEntities;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;

public class AmmoEntityDelegateRenderer<E extends Entity & AmmoIdHolder> extends DelegateRenderer<E> {
    public AmmoEntityDelegateRenderer(EntityRendererProvider.Context context) {
        super(context, ModEntities.BULLET::get);
    }

    @Override
    protected void setupDelegateInfo(E self, Entity delegate) {
        if (delegate instanceof BulletAccessor accessor) {
            accessor.setGunId(self.getGunId());
            accessor.setAmmoId(self.getAmmoId());
        }
        delegate.setXRot(-self.getXRot());
        delegate.setYRot(-self.getYRot());
        delegate.xRotO = -self.xRotO;
        delegate.yRotO = -self.yRotO;
    }
}
