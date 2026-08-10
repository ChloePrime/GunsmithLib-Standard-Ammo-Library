package cn.chloeprime.gunsmithlib_std_ammo.common;

import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.ApiStatus;

public final class GSACommonConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    static {
        BUILDER.push("bullet_merchant");
    }

    public static final ForgeConfigSpec.DoubleValue BM_GLOBAL_PRICE_SCALE = BUILDER
            .comment("""
                    Global price scale for the bullet merchant.
                    This only affects the final cost, thus does not affect max trade count.
                    
                    Added in version 1.1.0""")
            .defineInRange("bm_global_price_scale", 1.0, 0, Integer.MAX_VALUE);

    public static final ForgeConfigSpec.LongValue BM_SPAWN_PERIOD = BUILDER
            .comment("""
                    Period of triage to spawn bullet merchant.
                    Change to 0 to disable bullet merchant from spawning.
                    
                    Note: Tick period of bullet merchant spawner is hardcoded at 1200 (one minute).
                    So value below 1200 (and > 0) will not take effect.
                    
                    Added in version 1.1.0""")
            .defineInRange("bm_spawn_period", 24000L, 0, Long.MAX_VALUE);

    public static final ForgeConfigSpec.LongValue BM_PERSIST_TIME = BUILDER
            .comment("""
                    Delay before bullet merchant start natural despawning.
                    
                    Added in version 1.1.2""")
            .defineInRange("bm_persist_time", 12000L, 0, Long.MAX_VALUE);

    static {
        BUILDER.pop();
    }

    @ApiStatus.Internal
    public static final ForgeConfigSpec SPEC = BUILDER.build();

    private GSACommonConfig() {
    }
}
