package me.noci.advancedtooltip.v1_20_2.items;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import me.noci.advancedtooltip.core.referenceable.items.ComponentHelper;
import me.noci.advancedtooltip.core.utils.MapDecoration;
import me.noci.advancedtooltip.core.utils.SignText;
import me.noci.advancedtooltip.v1_20_2.utils.ItemCast;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.models.Implements;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;

import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

@Singleton
@Implements(ComponentHelper.class)
public class VersionedComponentHelper extends ComponentHelper.DefaultComponentHelper {

    private static final Gson GSON = new GsonBuilder().create();

    @Override
    public Optional<String> displayItemData(ItemStack labyItemStack, boolean withArrayContent) {
        var itemStack = ItemCast.toMinecraftItemStack(labyItemStack);
        if (itemStack.getTag() == null) return Optional.empty();
        return NbtUtils.prettyPrint(itemStack.getTag(), withArrayContent).describeConstable();
    }

    @Override
    public Optional<Integer> repairCost(ItemStack labyItemStack) {
        var itemStack = ItemCast.toMinecraftItemStack(labyItemStack);
        CompoundTag tag = itemStack.getTag();
        if (tag == null || !tag.contains("RepairCost", CompoundTag.TAG_INT)) return Optional.empty();
        return Optional.of(tag.getInt("RepairCost"));
    }

    @Override
    public Optional<List<MapDecoration>> mapDecorations(ItemStack labyItemStack) {
        var itemStack = ItemCast.toMinecraftItemStack(labyItemStack);
        CompoundTag tag = itemStack.getTag();
        if (tag == null || !tag.contains("Decorations", CompoundTag.TAG_LIST)) return Optional.empty();
        ListTag decorationsListTag = tag.getList("Decorations", ListTag.TAG_COMPOUND);
        if (decorationsListTag.isEmpty()) return Optional.empty();

        var decorations = decorationsListTag.stream().map(t -> {
            var compound = (CompoundTag) t;
            var type = MapDecoration.Type.byType(compound.getByte("type"));
            var x = compound.getDouble("x");
            var y = compound.getDouble("y");
            return new MapDecoration(type, x, y);
        }).toList();

        return Optional.of(decorations);
    }

    @Override
    public Optional<String> commandBlockCommand(ItemStack itemStack) {
        return blockEntityTag(itemStack)
                .filter(compoundTag -> compoundTag.contains("Command", CompoundTag.TAG_STRING))
                .map(compoundTag -> compoundTag.getString("Command"));
    }

    @Override
    public Optional<SignText> signText(ItemStack itemStack) {
        Optional<CompoundTag> optionalCompound = blockEntityTag(itemStack);
        if (optionalCompound.isEmpty()) return Optional.empty();
        CompoundTag blockEntityTag = optionalCompound.get();

        String[] frontText = null;
        String[] backText = null;

        if (blockEntityTag.contains("front_text")) {
            CompoundTag frontTextCompound = blockEntityTag.getCompound("front_text");
            ListTag lines = frontTextCompound.getList("messages", ListTag.TAG_STRING);
            frontText = lines.stream()
                    .map(tag -> GSON.fromJson(tag.getAsString(), JsonObject.class).get("text").getAsString())
                    .toArray(String[]::new);
        }

        if (blockEntityTag.contains("back_text")) {
            CompoundTag frontTextCompound = blockEntityTag.getCompound("back_text");
            ListTag lines = frontTextCompound.getList("messages", ListTag.TAG_STRING);
            backText = lines.stream()
                    .map(tag -> GSON.fromJson(tag.getAsString(), JsonObject.class).get("text").getAsString())
                    .toArray(String[]::new);
        }

        return Optional.of(new SignText(frontText, backText));
    }

    @Override
    public boolean unbreakable(ItemStack labyItemStack) {
        CompoundTag compoundTag = ItemCast.toMinecraftItemStack(labyItemStack).getTag();
        return compoundTag != null && compoundTag.contains("Unbreakable");
    }

    private Optional<CompoundTag> blockEntityTag(ItemStack labyItemStack) {
        var itemStack = ItemCast.toMinecraftItemStack(labyItemStack);
        CompoundTag compoundTag = itemStack.getTag();
        if (compoundTag == null || !compoundTag.contains("BlockEntityTag", CompoundTag.TAG_COMPOUND)) {
            return Optional.empty();
        }
        return Optional.of(compoundTag.getCompound("BlockEntityTag"));
    }

}
