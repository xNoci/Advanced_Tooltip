package me.noci.advancedtooltip.v1_21_4.components.renderer;

import com.google.common.collect.Lists;
import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ConsumableComponentRenderer implements ComponentRenderer<Consumable> {
    @Override
    public ComponentPrinter parse(Consumable value) {

        List<ComponentPrinter> values = Lists.newArrayList(
                ComponentPrinter.value("consume_seconds", value.consumeSeconds()),
                ComponentPrinter.value("use_animation", value.animation().name()),
                ComponentPrinter.value("sound", value.sound().getRegisteredName()),
                ComponentPrinter.value("has_consume_particle", value.hasConsumeParticles())
        );

        List<ConsumeEffect> consumeEffects = value.onConsumeEffects();

        if (!consumeEffects.isEmpty()) {

            List<ComponentPrinter> effects = getConsumeEffectPrinters(consumeEffects);
            values.add(ComponentPrinter.list("consume_effects", effects).handler(ComponentPrinter::print));
        }

        return ComponentPrinter.object(values);
    }

    public static @NotNull List<ComponentPrinter> getConsumeEffectPrinters(List<ConsumeEffect> consumeEffects) {
        List<ComponentPrinter> effects = Lists.newArrayList();

        for (ConsumeEffect consumeEffect : consumeEffects) {
            var printer = switch (consumeEffect) {
                case ApplyStatusEffectsConsumeEffect statusEffects -> ComponentPrinter.text("apply");
                case ClearAllStatusEffectsConsumeEffect statusEffects -> ComponentPrinter.text("clear");
                case PlaySoundConsumeEffect soundEffect -> ComponentPrinter.text("playsound");
                case RemoveStatusEffectsConsumeEffect statusEffect -> ComponentPrinter.text("remove");
                case TeleportRandomlyConsumeEffect teleportEffect -> ComponentPrinter.text("teleport");
                case null -> ComponentPrinter.unsupported();
                default -> ComponentPrinter.text("Unsupported: " + consumeEffect.getClass().getSimpleName());
            };
            effects.add(printer);
        }
        return effects;
    }
}
