package cn.chloeprime.gunsmithlib_std_ammo.common.item;

import cn.chloeprime.gunsmithlib_std_ammo.common.gunpack.EnhancedAmmoData;
import cn.chloeprime.gunsmithlib_std_ammo.common.gunpack.GSAGunpackExtension;
import cn.chloeprime.gunsmithlib_std_ammo.common.gunpack.PriceData;
import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.item.builder.AmmoItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.*;

@Mod.EventBusSubscriber
public final class GSABulletPriceDatabase {
    public record Entry(
            @Nonnull ResourceLocation id,
            @Nonnull PriceData price
    ) {
        public Entry {
            Objects.requireNonNull(id);
            Objects.requireNonNull(price);
        }
    }

    @SuppressWarnings("unused")
    public static List<Entry> entries() {
        return ENTRIES;
    }

    public static VillagerTrades.ItemListing[] listings() {
        return LISTINGS;
    }

    private static final List<Entry> ENTRIES = Collections.synchronizedList(new ArrayList<>(64));
    private static VillagerTrades.ItemListing[] LISTINGS = new VillagerTrades.ItemListing[0];

    @SubscribeEvent
    public static void onDatapackLoad(LevelEvent.Load event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        reload();
    }

    @SubscribeEvent
    public static void onDatapackReload(OnDatapackSyncEvent event) {
        if (event.getPlayer() != null) {
            return;
        }
        reload();
    }

    private static void reload() {
        ENTRIES.clear();
        TimelessAPI.getAllCommonAmmoIndex().stream()
                .map(kvp -> Pair.of(kvp.getKey(), ((EnhancedAmmoData) kvp.getValue().getPojo()).extension()))
                .map(kvp -> Pair.of(kvp.getKey(), Optional.ofNullable(kvp.getValue()).flatMap(GSAGunpackExtension::getPrice).orElse(null)))
                .filter(kvp -> kvp.getValue() != null)
                .map(kvp -> new Entry(kvp.getKey(), kvp.getValue()))
                .forEach(ENTRIES::add);
        LISTINGS = ENTRIES.stream()
                .map(GSABulletPriceDatabase::createListing)
                .toArray(VillagerTrades.ItemListing[]::new);
    }

    private static VillagerTrades.ItemListing createListing(Entry entry) {
        var ammo = AmmoItemBuilder.create()
                .setId(entry.id())
                .setCount(entry.price().amount())
                .build();
        return new BasicItemListing(entry.price().cost(), ammo, getMaxTrades(entry), getExp(entry));
    }

    private static int getMaxTrades(Entry entry) {
        var cost = entry.price().cost();
        if (cost > 64) {
            return 4;
        } else if (cost > 32) {
            return 16;
        } else if (cost > 16) {
            return 64;
        } else {
            return 4096;
        }
    }

    private static int getExp(Entry entry) {
        return (int) Math.max(1, Math.sqrt(entry.price().cost()));
    }

    private GSABulletPriceDatabase() {
    }
}
