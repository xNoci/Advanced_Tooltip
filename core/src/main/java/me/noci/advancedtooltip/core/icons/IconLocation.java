package me.noci.advancedtooltip.core.icons;

import lombok.Getter;
import net.labymod.api.Laby;
import net.labymod.api.client.gfx.pipeline.texture.atlas.TextureAtlas;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.loader.MinecraftVersions;

public class IconLocation {

    public static final IconLocation FOOD_FULL = new IconLocation("hud/food_full", 8, 52, 27);
    public static final IconLocation FOOD_HALF = new IconLocation("hud/food_half", 8, 61, 27);
    public static final IconLocation SATURATION_FULL = new IconLocation("hud/food_full_hunger", 8, 70, 27);
    public static final IconLocation SATURATION_HALF = new IconLocation("hud/food_half_hunger", 8, 79, 27);
    public static final IconLocation ARMOR_FULL = new IconLocation("hud/armor_full", 9, 34, 9);
    public static final IconLocation ARMOR_HALF = new IconLocation("hud/armor_half", 9, 25, 9);

    @Getter
    private final Icon icon;

    private IconLocation(String spriteLocation, int spriteSize, int x, int y) {
        icon = findIcon(spriteLocation, x, y, spriteSize);
    }

    private Icon findIcon(String spriteLocation, int x, int y, int spriteSize) {
        ResourceLocation iconsTexture = Laby.labyAPI().minecraft().textures().iconsTexture();

        if (MinecraftVersions.V1_20_1.orOlder()) {
            return Icon.sprite(iconsTexture, x, y, spriteSize, spriteSize, 256, 256);
        }

        TextureAtlas atlas = Laby.references().atlasRegistry().getAtlas(iconsTexture);
        ResourceLocation spriteResourceLocation = ResourceLocation.create("minecraft", spriteLocation);
        return Icon.sprite(atlas, atlas.findSprite(spriteResourceLocation));
    }
}
