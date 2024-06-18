package me.noci.advancedtooltip.v1_20_6.components;

import com.google.common.collect.Lists;
import com.mojang.authlib.properties.Property;
import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.v1_20_6.components.accessor.AdventureModePredicateAccessor;
import me.noci.advancedtooltip.v1_20_6.components.accessor.ArmorTrimAccessor;
import me.noci.advancedtooltip.v1_20_6.components.accessor.ItemContainerContentsAccessor;
import me.noci.advancedtooltip.v1_20_6.components.accessor.ItemEnchantmentsAccessor;
import net.labymod.api.util.I18n;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ComponentUtils {

    public static String prettyPrint(ItemStack itemStack) {
        List<ComponentPrinter> components = itemStack.getComponents().stream().map(ComponentUtils::formatComponent).toList();
        return ComponentPrinter.print(components);
    }

    private static ComponentPrinter formatComponent(TypedDataComponent<?> component) {
        ComponentFormatter formatter = new ComponentFormatter();

        formatter.forComponents(component, DataComponents.CUSTOM_DATA, DataComponents.ENTITY_DATA, DataComponents.BUCKET_ENTITY_DATA, DataComponents.BLOCK_ENTITY_DATA)
                .handle(data -> ComponentPrinter.nbt(data.copyTag()));

        formatter.forComponents(component, DataComponents.MAX_STACK_SIZE, DataComponents.MAX_DAMAGE, DataComponents.DAMAGE, DataComponents.REPAIR_COST, DataComponents.OMINOUS_BOTTLE_AMPLIFIER)
                .handle(integer -> ComponentPrinter.value("int", integer));

        formatter.forComponents(component, DataComponents.UNBREAKABLE)
                .handle(unbreakable -> ComponentPrinter.value("show_in_tooltip", unbreakable.showInTooltip()));

        formatter.forComponents(component, DataComponents.CHARGED_PROJECTILES)
                .handle(chargedProjectiles ->
                        ComponentPrinter.list("projectiles", chargedProjectiles.getItems()).handler(projectile -> {
                            String itemKey = Util.getRegisteredName(BuiltInRegistries.ITEM, projectile.getItem());
                            int amount = projectile.getCount();
                            return "'%s':%s".formatted(itemKey, amount);
                        })
                );

        formatter.forComponents(component, DataComponents.ENCHANTMENTS, DataComponents.STORED_ENCHANTMENTS)
                .handle(itemEnchantments -> {
                    boolean showInToolTip = ((ItemEnchantmentsAccessor) itemEnchantments).isShownInTooltip();
                    ComponentPrinter tooltipComponent = ComponentPrinter.value("show_in_tooltip", showInToolTip);

                    if (itemEnchantments.isEmpty()) {
                        return tooltipComponent;
                    }

                    ComponentPrinter enchantments = ComponentPrinter.list("enchantments", ((ItemEnchantmentsAccessor) itemEnchantments).enchantments())
                            .handler(enchantment -> {
                                String enchantmentKey = Util.getRegisteredName(BuiltInRegistries.ENCHANTMENT, enchantment);
                                int level = itemEnchantments.getLevel(enchantment);
                                return "'%s':%s".formatted(enchantmentKey, level);
                            });

                    return ComponentPrinter.object(tooltipComponent, enchantments);
                });

        formatter.forComponents(component, DataComponents.CUSTOM_NAME, DataComponents.ITEM_NAME)
                .handle(textComponent -> ComponentPrinter.value("text", textComponent.toString().replaceAll("\n", "<br>")));

        formatter.forComponents(component, DataComponents.LORE)
                .handle(itemLore ->
                        ComponentPrinter.expandableList("lines", itemLore.lines())
                                .handler(line -> line.toString().replaceAll("\n", "<br>"))
                );

        formatter.forComponents(component, DataComponents.CAN_PLACE_ON, DataComponents.CAN_BREAK)
                .handleAs(AdventureModePredicateAccessor.class, adventureModePredicate -> {
                    var tooltipComponent = ComponentPrinter.value("show_in_tooltip", adventureModePredicate.showInTooltip());
                    var blockListComponent = adventureModePredicate.predicates().stream()
                            .filter(blockPredicates -> blockPredicates.blocks().isPresent())
                            .map(blockPredicates -> Lists.newArrayList(blockPredicates.blocks().get()))
                            .filter(blocks -> !blocks.isEmpty())
                            .map(blocks -> ComponentPrinter.list("blocks", blocks).handler(block -> Util.getRegisteredName(BuiltInRegistries.BLOCK, block.value()))).toList();
                    var predicateListComponent = ComponentPrinter.expandableList("predicates", blockListComponent).handler(ComponentPrinter::print);
                    return ComponentPrinter.object(tooltipComponent, predicateListComponent);
                });

        formatter.forComponents(component, DataComponents.DYED_COLOR)
                .handle(dyedItemColor -> {
                    var tooltipComponent = ComponentPrinter.value("show_in_tooltip", dyedItemColor.showInTooltip());
                    var rgbComponent = ComponentPrinter.value("rgb", "0x%s".formatted(toHexInt(dyedItemColor.rgb())));
                    return ComponentPrinter.object(tooltipComponent, rgbComponent);
                });

        formatter.forComponents(component, DataComponents.ATTRIBUTE_MODIFIERS)
                .handle(modifiers -> {
                    var showInTooltipComponent = ComponentPrinter.value("show_in_tooltip", modifiers.showInTooltip());
                    var modifierObjects = modifiers.modifiers().stream().map(entry -> {
                        var attributeKeyComponent = ComponentPrinter.value("attribute", entry.attribute().getRegisteredName());
                        var equipmentSlotComponent = ComponentPrinter.value("equipment_slot", entry.slot().getSerializedName());

                        var attributeModifierComponent = ComponentPrinter.object("attribute_modifier",
                                ComponentPrinter.value("amount", entry.modifier().amount()),
                                ComponentPrinter.value("operation", entry.modifier().operation().getSerializedName()),
                                ComponentPrinter.value("id", entry.modifier().id().toString()),
                                ComponentPrinter.value("fullName", entry.modifier().name())
                        );

                        return ComponentPrinter.object(attributeKeyComponent, equipmentSlotComponent, attributeModifierComponent);
                    }).toList();

                    boolean hasModifiers = !modifiers.modifiers().isEmpty();
                    var modifierListComponent = ComponentPrinter.expandableList("modifiers", modifierObjects).handler(ComponentPrinter::print);

                    return ComponentPrinter.object(showInTooltipComponent, modifierListComponent).inline(!hasModifiers);
                });

        formatter.forComponents(component, DataComponents.CHARGED_PROJECTILES)
                .handle(chargedProjectiles ->
                        ComponentPrinter.list("projectiles", chargedProjectiles.getItems()).handler(projectile -> {
                            String itemKey = Util.getRegisteredName(BuiltInRegistries.ITEM, projectile.getItem());
                            int amount = projectile.getCount();
                            return "'%s':%s".formatted(itemKey, amount);
                        })
                );

        formatter.forComponents(component, DataComponents.HIDE_ADDITIONAL_TOOLTIP, DataComponents.HIDE_TOOLTIP, DataComponents.CREATIVE_SLOT_LOCK, DataComponents.INTANGIBLE_PROJECTILE, DataComponents.FIRE_RESISTANT)
                .handleRaw((unit, typedDataComponent) -> ComponentPrinter.unit(component));

        formatter.forComponents(component, DataComponents.BUNDLE_CONTENTS)
                .handle(bundleContents -> {
                    var weightComponent = ComponentPrinter.value("weight", bundleContents.weight());

                    if (bundleContents.isEmpty()) {
                        return weightComponent;
                    }

                    var itemsComponent = ComponentPrinter.expandableList("items", bundleContents.items()).handler(itemStack -> {
                        String itemKey = Util.getRegisteredName(BuiltInRegistries.ITEM, itemStack.getItem());
                        int amount = itemStack.getCount();
                        return "'%s':%s".formatted(itemKey, amount);
                    });

                    return ComponentPrinter.object(weightComponent, itemsComponent);
                });


        formatter.forComponents(component, DataComponents.MAP_COLOR)
                .handle(mapItemColor -> ComponentPrinter.value("rgb", toHexInt(mapItemColor.rgb())));

        formatter.forComponents(component, DataComponents.MAP_DECORATIONS)
                .handle(decorations ->
                        ComponentPrinter.expandableMap("decorations", decorations.decorations())
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
                                        })
                );

        formatter.forComponents(component, DataComponents.MAP_ID)
                .handle(mapId -> ComponentPrinter.value("map_id", mapId.id()));

        formatter.forComponents(component, DataComponents.CUSTOM_MODEL_DATA)
                .handle(customModelData -> ComponentPrinter.value("custom_model_data", customModelData.value()));

        formatter.forComponents(component, DataComponents.POTION_CONTENTS)
                .handle(potionContents -> {
                    Optional<Holder<Potion>> potion = potionContents.potion();
                    Optional<Integer> customColor = potionContents.customColor();
                    List<MobEffectInstance> customEffects = potionContents.customEffects();

                    Optional<ComponentPrinter> customColorComponent = customColor.map(color -> ComponentPrinter.value("custom_color", toHexInt(color)));
                    ComponentPrinter customEffectListComponent = ComponentPrinter.list("custom_effects", customEffects).handler(ComponentUtils::potionEffectToString);
                    Optional<ComponentPrinter> potionComponent = potion.map(potionHolder -> {
                        var nameComponent = ComponentPrinter.value("fullName", Potion.getName(potion, ""));
                        var listComponent = ComponentPrinter.list("effects", potionHolder.value().getEffects()).handler(ComponentUtils::potionEffectToString);
                        return ComponentPrinter.object("potion", nameComponent, listComponent);
                    });

                    List<ComponentPrinter> components = Lists.newArrayList();
                    customColorComponent.ifPresent(components::add);
                    potionComponent.ifPresent(components::add);
                    components.add(customEffectListComponent);

                    return ComponentPrinter.object(components);
                });

        formatter.forComponents(component, DataComponents.WRITABLE_BOOK_CONTENT)
                .handle(writableBookContent -> {
                    var pages = writableBookContent.getPages(false).map(s -> s.replaceAll("\n", "<br>")).toList();
                    return ComponentPrinter.expandableList("pages", pages).handler("'%s'"::formatted);
                });

        formatter.forComponents(component, DataComponents.WRITTEN_BOOK_CONTENT)
                .handle(writtenBookContent -> {
                    var pages = writtenBookContent.getPages(false).stream().map(page -> page.toString().replaceAll("\n", "<br>")).toList();

                    var titleComponent = ComponentPrinter.value("title", writtenBookContent.title().get(false));
                    var authorComponent = ComponentPrinter.value("title", writtenBookContent.author());
                    var generationComponent = ComponentPrinter.value("title", writtenBookContent.generation());
                    var resolvedComponent = ComponentPrinter.value("title", writtenBookContent.resolved());
                    var pagesComponent = ComponentPrinter.expandableList("pages", pages).handler(Objects::toString);

                    return ComponentPrinter.object(titleComponent, authorComponent, generationComponent, resolvedComponent, pagesComponent);
                });

        formatter.forComponents(component, DataComponents.TRIM)
                .handle(armorTrim -> {
                    var patternComponent = ComponentPrinter.value("pattern", armorTrim.pattern().getRegisteredName());
                    var materialComponent = ComponentPrinter.value("material", armorTrim.material().getRegisteredName());
                    var show_in_tooltip = ComponentPrinter.value("show_in_tooltip", ((ArmorTrimAccessor) armorTrim).isShownInTooltip());
                    return ComponentPrinter.object(patternComponent, materialComponent, show_in_tooltip);
                });

        formatter.forComponents(component, DataComponents.SUSPICIOUS_STEW_EFFECTS)
                .handle(stewEffects ->
                        ComponentPrinter.list("effects", stewEffects.effects())
                                .handler(effect -> "'%s':%s".formatted(effect.effect().getRegisteredName(), effect.duration()))
                );

        formatter.forComponents(component, DataComponents.DEBUG_STICK_STATE)
                .handle(debugStickState -> {
                    var entries = debugStickState.properties().entrySet().stream().map(propertyEntry -> {
                        String blockKey = propertyEntry.getKey().getRegisteredName();
                        String propertyName = propertyEntry.getValue().getName();
                        String propertyValues = propertyEntry.getValue().getPossibleValues().toString();
                        return ComponentPrinter.text("'%s'=%s %s".formatted(blockKey, propertyName, propertyValues));
                    }).toList();

                    return ComponentPrinter.expandableObject(entries);
                });

        formatter.forComponents(component, DataComponents.INSTRUMENT)
                .handle(instrumentHolder -> ComponentPrinter.value("instrument", instrumentHolder.getRegisteredName()));

        formatter.forComponents(component, DataComponents.RECIPES)
                .handle(recipes -> ComponentPrinter.list("recipes", recipes).handler("'%s'"::formatted));

        formatter.forComponents(component, DataComponents.LODESTONE_TRACKER)
                .handle(lodestoneTracker -> {
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

                    return ComponentPrinter.object(components);
                });

        formatter.forComponents(component, DataComponents.FIREWORK_EXPLOSION)
                .handle(fireworkExplosion -> {
                    var shapeComponent = ComponentPrinter.value("shape", fireworkExplosion.shape().name());
                    var colorsListComponent = ComponentPrinter.list("colors", fireworkExplosion.colors()).handler(ComponentUtils::toHexInt);
                    var fadeColorsListComponent = ComponentPrinter.list("fade_colors", fireworkExplosion.fadeColors()).handler(ComponentUtils::toHexInt);
                    var hasTrailComponent = ComponentPrinter.value("has_trail", fireworkExplosion.hasTrail());
                    var hasTwinkleComponent = ComponentPrinter.value("has_twinkle", fireworkExplosion.hasTwinkle());

                    return ComponentPrinter.object(shapeComponent, colorsListComponent, fadeColorsListComponent, hasTrailComponent, hasTwinkleComponent);

                });

        formatter.forComponents(component, DataComponents.FIREWORKS)
                .handle(fireworks -> {
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
                    return ComponentPrinter.object(flightDurationComponent, explosionsListComponent);

                });

        formatter.forComponents(component, DataComponents.PROFILE)
                .handle(resolvableProfile -> {
                    var nameComponent = ComponentPrinter.value("fullName", resolvableProfile.name());
                    var idComponent = resolvableProfile.id().map(uuid -> ComponentPrinter.value("uuid", uuid.toString()));
                    var propertiesComponent = resolvableProfile.properties().entries().stream().map(entry -> {
                        String key = entry.getKey();
                        Property property = entry.getValue();
                        List<ComponentPrinter> components = Lists.newArrayList();
                        components.add(ComponentPrinter.value("fullName", property.name()));
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

                    return ComponentPrinter.object(components);
                });

        formatter.forComponents(component, DataComponents.NOTE_BLOCK_SOUND)
                .handle(resourceLocation -> ComponentPrinter.value("resource_location", resourceLocation.toString()));

        formatter.forComponents(component, DataComponents.BASE_COLOR)
                .handle(dyeColor -> ComponentPrinter.value("dye_color", dyeColor.getName()));

        formatter.forComponents(component, DataComponents.BANNER_PATTERNS)
                .handle(bannerPatternLayers -> {
                    var bannerLayers = bannerPatternLayers.layers().stream().map(layer -> {
                        var patternComponent = ComponentPrinter.value("pattern", layer.pattern().getRegisteredName());
                        var dyeColorComponent = ComponentPrinter.value("dye_color", layer.color().getName());
                        return ComponentPrinter.object(patternComponent, dyeColorComponent);
                    }).toList();

                    return ComponentPrinter.expandableList("layers", bannerLayers).handler(ComponentPrinter::print);
                });

        formatter.forComponents(component, DataComponents.POT_DECORATIONS)
                .handle(decorations -> {
                    List<ComponentPrinter> components = Lists.newArrayList();
                    decorations.back().ifPresent(item -> components.add(ComponentPrinter.value("back", Util.getRegisteredName(BuiltInRegistries.ITEM, item))));
                    decorations.left().ifPresent(item -> components.add(ComponentPrinter.value("left", Util.getRegisteredName(BuiltInRegistries.ITEM, item))));
                    decorations.right().ifPresent(item -> components.add(ComponentPrinter.value("right", Util.getRegisteredName(BuiltInRegistries.ITEM, item))));
                    decorations.front().ifPresent(item -> components.add(ComponentPrinter.value("front", Util.getRegisteredName(BuiltInRegistries.ITEM, item))));
                    return ComponentPrinter.object(components);
                });

        formatter.forComponents(component, DataComponents.CONTAINER)
                .handleAs(ItemContainerContentsAccessor.class, contents -> {
                    var slots = contents.slots().stream()
                            .map(slot -> {
                                String slotIndex = Integer.toString(slot.index());
                                String itemKey = Util.getRegisteredName(BuiltInRegistries.ITEM, slot.item().getItem());
                                return ComponentPrinter.value(slotIndex, "'%s':%s".formatted(itemKey, slot.item().getCount()));
                            }).toList();
                    return ComponentPrinter.expandableObject("slots", slots);
                });

        formatter.forComponents(component, DataComponents.BEES)
                .handle(occupants -> {
                    var bees = occupants.stream().map(occupant -> {
                        var ticksInHiveComponent = ComponentPrinter.value("ticks_in_hive", occupant.ticksInHive());
                        var minTicksInHiveComponent = ComponentPrinter.value("min_ticks_in_hive", occupant.minTicksInHive());
                        var customData = ComponentPrinter.nbt("entity_data", occupant.entityData().copyTag());

                        return ComponentPrinter.object(ticksInHiveComponent, minTicksInHiveComponent, customData);
                    }).toList();

                    return ComponentPrinter.list("bees", bees).handler(ComponentPrinter::print);
                });

        formatter.forComponents(component, DataComponents.LOCK)
                .handle(lockCode -> {
                    if (lockCode.key().isBlank()) {
                        return ComponentPrinter.text(I18n.translate("advancedtooltip.components.no_lock_set"));
                    }
                    return ComponentPrinter.value("lock", lockCode.key());
                });

        formatter.forComponents(component, DataComponents.CONTAINER_LOOT)
                .handle(seededContainerLoot -> {
                    var lootTableComponent = ComponentPrinter.value("loot_table", seededContainerLoot.lootTable().toString());
                    var seedComponent = ComponentPrinter.value("seed", seededContainerLoot.seed());
                    return ComponentPrinter.object(lootTableComponent, seedComponent);
                });

        formatter.forComponents(component, DataComponents.BLOCK_STATE)
                .handle(properties -> ComponentPrinter.map("properites", properties.properties()).handler(key -> key, ComponentPrinter::text));

        formatter.forComponents(component, DataComponents.ENCHANTMENT_GLINT_OVERRIDE)
                .handle(aBoolean -> ComponentPrinter.value("boolean", aBoolean));

        formatter.forComponents(component, DataComponents.MAP_POST_PROCESSING)
                .handle(mapPostProcessing -> ComponentPrinter.value("post_processing", mapPostProcessing.name().toLowerCase()));

        formatter.forComponents(component, DataComponents.FOOD)
                .handle(foodProperties -> {
                    var effectComponents = foodProperties.effects().stream().map(possibleEffect -> ComponentPrinter.expandableObject(
                            ComponentPrinter.value("effect", potionEffectToString(possibleEffect.effect())),
                            ComponentPrinter.value("probability", possibleEffect.probability())
                    )).toList();

                    return ComponentPrinter.object(
                            ComponentPrinter.value("nutrition", foodProperties.nutrition()),
                            ComponentPrinter.value("saturation", foodProperties.saturation()),
                            ComponentPrinter.value("can_always_eat", foodProperties.canAlwaysEat()),
                            ComponentPrinter.value("eat_seconds", foodProperties.eatSeconds()),
                            ComponentPrinter.expandableList("effects", effectComponents).handler(ComponentPrinter::print)
                    );
                });

        formatter.forComponents(component, DataComponents.RARITY)
                .handle(rarity -> {
                    List<ComponentPrinter> components = Lists.newArrayList(ComponentPrinter.value("name", rarity.getSerializedName()));
                    if (rarity.color().getColor() != null) {
                        components.add(ComponentPrinter.value("color", toHexInt(rarity.color().getColor())));
                    }

                    return ComponentPrinter.object("rarity", components).inline();
                });

        formatter.forComponents(component, DataComponents.TOOL)
                .handle(tool -> {
                    var rulesComponents = tool.rules().stream().map(rule -> {
                        List<ComponentPrinter> ruleComponents = Lists.newArrayList();
                        ruleComponents.add(ComponentPrinter.expandableList("blocks", rule.blocks().stream().map(Holder::getRegisteredName).toList()));
                        rule.speed().ifPresent(speed -> ruleComponents.add(ComponentPrinter.value("speed", speed)));
                        rule.correctForDrops().ifPresent(correctForDrops -> ruleComponents.add(ComponentPrinter.value("correct_for_drops", correctForDrops)));
                        return ComponentPrinter.object(ruleComponents);
                    }).toList();

                    return ComponentPrinter.object(
                                    ComponentPrinter.value("mining_speed", tool.defaultMiningSpeed()),
                                    ComponentPrinter.value("damage_per_block", tool.damagePerBlock()),
                                    ComponentPrinter.list("rules", rulesComponents).handler(ComponentPrinter::print)
                            )
                            .inline(tool.rules().isEmpty());
                });

        return formatter.getOr(ComponentPrinter.unsupported(component));
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
