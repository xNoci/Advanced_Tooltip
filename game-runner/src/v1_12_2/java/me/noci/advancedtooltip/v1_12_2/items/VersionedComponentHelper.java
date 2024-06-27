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
import org.jetbrains.annotations.Nullable;

import javax.inject.Singleton;
import java.util.List;

@Singleton
@Implements(ComponentHelper.class)
public class VersionedComponentHelper extends ComponentHelper.DefaultComponentHelper {

    @Override
    public @Nullable String displayItemData(ItemStack labyItemStack, boolean withArrayContent) {
        var itemStack = ItemCast.toMinecraftItemStack(labyItemStack);
        return NBTPrinter.prettyPrint(itemStack.getTagCompound(), withArrayContent);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public int repairCost(ItemStack labyItemStack) {
        var itemStack = ItemCast.toMinecraftItemStack(labyItemStack);
        NBTTagCompound compound = itemStack.getTagCompound();

        if (!itemStack.hasTagCompound() || !compound.hasKey("RepairCost", 3 /*TAG_INT*/)) return 0;
        return compound.getInteger("RepairCost");
    }

    @Override
    public @Nullable List<MapDecoration> mapDecorations(ItemStack labyItemStack) {
        var itemStack = ItemCast.toMinecraftItemStack(labyItemStack);
        NBTTagCompound tag = itemStack.getTagCompound();
        if (tag == null || !tag.hasKey("Decorations", 9 /*TAG_LIST*/)) return null;
        NBTTagList decorationsListTag = tag.getTagList("Decorations", 10 /*TAG_COMPOUND*/);
        if (decorationsListTag.isEmpty()) return null;

        List<MapDecoration> decorations = Lists.newArrayList();
        for (int i = 0; i < decorationsListTag.tagCount(); i++) {
            NBTTagCompound compound = decorationsListTag.getCompoundTagAt(i);

            var type = MapDecoration.Type.byType(compound.getByte("type"));
            var x = compound.getDouble("x");
            var y = compound.getDouble("y");
            decorations.add(new MapDecoration(type, x, y));
        }

        return decorations;
    }

    @Override
    public @Nullable String commandBlockCommand(ItemStack itemStack) {
        NBTTagCompound compound = blockEntityTag(itemStack);
        if (compound == null || !compound.hasKey("Command", 8 /*TAG_STRING*/)) return null;
        return compound.getString("Command");
    }

    @Override
    public @Nullable SignText signText(ItemStack itemStack) {
        NBTTagCompound compound = blockEntityTag(itemStack);
        if (compound == null) return null;

        String[] frontText = new String[4];

        for (int i = 1; i < 5; i++) {
            String tag = "Text" + i;
            if (!compound.hasKey(tag, 8 /*TAG_STRING*/)) continue;
            frontText[i - 1] = compound.getString(tag);
        }

        return new SignText(frontText, null);
    }

    @Override
    public boolean unbreakable(ItemStack labyItemStack) {
        NBTTagCompound compoundTag = ItemCast.toMinecraftItemStack(labyItemStack).getTagCompound();
        return compoundTag != null && compoundTag.hasKey("Unbreakable");
    }

    private @Nullable NBTTagCompound blockEntityTag(ItemStack labyItemStack) {
        var itemStack = ItemCast.toMinecraftItemStack(labyItemStack);
        NBTTagCompound compoundTag = itemStack.getTagCompound();
        if (compoundTag == null || !compoundTag.hasKey("BlockEntityTag", 10 /*TAG_COMPOUND*/)) return null;
        return compoundTag.getCompoundTag("BlockEntityTag");
    }

}
