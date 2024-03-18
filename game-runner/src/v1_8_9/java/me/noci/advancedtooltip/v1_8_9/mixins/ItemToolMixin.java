package me.noci.advancedtooltip.v1_8_9.mixins;


import me.noci.advancedtooltip.v1_8_9.items.accessors.ItemToolAccessor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTool;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemTool.class)
public class ItemToolMixin implements ItemToolAccessor {


    @Shadow Item.ToolMaterial toolMaterial;

    @Override
    public Item.ToolMaterial toolMaterial() {
        return toolMaterial;
    }
}
