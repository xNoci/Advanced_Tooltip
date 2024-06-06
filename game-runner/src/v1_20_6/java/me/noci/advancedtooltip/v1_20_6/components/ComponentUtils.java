package me.noci.advancedtooltip.v1_20_6.components;

import com.google.common.collect.Lists;
import com.mojang.authlib.properties.Property;
import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ObjectComponentPrinter;
import me.noci.advancedtooltip.v1_20_6.components.accessor.AdventureModePredicateAccessor;
import me.noci.advancedtooltip.v1_20_6.components.accessor.ArmorTrimAccessor;
import me.noci.advancedtooltip.v1_20_6.components.accessor.ItemContainerContentsAccessor;
import me.noci.advancedtooltip.v1_20_6.components.accessor.ItemEnchantmentsAccessor;
import me.noci.advancedtooltip.v1_20_6.components.renderer.ComponentRenderRegistry;
import me.noci.advancedtooltip.v1_20_6.components.renderer.ComponentRenderer;
import net.labymod.api.util.I18n;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ComponentUtils {

    private final static ComponentRenderRegistry renderer = new ComponentRenderRegistry()
            .register(ComponentRenderer.value(customData -> ComponentPrinter.nbt(customData.copyTag())),
                    DataComponents.CUSTOM_DATA, DataComponents.ENTITY_DATA, DataComponents.BUCKET_ENTITY_DATA, DataComponents.BLOCK_ENTITY_DATA)
            .register(ComponentRenderer.value(integer -> ComponentPrinter.value("int", integer)),
                    DataComponents.MAX_STACK_SIZE, DataComponents.MAX_DAMAGE, DataComponents.DAMAGE, DataComponents.REPAIR_COST, DataComponents.OMINOUS_BOTTLE_AMPLIFIER)
            .register(ComponentRenderer.value(unbreakable -> ComponentPrinter.value("show_in_tooltip", unbreakable.showInTooltip())),
                    DataComponents.UNBREAKABLE)
            .register(ComponentRenderer.value(chargedProjectiles ->
                            ComponentPrinter.list("projectiles", chargedProjectiles.getItems()).handler(projectile -> {
                                String itemKey = Util.getRegisteredName(BuiltInRegistries.ITEM, projectile.getItem());
                                int amount = projectile.getCount();
                                return "'%s':%s".formatted(itemKey, amount);
                            })),
                    DataComponents.CHARGED_PROJECTILES)
            .register(ComponentRenderer.value(itemEnchantments -> {
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
                    }),
                    DataComponents.ENCHANTMENTS, DataComponents.STORED_ENCHANTMENTS)
            .register(ComponentRenderer.value(textComponent -> ComponentPrinter.value("text", textComponent.toString().replaceAll("\n", "<br>"))),
                    DataComponents.CUSTOM_NAME, DataComponents.ITEM_NAME)
            .register(ComponentRenderer.value(itemLore -> ComponentPrinter
                            .expandableList("lines", itemLore.lines())
                            .handler(line -> line.toString().replaceAll("\n", "<br>"))
                    ),
                    DataComponents.LORE)
            .register(ComponentRenderer.cast(AdventureModePredicateAccessor.class, adventureModePredicate -> {
                        var tooltipComponent = ComponentPrinter.value("show_in_tooltip", adventureModePredicate.showInTooltip());
                        var blockListComponent = adventureModePredicate.predicates().stream()
                                .filter(blockPredicates -> blockPredicates.blocks().isPresent())
                                .map(blockPredicates -> Lists.newArrayList(blockPredicates.blocks().get()))
                                .filter(blocks -> !blocks.isEmpty())
                                .map(blocks -> ComponentPrinter.list("blocks", blocks).handler(block -> Util.getRegisteredName(BuiltInRegistries.BLOCK, block.value()))).toList();
                        var predicateListComponent = ComponentPrinter.expandableList("predicates", blockListComponent).handler(ComponentPrinter::print);
                        return ComponentPrinter.object(tooltipComponent, predicateListComponent);
                    }),
                    DataComponents.CAN_PLACE_ON, DataComponents.CAN_BREAK)
            .register(ComponentRenderer.value(dyedItemColor -> {
                        var tooltipComponent = ComponentPrinter.value("show_in_tooltip", dyedItemColor.showInTooltip());
                        var rgbComponent = ComponentPrinter.value("rgb", "0x%s".formatted(toHexInt(dyedItemColor.rgb())));
                        return ComponentPrinter.object(tooltipComponent, rgbComponent);
                    }),
                    DataComponents.DYED_COLOR)
            .register(ComponentRenderer.value(modifiers -> {
                        ComponentPrinter showInTooltipComponent = ComponentPrinter.value("show_in_tooltip", modifiers.showInTooltip());
                        List<ObjectComponentPrinter> modifierObjects = modifiers.modifiers().stream().map(entry -> {
                            ComponentPrinter attributeKeyComponent = ComponentPrinter.value("attribute", entry.attribute().getRegisteredName());
                            ComponentPrinter equipmentSlotComponent = ComponentPrinter.value("equipment_slot", entry.slot().getSerializedName());

                            ComponentPrinter attributeModifierComponent = ComponentPrinter.object("attribute_modifier",
                                    ComponentPrinter.value("amount", entry.modifier().amount()),
                                    ComponentPrinter.value("operation", entry.modifier().operation().getSerializedName()),
                                    ComponentPrinter.value("id", entry.modifier().id().toString()),
                                    ComponentPrinter.value("fullName", entry.modifier().name())
                            );

                            return ComponentPrinter.object(attributeKeyComponent, equipmentSlotComponent, attributeModifierComponent);
                        }).toList();

                        boolean hasModifiers = !modifiers.modifiers().isEmpty();
                        ComponentPrinter modifierListComponent = ComponentPrinter.expandableList("modifiers", modifierObjects).handler(ComponentPrinter::print);

                        return ComponentPrinter.object(showInTooltipComponent, modifierListComponent).inline(!hasModifiers);
                    }),
                    DataComponents.ATTRIBUTE_MODIFIERS)
            .register(ComponentPrinter::unit,
                    DataComponents.HIDE_ADDITIONAL_TOOLTIP, DataComponents.HIDE_TOOLTIP, DataComponents.CREATIVE_SLOT_LOCK, DataComponents.INTANGIBLE_PROJECTILE, DataComponents.FIRE_RESISTANT)
            .register(ComponentRenderer.value(bundleContents -> {
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
                    }),
                    DataComponents.BUNDLE_CONTENTS)
            .register(ComponentRenderer.value(mapItemColor -> ComponentPrinter.value("rgb", toHexInt(mapItemColor.rgb()))),
                    DataComponents.MAP_COLOR)
            .register(ComponentRenderer.value(decorations ->
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
                                            })),
                    DataComponents.MAP_DECORATIONS)
            .register(ComponentRenderer.value(mapId -> ComponentPrinter.value("map_id", mapId.id())),
                    DataComponents.MAP_ID)
            .register(ComponentRenderer.value(customModelData -> ComponentPrinter.value("custom_model_data", customModelData.value())),
                    DataComponents.CUSTOM_MODEL_DATA)
            .register(ComponentRenderer.value(potionContents -> {
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
                    }),
                    DataComponents.POTION_CONTENTS)
            .register(ComponentRenderer.value(writableBookContent -> {
                        var pages = writableBookContent.getPages(false).map(s -> s.replaceAll("\n", "<br>")).toList();
                        return ComponentPrinter.expandableList("pages", pages).handler("'%s'"::formatted);
                    }),
                    DataComponents.WRITABLE_BOOK_CONTENT)
            .register(ComponentRenderer.value(writtenBookContent -> {
                        var pages = writtenBookContent.getPages(false).stream().map(page -> page.toString().replaceAll("\n", "<br>")).toList();

                        var titleComponent = ComponentPrinter.value("title", writtenBookContent.title().get(false));
                        var authorComponent = ComponentPrinter.value("title", writtenBookContent.author());
                        var generationComponent = ComponentPrinter.value("title", writtenBookContent.generation());
                        var resolvedComponent = ComponentPrinter.value("title", writtenBookContent.resolved());
                        var pagesComponent = ComponentPrinter.expandableList("pages", pages).handler(Objects::toString);

                        return ComponentPrinter.object(titleComponent, authorComponent, generationComponent, resolvedComponent, pagesComponent);
                    }),
                    DataComponents.WRITTEN_BOOK_CONTENT)
            .register(ComponentRenderer.value(armorTrim -> {
                        var patternComponent = ComponentPrinter.value("pattern", armorTrim.pattern().getRegisteredName());
                        var materialComponent = ComponentPrinter.value("material", armorTrim.material().getRegisteredName());
                        var show_in_tooltip = ComponentPrinter.value("show_in_tooltip", ((ArmorTrimAccessor) armorTrim).isShownInTooltip());
                        return ComponentPrinter.object(patternComponent, materialComponent, show_in_tooltip);
                    }),
                    DataComponents.TRIM)
            .register(ComponentRenderer.value(stewEffects ->
                            ComponentPrinter.list("effects", stewEffects.effects())
                                    .handler(effect -> "'%s':%s".formatted(effect.effect().getRegisteredName(), effect.duration()))
                    ),
                    DataComponents.SUSPICIOUS_STEW_EFFECTS)
            .register(ComponentRenderer.value(debugStickState -> {
                        var entries = debugStickState.properties().entrySet().stream().map(propertyEntry -> {
                            String blockKey = propertyEntry.getKey().getRegisteredName();
                            String propertyName = propertyEntry.getValue().getName();
                            String propertyValues = propertyEntry.getValue().getPossibleValues().toString();
                            return ComponentPrinter.text("'%s'=%s %s".formatted(blockKey, propertyName, propertyValues));
                        }).toList();

                        return ComponentPrinter.expandableObject(entries);
                    }),
                    DataComponents.DEBUG_STICK_STATE)
            .register(ComponentRenderer.value(instrumentHolder -> ComponentPrinter.value("instrument", instrumentHolder.getRegisteredName())),
                    DataComponents.INSTRUMENT)
            .register(ComponentRenderer.value(recipes -> ComponentPrinter.list("recipes", recipes).handler("'%s'"::formatted)),
                    DataComponents.RECIPES)
            .register(ComponentRenderer.value(lodestoneTracker -> {
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
                    }),
                    DataComponents.LODESTONE_TRACKER)
            .register(ComponentRenderer.value(fireworkExplosion -> {
                        var shapeComponent = ComponentPrinter.value("shape", fireworkExplosion.shape().name());
                        var colorsListComponent = ComponentPrinter.list("colors", fireworkExplosion.colors()).handler(ComponentUtils::toHexInt);
                        var fadeColorsListComponent = ComponentPrinter.list("fade_colors", fireworkExplosion.fadeColors()).handler(ComponentUtils::toHexInt);
                        var hasTrailComponent = ComponentPrinter.value("has_trail", fireworkExplosion.hasTrail());
                        var hasTwinkleComponent = ComponentPrinter.value("has_twinkle", fireworkExplosion.hasTwinkle());

                        return ComponentPrinter.object(shapeComponent, colorsListComponent, fadeColorsListComponent, hasTrailComponent, hasTwinkleComponent);

                    }),
                    DataComponents.FIREWORK_EXPLOSION)
            .register(ComponentRenderer.value(fireworks -> {
                        var flightDurationComponent = ComponentPrinter.value("flight_duration", fireworks.flightDuration());

                        List<ObjectComponentPrinter> explosionsObjectComponentList = fireworks.explosions().stream().map(explosion -> {
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
                    }),
                    DataComponents.FIREWORKS)
            .register(ComponentRenderer.value(resolvableProfile -> {
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
                    }),
                    DataComponents.PROFILE)
            .register(ComponentRenderer.value(resourceLocation -> ComponentPrinter.value("resource_location", resourceLocation.toString())),
                    DataComponents.NOTE_BLOCK_SOUND)
            .register(ComponentRenderer.value(dyeColor -> ComponentPrinter.value("dye_color", dyeColor.getName())),
                    DataComponents.BASE_COLOR)
            .register(ComponentRenderer.value(bannerPatternLayers -> {
                        List<ObjectComponentPrinter> bannerLayers = bannerPatternLayers.layers().stream().map(layer -> {
                            var patternComponent = ComponentPrinter.value("pattern", layer.pattern().getRegisteredName());
                            var dyeColorComponent = ComponentPrinter.value("dye_color", layer.color().getName());
                            return ComponentPrinter.object(patternComponent, dyeColorComponent);
                        }).toList();

                        return ComponentPrinter.expandableList("layers", bannerLayers).handler(ComponentPrinter::print);
                    }),
                    DataComponents.BANNER_PATTERNS)
            .register(ComponentRenderer.value(decorations -> {
                        List<ComponentPrinter> components = Lists.newArrayList();
                        decorations.back().ifPresent(item -> components.add(ComponentPrinter.value("back", Util.getRegisteredName(BuiltInRegistries.ITEM, item))));
                        decorations.left().ifPresent(item -> components.add(ComponentPrinter.value("left", Util.getRegisteredName(BuiltInRegistries.ITEM, item))));
                        decorations.right().ifPresent(item -> components.add(ComponentPrinter.value("right", Util.getRegisteredName(BuiltInRegistries.ITEM, item))));
                        decorations.front().ifPresent(item -> components.add(ComponentPrinter.value("front", Util.getRegisteredName(BuiltInRegistries.ITEM, item))));
                        return ComponentPrinter.object(components);
                    }),
                    DataComponents.POT_DECORATIONS)
            .register(ComponentRenderer.cast(ItemContainerContentsAccessor.class, contents -> {
                        var slots = contents.slots().stream()
                                .map(slot -> {
                                    String slotIndex = Integer.toString(slot.index());
                                    String itemKey = Util.getRegisteredName(BuiltInRegistries.ITEM, slot.item().getItem());
                                    return ComponentPrinter.value(slotIndex, "'%s':%s".formatted(itemKey, slot.item().getCount()));
                                }).toList();
                        return ComponentPrinter.expandableObject("slots", slots);
                    }),
                    DataComponents.CONTAINER)
            .register(ComponentRenderer.value(occupants -> {
                        List<ObjectComponentPrinter> bees = occupants.stream().map(occupant -> {
                            var ticksInHiveComponent = ComponentPrinter.value("ticks_in_hive", occupant.ticksInHive());
                            var minTicksInHiveComponent = ComponentPrinter.value("min_ticks_in_hive", occupant.minTicksInHive());
                            var customData = ComponentPrinter.nbt("entity_data", occupant.entityData().copyTag());

                            return ComponentPrinter.object(ticksInHiveComponent, minTicksInHiveComponent, customData);
                        }).toList();

                        return ComponentPrinter.list("bees", bees).handler(ComponentPrinter::print);
                    }), DataComponents.BEES
            )
            .register(ComponentRenderer.value(lockCode -> {
                        if (lockCode.key().isBlank()) {
                            return ComponentPrinter.text(I18n.translate("advancedtooltip.components.no_lock_set"));
                        }
                        return ComponentPrinter.value("lock", lockCode.key());
                    }),
                    DataComponents.LOCK)
            .register(ComponentRenderer.value(seededContainerLoot -> {
                        var lootTableComponent = ComponentPrinter.value("loot_table", seededContainerLoot.lootTable().toString());
                        var seedComponent = ComponentPrinter.value("seed", seededContainerLoot.seed());
                        return ComponentPrinter.object(lootTableComponent, seedComponent);
                    }),
                    DataComponents.CONTAINER_LOOT)
            .register(ComponentRenderer.value(properties -> ComponentPrinter.map("properites", properties.properties()).handler(key -> key, ComponentPrinter::text)),
                    DataComponents.BLOCK_STATE)
            .register(
                    ComponentRenderer.value(aBoolean -> ComponentPrinter.value("boolean", aBoolean)),
                    DataComponents.ENCHANTMENT_GLINT_OVERRIDE
            )
            .register(ComponentRenderer.value(mapPostProcessing -> ComponentPrinter.value("post_processing", mapPostProcessing.name().toLowerCase())),
                    DataComponents.MAP_POST_PROCESSING)
            .register(ComponentRenderer.value(foodProperties -> {
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
                    }),
                    DataComponents.FOOD)
            .register(ComponentRenderer.value(rarity -> {
                        List<ComponentPrinter> components = Lists.newArrayList(ComponentPrinter.value("name", rarity.getSerializedName()));
                        if (rarity.color().getColor() != null) {
                            components.add(ComponentPrinter.value("color", toHexInt(rarity.color().getColor())));
                        }

                        return ComponentPrinter.object("rarity", components).inline();
                    }),
                    DataComponents.RARITY)
            .register(ComponentRenderer.value(tool -> {
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
                    }),
                    DataComponents.TOOL);


    public static String prettyPrint(ItemStack itemStack) {
        List<ComponentPrinter> components = itemStack.getComponents().stream()
                .map(renderer::printer)
                .toList();

        return ComponentPrinter.print(components);
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
