package me.noci.advancedtooltip.v1_8_9.items;

import me.noci.advancedtooltip.core.referenceable.items.ComponentHelper;
import me.noci.advancedtooltip.core.utils.SignText;
import me.noci.advancedtooltip.v1_8_9.utils.ItemCast;
import me.noci.advancedtooltip.v1_8_9.utils.NBTPrinter;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.models.Implements;
import net.minecraft.nbt.NBTTagCompound;
import org.jetbrains.annotations.Nullable;

import javax.inject.Singleton;

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
    public String commandBlockCommand(ItemStack itemStack) {
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
