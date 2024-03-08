package me.noci.advancedtooltip.v1_8_9.items;

import me.noci.advancedtooltip.core.referenceable.items.ComponentHelper;
import me.noci.advancedtooltip.core.utils.SignText;
import me.noci.advancedtooltip.v1_8_9.utils.ItemCast;
import me.noci.advancedtooltip.v1_8_9.utils.NBTPrinter;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.models.Implements;
import net.minecraft.nbt.NBTTagCompound;

import javax.inject.Singleton;
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
