package me.noci.advancedtooltip.v24w09a.components;

import com.google.common.collect.Lists;
import me.noci.advancedtooltip.core.AdvancedTooltipAddon;
import me.noci.advancedtooltip.v24w09a.components.accessor.AdventureModePredicateAccessor;
import me.noci.advancedtooltip.v24w09a.components.accessor.ArmorTrimAccessor;
import me.noci.advancedtooltip.v24w09a.components.accessor.ItemEnchantmentsAccessor;
import net.labymod.api.util.I18n;
import net.minecraft.Util;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.NbtUtils;
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
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.saveddata.maps.MapId;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ComponentUtils {

    private static final int WRAPPED_VALUE_INTENT = 2;
    private static final String WRAPPED_VALUE_SPACES = " ".repeat(2 * WRAPPED_VALUE_INTENT);

    public static String prettyPrint(ItemStack itemStack, boolean withNbtArrayData, boolean expandComponents) {
        StringBuilder builder = new StringBuilder();
        int componentCount = itemStack.getComponents().size();
        builder.append(I18n.translate("advancedtooltip.components.count", componentCount)).append(" [\n");
        itemStack.getComponents().forEach(component -> formatComponent(builder, component, withNbtArrayData, expandComponents));
        builder.append("]");

        return builder.toString();
    }

    private static void formatComponent(StringBuilder builder, TypedDataComponent<?> component, boolean withNbtArrayData, boolean expandComponents) {
        builder.append(" ").append(component.type()).append("=>");

        if (component.value() instanceof CustomData customData) {
            if (expandComponents) {
                appendWrappedValue(builder, wrappedBuilder -> {
                    NbtUtils.prettyPrint(wrappedBuilder, customData.copyTag(), WRAPPED_VALUE_INTENT, withNbtArrayData);
                    wrappedBuilder.delete(0, wrappedBuilder.indexOf("{"));
                    wrappedBuilder.insert(wrappedBuilder.indexOf("{") + 1, new StringBuilder("\n").append(WRAPPED_VALUE_SPACES));
                    wrappedBuilder.delete(wrappedBuilder.lastIndexOf("\n") + 1, wrappedBuilder.lastIndexOf(" "));
                }, false);
            } else {
                builder.append("[").append(pressToShowObject()).append("]");
            }
        }

        if (component.value() instanceof Integer value) {
            builder.append("[int=").append(value).append("]");
        }

        if (component.value() instanceof Unbreakable unbreakable) {
            builder.append("[show_in_tooltip=").append(unbreakable.showInTooltip()).append("]");
        }

        if (component.value() instanceof ItemEnchantments itemEnchantments) {
            boolean showInToolTip = ((ItemEnchantmentsAccessor) itemEnchantments).isShownInTooltip();

            if (itemEnchantments.isEmpty()) {
                builder.append("[show_in_tooltip=").append(showInToolTip).append("]");
            } else {
                Enchantment[] enchantments = ((ItemEnchantmentsAccessor) itemEnchantments).enchantments();
                appendWrappedValue(builder, wrappedBuilder -> {
                    wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(" show_in_tooltip=").append(showInToolTip).append(",\n");
                    wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(" levels: [\n");

                    for (int i = 0; i < enchantments.length; i++) {
                        Enchantment enchantment = enchantments[i];
                        String enchantmentKey = Util.getRegisteredName(BuiltInRegistries.ENCHANTMENT, enchantment);
                        int level = itemEnchantments.getLevel(enchantment);
                        wrappedBuilder.append(WRAPPED_VALUE_SPACES)
                                .append("  '")
                                .append(enchantmentKey)
                                .append("':")
                                .append(level);

                        if (i + 1 != enchantments.length) wrappedBuilder.append(",");
                        wrappedBuilder.append("\n");
                    }

                    wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(" ]");
                });
            }
        }

        if (component.value() instanceof Component textComponent) {
            builder.append("[").append(textComponent.toString().replaceAll("\n", "<br>")).append("]");
        }

        if (component.value() instanceof ItemLore itemLore) {
            builder.append("[lore: ").append(itemLore.lines()).append("]");
        }

        if (component.value() instanceof AdventureModePredicateAccessor adventureModePredicate) { //TODO Finish this one
            boolean showInTooltip = adventureModePredicate.showInTooltip();

            if (adventureModePredicate.isEmpty()) {
                builder.append("[show_in_tooltip=").append(showInTooltip).append("]");
            } else {
                List<BlockPredicate> predicates = adventureModePredicate.predicates();

                appendWrappedValue(builder, wrappedBuilder -> {
                    wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(" show_in_tooltip=").append(showInTooltip).append(",\n");
                    wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(" predicates: [\n");

                    for (int i = 0; i < predicates.size(); i++) {
                        BlockPredicate predicate = predicates.get(i);

                        String blocks = "[]";

                        if (predicate.blocks().isPresent()) {
                            blocks = predicate.blocks().get().stream()
                                    .map(Holder::value)
                                    .map(block -> Util.getRegisteredName(BuiltInRegistries.BLOCK, block))
                                    .collect(Collectors.joining(",\n  " + WRAPPED_VALUE_SPACES));
                        }
                        wrappedBuilder.append(WRAPPED_VALUE_SPACES)
                                .append("  ")
                                .append(blocks);

                        if (i + 1 != predicates.size()) wrappedBuilder.append(",");
                        wrappedBuilder.append("\n");
                    }

                    wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(" ]");
                });
            }
        }

        if (component.value() instanceof DyedItemColor dyedItemColor) {
            appendWrappedValue(builder, wrappedBuilder -> {
                wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(" show_in_tooltip=").append(dyedItemColor.showInTooltip()).append(",\n");
                wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(" rgb=0x").append(Integer.toHexString(dyedItemColor.rgb()));
            });
        }

        if (component.value() instanceof ItemAttributeModifiers modifiers) {
            //TODO
        }

        if (component.value() instanceof ChargedProjectiles chargedProjectiles) {
            if (chargedProjectiles.isEmpty()) {
                builder.append("[projectiles: []]");
            } else {
                appendWrappedValue(builder, wrappedBuilder -> {
                    wrappedBuilder.append("projectiles: [\n");

                    List<ItemStack> projectiles = chargedProjectiles.getItems();
                    for (int i = 0; i < projectiles.size(); i++) {
                        ItemStack projectile = projectiles.get(i);
                        String itemKey = Util.getRegisteredName(BuiltInRegistries.ITEM, projectile.getItem());
                        int amount = projectile.getCount();
                        wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(" '").append(itemKey).append("':").append(amount);
                        if (i + 1 != projectiles.size()) wrappedBuilder.append(",");
                        wrappedBuilder.append("\n");
                    }

                    wrappedBuilder.append(" ]");
                }, false);
            }
        }

        if (component.value() instanceof Unit) {
            builder.append("[{}]");
        }

        if (component.value() instanceof BundleContents bundleContents) {
            int weight = bundleContents.weight();
            if (bundleContents.isEmpty()) {
                builder.append("[weight=").append(weight).append("]");
            } else {
                appendWrappedValue(builder, wrappedBuilder -> {
                    wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(" weight=").append(weight).append(",\n");
                    if (expandComponents) {
                        wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(" items: [\n");

                        List<ItemStack> items = bundleContents.items().toList();
                        for (int i = 0; i < items.size(); i++) {
                            ItemStack itemStack = items.get(i);

                            String itemKey = Util.getRegisteredName(BuiltInRegistries.ITEM, itemStack.getItem());
                            int amount = itemStack.getCount();
                            wrappedBuilder.append(WRAPPED_VALUE_SPACES).append("  '").append(itemKey).append("':").append(amount);

                            if (i + 1 != items.size()) wrappedBuilder.append(",");
                            wrappedBuilder.append("\n");
                        }

                        wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(" ]");
                    } else {
                        wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(" items: ").append(pressToShowList());
                    }
                });
            }
        }

        if (component.value() instanceof MapItemColor mapItemColor) {
            builder.append("[rgb=0x").append(Integer.toHexString(mapItemColor.rgb())).append("]");
        }

        if (component.value() instanceof MapDecorations decorations) {
            if (decorations.decorations().isEmpty()) {
                builder.append("[decorations: {}]");
            } else if (!expandComponents) {
                builder.append("[decorations: ").append(pressToShowObject()).append("]");
            } else {
                appendWrappedValue(builder, wrappedBuilder -> {
                    wrappedBuilder.append("decorations: {\n");

                    var mapDecorations = Lists.newArrayList(decorations.decorations().entrySet());
                    for (int i = 0; i < mapDecorations.size(); i++) {
                        var decorationEntry = mapDecorations.get(i);
                        String markerName = decorationEntry.getKey();
                        MapDecorations.Entry decoration = decorationEntry.getValue();

                        wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(" '").append(markerName).append("': {\n");

                        wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(WRAPPED_VALUE_SPACES).append(" type=").append(decoration.type().getSerializedName()).append(",\n");
                        wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(WRAPPED_VALUE_SPACES).append(" x=").append(decoration.x()).append(",\n");
                        wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(WRAPPED_VALUE_SPACES).append(" z=").append(decoration.z()).append(",\n");
                        wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(WRAPPED_VALUE_SPACES).append(" rotation=").append(decoration.rotation()).append("\n");

                        wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(" }");
                        if (i + 1 != mapDecorations.size()) wrappedBuilder.append(",");
                        wrappedBuilder.append("\n");
                    }

                    wrappedBuilder.append(" }");
                }, false);
            }
        }

        if (component.value() instanceof MapId mapId) {
            builder.append("[map_id=").append(mapId.id()).append("]");
        }

        if (component.value() instanceof CustomModelData customModelData) {
            builder.append("[custom_model_data=").append(customModelData.value()).append("]");
        }

        if (component.value() instanceof PotionContents potionContents) {
            Optional<Holder<Potion>> potionHolder = potionContents.potion();
            Optional<Integer> customColor = potionContents.customColor();
            List<MobEffectInstance> customEffects = potionContents.customEffects();

            appendWrappedValue(builder, wrappedBuilder -> {
                customColor.ifPresent(color -> wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(" custom_color=").append(Integer.toHexString(color)).append(",\n"));

                potionHolder.ifPresent(pH -> {
                    String name = Potion.getName(potionHolder, "");
                    List<MobEffectInstance> potionEffects = pH.value().getEffects();

                    wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(" potion: {\n");
                    wrappedBuilder.append(WRAPPED_VALUE_SPACES).append("  name='").append(name).append("',\n");

                    if (potionEffects.isEmpty()) {
                        wrappedBuilder.append(WRAPPED_VALUE_SPACES).append("  effects: [],\n");
                    } else {
                        wrappedBuilder.append(WRAPPED_VALUE_SPACES).append("  effects: [\n");

                        for (int i = 0; i < potionEffects.size(); i++) {
                            MobEffectInstance effect = potionEffects.get(i);
                            wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(WRAPPED_VALUE_SPACES)
                                    .append(" ").append(potionEffectToString(effect));

                            if (i + 1 != potionEffects.size()) wrappedBuilder.append(",");
                            wrappedBuilder.append("\n");
                        }

                        wrappedBuilder.append(WRAPPED_VALUE_SPACES).append("  ]\n");
                    }

                    wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(" },\n");
                });

                if (customEffects.isEmpty()) {
                    wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(" custom_effects: []");
                } else {
                    wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(" custom_effects: [\n");
                    for (int i = 0; i < customEffects.size(); i++) {
                        MobEffectInstance effect = customEffects.get(i);
                        wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(WRAPPED_VALUE_SPACES)
                                .append(" ").append(potionEffectToString(effect));

                        if (i + 1 != customEffects.size()) wrappedBuilder.append(",");
                        wrappedBuilder.append("\n");
                    }
                    wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(" ]");
                }
            });

        }

        if (component.value() instanceof WritableBookContent writableBookContent) {
            if (writableBookContent.pages().isEmpty()) {
                builder.append("[pages: []]");
            } else {
                appendWrappedValue(builder, wrappedBuilder -> {
                    if (expandComponents) {
                        wrappedBuilder.append("pages: [\n");

                        List<String> pages = writableBookContent.getPages(false).map(s -> s.replaceAll("\n", "<br>")).toList();
                        for (int i = 0; i < pages.size(); i++) {
                            String page = pages.get(i);
                            wrappedBuilder.append(WRAPPED_VALUE_SPACES).append("  '").append(page).append("'");

                            if (i + 1 != pages.size()) wrappedBuilder.append(",");
                            wrappedBuilder.append("\n");
                        }

                        wrappedBuilder.append(" ]");
                    } else {
                        wrappedBuilder.append("pages: ").append(pressToShowList());
                    }
                }, false);
            }
        }

        if (component.value() instanceof WrittenBookContent writtenBookContent) {
            appendWrappedValue(builder, wrappedBuilder -> {
                wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(" title='").append(writtenBookContent.title().get(false)).append("',\n");
                wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(" author='").append(writtenBookContent.author()).append("',\n");
                wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(" generation=").append(writtenBookContent.generation()).append(",\n");
                wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(" resolved=").append(writtenBookContent.resolved()).append(",\n");

                var pages = writtenBookContent.getPages(false).stream().map(page -> page.toString().replaceAll("\n", "<br>")).toList();
                if (pages.isEmpty()) {
                    wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(" pages: []");
                } else if (!expandComponents) {
                    wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(" pages: ").append(pressToShowList());
                } else {
                    wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(" pages: [\n");

                    for (int i = 0; i < pages.size(); i++) {
                        String page = pages.get(i);
                        wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(WRAPPED_VALUE_SPACES)
                                .append(" ").append(page);

                        if (i + 1 != pages.size()) wrappedBuilder.append(",");
                        wrappedBuilder.append("\n");
                    }

                    wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(" ]");
                }
            });

        }

        if (component.value() instanceof ArmorTrim armorTrim) {
            appendWrappedValue(builder, wrappedBuilder -> {
                boolean showInToolTip = ((ArmorTrimAccessor) armorTrim).isShownInTooltip();
                String patternKey = armorTrim.pattern().getRegisteredName();
                String itemKey = armorTrim.material().getRegisteredName();

                wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(" pattern=").append(patternKey).append(",\n");
                wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(" material=").append(itemKey).append(",\n");
                wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(" show_in_tooltip=").append(showInToolTip);
            });
        }

        if (component.value() instanceof SuspiciousStewEffects stewEffects) {
            var effects = stewEffects.effects();

            if (effects.isEmpty()) {
                builder.append("[effects: []]");
            } else {
                appendWrappedValue(builder, wrappedBuilder -> {
                    wrappedBuilder.append("effects: [\n");

                    for (int i = 0; i < effects.size(); i++) {
                        SuspiciousStewEffects.Entry effect = effects.get(i);
                        String effectKey = effect.effect().getRegisteredName();

                        wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(" '").append(effectKey).append("':").append(effect.duration());
                        if (i + 1 != effects.size()) wrappedBuilder.append(",");
                        wrappedBuilder.append("\n");
                    }

                    wrappedBuilder.append(" ]");
                }, false);
            }

        }

        if (component.value() instanceof DebugStickState debugStickState) {
            if (debugStickState.properties().isEmpty()) {
                builder.append("[{}]");
            } else if (!expandComponents) {
                builder.append("[").append(pressToShowObject()).append("]");
            } else {
                appendWrappedValue(builder, wrappedBuilder -> {
                    var properties = Lists.newArrayList(debugStickState.properties().entrySet());
                    for (int i = 0; i < properties.size(); i++) {
                        var propertyEntry = properties.get(i);
                        String blockKey = propertyEntry.getKey().getRegisteredName();
                        String propertyName = propertyEntry.getValue().getName();
                        String propertyValues = propertyEntry.getValue().getPossibleValues().toString();
                        wrappedBuilder.append(WRAPPED_VALUE_SPACES)
                                .append(" '").append(blockKey).append("'=")
                                .append(propertyName).append(" ").append(propertyValues);

                        if (i + 1 != properties.size()) wrappedBuilder.append(",\n");
                    }
                });
            }
        }

        if (component.type() == DataComponents.INSTRUMENT) {
            Holder<Instrument> holder = (Holder<Instrument>) component.value();
            builder.append("[instrument=").append(holder.getRegisteredName()).append("]");
        }

        if (component.type() == DataComponents.RECIPES) {
            List<ResourceLocation> recipes = (List<ResourceLocation>) component.value();
            if (recipes.isEmpty()) {
                builder.append("[recipes: []]");
            } else {
                appendWrappedValue(builder, wrappedBuilder -> {
                    wrappedBuilder.append("recipes: [\n");

                    for (int i = 0; i < recipes.size(); i++) {
                        ResourceLocation recipe = recipes.get(i);
                        wrappedBuilder.append(WRAPPED_VALUE_SPACES).append(" '").append(recipe).append("'");
                        if (i + 1 != recipes.size()) wrappedBuilder.append(",");
                        wrappedBuilder.append("\n");
                    }

                    wrappedBuilder.append(" ]");
                }, false);
            }
        }

        //TODO MINECRAFT:LODESTONE_TARGET and onwards
        //TODO https://www.minecraft.net/en-us/article/minecraft-snapshot-24w09a#:~:text=in%20standalone%20file)-,ITEM%20STACK%20COMPONENTS,-We%20are%20making


        //If not already handled
        //TODO REMOVE
        if (builder.lastIndexOf("=>") + 2 == builder.length()) {
            builder.append(I18n.translate("advancedtooltip.components.currentlyNotSupported"));
        }

        builder.append(",\n");
    }

    private static void appendWrappedValue(StringBuilder builder, WrappedValue value) {
        appendWrappedValue(builder, value, true);
    }

    private static void appendWrappedValue(StringBuilder builder, WrappedValue value, boolean addBrackets) {
        StringBuilder wrappedBuilder = new StringBuilder();
        value.apply(wrappedBuilder);

        builder.append("[");
        if (addBrackets) builder.append("{\n");
        builder.append(wrappedBuilder);
        if (addBrackets) builder.append("\n }");
        builder.append("]");
    }

    private static String pressToShowObject() {
        String keyTranslationKey = AdvancedTooltipAddon.getInstance().configuration().developerSettings().expandComponents().get().getTranslationKey();
        return I18n.translate("advancedtooltip.components.pressToShowObject", I18n.translate(keyTranslationKey));
    }


    private static String pressToShowList() {
        String keyTranslationKey = AdvancedTooltipAddon.getInstance().configuration().developerSettings().expandComponents().get().getTranslationKey();
        return I18n.translate("advancedtooltip.components.pressToShowList", I18n.translate(keyTranslationKey));
    }

    private static String potionEffectToString(MobEffectInstance effect) {
        String effectKey = effect.getEffect().getRegisteredName();
        String amplifier = Component.translatable("potion.potency." + effect.getAmplifier()).getString();

        if (effect.getAmplifier() > 0) {
            return I18n.translate("advancedtooltip.components.potion.effect.amplified", effectKey, effect.getDuration(), amplifier);
        }

        return I18n.translate("advancedtooltip.components.potion.effect.not_amplified", effectKey, effect.getDuration());
    }

    @FunctionalInterface
    private interface WrappedValue {
        void apply(StringBuilder builder);
    }

}
