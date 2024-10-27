package me.noci.advancedtooltip.v1_21_3.items;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import me.noci.advancedtooltip.core.referenceable.items.ComponentHelper;
import me.noci.advancedtooltip.core.utils.MapDecoration;
import me.noci.advancedtooltip.core.utils.SignText;
import me.noci.advancedtooltip.v1_21_3.components.ComponentRenderRegistry;
import me.noci.advancedtooltip.v1_21_3.utils.ItemCast;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.models.Implements;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.MapDecorations;
import org.jetbrains.annotations.Nullable;

import javax.inject.Singleton;
import java.util.List;

@Singleton
@Implements(ComponentHelper.class)
public class VersionedComponentHelper implements ComponentHelper {

    private static final Gson GSON = new GsonBuilder().create();

    @Override
    public @Nullable String componentName(Object component) {
        if (!(component instanceof TypedDataComponent<?> dataComponent)) return null;
        return dataComponent.type().toString();
    }

    @Override
    public @Nullable StringBuilder prettyPrintCompound(Object compoundTag, int indentLevel, boolean withNbtArrayData) {
        if (!(compoundTag instanceof CompoundTag tag)) return null;
        StringBuilder builder = new StringBuilder();
        NbtUtils.prettyPrint(builder, tag, indentLevel, withNbtArrayData);
        return builder;
    }

    @Override
    public boolean isEmptyCompound(Object compoundTag) {
        if (!(compoundTag instanceof CompoundTag tag)) return true;
        return tag.isEmpty();
    }

    @Override
    public @Nullable String displayItemData(ItemStack labyItemStack, boolean withNbtArrayData) {
        var itemStack = ItemCast.toMinecraftItemStack(labyItemStack);
        return ComponentRenderRegistry.prettyPrint(itemStack);
    }

    @Override
    public int repairCost(ItemStack itemStack) {
        Integer repairCost = ItemCast.typedDataComponent(itemStack, DataComponents.REPAIR_COST);
        return repairCost != null ? repairCost : 0;
    }

    @Override
    public @Nullable List<MapDecoration> mapDecorations(ItemStack itemStack) {
        MapDecorations decorations = ItemCast.typedDataComponent(itemStack, DataComponents.MAP_DECORATIONS);
        if (decorations == null) return null;

        List<MapDecoration> mapDecorations = Lists.newArrayList();

        for (MapDecorations.Entry value : decorations.decorations().values()) {
            var type = MapDecoration.Type.byResourceLocation(resourceLocation -> value.type().is((ResourceLocation) resourceLocation.getMinecraftLocation()));
            mapDecorations.add(new MapDecoration(type, value.x(), value.z()));
        }

        return mapDecorations;
    }

    @Override
    public @Nullable String commandBlockCommand(ItemStack itemStack) {
        CustomData customData = ItemCast.typedDataComponent(itemStack, DataComponents.BLOCK_ENTITY_DATA);
        if (customData == null) return null;
        CompoundTag compoundTag = customData.copyTag();
        if (!compoundTag.contains("Command", CompoundTag.TAG_STRING)) return null;
        return compoundTag.getString("Command");
    }

    @Override
    public @Nullable SignText signText(ItemStack itemStack) {
        CustomData customData = ItemCast.typedDataComponent(itemStack, DataComponents.BLOCK_ENTITY_DATA);
        if (customData == null) return null;
        CompoundTag compoundTag = customData.copyTag();

        String[] frontText = null;
        String[] backText = null;

        if (compoundTag.contains("front_text")) {
            CompoundTag frontTextCompound = compoundTag.getCompound("front_text");
            ListTag lines = frontTextCompound.getList("messages", ListTag.TAG_STRING);
            frontText = new String[lines.size()];

            for (int i = 0; i < lines.size(); i++) {
                Tag tag = lines.get(i);
                frontText[i] = GSON.fromJson(tag.getAsString(), JsonObject.class).get("text").getAsString();
            }
        }

        if (compoundTag.contains("back_text")) {
            CompoundTag frontTextCompound = compoundTag.getCompound("back_text");
            ListTag lines = frontTextCompound.getList("messages", ListTag.TAG_STRING);
            backText = new String[lines.size()];

            for (int i = 0; i < lines.size(); i++) {
                Tag tag = lines.get(i);
                backText[i] = GSON.fromJson(tag.getAsString(), JsonObject.class).get("text").getAsString();
            }
        }

        return new SignText(frontText, backText);
    }

    @Override
    public boolean unbreakable(ItemStack itemStack) {
        return ItemCast.typedDataComponent(itemStack, DataComponents.UNBREAKABLE) != null;
    }

}
