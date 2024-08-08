package me.noci.advancedtooltip.v1_21_1.components.renderer;

import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.component.ChargedProjectiles;

public class ChargedProjectilesComponentRenderer implements ComponentRenderer<ChargedProjectiles> {
    @Override
    public ComponentPrinter parse(ChargedProjectiles value) {
        return ComponentPrinter.list("projectiles", value.getItems()).handler(projectile -> {
            String itemKey = Util.getRegisteredName(BuiltInRegistries.ITEM, projectile.getItem());
            int amount = projectile.getCount();
            return "'%s':%s".formatted(itemKey, amount);
        });
    }
}
