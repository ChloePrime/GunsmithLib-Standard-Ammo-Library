package cn.chloeprime.gunsmithlib_std_ammo.common.entity;

import cn.chloeprime.gunsmithlib_std_ammo.GunsmithLibStdAmmoMod;
import cn.chloeprime.gunsmithlib_std_ammo.common.GSACommonConfig;
import cn.chloeprime.gunsmithlib_std_ammo.common.level.GSABiomeTags;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.*;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Optional;

@Mod.EventBusSubscriber
public class BulletMerchantSpawner extends SavedData {
    public static final long TICK_PERIOD = 1200;

    public static final Codec<BulletMerchantSpawner> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.LONG.optionalFieldOf("spawn_delay", 36000L).forGetter(spw -> spw.spawnDelay),
            Codec.INT.optionalFieldOf("dice_failures", 0).forGetter(spw -> spw.diceFailureCount),
            Codec.BOOL.optionalFieldOf("scheduled", false).forGetter(spw -> spw.scheduled)
    ).apply(instance, BulletMerchantSpawner::new));

    private static final String SAVED_DATA_FILE = "%s$bullet_merchant_spawner".formatted(GunsmithLibStdAmmoMod.MOD_ID);
    private long spawnDelay;
    private int diceFailureCount;
    private boolean scheduled;
    private transient long tickDelay = TICK_PERIOD;

    public BulletMerchantSpawner(long spawnDelay, int diceFailureCount, boolean scheduled) {
        this.spawnDelay = spawnDelay;
        this.diceFailureCount = diceFailureCount;
        this.scheduled = scheduled;
    }

    public static BulletMerchantSpawner of(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(BulletMerchantSpawner::load, BulletMerchantSpawner::create, SAVED_DATA_FILE);
    }

    public static BulletMerchantSpawner load(CompoundTag compound) {
        return CODEC.decode(NbtOps.INSTANCE, compound)
                .resultOrPartial(msg -> GunsmithLibStdAmmoMod.LOGGER.error("Error decoding bullet merchant spawn data: {}", msg))
                .map(Pair::getFirst)
                .orElseGet(BulletMerchantSpawner::create);
    }

    public static BulletMerchantSpawner create() {
        return new BulletMerchantSpawner(Math.round(getSpawnPeriod() * 1.5), 0, false);
    }

    public static boolean isEnabled() {
        return getSpawnPeriod() > 0;
    }

    public static long getSpawnPeriod() {
        return GSACommonConfig.BM_SPAWN_PERIOD.get();
    }

    @Override
    public @Nonnull CompoundTag save(@Nonnull CompoundTag compound) {
        return CODEC.encodeStart(NbtOps.INSTANCE, this)
                .resultOrPartial(msg -> GunsmithLibStdAmmoMod.LOGGER.error("Error encoding bullet merchant spawn data: {}", msg))
                .map(CompoundTag.class::cast)
                .orElse(compound);
    }

    public void tick(ServerLevel level) {
        if (--tickDelay > 0) {
            return;
        }
        tickDelay += TICK_PERIOD;
        if (!isEnabled()) {
            return;
        }
        if (!level.getGameRules().getBoolean(GameRules.RULE_DO_TRADER_SPAWNING)) {
            return;
        }
        spawnDelay -= TICK_PERIOD;
        setDirty();
        if (spawnDelay > 0) {
            return;
        }
        boolean success = trySpawn(level);
        spawnDelay += success ? Math.max(TICK_PERIOD, getSpawnPeriod()) : TICK_PERIOD;
    }

    public float getSpawnChance() {
        return Math.min(diceFailureCount + 1, 3) * 0.25F;
    }

    /**
     * @return 如果被外部因素拦截则返回 false，成功生成或概率检定失败则返回 true
     */
    public boolean trySpawn(ServerLevel level) {
        if (!level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
            return false;
        }
        if (!scheduled) {
            if (level.getRandom().nextFloat() >= getSpawnChance()) {
                diceFailureCount++;
                return true;
            }
            diceFailureCount = 0;
            scheduled = true;
        }
        var success = spawn(level);
        if (success) {
            scheduled = false;
        }
        return success;
    }

    private boolean spawn(ServerLevel overworld) {
        var players = overworld.getServer().getPlayerList().getPlayers().stream()
                .filter(LivingEntity::isAlive)
                .filter(pl -> !pl.isSpectator())
                .toList();
        var player = players.get(overworld.getRandom().nextInt(players.size()));
        if (player == null) {
            return false;
        }
        var level = player.serverLevel();
        var spawnPos = findSpawnPositionNear(level, player.blockPosition(), 48).orElse(null);
        if (spawnPos == null || !hasEnoughSpace(level, spawnPos)) {
            return false;
        }
        if (level.getBiome(spawnPos).is(GSABiomeTags.WITHOUT_BULLET_MERCHANT_SPAWNS)) {
            return false;
        }
        var entity = GSAEntities.BULLET_MERCHANT.get().spawn(level, spawnPos, MobSpawnType.EVENT);
        if (entity == null) {
            return false;
        }
        return entity.isAlive() && entity.isAddedToWorld();
    }

    public static Optional<BlockPos> findSpawnPositionNear(Level level, BlockPos center, int maxDistance) {
        var random = level.getRandom();
        var buffer = new BlockPos.MutableBlockPos();
        var isOverworld = Objects.equals(level.dimensionTypeId(), BuiltinDimensionTypes.OVERWORLD);
        final int maxAttempts = isOverworld ? 10 : 25;

        for (int i = 0; i < maxAttempts; ++i) {
            int x = center.getX() + random.nextInt(maxDistance * 2) - maxDistance;
            int z = center.getZ() + random.nextInt(maxDistance * 2) - maxDistance;
            int y = isOverworld
                    ? level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z)
                    : center.getY() + random.nextInt(3) - 1;
            buffer.set(x, y, z);
            if (NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, level, buffer, EntityType.WANDERING_TRADER)) {
                return Optional.of(buffer.immutable());
            }
        }

        return Optional.empty();
    }

    public static boolean hasEnoughSpace(BlockGetter level, BlockPos origin) {
        for (var candidate : BlockPos.betweenClosed(origin, origin.offset(1, 2, 1))) {
            if (!level.getBlockState(candidate).getCollisionShape(level, candidate).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            return;
        }
        onOverworldTick(Objects.requireNonNull(event.getServer().overworld()));
    }

    private static void onOverworldTick(@Nonnull ServerLevel level) {
        of(level).tick(level);
    }
}
