package me.noci.advancedtooltip.v1_12_2.items;

import com.google.common.collect.Lists;
import me.noci.advancedtooltip.core.referenceable.items.ComponentHelper;
import me.noci.advancedtooltip.core.utils.MapDecoration;
import me.noci.advancedtooltip.core.utils.SignText;
import me.noci.advancedtooltip.v1_12_2.utils.ItemCast;
import me.noci.advancedtooltip.v1_12_2.utils.NBTPrinter;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.models.Implements;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

@Singleton
@Implements(ComponentHelper.class)
public class VersionedComponentHelper extends ComponentHelper.DefaultComponentHelper {

    @Override
    public Optional<String> displayItemData(ItemStack labyItemStack, boolean withArrayContent) {
        var itemStack = ItemCast.toMinecraftItemStack(labyItemStack);
        return NBTPrinter.prettyPrint(itemStack.getTagCompound(), withArrayContent).describeConstable();
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public Optional<Integer> repairCost(ItemStack labyItemStack) {
        var itemStack = ItemCast.toMinecraftItemStack(labyItemStack);
        NBTTagCompound compound = itemStack.getTagCompound();

        if (!itemStack.hasTagCompound() || !compound.hasKey("RepairCost", 3 /*TAG_INT*/)) return Optional.empty();
        return Optional.of(compound.getInteger("RepairCost"));
    }

    @Override
    public Optional<List<MapDecoration>> mapDecorations(ItemStack labyItemStack) {
        var itemStack = ItemCast.toMinecraftItemStack(labyItemStack);
        NBTTagCompound tag = itemStack.getTagCompound();
        if (tag == null || !tag.hasKey("Decorations", 9 /*TAG_LIST*/)) return Optional.empty();
        NBTTagList decorationsListTag = tag.getTagList("Decorations", 10 /*TAG_COMPOUND*/);
        if (decorationsListTag.isEmpty()) return Optional.empty();

        List<MapDecoration> decorations = Lists.newArrayList();
        for (int i = 0; i < decorationsListTag.tagCount(); i++) {
            NBTTagCompound compound = decorationsListTag.getCompoundTagAt(i);

            var type = MapDecoration.Type.byType(compound.getByte("type"));
            var x = compound.getDouble("x");
            var y = compound.getDouble("y");
            decorations.add(new MapDecoration(type, x, y));
        }

        return Optional.of(decorations);
    }

    @Override
    public Optional<String> commandBlockCommand(ItemStack itemStack) {
        return blockEntityTag(itemStack)
                .filter(compoundTag -> compoundTag.hasKey("Command", 8 /*TAG_STRING*/))
                .map(compoundTag -> compoundTag.getString("Command"));
    }

    @Override
    public Optional<SignText> signText(ItemStack itemStack) {
        Optional<NBTTagCompound> optionalCompound = blockEntityTag(itemStack);
        if (optionalCompound.isEmpty()) return Optional.empty();
        NBTTagCompound blockEntityTag = optionalCompound.get();

        String[] frontText = new String[4];

        for (int i = 1; i < 5; i++) {
            String tag = "Text" + i;
            if (!blockEntityTag.hasKey(tag, 8 /*TAG_STRING*/)) continue;
            frontText[i - 1] = blockEntityTag.getString(tag);
        }

        return Optional.of(new SignText(frontText, null));
    }

    @Override
    public boolean unbreakable(ItemStack labyItemStack) {
        NBTTagCompound compoundTag = ItemCast.toMinecraftItemStack(labyItemStack).getTagCompound();
        return compoundTag != null && compoundTag.hasKey("Unbreakable");
    }

    private Optional<NBTTagCompound> blockEntityTag(ItemStack labyItemStack) {
        var itemStack = ItemCast.toMinecraftItemStack(labyItemStack);
        NBTTagCompound compoundTag = itemStack.getTagCompound();
        if (compoundTag == null || !compoundTag.hasKey("BlockEntityTag", 10 /*TAG_COMPOUND*/)) return Optional.empty();
        return Optional.of(compoundTag.getCompoundTag("BlockEntityTag"));
    }

}
