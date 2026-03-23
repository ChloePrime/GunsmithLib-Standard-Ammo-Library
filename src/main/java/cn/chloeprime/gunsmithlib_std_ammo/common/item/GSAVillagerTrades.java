package cn.chloeprime.gunsmithlib_std_ammo.common.item;

import cn.chloeprime.gunsmithlib_std_ammo.GunsmithLibStdAmmoMod;
import com.google.common.base.Suppliers;
import com.tacz.guns.api.item.builder.GunItemBuilder;
import com.tacz.guns.api.item.gun.FireMode;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Supplier;

@Mod.EventBusSubscriber
public final class GSAVillagerTrades {
    public static final Supplier<ItemStack> LEON_ROCKET_ITEM = Suppliers.memoize(() -> GunItemBuilder.create()
            .setId(GunsmithLibStdAmmoMod.loc("rpg7_leon"))
            .setFireMode(FireMode.SEMI)
            .setAmmoCount(1)
            .build());

    public static final Supplier<VillagerTrades.ItemListing> LEON_ROCKET = Suppliers.memoize(() -> new BasicItemListing(
            new ItemStack(Items.NETHERITE_BLOCK, 64),
            new ItemStack(Items.NETHER_STAR, 64),
            LEON_ROCKET_ITEM.get(),
            1, 100, 1));

    @SubscribeEvent
    public static void onWanderTrade(WandererTradesEvent event) {
        event.getRareTrades().add(LEON_ROCKET.get());
    }

    private GSAVillagerTrades() {
    }
}
