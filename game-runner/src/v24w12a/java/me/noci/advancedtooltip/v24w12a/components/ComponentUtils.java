package me.noci.advancedtooltip.v24w12a.components;

import com.google.common.collect.Lists;
import com.mojang.authlib.properties.Property;
import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.v24w12a.components.accessor.AdventureModePredicateAccessor;
import me.noci.advancedtooltip.v24w12a.components.accessor.ArmorTrimAccessor;
import me.noci.advancedtooltip.v24w12a.components.accessor.ItemContainerContentsAccessor;
import me.noci.advancedtooltip.v24w12a.components.accessor.ItemEnchantmentsAccessor;
import net.labymod.api.util.I18n;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.world.LockCode;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.PotDecorations;
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
            var showInTooltipComponent = ComponentPrinter.value("show_in_tooltip", modifiers.showInTooltip());
            var modifierObjects = modifiers.modifiers().stream().map(entry -> {
                var attributeKeyComponent = ComponentPrinter.value("attribute", entry.attribute().getRegisteredName());
                var equipmentSlotComponent = ComponentPrinter.value("equipment_slot", entry.slot().getSerializedName());

                var attributeModifierComponent = ComponentPrinter.object("attribute_modifier",
                        ComponentPrinter.value("amount", entry.modifier().amount()),
                        ComponentPrinter.value("operation", entry.modifier().operation().getSerializedName()),
                        ComponentPrinter.value("id", entry.modifier().id().toString()),
                        ComponentPrinter.value("name", entry.modifier().name())
                );

                return ComponentPrinter.object(attributeKeyComponent, equipmentSlotComponent, attributeModifierComponent);
            }).toList();

            boolean hasModifiers = !modifiers.modifiers().isEmpty();
            var modifierListComponent = ComponentPrinter.expandableList("modifiers", modifierObjects).handler(ComponentPrinter::print);

            return ComponentPrinter.component(component, ComponentPrinter.object(showInTooltipComponent, modifierListComponent).inline(!hasModifiers));
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
                                var decorationType = decoration.type().value();
                                var typeComponent = ComponentPrinter.expandableObject("type",
                                        ComponentPrinter.value("assetId", decorationType.assetId().toString()),
                                        ComponentPrinter.value("show_on_item_frame", decorationType.showOnItemFrame()),
                                        ComponentPrinter.value("map_color", toHexInt(decorationType.mapColor())),
                                        ComponentPrinter.value("exploration_map_element", decorationType.explorationMapElement()),
                                        ComponentPrinter.value("track_count", decorationType.trackCount())
                                );
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

            return ComponentPrinter.component(component, ComponentPrinter.object(components));
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
            }).toList();

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

        if (component.value() instanceof LodestoneTracker lodestoneTracker) {
            List<ComponentPrinter> components = Lists.newArrayList();
            lodestoneTracker.target().ifPresent(globalPos -> {
                var blockPos = globalPos.pos();
                var xComponent = ComponentPrinter.value("x", blockPos.getX());
                var yComponent = ComponentPrinter.value("y", blockPos.getY());
                var zComponent = ComponentPrinter.value("z", blockPos.getZ());

                components.add(ComponentPrinter.object("pos", xComponent, yComponent, zComponent));
                components.add(ComponentPrinter.value("dimension", globalPos.dimension().location().toString()));
            });

            components.add(ComponentPrinter.value("tracked", lodestoneTracker.tracked()));

            return ComponentPrinter.component(component, ComponentPrinter.object(components));
        }

        if (component.value() instanceof FireworkExplosion fireworkExplosion) {
            var shapeComponent = ComponentPrinter.value("shape", fireworkExplosion.shape().name());
            var colorsListComponent = ComponentPrinter.list("colors", fireworkExplosion.colors()).handler(ComponentUtils::toHexInt);
            var fadeColorsListComponent = ComponentPrinter.list("fade_colors", fireworkExplosion.fadeColors()).handler(ComponentUtils::toHexInt);
            var hasTrailComponent = ComponentPrinter.value("has_trail", fireworkExplosion.hasTrail());
            var hasTwinkleComponent = ComponentPrinter.value("has_twinkle", fireworkExplosion.hasTwinkle());

            return ComponentPrinter.component(component, ComponentPrinter.object(shapeComponent, colorsListComponent, fadeColorsListComponent, hasTrailComponent, hasTwinkleComponent));
        }

        if (component.value() instanceof Fireworks fireworks) {
            var flightDurationComponent = ComponentPrinter.value("flight_duration", fireworks.flightDuration());

            var explosionsObjectComponentList = fireworks.explosions().stream().map(explosion -> {
                        var shapeComponent = ComponentPrinter.value("shape", explosion.shape().name());
                        var colorsListComponent = ComponentPrinter.list("colors", explosion.colors()).handler(ComponentUtils::toHexInt);
                        var fadeColorsListComponent = ComponentPrinter.list("fade_colors", explosion.fadeColors()).handler(ComponentUtils::toHexInt);
                        var hasTrailComponent = ComponentPrinter.value("has_trail", explosion.hasTrail());
                        var hasTwinkleComponent = ComponentPrinter.value("has_twinkle", explosion.hasTwinkle());
                        return ComponentPrinter.object(shapeComponent, colorsListComponent, fadeColorsListComponent, hasTrailComponent, hasTwinkleComponent);
                    }
            ).toList();

            var explosionsListComponent = ComponentPrinter.expandableList("explosions", explosionsObjectComponentList).handler(ComponentPrinter::print);
            return ComponentPrinter.component(component, ComponentPrinter.object(flightDurationComponent, explosionsListComponent));
        }

        if (component.value() instanceof ResolvableProfile resolvableProfile) {
            var nameComponent = ComponentPrinter.value("name", resolvableProfile.name());
            var idComponent = resolvableProfile.id().map(uuid -> ComponentPrinter.value("uuid", uuid.toString()));
            var propertiesComponent = resolvableProfile.properties().entries().stream().map(entry -> {
                String key = entry.getKey();
                Property property = entry.getValue();
                List<ComponentPrinter> components = Lists.newArrayList();
                components.add(ComponentPrinter.value("name", property.name()));
                components.add(ComponentPrinter.value("value", property.value()));
                if (property.hasSignature()) {
                    components.add(ComponentPrinter.value("signature", property.signature()));
                }

                return ComponentPrinter.list(key, components).handler(ComponentPrinter::print);
            }).toList();

            List<ComponentPrinter> components = Lists.newArrayList();
            components.add(nameComponent);
            idComponent.ifPresent(components::add);
            components.add(ComponentPrinter.list("properties", propertiesComponent).handler(ComponentPrinter::print));

            return ComponentPrinter.component(component, ComponentPrinter.object(components));
        }

        if (component.value() instanceof ResourceLocation resourceLocation) {
            return ComponentPrinter.component(component, ComponentPrinter.value("resource_location", resourceLocation.toString()));
        }

        if (component.value() instanceof DyeColor dyeColor) {
            return ComponentPrinter.component(component, ComponentPrinter.value("dye_color", dyeColor.getName()));
        }

        if (component.value() instanceof BannerPatternLayers bannerPatternLayers) {
            var bannerLayers = bannerPatternLayers.layers().stream().map(layer -> {
                var patternComponent = ComponentPrinter.value("pattern", layer.pattern().getRegisteredName());
                var dyeColorComponent = ComponentPrinter.value("dye_color", layer.color().getName());
                return ComponentPrinter.object(patternComponent, dyeColorComponent);
            }).toList();

            var bannerLayersComponent = ComponentPrinter.expandableList("layers", bannerLayers).handler(ComponentPrinter::print);
            return ComponentPrinter.component(component, bannerLayersComponent);
        }

        if (component.value() instanceof PotDecorations decorations) {
            List<ComponentPrinter> components = Lists.newArrayList();

            decorations.back().ifPresent(item -> components.add(ComponentPrinter.value("back", Util.getRegisteredName(BuiltInRegistries.ITEM, item))));
            decorations.left().ifPresent(item -> components.add(ComponentPrinter.value("left", Util.getRegisteredName(BuiltInRegistries.ITEM, item))));
            decorations.right().ifPresent(item -> components.add(ComponentPrinter.value("right", Util.getRegisteredName(BuiltInRegistries.ITEM, item))));
            decorations.front().ifPresent(item -> components.add(ComponentPrinter.value("front", Util.getRegisteredName(BuiltInRegistries.ITEM, item))));

            return ComponentPrinter.component(component, ComponentPrinter.object(components));
        }

        if (component.value() instanceof ItemContainerContentsAccessor contents) {
            var slots = contents.slots().stream()
                    .map(slot -> {
                        String slotIndex = Integer.toString(slot.index());
                        String itemKey = Util.getRegisteredName(BuiltInRegistries.ITEM, slot.item().getItem());
                        return ComponentPrinter.value(slotIndex, "'%s':%s".formatted(itemKey, slot.item().getCount()));
                    }).toList();
            return ComponentPrinter.component(component, ComponentPrinter.expandableObject("slots", slots));
        }

        if (component.type() == DataComponents.BEES) {
            @SuppressWarnings("unchecked") List<BeehiveBlockEntity.Occupant> occupants = (List<BeehiveBlockEntity.Occupant>) component.value();

            var bees = occupants.stream().map(occupant -> {
                var ticksInHiveComponent = ComponentPrinter.value("ticks_in_hive", occupant.ticksInHive());
                var minTicksInHiveComponent = ComponentPrinter.value("min_ticks_in_hive", occupant.minTicksInHive());
                var customData = ComponentPrinter.nbt("entity_data", occupant.entityData().copyTag());

                return ComponentPrinter.object(ticksInHiveComponent, minTicksInHiveComponent, customData);
            }).toList();

            return ComponentPrinter.component(component, ComponentPrinter.list("bees", bees).handler(ComponentPrinter::print));
        }

        if (component.value() instanceof LockCode lockCode) {
            if (lockCode.key().isBlank()) {
                return ComponentPrinter.component(component, ComponentPrinter.text(I18n.translate("advancedtooltip.components.no_lock_set")));
            }
            return ComponentPrinter.component(component, ComponentPrinter.value("lock", lockCode.key()));
        }

        if (component.value() instanceof SeededContainerLoot seededContainerLoot) {
            var lootTableComponent = ComponentPrinter.value("loot_table", seededContainerLoot.lootTable().toString());
            var seedComponent = ComponentPrinter.value("seed", seededContainerLoot.seed());
            return ComponentPrinter.component(component, ComponentPrinter.object(lootTableComponent, seedComponent));
        }

        if (component.value() instanceof BlockItemStateProperties properties) {
            var propertiesMapComponent = ComponentPrinter.map("properites", properties.properties()).handler(key -> key, ComponentPrinter::text);
            return ComponentPrinter.component(component, propertiesMapComponent);
        }

        if (component.value() instanceof Boolean value) {
            return ComponentPrinter.component(component, ComponentPrinter.value("boolean", value));
        }

        if (component.value() instanceof MapPostProcessing mapPostProcessing) {
            return ComponentPrinter.component(component, ComponentPrinter.value("post_processing", mapPostProcessing.name().toLowerCase()));
        }

        if (component.value() instanceof FoodProperties foodProperties) {
            var effectComponents = foodProperties.effects().stream().map(possibleEffect -> ComponentPrinter.expandableObject(
                    ComponentPrinter.value("effect", potionEffectToString(possibleEffect.effect())),
                    ComponentPrinter.value("probability", possibleEffect.probability())
            )).toList();

            var foodComponent = ComponentPrinter.object(
                    ComponentPrinter.value("nutrition", foodProperties.nutrition()),
                    ComponentPrinter.value("saturation_modifier", foodProperties.saturationModifier()),
                    ComponentPrinter.value("can_always_eat", foodProperties.canAlwaysEat()),
                    ComponentPrinter.value("eat_seconds", foodProperties.eatSeconds()),
                    ComponentPrinter.expandableList("effects", effectComponents).handler(ComponentPrinter::print)
            );
            return ComponentPrinter.component(component, foodComponent);
        }

        if (component.value() instanceof Rarity rarity) {
            List<ComponentPrinter> components = Lists.newArrayList(ComponentPrinter.value("name", rarity.getSerializedName()));
            if (rarity.color().getColor() != null) {
                components.add(ComponentPrinter.value("color", toHexInt(rarity.color().getColor())));
            }

            return ComponentPrinter.component(component, ComponentPrinter.object("rarity", components).inline());
        }

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
