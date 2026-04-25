package cn.chloeprime.gunsmithlib_std_ammo.common.entity.ai;

import cn.chloeprime.commons.rpc.RPC;
import cn.chloeprime.commons.rpc.RPCFlow;
import cn.chloeprime.commons.rpc.RPCTarget;
import cn.chloeprime.commons.rpc.RemoteCallable;
import cn.chloeprime.gunsmithlib_std_ammo.client.util.TaCZClientSounds;
import com.tacz.guns.api.entity.IGunOperator;
import com.tacz.guns.api.item.IGun;
import mod.chloeprime.gunsmithlib.api.util.Gunsmith;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class GunfightGoal<T extends Mob & GunfightMob> extends Goal {
    private final T mob;
    private final double speedModifier;
    private final float attackRadiusSqr;
    private int attackTime = -1;
    private int seeTime;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;
    private int startAimingTime;
    private boolean reloading;
    private int reloadTime;
    private int stopDelay;

    public GunfightGoal(T pMob, double pSpeedModifier, float pAttackRadius) {
        this.mob = pMob;
        this.speedModifier = pSpeedModifier;
        this.attackRadiusSqr = pAttackRadius * pAttackRadius;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public boolean isHoldingGun() {
        return isGun(this.mob.getMainHandItem()) || isGun(this.mob.getOffhandItem());
    }

    public static boolean isGun(ItemStack item) {
        return IGun.getIGunOrNull(item) != null;
    }

    public int getReloadTime() {
        return Gunsmith.getGunInfo(this.mob.getMainHandItem())
                .map(gi -> gi.index().getGunData().getReloadData().getCooldown().getTacticalTime())
                .map(seconds -> Math.round(seconds * 20))
                .orElse(20);
    }

    public static void swapHandItems(LivingEntity mob) {
        var main = mob.getMainHandItem();
        var offH = mob.getOffhandItem();
        mob.setItemSlot(EquipmentSlot.MAINHAND, offH);
        mob.setItemSlot(EquipmentSlot.OFFHAND, main);
    }

    @Override
    public boolean canUse() {
        return this.mob.getTarget() != null && isHoldingGun();
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() || stopDelay > 0;
    }

    @Override
    public void start() {
        super.start();
        this.mob.setAggressive(true);
        if (!isGun(this.mob.getMainHandItem()) && isGun(this.mob.getOffhandItem())) {
            swapHandItems(this.mob);
        }
        IGunOperator.fromLivingEntity(this.mob).draw(this.mob::getMainHandItem);
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    @Override
    public void stop() {
        super.stop();
        this.mob.setAggressive(false);
        this.seeTime = 0;
        this.attackTime = -1;
        this.mob.stopUsingItem();
        if (isGun(this.mob.getMainHandItem()) && !isGun(this.mob.getOffhandItem())) {
            swapHandItems(this.mob);
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void tick() {
        var target = this.mob.getTarget();
        if (target != null) {
            double sqrDistance = this.mob.distanceToSqr(target.getX(), target.getY(), target.getZ());
            boolean hasLos = this.mob.getSensing().hasLineOfSight(target);
            boolean hasSeen = this.seeTime > 0;
            if (hasLos != hasSeen) {
                this.seeTime = 0;
            }

            if (hasLos) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }

            if (sqrDistance <= this.attackRadiusSqr && this.seeTime >= 20) {
                this.mob.getNavigation().stop();
                ++this.strafingTime;
            } else {
                this.mob.getNavigation().moveTo(target, this.speedModifier);
                this.strafingTime = -1;
            }

            tickStrafe(target, sqrDistance);
            this.mob.lookAt(target, 30.0F, 30.0F);
            tickWeapon(target, hasLos);
            this.stopDelay = 20;
        } else {
            if (stopDelay > 0) {
                stopDelay--;
            }
        }
    }

    private void tickStrafe(LivingEntity target, double sqrDistance) {
        if (this.strafingTime >= 20) {
            if (this.mob.getRandom().nextFloat() < 0.3) {
                this.strafingClockwise = !this.strafingClockwise;
            }

            if (this.mob.getRandom().nextFloat() < 0.3) {
                this.strafingBackwards = !this.strafingBackwards;
            }

            this.strafingTime = 0;
        }

        if (this.strafingTime > -1) {
            if (sqrDistance > this.attackRadiusSqr * 0.75) {
                this.strafingBackwards = false;
            } else if (sqrDistance < this.attackRadiusSqr * 0.25) {
                this.strafingBackwards = true;
            }
            this.mob.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);

            if (this.mob.getControlledVehicle() instanceof Mob vehicle) {
                vehicle.lookAt(target, 30.0F, 30.0F);
            }
        }
    }

    private void tickWeapon(LivingEntity target, boolean hasLos) {
        var weapon = Gunsmith.getGunInfo(this.mob.getMainHandItem()).orElse(null);
        if (weapon == null) {
            return;
        }
        if (reloading) {
            reloadTime--;
            if (reloadTime <= 0) {
                reloadTime = 0;
                reloading = false;
                weapon.setTotalAmmo(weapon.getTotalMagazineSize());
            }
            return;
        }
        if (weapon.getTotalAmmo() == 0) {
            LivingEntity self = this.mob; // Prevent method reference being converted to lambda
            RPC.call(RPCTarget.near(self), GunfightGoal::rpcPlayReloadSound, self);
            reloading = true;
            reloadTime = getReloadTime();
            return;
        }

        if (this.mob.isAiming()) {
            if (!hasLos && this.seeTime < -60) {
                this.mob.stopAiming();
            } else if (hasLos) {
                int i = this.startAimingTime++;
                if (i >= 20) {
                    if (!canHit(target)) {
                        this.startAimingTime -= 10;
                        return;
                    }
                    var report = this.mob.shoot();
                    this.attackTime = report.map(GunfightMob.ShootReport::delay).orElse(20);
                }
            }
        } else if (--this.attackTime <= 0 && this.seeTime >= -60) {
            this.mob.startAiming();
            this.startAimingTime = 0;
        }
    }

    @RemoteCallable(flow = RPCFlow.SERVER_TO_CLIENT)
    private static void rpcPlayReloadSound(LivingEntity target) {
        TaCZClientSounds.playTacReload(target);
    }

    private boolean canHit(LivingEntity target) {
        var threshold = target.distanceToSqr(this.mob) <= 8 * 8
                ? Math.toRadians(60)
                : Math.toRadians(15);
        for (Vec3 candidate : new Vec3[]{target.getEyePosition(), target.getBoundingBox().getCenter(), target.position()}) {
            if (canHit(candidate, threshold)) {
                return true;
            }
        }
        return false;
    }

    private boolean canHit(Vec3 pos, double threshold) {
        var muzzle = this.mob.getEyePosition();
        var offset = pos.subtract(muzzle);
        var cos = offset.dot(this.mob.getLookAngle()) / offset.length();
        return cos >= Math.cos(threshold);
    }
}
