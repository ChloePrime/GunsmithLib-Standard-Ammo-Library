package cn.chloeprime.gunsmithlib_std_ammo.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Objects;

public class ItemTooltipLoader {
    public static void load(ItemStack stack, List<Component> tooltip) {
        // 有 Lore 时不显示自带 tooltip
        if (stack.hasTag()) {
            var tag = Objects.requireNonNull(stack.getTag());
            if (tag.contains(ItemStack.TAG_DISPLAY, Tag.TAG_COMPOUND)) {
                if (tag.getCompound(ItemStack.TAG_DISPLAY).contains(ItemStack.TAG_LORE, Tag.TAG_LIST)) {
                    return;
                }
            }
        }

        var sb = new StringBuilder(stack.getDescriptionId());
        sb.append(".desc.");
        for (int i = 0; i < 65536; i++) {
            sb.append(i);
            var lineKey = sb.toString();
            if (I18n.exists(lineKey) && !I18n.get(lineKey).isEmpty()) {
                tooltip.add(Component.translatable(lineKey).withStyle(ChatFormatting.GRAY));
            } else {
                break;
            }
            sb.setLength(sb.length() - 1);
        }
    }
}
