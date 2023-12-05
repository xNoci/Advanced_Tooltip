package me.noci.advancedtooltip.core.icons;

import net.labymod.api.Laby;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;

public class IconLocation {

    public static final IconLocation FOOD_FULL = new IconLocation(8, 52, 27, "hud/food_full");
    public static final IconLocation FOOD_HALF = new IconLocation(8, 61, 27, "hud/food_half");
    public static final IconLocation SATURATION_FULL = new IconLocation(8, 70, 27, "hud/food_full_hunger");
    public static final IconLocation SATURATION_HALF = new IconLocation(8, 79, 27, "hud/food_half_hunger");
    public static final IconLocation ARMOR_FULL = new IconLocation(9, 34, 9, "hud/armor_full");
    public static final IconLocation ARMOR_HALF = new IconLocation(9, 25, 9, "hud/armor_half");

    private final Icon icon;

    private IconLocation(int spriteSize, int x, int y, String spriteLocation) {
        icon = queryIcon(spriteLocation, x, y, spriteSize);
    }

    private Icon queryIcon(String spriteLocation, int x, int y, int spriteSize) {
        int protocolVersion = Laby.labyAPI().minecraft().getProtocolVersion();
        var iconsTexture = Laby.labyAPI().minecraft().textures().iconsTexture();

        if (protocolVersion >= 764) { //>= 1.20.2
            var atlas = Laby.references().atlasRegistry().getAtlas(iconsTexture);
            var spriteResourceLocation = ResourceLocation.create("minecraft", spriteLocation);

            return Icon.sprite(atlas, atlas.findSprite(spriteResourceLocation));
        }

        return Icon.sprite(iconsTexture, x, y, spriteSize, spriteSize, 256, 256);
    }

    public Icon icon() {
        return icon;
    }
}
