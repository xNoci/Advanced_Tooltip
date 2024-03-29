package me.noci.advancedtooltip.v24w13a.items;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import me.noci.advancedtooltip.core.referenceable.items.ComponentHelper;
import me.noci.advancedtooltip.core.utils.MapDecoration;
import me.noci.advancedtooltip.core.utils.SignText;
import me.noci.advancedtooltip.v24w13a.components.ComponentUtils;
import me.noci.advancedtooltip.v24w13a.utils.ItemCast;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.models.Implements;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;

import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

@Singleton
@Implements(ComponentHelper.class)
public class VersionedComponentHelper implements ComponentHelper {

    private static final Gson GSON = new GsonBuilder().create();

    @Override
    public Optional<String> componentName(Object component) {
        if (!(component instanceof TypedDataComponent<?> dataComponent)) return Optional.empty();
        return dataComponent.type().toString().describeConstable();
    }

    @Override
    public Optional<StringBuilder> prettyPrintCompound(Object compoundTag, int indentLevel, boolean withNbtArrayData) {
        if (!(compoundTag instanceof CompoundTag tag)) return Optional.empty();
        StringBuilder builder = new StringBuilder();
        NbtUtils.prettyPrint(builder, tag, indentLevel, withNbtArrayData);
        return Optional.of(builder);
    }

    @Override
    public boolean isEmptyCompound(Object compoundTag) {
        if (!(compoundTag instanceof CompoundTag tag)) return true;
        return tag.isEmpty();
    }

    @Override
    public Optional<String> displayItemData(ItemStack labyItemStack, boolean withNbtArrayData) {
        var itemStack = ItemCast.toMinecraftItemStack(labyItemStack);
        return ComponentUtils.prettyPrint(itemStack).describeConstable();
    }

    @Override
    public Optional<Integer> repairCost(ItemStack itemStack) {
        return ItemCast.typedDataComponent(itemStack, DataComponents.REPAIR_COST);
    }

    @Override
    public Optional<List<MapDecoration>> mapDecorations(ItemStack itemStack) {
        var decorations = ItemCast.typedDataComponent(itemStack, DataComponents.MAP_DECORATIONS)
                .stream()
                .flatMap(mapDecorations -> mapDecorations.decorations().values().stream())
                .map(decoration -> {
                    var type = MapDecoration.Type.byResourceLocation(resourceLocation -> decoration.type().is((ResourceLocation) resourceLocation.getMinecraftLocation()));
                    var x = decoration.x();
                    var z = decoration.z();
                    return new MapDecoration(type, x, z);
                }).toList();
        return Optional.of(decorations);
    }

    @Override
    public Optional<String> commandBlockCommand(ItemStack itemStack) {
        return ItemCast.typedDataComponent(itemStack, DataComponents.BLOCK_ENTITY_DATA).map(customData -> {
            CompoundTag compoundTag = customData.copyTag();
            if (!compoundTag.contains("Command", CompoundTag.TAG_STRING)) return null;
            return compoundTag.getString("Command");
        });
    }

    @Override
    public Optional<SignText> signText(ItemStack itemStack) {
        return ItemCast.typedDataComponent(itemStack, DataComponents.BLOCK_ENTITY_DATA).map(customData -> {
            CompoundTag compoundTag = customData.copyTag();

            String[] frontText = null;
            String[] backText = null;

            if (compoundTag.contains("front_text")) {
                CompoundTag frontTextCompound = compoundTag.getCompound("front_text");
                ListTag lines = frontTextCompound.getList("messages", ListTag.TAG_STRING);
                frontText = lines.stream()
                        .map(tag -> GSON.fromJson(tag.getAsString(), JsonObject.class).get("text").getAsString())
                        .toArray(String[]::new);
            }

            if (compoundTag.contains("back_text")) {
                CompoundTag frontTextCompound = compoundTag.getCompound("back_text");
                ListTag lines = frontTextCompound.getList("messages", ListTag.TAG_STRING);
                backText = lines.stream()
                        .map(tag -> GSON.fromJson(tag.getAsString(), JsonObject.class).get("text").getAsString())
                        .toArray(String[]::new);
            }

            return new SignText(frontText, backText);
        });
    }

    @Override
    public boolean unbreakable(ItemStack itemStack) {
        return ItemCast.typedDataComponent(itemStack, DataComponents.UNBREAKABLE).isPresent();
    }

}
