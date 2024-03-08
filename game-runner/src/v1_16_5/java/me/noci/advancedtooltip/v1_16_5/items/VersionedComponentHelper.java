package me.noci.advancedtooltip.v1_16_5.items;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import me.noci.advancedtooltip.core.referenceable.items.ComponentHelper;
import me.noci.advancedtooltip.core.utils.MapDecoration;
import me.noci.advancedtooltip.core.utils.SignText;
import me.noci.advancedtooltip.v1_16_5.utils.ItemCast;
import me.noci.advancedtooltip.v1_16_5.utils.NBTPrinter;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.models.Implements;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

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
        return NBTPrinter.prettyPrint(itemStack.getTag(), withArrayContent).describeConstable();
    }

    @Override
    public Optional<Integer> repairCost(ItemStack labyItemStack) {
        var itemStack = ItemCast.toMinecraftItemStack(labyItemStack);
        CompoundTag tag = itemStack.getTag();
        if (tag == null || !tag.contains("RepairCost", 3 /*TAG_INT*/)) return Optional.empty();
        return Optional.of(tag.getInt("RepairCost"));
    }

    @Override
    public Optional<List<MapDecoration>> mapDecorations(ItemStack labyItemStack) {
        var itemStack = ItemCast.toMinecraftItemStack(labyItemStack);
        CompoundTag tag = itemStack.getTag();
        if (tag == null || !tag.contains("Decorations", 9 /*TAG_LIST*/)) return Optional.empty();
        ListTag decorationsListTag = tag.getList("Decorations", 10 /*TAG_COMPOUND*/);
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
                .filter(compoundTag -> compoundTag.contains("Command", 8 /*TAG_STRING*/))
                .map(compoundTag -> compoundTag.getString("Command"));
    }

    @Override
    public Optional<SignText> signText(ItemStack itemStack) {
        Optional<CompoundTag> optionalCompound = blockEntityTag(itemStack);
        if (optionalCompound.isEmpty()) return Optional.empty();
        CompoundTag blockEntityTag = optionalCompound.get();

        String[] frontText = new String[4];

        for (int i = 1; i < 5; i++) {
            String tag = "Text" + i;
            if (!blockEntityTag.contains(tag, 8 /*TAG_STRING*/)) continue;
            frontText[i - 1] = GSON.fromJson(blockEntityTag.getString(tag), JsonObject.class).get("text").getAsString();
        }

        return Optional.of(new SignText(frontText, null));
    }

    @Override
    public boolean unbreakable(ItemStack labyItemStack) {
        CompoundTag compoundTag = ItemCast.toMinecraftItemStack(labyItemStack).getTag();
        return compoundTag != null && compoundTag.contains("Unbreakable");
    }

    private Optional<CompoundTag> blockEntityTag(ItemStack labyItemStack) {
        var itemStack = ItemCast.toMinecraftItemStack(labyItemStack);
        CompoundTag compoundTag = itemStack.getTag();
        if (compoundTag == null || !compoundTag.contains("BlockEntityTag", 10 /*TAG_COMPOUND*/))
            return Optional.empty();
        return Optional.of(compoundTag.getCompound("BlockEntityTag"));
    }

}
