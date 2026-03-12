package cn.chloeprime.gunsmithlib_std_ammo.common.item;

import cn.chloeprime.gunsmithlib_std_ammo.client.ItemTooltipLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.loading.FMLLoader;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class SimpleItem extends Item {
    private static final boolean IS_DEDICATED_SERVER = FMLLoader.getDist().isDedicatedServer();

    public SimpleItem(Properties properties) {
        super(properties);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flags) {
        super.appendHoverText(stack, level, tooltip, flags);
        if (!IS_DEDICATED_SERVER) {
            if (level == null || level.isClientSide()) {
                ItemTooltipLoader.load(stack, tooltip);
            }
        }
    }
}
