package me.noci.advancedtooltip.v24w12a.mixins.component.accessor;

import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import me.noci.advancedtooltip.v24w12a.components.accessor.ItemEnchantmentsAccessor;
import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ItemEnchantments.class)
public class ItemEnchantmentsMixin implements ItemEnchantmentsAccessor {

    @Final
    @Shadow boolean showInTooltip;

    @Final @Shadow Object2IntLinkedOpenHashMap<Holder<Enchantment>> enchantments;

    @Override
    public boolean isShownInTooltip() {
        return showInTooltip;
    }

    @Override
    public List<Enchantment> enchantments() {
        return enchantments.keySet().stream().map(Holder::value).toList();
    }
}
