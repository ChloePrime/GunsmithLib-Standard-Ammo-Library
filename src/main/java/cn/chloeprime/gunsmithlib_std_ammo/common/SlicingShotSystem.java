package cn.chloeprime.gunsmithlib_std_ammo.common;

import cn.chloeprime.gunsmithlib_std_ammo.common.entity.SlicingWarhead;
import cn.chloeprime.gunsmithlib_std_ammo.common.gunpack.GSAGunpackExtension;
import cn.chloeprime.gunsmithlib_std_ammo.mixin.BulletAccessor;
import com.tacz.guns.api.event.common.EntityHurtByGunEvent;
import mod.chloeprime.gunsmithlib.api.util.Gunsmith;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class SlicingShotSystem {
    @SubscribeEvent
    public static void onGunshotPost(EntityHurtByGunEvent.Post event) {
        if (event.getLogicalSide().isClient()) {
            return;
        }
        var bullet = event.getBullet();
        var shooter = event.getAttacker();
        var victim = event.getHurtEntity();
        if (shooter == null || victim == null || bullet == null) {
            return;
        }
        var ext = Gunsmith.getGunInfo(shooter.getMainHandItem())
                .map(gun -> gun.index().getGunData())
                .flatMap(GSAGunpackExtension::of)
                .orElse(null);
        if (ext == null) {
            return;
        }
        var props = ext.getSlicingShotProperties().orElse(null);
        if (props == null) {
            return;
        }
        var slicer = new SlicingWarhead(victim.level(), props);
        var hitPos = victim.getBoundingBox()
                .clip(bullet.position(), bullet.position().add(bullet.getDeltaMovement()))
                .orElseGet(() -> bullet.position().lerp(victim.getEyePosition(), 0.8));
        if (bullet instanceof BulletAccessor accessor) {
            slicer.setGunId(accessor.getGunId());
            slicer.setAmmoId(ext.getSlicingShotModelOverride().orElseGet(accessor::getAmmoId));
        }
        slicer.setSlicingTarget(victim);
        slicer.setPos(hitPos);
        slicer.setHitInfo(victim, hitPos, bullet.getDeltaMovement().normalize().scale(-1));
        victim.level().addFreshEntity(slicer);
    }
}
