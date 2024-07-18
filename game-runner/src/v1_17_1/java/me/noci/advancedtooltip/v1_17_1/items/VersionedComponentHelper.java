package me.noci.advancedtooltip.v1_17_1.items;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import me.noci.advancedtooltip.core.referenceable.items.ComponentHelper;
import me.noci.advancedtooltip.core.utils.MapDecoration;
import me.noci.advancedtooltip.core.utils.SignText;
import me.noci.advancedtooltip.v1_17_1.utils.ItemCast;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.models.Implements;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.Nullable;

import javax.inject.Singleton;
import java.util.List;

@Singleton
@Implements(ComponentHelper.class)
public class VersionedComponentHelper extends ComponentHelper.DefaultComponentHelper {

    private static final Gson GSON = new GsonBuilder().create();

    @Override
    public @Nullable String displayItemData(ItemStack labyItemStack, boolean withArrayContent) {
        var itemStack = ItemCast.toMinecraftItemStack(labyItemStack);
        if (itemStack.getTag() == null) return null;
        return NbtUtils.prettyPrint(itemStack.getTag(), withArrayContent);
    }

    @Override
    public int repairCost(ItemStack labyItemStack) {
        var itemStack = ItemCast.toMinecraftItemStack(labyItemStack);
        CompoundTag tag = itemStack.getTag();
        if (tag == null || !tag.contains("RepairCost", CompoundTag.TAG_INT)) return 0;
        return tag.getInt("RepairCost");
    }

    @Override
    public @Nullable List<MapDecoration> mapDecorations(ItemStack labyItemStack) {
        var itemStack = ItemCast.toMinecraftItemStack(labyItemStack);
        CompoundTag tag = itemStack.getTag();
        if (tag == null || !tag.contains("Decorations", CompoundTag.TAG_LIST)) return null;
        ListTag decorationsListTag = tag.getList("Decorations", ListTag.TAG_COMPOUND);
        if (decorationsListTag.isEmpty()) return null;

        List<MapDecoration> decorations = Lists.newArrayList();

        for (Tag listTag : decorationsListTag) {
            var compound = (CompoundTag) listTag;
            var type = MapDecoration.Type.byType(compound.getByte("type"));
            var x = compound.getDouble("x");
            var y = compound.getDouble("y");
            decorations.add(new MapDecoration(type, x, y));
        }

        return decorations;
    }

    @Override
    public @Nullable String commandBlockCommand(ItemStack itemStack) {
        CompoundTag tag = blockEntityTag(itemStack);
        if (tag == null || !tag.contains("Command", CompoundTag.TAG_STRING)) return null;
        return tag.getString("Command");
    }

    @Override
    public @Nullable SignText signText(ItemStack itemStack) {
        CompoundTag tag = blockEntityTag(itemStack);
        if (tag == null) return null;
        String[] frontText = new String[4];

        for (int i = 1; i < 5; i++) {
            String key = "Text" + i;
            if (!tag.contains(key, CompoundTag.TAG_STRING)) continue;
            frontText[i - 1] = GSON.fromJson(tag.getString(key), JsonObject.class).get("text").getAsString();
        }

        return new SignText(frontText, null);
    }

    @Override
    public boolean unbreakable(ItemStack labyItemStack) {
        CompoundTag compoundTag = ItemCast.toMinecraftItemStack(labyItemStack).getTag();
        return compoundTag != null && compoundTag.contains("Unbreakable");
    }

    private @Nullable CompoundTag blockEntityTag(ItemStack labyItemStack) {
        var itemStack = ItemCast.toMinecraftItemStack(labyItemStack);
        CompoundTag compoundTag = itemStack.getTag();
        if (compoundTag == null || !compoundTag.contains("BlockEntityTag", CompoundTag.TAG_COMPOUND)) {
            return null;
        }
        return compoundTag.getCompound("BlockEntityTag");
    }

}
