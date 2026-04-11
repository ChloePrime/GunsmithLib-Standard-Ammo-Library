package cn.chloeprime.gunsmithlib_std_ammo.common.effect;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.FakePlayerFactory;

import javax.annotation.Nonnull;
import java.util.UUID;

public class InstantDeathEffect extends InstantenousMobEffect {
    public static boolean isBoss(Entity entity) {
        return entity.getType().is(Tags.EntityTypes.BOSSES);
    }

    private static final GameProfile INVULNERABILITY_TESTER_PROFILE = new GameProfile(
            UUID.fromString("9dfe73d1-d877-43c9-b528-4c95630fcf15"),
            "[Invulnerability Tester]");

    public InstantDeathEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyEffectTick(@Nonnull LivingEntity victim, int amplifier) {
        if (victim.level().isClientSide()) {
            return;
        }
        if (victim instanceof Player player) {
            if (player.isCreative() || player.getAbilities().invulnerable) {
                return;
            }
        } else if (isBoss(victim)) {
            return;
        } else if (victim.level() instanceof ServerLevel serverLevel) {
            var tester = FakePlayerFactory.get(serverLevel, INVULNERABILITY_TESTER_PROFILE);
            if (victim.isInvulnerableTo(victim.damageSources().playerAttack(tester))) {
                return;
            }
        }
        victim.kill();
    }

    public static MobEffect bootstrap() {
        return new InstantDeathEffect(MobEffectCategory.HARMFUL, 0);
    }
}
