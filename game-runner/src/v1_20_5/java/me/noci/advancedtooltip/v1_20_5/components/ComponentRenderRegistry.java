package me.noci.advancedtooltip.v1_20_5.components;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import me.noci.advancedtooltip.v1_20_5.components.renderer.*;
import net.labymod.api.Laby;
import net.labymod.api.util.logging.Logging;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.List;

public class ComponentRenderRegistry {

    private static final Logging LOGGER = Laby.references().loggingFactory().create(ComponentRenderRegistry.class);
    private static final HashMap<DataComponentType<?>, ComponentRenderer<?>> RENDERERS = Maps.newHashMap();

    static {
        register(new CustomDataComponentRenderer(), DataComponents.CUSTOM_DATA, DataComponents.ENTITY_DATA, DataComponents.BUCKET_ENTITY_DATA, DataComponents.BLOCK_ENTITY_DATA);
        register(new IntegerComponentRenderer(), DataComponents.MAX_STACK_SIZE, DataComponents.MAX_DAMAGE, DataComponents.DAMAGE, DataComponents.REPAIR_COST, DataComponents.OMINOUS_BOTTLE_AMPLIFIER);
        register(new UnbreakableComponentRenderer(), DataComponents.UNBREAKABLE);
        register(new ChargedProjectilesComponentRenderer(), DataComponents.CHARGED_PROJECTILES);
        register(new ItemEnchantmentsComponentRenderer(), DataComponents.ENCHANTMENTS, DataComponents.STORED_ENCHANTMENTS);
        register(new TextComponentComponentRenderer(), DataComponents.CUSTOM_NAME, DataComponents.ITEM_NAME);
        register(new ItemLoreComponentRenderer(), DataComponents.LORE);
        register(new AdventureModePredicateComponentRenderer(), DataComponents.CAN_PLACE_ON, DataComponents.CAN_BREAK);
        register(new DyedItemColorComponentRenderer(), DataComponents.DYED_COLOR);
        register(new ItemAttributeModifiersComponentRenderer(), DataComponents.ATTRIBUTE_MODIFIERS);
        register(new UnitComponentRenderer(), DataComponents.HIDE_ADDITIONAL_TOOLTIP, DataComponents.HIDE_TOOLTIP, DataComponents.CREATIVE_SLOT_LOCK, DataComponents.INTANGIBLE_PROJECTILE, DataComponents.FIRE_RESISTANT);
        register(new BundleContentsComponentRenderer(), DataComponents.BUNDLE_CONTENTS);
        register(new MapItemColorComponentRenderer(), DataComponents.MAP_COLOR);
        register(new MapDecorationsComponentRenderer(), DataComponents.MAP_DECORATIONS);
        register(new MapIdComponentRenderer(), DataComponents.MAP_ID);
        register(new CustomModelDataComponentRenderer(), DataComponents.CUSTOM_MODEL_DATA);
        register(new PotionContentsComponentRenderer(), DataComponents.POTION_CONTENTS);
        register(new WritableBookContentComponentRenderer(), DataComponents.WRITABLE_BOOK_CONTENT);
        register(new WrittenBookContentComponentRenderer(), DataComponents.WRITTEN_BOOK_CONTENT);
        register(new ArmorTrimComponentRenderer(), DataComponents.TRIM);
        register(new SuspiciousStewEffectsComponentRenderer(), DataComponents.SUSPICIOUS_STEW_EFFECTS);
        register(new DebugStickStateComponentRenderer(), DataComponents.DEBUG_STICK_STATE);
        register(new InstrumentComponentRenderer(), DataComponents.INSTRUMENT);
        register(new ResourceLocationListComponentRenderer(), DataComponents.RECIPES);
        register(new LodestoneComponentRenderer(), DataComponents.LODESTONE_TRACKER);
        register(new FireworkExplosionComponentRenderer(), DataComponents.FIREWORK_EXPLOSION);
        register(new FireworksComponentRenderer(), DataComponents.FIREWORKS);
        register(new ResolvableProfileComponentRenderer(), DataComponents.PROFILE);
        register(new ResourceLocationComponentRenderer(), DataComponents.NOTE_BLOCK_SOUND);
        register(new DyeColorComponentRenderer(), DataComponents.BASE_COLOR);
        register(new BannerPatternsLayersComponentRenderer(), DataComponents.BANNER_PATTERNS);
        register(new PotDecorationsComponentRenderer(), DataComponents.POT_DECORATIONS);
        register(new ItemContainerContentsComponentRenderer(), DataComponents.CONTAINER);
        register(new BeehiveOccupantListComponentRenderer(), DataComponents.BEES);
        register(new LockCodeComponentRenderer(), DataComponents.LOCK);
        register(new SeededContainerLootComponentRenderer(), DataComponents.CONTAINER_LOOT);
        register(new BlockItemStatePropertiesComponentRenderer(), DataComponents.BLOCK_STATE);
        register(new BooleanComponentRenderer(), DataComponents.ENCHANTMENT_GLINT_OVERRIDE);
        register(new MapPostProcessingComponentRenderer(), DataComponents.MAP_POST_PROCESSING);
        register(new FoodPropertiesComponentRenderer(), DataComponents.FOOD);
        register(new RarityComponentRenderer(), DataComponents.RARITY);
        register(new ToolComponentRenderer(), DataComponents.TOOL);
    }

    public static String prettyPrint(ItemStack itemStack) {
        List<ComponentPrinter> componentPrinters = Lists.newArrayList();

        for (TypedDataComponent<?> component : itemStack.getComponents()) {
            componentPrinters.add(render(component));
        }

        return ComponentPrinter.print(componentPrinters);
    }

    @SuppressWarnings("unchecked")
    private static <T> ComponentPrinter render(TypedDataComponent<T> component) {
        ComponentRenderer<T> renderer = (ComponentRenderer<T>) RENDERERS.getOrDefault(component.type(), ComponentRenderer.FALLBACK);
        return renderer.render(component, component.value());
    }

    @SafeVarargs
    private static <T> void register(ComponentRenderer<T> renderer, DataComponentType<T>... components) {
        for (DataComponentType<T> component : components) {
            try {
                register(renderer, component);
            } catch (Exception e) {
                LOGGER.info("Failed to register renderer", e);
            }
        }
    }

    private static <T> void register(ComponentRenderer<T> renderer, DataComponentType<T> component) {
        if (RENDERERS.containsKey(component)) {
            throw new IllegalStateException("Already registered a renderer for component: " + component);
        }
        RENDERERS.put(component, renderer);
    }

}
