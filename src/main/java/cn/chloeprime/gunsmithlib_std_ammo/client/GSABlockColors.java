package cn.chloeprime.gunsmithlib_std_ammo.client;

import cn.chloeprime.gunsmithlib_std_ammo.common.block.GSABlocks;
import cn.chloeprime.gunsmithlib_std_ammo.common.block.TiberiumLeavesBlock;
import cn.chloeprime.gunsmithlib_std_ammo.common.util.Colors;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Vector4f;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GSABlockColors {
    private static final int TIB_YELLOW_COLOR = Colors.vecToPacked(TiberiumLeavesBlock.STABLE);

    @SubscribeEvent
    public static void onBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register(((state, level, pos, index) -> {
            if (level == null || pos == null) {
                return TIB_YELLOW_COLOR;
            }
            Vector4f baseColor = Colors.vecFromIntOpaque(BiomeColors.getAverageFoliageColor(level, pos), BASE_COLOR_BUF);
            Vector4f finalColor = TiberiumLeavesBlock.colorFunc(state, baseColor);
            return Colors.vecToPacked(finalColor);
        }), GSABlocks.TIBERIUM_LEAVES.get());
    }

    @SubscribeEvent
    public static void onItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, index) -> TIB_YELLOW_COLOR, GSABlocks.TIBERIUM_LEAVES.get());
    }

    private static final Vector4f BASE_COLOR_BUF = new Vector4f();
}
