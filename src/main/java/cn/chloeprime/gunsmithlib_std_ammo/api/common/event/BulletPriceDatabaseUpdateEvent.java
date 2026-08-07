package cn.chloeprime.gunsmithlib_std_ammo.api.common.event;

import cn.chloeprime.gunsmithlib_std_ammo.common.item.GSABulletPriceDatabase;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

public class BulletPriceDatabaseUpdateEvent extends Event {
    private final List<GSABulletPriceDatabase.Entry> entries;
    private final VillagerTrades.ItemListing[] listings;

    @ApiStatus.Internal
    public BulletPriceDatabaseUpdateEvent(List<GSABulletPriceDatabase.Entry> entries, VillagerTrades.ItemListing[] listings) {
        this.entries = entries;
        this.listings = listings;
    }

    public List<GSABulletPriceDatabase.Entry> getEntries() {
        return entries;
    }

    public VillagerTrades.ItemListing[] getListings() {
        return listings;
    }
}
