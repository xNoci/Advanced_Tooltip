package me.noci.advancedtooltip.v24w10a.mixins;

import me.noci.advancedtooltip.core.TooltipAddon;
import me.noci.advancedtooltip.core.icons.IconQuery;
import me.noci.advancedtooltip.v24w10a.utils.ItemCast;
import me.noci.advancedtooltip.v24w10a.utils.VersionedClientIconComponent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenTooltipRenderer extends Screen {

    protected AbstractContainerScreenTooltipRenderer(Component $$0) {
        super($$0);
    }

    @Shadow
    protected abstract List<Component> getTooltipFromContainerItem(ItemStack $$0);

    @Inject(
            method = "renderTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;Ljava/util/Optional;II)V"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true
    )
    private void injectIcons(GuiGraphics graphics, int x, int y, CallbackInfo ci, ItemStack itemStack) {
        if (!TooltipAddon.enabled()) {
            return;
        }

        List<Component> tooltip = getTooltipFromContainerItem(itemStack);
        net.labymod.api.client.world.item.ItemStack labyItemStack = ItemCast.toLabyItemStack(itemStack);
        List<VersionedClientIconComponent> icons = IconQuery.iconComponents(labyItemStack, VersionedClientIconComponent::new);

        tooltip.addAll(icons);

        graphics.renderTooltip(this.font, tooltip, itemStack.getTooltipImage(), x, y);

        ci.cancel();
    }


}
