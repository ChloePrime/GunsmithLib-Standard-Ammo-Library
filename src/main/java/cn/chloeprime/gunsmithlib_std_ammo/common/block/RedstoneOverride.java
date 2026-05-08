package cn.chloeprime.gunsmithlib_std_ammo.common.block;

import cn.chloeprime.commons.async.TaskScheduler;
import cn.chloeprime.gunsmithlib_std_ammo.GunsmithLibStdAmmoMod;
import cn.chloeprime.gunsmithlib_std_ammo.common.gunpack.RedstoneData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.*;

@Mod.EventBusSubscriber
public final class RedstoneOverride {
    public record Key(ResourceKey<Level> dimension, BlockPos hitPos) {
    }

    public record Value(int strength, Direction hitDirection) {
    }

    public static OptionalInt getOverrideSignalFor(Level level, BlockPos pos, Direction direction) {
        var selfKey = new Key(level.dimension(), pos);
        var self = Optional.ofNullable(OVERRIDE_MAP.get(selfKey))
                .map(Value::strength)
                .map(OptionalInt::of)
                .orElse(OptionalInt.empty());
        if (self.isPresent()) {
            return self;
        }
        var neighborPos = pos.relative(direction, -1);
        if (!level.isLoaded(neighborPos)) {
            return OptionalInt.empty();
        }
        var neighborKey = new Key(level.dimension(), neighborPos);
        return Optional.ofNullable(OVERRIDE_MAP.get(neighborKey))
                .filter(value -> Objects.equals(direction, value.hitDirection()))
                .map(Value::strength)
                .map(OptionalInt::of)
                .orElse(OptionalInt.empty());
    }

    public static void override(Level level, BlockPos pos, Direction hitDirection, RedstoneData data) {
        if (level.isClientSide()) {
            throw new IllegalArgumentException("Client");
        }
        var dim = level.dimension();
        var key = new Key(dim, pos);
        var val = new Value(data.powerLevel(), hitDirection);
        OVERRIDE_MAP.put(key, val);
        var server = level.getServer();
        SCHEDULER.delay(data.durationTicks(), task -> clearOverride(pos, hitDirection, key, server, dim));
    }

    private static void clearOverride(
            BlockPos pos, Direction hitDirection, Key key,
            @Nullable MinecraftServer server, ResourceKey<Level> dim
    ) {
        OVERRIDE_MAP.remove(key);
        if (server == null) {
            return;
        }
        Optional.ofNullable(server.getLevel(dim))
                .filter(lvl -> lvl.isLoaded(pos))
                .ifPresent(lvl -> {
                    var hitFromPos = pos.relative(hitDirection);
                    lvl.neighborChanged(pos, lvl.getBlockState(hitFromPos).getBlock(), hitFromPos);
                    lvl.updateNeighborsAt(pos, lvl.getBlockState(pos).getBlock());
                });
    }

    private static final Map<Key, Value> OVERRIDE_MAP = new WeakHashMap<>();
    private static final TaskScheduler SCHEDULER = TaskScheduler.createTickBased(LogicalSide.SERVER);

    @SubscribeEvent
    public static void onChunkUnloading(ChunkEvent.Unload event) {
        if (event.getLevel().isClientSide() || !(event.getLevel() instanceof ServerLevel level)) {
            return;
        }
        for (var iterator = OVERRIDE_MAP.entrySet().iterator(); iterator.hasNext(); ) {
            var kvp = iterator.next();
            var key = kvp.getKey();
            var val = kvp.getValue();
            var chunkPos = event.getChunk().getPos();
            var blockPos = key.hitPos();
            var insideX = blockPos.getX() >= chunkPos.getMinBlockX() && blockPos.getX() <= chunkPos.getMaxBlockX();
            var insideZ = blockPos.getZ() >= chunkPos.getMinBlockZ() && blockPos.getZ() <= chunkPos.getMaxBlockZ();
            if (insideX && insideZ) {
                try {
                    iterator.remove();
                    var hitFromPos = blockPos.relative(val.hitDirection());
                    level.neighborChanged(blockPos, level.getBlockState(hitFromPos).getBlock(), hitFromPos);
                    level.updateNeighborsAt(blockPos, event.getChunk().getBlockState(blockPos).getBlock());
                } catch (Exception ex) {
                    GunsmithLibStdAmmoMod.LOGGER.error(
                            "Failed to extinguish redstone override at ({}, {}, {})",
                            blockPos.getX(), blockPos.getY(), blockPos.getZ());
                }
            }
        }
    }

    private RedstoneOverride() {

    }
}
