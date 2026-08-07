package cn.chloeprime.gunsmithlib_std_ammo.common.item;

import cn.chloeprime.gunsmithlib_std_ammo.GunsmithLibStdAmmoMod;
import com.google.common.base.Suppliers;
import com.tacz.guns.api.item.builder.GunItemBuilder;
import com.tacz.guns.api.item.gun.FireMode;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import net.minecraftforge.server.ServerLifecycleHooks;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
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

    public enum AutocannonTradingRecipe implements VillagerTrades.ItemListing {
        INSTANCE;

        public static final Supplier<List<ItemStack>> RARITIES = Suppliers.memoize(() -> new ArrayList<>(List.of(
                // 绿色染料
                new ItemStack(Items.GREEN_DYE, 64),
                // 棕色染料
                new ItemStack(Items.BROWN_DYE, 64),
                // 潮涌核心
                new ItemStack(Items.CONDUIT, 4),
                // 火把花
                new ItemStack(Items.TORCHFLOWER),
                // 瓶子草
                new ItemStack(Items.PITCHER_PLANT),
                // 尖啸体
                new ItemStack(Items.SCULK_SHRIEKER, 32),
                // 海绵
                new ItemStack(Items.SPONGE, 9),
                // 珠光（热带）蛙明灯
                new ItemStack(Items.PEARLESCENT_FROGLIGHT, 16),
                // 青翠（寒带）蛙明灯
                new ItemStack(Items.VERDANT_FROGLIGHT, 16)
        )));

        public static final Supplier<List<ItemStack>> PROGRESS_LOCKS = Suppliers.memoize(() -> new ArrayList<>(List.of(
                // 下界合金锭
                new ItemStack(Items.NETHERITE_INGOT, 4),
                // 下界之星
                new ItemStack(Items.NETHER_STAR),
                // 泰伯利亚种子
                new ItemStack(GSAItems.TIBERIUM_SEED.get())
        )));

        public static final Supplier<ItemStack> AUTOCANNON = Suppliers.memoize(() -> GunItemBuilder
                .create()
                .setId(GunsmithLibStdAmmoMod.loc("autocannon"))
                .setFireMode(FireMode.AUTO)
                .forceBuild());

        @Override
        @ParametersAreNonnullByDefault
        public @Nonnull MerchantOffer getOffer(Entity pTrader, RandomSource pRandom) {
            var price1 = pick(RARITIES.get()).copy();
            var price2 = pick(PROGRESS_LOCKS.get()).copy();
            return new MerchantOffer(price1, price2, AUTOCANNON.get().copy(), 1, 4000, 1);
        }

        private static <T> T pick(List<T> list) {
            var nextIntFp = Optional.ofNullable(ServerLifecycleHooks.getCurrentServer())
                    .filter(srv -> EffectiveSide.get().isServer())
                    .map(MinecraftServer::overworld)
                    .map(Level::getRandom)
                    .map(rng -> (Int2IntFunction) rng::nextInt)
                    .orElseGet(() -> new Random()::nextInt);
            return list.get(nextIntFp.get(list.size()));
        }
    }

    private GSAVillagerTrades() {
    }
}
