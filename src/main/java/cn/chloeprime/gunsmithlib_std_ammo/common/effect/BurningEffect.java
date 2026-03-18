package cn.chloeprime.gunsmithlib_std_ammo.common.effect;

import cn.chloeprime.commons.async.TaskScheduler;
import cn.chloeprime.commons.rpc.RPC;
import cn.chloeprime.commons.rpc.RPCFlow;
import cn.chloeprime.commons.rpc.RPCTarget;
import cn.chloeprime.commons.rpc.RemoteCallable;
import cn.chloeprime.gunsmithlib_std_ammo.mixin.EntityAccessor;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.LogicalSide;
import org.apache.commons.lang3.mutable.MutableInt;

import javax.annotation.Nonnull;
import java.util.EnumMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * 拥有此 buff 时持有者受到的火焰伤害增加，且着火无法扑灭。
 */
public class BurningEffect extends MobEffectBaseUtility {
    public BurningEffect(MobEffectCategory category, int color) {
        super(category, color);
        MinecraftForge.EVENT_BUS.addListener(this::onLivingHurt);
    }

    public static MobEffect bootstrap() {
        return new BurningEffect(MobEffectCategory.HARMFUL, 0xFFE100);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 10 == 0;
    }

    @Override
    public void applyEffectTick(@Nonnull LivingEntity victim, int amplifier) {
        super.applyEffectTick(victim, amplifier);
        extendFireTicksFor(victim);
    }

    public float getCoefficientFor(LivingEntity victim) {
        // 在水里时受到的灼烧伤害极大幅降低
        if (victim.isInWater()) {
            return 0.05F;
        }
        // 在雨中时受到的灼烧伤害大幅降低
        if (((EntityAccessor) victim).invokeIsInRain()) {
            return 0.2F;
        }
        // 趴下和蹲下
        if (!victim.isSwimming() && victim.getPose() == Pose.SWIMMING) {
            return 0.25F;
        }
        if (victim.isCrouching()) {
            return 0.75F;
        }
        if (victim.isSprinting()) {
            return 1.5F;
        }
        return 1;
    }

    private void extendFireTicksFor(LivingEntity victim) {
        if (victim.level().isClientSide()) {
            return;
        }
        // 创造模式不续火
        if (victim instanceof Player player && player.getAbilities().instabuild) {
            return;
        }
        // 在水中时造成火焰伤害，但是伤害量大幅降低
        if (victim.isInWaterOrRain()) {
            tryDamageForFireImmuneOnes(victim, false);
            return;
        }
        // 免疫火焰伤害时转而魔法伤害
        if (victim.fireImmune() || victim.isInvulnerableTo(victim.damageSources().onFire())) {
            tryDamageForFireImmuneOnes(victim, true);
            return;
        }
        // 续火
        if (victim.getRemainingFireTicks() < 20) {
            victim.setRemainingFireTicks(victim.getRemainingFireTicks() + 40);
        }
    }

    private void tryDamageForFireImmuneOnes(LivingEntity victim, boolean isIntrinsic) {
        if (victim.level().getGameTime() % 20 >= 10) {
            return;
        }
        var level = getLevelFor(victim);
        if (level <= 0) {
            return;
        }
        // 防止切换入水出水后，由于原版系统和水中伤害系统基准时间差异
        // 导致的两次连续伤害
        if (isInHurtSafeInsurance(victim)) {
            return;
        }
        // 造成伤害
        var damageBuf = getDamageFor(victim, getLevelFor(victim)) * getCoefficientFor(victim);
        var newDamage = Mth.lerp(getCoefficientFor(victim), 1, damageBuf);
        var source = isIntrinsic
                ? victim.damageSources().magic()
                : victim.damageSources().onFire();
        var isInternalCall = IS_INTERNAL_HURT_CALL.get();
        try {
            isInternalCall.increment();
            victim.hurt(source, newDamage);
        } finally {
            isInternalCall.decrement();
        }
        // 必须先打出伤害再更新上次受伤时间，
        // 否则应该打出去的伤害会被连续伤害保险拦截。
        setLastHurtByFire(victim);
    }

    private static float getDamageFor(LivingEntity victim, int level) {
        return level * (float) (1 + 5 * Math.log(Math.max(victim.getMaxHealth(), 20) / 20) / Math.log(15));
    }

    private static final EnumMap<LogicalSide, TaskScheduler> DELAYER = new EnumMap<>(LogicalSide.class);
    private static final ThreadLocal<Map<Entity, Long>> LAST_HURT_BY_FIRE = ThreadLocal.withInitial(WeakHashMap::new);
    private static final ThreadLocal<MutableInt> IS_INTERNAL_HURT_CALL = ThreadLocal.withInitial(MutableInt::new);

    private static void setLastHurtByFire(Entity victim) {
        RPC.call(RPCTarget.near(victim), BurningEffect::rpcSetLastHurtByFire, victim);
    }

    @RemoteCallable(flow = RPCFlow.SERVER_TO_CLIENT, callLocally = true)
    private static void rpcSetLastHurtByFire(Entity victim) {
        if (victim == null) {
            return;
        }
        var side = victim.level().isClientSide() ? LogicalSide.CLIENT : LogicalSide.SERVER;
        var delay = 1;
        DELAYER.computeIfAbsent(side, TaskScheduler::createTickBased).delay(delay).thenRun(() -> {
            if (victim.isAlive()) {
                LAST_HURT_BY_FIRE.get().put(victim, victim.level().getGameTime() - delay);
            }
        });
    }

    public static boolean isInHurtSafeInsurance(Entity victim) {
        var lastHurt = LAST_HURT_BY_FIRE.get().getOrDefault(victim, 0L);
        var now = victim.level().getGameTime();
        return now - lastHurt < 20;
    }

    private void onLivingHurt(LivingHurtEvent event) {
        if (IS_INTERNAL_HURT_CALL.get().getValue() > 0) {
            return;
        }
        if (!event.getSource().is(DamageTypes.ON_FIRE)) {
            return;
        }
        var victim = event.getEntity();

        // 客户端会单独进行火焰伤害，
        // 上面的代码需要在客户端也执行，来减少被禁用伤害的反馈。
        if (victim.level().isClientSide()) {
            // 客户端无法获取效果等级，
            // 所以要在判断是否有效果之前就判断连续伤害保险
            if (isInHurtSafeInsurance(victim)) {
                event.setCanceled(true);
            }
            return;
        }

        var level = getLevelFor(victim);
        if (level <= 0) {
            return;
        }
        // 防止切换入水出水后，由于原版系统和水中伤害系统基准时间差异
        // 导致的两次连续伤害
        if (isInHurtSafeInsurance(victim)) {
            event.setCanceled(true);
            return;
        }
        setLastHurtByFire(victim);
        // 1 级时：
        // f(20) = 1
        // f(300) = 6
        var damageBuf = getDamageFor(victim, level);
        var oldDamage = event.getAmount();
        var newDamage = oldDamage + event.getAmount() + Math.max(0, damageBuf - 1);
        // 减掉自带的 1 点火焰伤害
        event.setAmount(Mth.lerp(getCoefficientFor(victim), oldDamage, newDamage));
    }
}
