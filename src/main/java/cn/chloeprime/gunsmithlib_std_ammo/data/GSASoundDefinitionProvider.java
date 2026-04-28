package cn.chloeprime.gunsmithlib_std_ammo.data;

import cn.chloeprime.gunsmithlib_std_ammo.GunsmithLibStdAmmoMod;
import cn.chloeprime.gunsmithlib_std_ammo.common.GSASoundEvents;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class GSASoundDefinitionProvider extends SoundDefinitionsProvider {
    public GSASoundDefinitionProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, GunsmithLibStdAmmoMod.MOD_ID, helper);
    }

    @Override
    public void registerSounds() {
        add(GSASoundEvents.BULLET_MERCHANT_AMBIENT, "entity", "bullet_merchant.ambient",
                loc("entity/generic_girl/uh_huh"),
                loc("entity/generic_girl/hrmm_0"),
                loc("entity/generic_girl/hrmm_1"));
        add(GSASoundEvents.BULLET_MERCHANT_TRADE, "entity", "bullet_merchant.trade",
                loc("entity/generic_girl/uh_huh"),
                loc("entity/generic_girl/huh_0"),
                loc("entity/generic_girl/huh_1"));
        add(GSASoundEvents.BULLET_MERCHANT_HURT, "entity", "bullet_merchant.hurt", loc("entity/generic_girl/hurt_%s", 2));
        add(GSASoundEvents.BULLET_MERCHANT_DEATH, "entity", "bullet_merchant.death", loc("entity/generic_girl/call_medic_%s", 5));
        add(GSASoundEvents.BULLET_MERCHANT_YES, "entity", "bullet_merchant.yes", loc("entity/generic_girl/yes_%s", 2));
        add(GSASoundEvents.BULLET_MERCHANT_NO, "entity", "bullet_merchant.no", loc("entity/generic_girl/baka"));
    }

    private static String[] loc(String template, int count) {
        return IntStream.range(0, count)
                .mapToObj(template::formatted)
                .map(GSASoundDefinitionProvider::loc)
                .toArray(String[]::new);
    }

    private static String loc(String path) {
        return GunsmithLibStdAmmoMod.loc(path).toString();
    }

    private void add(Supplier<SoundEvent> se, String category, String usage, String... sounds) {
        var soundsOop = Arrays.stream(sounds)
                .map(ResourceLocation::tryParse)
                .filter(Objects::nonNull)
                .map(SoundDefinitionsProvider::sound)
                .toArray(SoundDefinition.Sound[]::new);
        add(se, category, usage, soundsOop);
    }

    private void add(Supplier<SoundEvent> se, String category, String usage, SoundDefinition.Sound... sounds) {
        var reg = (RegistryObject<SoundEvent>) se;
        var id = Objects.requireNonNull(reg.getId());
        var subtitle = "subtitles.%s.%s.%s".formatted(category, id.getNamespace(), usage);
        add(se.get(), definition().subtitle(subtitle).with(sounds));
    }
}
