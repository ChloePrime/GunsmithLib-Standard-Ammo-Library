package cn.chloeprime.gunsmithlib_std_ammo.client.util;

import cn.chloeprime.gunsmithlib_std_ammo.GunsmithLibStdAmmoMod;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.client.sound.SoundPlayManager;
import com.tacz.guns.sound.SoundManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class TaCZClientSounds {
    public static final ResourceLocation DEFAULT_RELOAD_SOUNDS = GunsmithLibStdAmmoMod.loc("tacz", "m4a1/m4a1_reload_empty");

    public static void playTacReload(@Nullable LivingEntity entity) {
        if (entity == null) {
            return;
        }
        TimelessAPI.getGunDisplay(entity.getMainHandItem()).ifPresent(display -> {
            var sound = Objects.requireNonNullElse(display.getSounds(SoundManager.RELOAD_TACTICAL_SOUND), DEFAULT_RELOAD_SOUNDS);
            SoundPlayManager.playClientSound(entity, sound, 1, 1, 16);
        });
    }
}
