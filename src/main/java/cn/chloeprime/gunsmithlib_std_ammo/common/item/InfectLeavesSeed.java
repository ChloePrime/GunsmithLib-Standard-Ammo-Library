package cn.chloeprime.gunsmithlib_std_ammo.common.item;

import cn.chloeprime.gunsmithlib_std_ammo.common.block.InfectiousLeavesBlock;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class InfectLeavesSeed extends Item {
    private final Supplier<? extends InfectiousLeavesBlock> convertTo;

    public InfectLeavesSeed(
            Supplier<? extends InfectiousLeavesBlock> convertTo,
            Properties pProperties
    ) {
        super(pProperties);
        this.convertTo = convertTo;
    }

    @Override
    public @Nonnull InteractionResult useOn(UseOnContext context) {
        LivingEntity user = context.getPlayer();
        if (user == null) {
            return InteractionResult.PASS;
        }

        var stack = user.getItemInHand(context.getHand());
        if (convertTo.get().convert(context.getLevel(), context.getClickedPos(), 0)) {
            if (!context.getLevel().isClientSide) {
                stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
