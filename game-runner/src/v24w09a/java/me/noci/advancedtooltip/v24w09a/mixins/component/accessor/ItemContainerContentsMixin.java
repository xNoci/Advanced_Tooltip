package me.noci.advancedtooltip.v24w09a.mixins.component.accessor;

import me.noci.advancedtooltip.v24w09a.components.accessor.ItemContainerContentsAccessor;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import org.apache.commons.compress.utils.Lists;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ItemContainerContents.class)
public class ItemContainerContentsMixin implements ItemContainerContentsAccessor {

    @Shadow @Final private NonNullList<ItemStack> items;


    @Override
    public List<Slot> slots() {
        List<Slot> slots = Lists.newArrayList();

        for (int i = 0; i < this.items.size(); i++) {
            ItemStack itemStack = this.items.get(i);
            if (!itemStack.isEmpty()) {
                slots.add(new Slot(i, itemStack));
            }
        }

        return slots;
    }
}
