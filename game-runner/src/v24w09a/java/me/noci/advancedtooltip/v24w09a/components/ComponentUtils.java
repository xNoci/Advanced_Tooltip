package me.noci.advancedtooltip.v24w09a.components;

import com.google.common.collect.Lists;
import me.noci.advancedtooltip.v24w09a.components.accessor.AdventureModePredicateAccessor;
import me.noci.advancedtooltip.v24w09a.components.accessor.ArmorTrimAccessor;
import me.noci.advancedtooltip.v24w09a.components.accessor.ItemEnchantmentsAccessor;
import me.noci.advancedtooltip.v24w09a.components.printer.ComponentPrinter;
import net.labymod.api.util.I18n;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.saveddata.maps.MapId;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ComponentUtils {

    public static String prettyPrint(ItemStack itemStack) {
        List<ComponentPrinter> components = itemStack.getComponents().stream().map(ComponentUtils::formatComponent).toList();
        return ComponentPrinter.print(components);
    }

    private static ComponentPrinter formatComponent(TypedDataComponent<?> component) {
        if (component.value() instanceof CustomData customData) {
            return ComponentPrinter.component(component, ComponentPrinter.nbt(customData.copyTag()));
        }

        if (component.value() instanceof Integer value) {
            return ComponentPrinter.component(component, ComponentPrinter.value("int", value));
        }

        if (component.value() instanceof Unbreakable unbreakable) {
            return ComponentPrinter.component(component, ComponentPrinter.value("show_in_tooltip", unbreakable.showInTooltip()));
        }

        if (component.value() instanceof ChargedProjectiles chargedProjectiles) {
            ComponentPrinter projectileComponent = ComponentPrinter.list("projectiles", chargedProjectiles.getItems()).handler(projectile -> {
                String itemKey = Util.getRegisteredName(BuiltInRegistries.ITEM, projectile.getItem());
                int amount = projectile.getCount();
                return "'%s':%s".formatted(itemKey, amount);
            });
            return ComponentPrinter.component(component, projectileComponent);
        }

        if (component.value() instanceof ItemEnchantments itemEnchantments) {
            boolean showInToolTip = ((ItemEnchantmentsAccessor) itemEnchantments).isShownInTooltip();

            ComponentPrinter tooltipComponent = ComponentPrinter.value("show_in_tooltip", showInToolTip);

            if (itemEnchantments.isEmpty()) {
                return ComponentPrinter.component(component, tooltipComponent);
            }

            ComponentPrinter enchantments = ComponentPrinter.list("levels", ((ItemEnchantmentsAccessor) itemEnchantments).enchantments())
                    .handler(enchantment -> {
                        String enchantmentKey = Util.getRegisteredName(BuiltInRegistries.ENCHANTMENT, enchantment);
                        int level = itemEnchantments.getLevel(enchantment);
                        return "'%s':%s".formatted(enchantmentKey, level);
                    });
            return ComponentPrinter.component(component, ComponentPrinter.object(tooltipComponent, enchantments));
        }

        if (component.value() instanceof Component textComponent) {
            return ComponentPrinter.component(component, ComponentPrinter.value("text", textComponent.toString().replaceAll("\n", "<br>")));
        }

        if (component.value() instanceof ItemLore itemLore) {
            return ComponentPrinter.component(component, ComponentPrinter.expandableList("lines", itemLore.lines()).handler(
                    line -> line.toString().replaceAll("\n", "<br>")
            ));
        }

        if (component.value() instanceof AdventureModePredicateAccessor adventureModePredicate) {
            var tooltipComponent = ComponentPrinter.value("show_in_tooltip", adventureModePredicate.showInTooltip());
            var blockListComponent = adventureModePredicate.predicates().stream()
                    .filter(blockPredicates -> blockPredicates.blocks().isPresent())
                    .map(blockPredicates -> Lists.newArrayList(blockPredicates.blocks().get()))
                    .filter(blocks -> !blocks.isEmpty())
                    .map(blocks -> ComponentPrinter.list("blocks", blocks).handler(block -> Util.getRegisteredName(BuiltInRegistries.BLOCK, block.value()))).toList();
            var predicateListComponent = ComponentPrinter.expandableList("predicates", blockListComponent).handler(ComponentPrinter::print);
            return ComponentPrinter.component(component, ComponentPrinter.object(tooltipComponent, predicateListComponent));
        }

        if (component.value() instanceof DyedItemColor dyedItemColor) {
            var tooltipComponent = ComponentPrinter.value("show_in_tooltip", dyedItemColor.showInTooltip());
            var rgbComponent = ComponentPrinter.value("rgb", "0x%s".formatted(toHexInt(dyedItemColor.rgb())));
            return ComponentPrinter.component(component, ComponentPrinter.object(tooltipComponent, rgbComponent));
        }

        if (component.value() instanceof ItemAttributeModifiers modifiers) {
            //TODO
        }

        if (component.value() instanceof ChargedProjectiles chargedProjectiles) {
            return ComponentPrinter.component(component, ComponentPrinter.list("projectiles", chargedProjectiles.getItems()).handler(projectile -> {
                String itemKey = Util.getRegisteredName(BuiltInRegistries.ITEM, projectile.getItem());
                int amount = projectile.getCount();
                return "'%s':%s".formatted(itemKey, amount);
            }));
        }

        if (component.value() instanceof Unit) {
            return ComponentPrinter.component(component, ComponentPrinter.object());
        }

        if (component.value() instanceof BundleContents bundleContents) {
            var weightComponent = ComponentPrinter.value("weight", bundleContents.weight());

            if (bundleContents.isEmpty()) {
                return ComponentPrinter.component(component, weightComponent);
            }

            var itemsComponent = ComponentPrinter.expandableList("items", bundleContents.items().toList()).handler(itemStack -> {
                String itemKey = Util.getRegisteredName(BuiltInRegistries.ITEM, itemStack.getItem());
                int amount = itemStack.getCount();
                return "'%s':%s".formatted(itemKey, amount);
            });

            return ComponentPrinter.component(component, ComponentPrinter.object(weightComponent, itemsComponent));
        }

        if (component.value() instanceof MapItemColor mapItemColor) {
            return ComponentPrinter.component(component, ComponentPrinter.value("rgb", toHexInt(mapItemColor.rgb())));
        }

        if (component.value() instanceof MapDecorations decorations) {
            return ComponentPrinter.component(component, ComponentPrinter.expandableMap("decorations", decorations.decorations())
                    .handler(key -> key,
                            decoration -> {
                                var typeComponent = ComponentPrinter.value("type", decoration.type().getSerializedName());
                                var xComponent = ComponentPrinter.value("x", decoration.x());
                                var zComponent = ComponentPrinter.value("y", decoration.z());
                                var rotationComponent = ComponentPrinter.value("rotation", decoration.rotation());
                                return ComponentPrinter.object(typeComponent, xComponent, zComponent, rotationComponent);
                            }));
        }

        if (component.value() instanceof MapId mapId) {
            return ComponentPrinter.component(component, ComponentPrinter.value("map_id", mapId.id()));
        }

        if (component.value() instanceof CustomModelData customModelData) {
            return ComponentPrinter.component(component, ComponentPrinter.value("custom_model_data", customModelData.value()));
        }

        if (component.value() instanceof PotionContents potionContents) {
            Optional<Holder<Potion>> potion = potionContents.potion();
            Optional<Integer> customColor = potionContents.customColor();
            List<MobEffectInstance> customEffects = potionContents.customEffects();

            Optional<ComponentPrinter> customColorComponent = customColor.map(color -> ComponentPrinter.value("custom_color", toHexInt(color)));
            ComponentPrinter customEffectListComponent = ComponentPrinter.list("custom_effects", customEffects).handler(ComponentUtils::potionEffectToString);
            Optional<ComponentPrinter> potionComponent = potion.map(potionHolder -> {
                var nameComponent = ComponentPrinter.value("name", Potion.getName(potion, ""));
                var listComponent = ComponentPrinter.list("effects", potionHolder.value().getEffects()).handler(ComponentUtils::potionEffectToString);
                return ComponentPrinter.object("potion", nameComponent, listComponent);
            });

            List<ComponentPrinter> components = Lists.newArrayList();
            customColorComponent.ifPresent(components::add);
            potionComponent.ifPresent(components::add);
            components.add(customEffectListComponent);

            return ComponentPrinter.component(component, ComponentPrinter.object(components.toArray(ComponentPrinter[]::new)));
        }

        if (component.value() instanceof WritableBookContent writableBookContent) {
            var pages = writableBookContent.getPages(false).map(s -> s.replaceAll("\n", "<br>")).toList();
            return ComponentPrinter.component(component, ComponentPrinter.expandableList("pages", pages).handler("'%s'"::formatted));
        }

        if (component.value() instanceof WrittenBookContent writtenBookContent) {
            var pages = writtenBookContent.getPages(false).stream().map(page -> page.toString().replaceAll("\n", "<br>")).toList();

            var titleComponent = ComponentPrinter.value("title", writtenBookContent.title().get(false));
            var authorComponent = ComponentPrinter.value("title", writtenBookContent.author());
            var generationComponent = ComponentPrinter.value("title", writtenBookContent.generation());
            var resolvedComponent = ComponentPrinter.value("title", writtenBookContent.resolved());
            var pagesComponent = ComponentPrinter.expandableList("pages", pages).handler(Objects::toString);

            return ComponentPrinter.component(component, ComponentPrinter.object(titleComponent, authorComponent, generationComponent, resolvedComponent, pagesComponent));
        }

        if (component.value() instanceof ArmorTrim armorTrim) {
            var patternComponent = ComponentPrinter.value("pattern", armorTrim.pattern().getRegisteredName());
            var materialComponent = ComponentPrinter.value("material", armorTrim.material().getRegisteredName());
            var show_in_tooltip = ComponentPrinter.value("show_in_tooltip", ((ArmorTrimAccessor) armorTrim).isShownInTooltip());
            return ComponentPrinter.component(component, ComponentPrinter.object(patternComponent, materialComponent, show_in_tooltip));
        }

        if (component.value() instanceof SuspiciousStewEffects stewEffects) {
            var effects = ComponentPrinter.list("effects", stewEffects.effects())
                    .handler(effect -> "'%s':%s".formatted(effect.effect().getRegisteredName(), effect.duration()));
            return ComponentPrinter.component(component, effects);
        }

        if (component.value() instanceof DebugStickState debugStickState) {
            var entries = debugStickState.properties().entrySet().stream().map(propertyEntry -> {
                String blockKey = propertyEntry.getKey().getRegisteredName();
                String propertyName = propertyEntry.getValue().getName();
                String propertyValues = propertyEntry.getValue().getPossibleValues().toString();
                return ComponentPrinter.text("'%s'=%s %s".formatted(blockKey, propertyName, propertyValues));
            }).toArray(ComponentPrinter[]::new);

            return ComponentPrinter.component(component, ComponentPrinter.expandableObject(entries));
        }

        if (component.type() == DataComponents.INSTRUMENT) {
            @SuppressWarnings("unchecked") String instrumentName = ((Holder<Instrument>) component.value()).getRegisteredName();
            return ComponentPrinter.component(component, ComponentPrinter.value("instrument", instrumentName));
        }

        if (component.type() == DataComponents.RECIPES) {
            @SuppressWarnings("unchecked") List<ResourceLocation> recipes = (List<ResourceLocation>) component.value();
            return ComponentPrinter.component(component, ComponentPrinter.list("recipes", recipes).handler("'%s'"::formatted));
        }

        //TODO MINECRAFT:LODESTONE_TARGET and onwards
        //TODO https://www.minecraft.net/en-us/article/minecraft-snapshot-24w09a#:~:text=in%20standalone%20file)-,ITEM%20STACK%20COMPONENTS,-We%20are%20making

        return ComponentPrinter.component(component, ComponentPrinter.unsupported());
    }

    private static String potionEffectToString(MobEffectInstance effect) {
        String effectKey = effect.getEffect().getRegisteredName();
        String amplifier = Component.translatable("potion.potency." + effect.getAmplifier()).getString();

        if (effect.getAmplifier() > 0) {
            return I18n.translate("advancedtooltip.components.potion.effect.amplified", effectKey, effect.getDuration(), amplifier);
        }

        return I18n.translate("advancedtooltip.components.potion.effect.not_amplified", effectKey, effect.getDuration());
    }

    private static String toHexInt(int value) {
        return "0x" + Integer.toHexString(value);
    }

}
