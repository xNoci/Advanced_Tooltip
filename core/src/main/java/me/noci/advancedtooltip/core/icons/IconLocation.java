package me.noci.advancedtooltip.core.icons;

import net.labymod.api.Laby;
import net.labymod.api.client.gfx.pipeline.texture.atlas.TextureAtlas;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;

public class IconLocation {

    public static final IconLocation FOOD_FULL = new IconLocation(8, 52, 27, "hud/food_full");
    public static final IconLocation FOOD_HALF = new IconLocation(8, 61, 27, "hud/food_half");
    public static final IconLocation SATURATION_FULL = new IconLocation(8, 70, 27, "hud/food_full_hunger");
    public static final IconLocation SATURATION_HALF = new IconLocation(8, 79, 27, "hud/food_half_hunger");
    public static final IconLocation ARMOR_FULL = new IconLocation(9, 34, 9, "hud/armor_full");
    public static final IconLocation ARMOR_HALF = new IconLocation(9, 25, 9, "hud/armor_half");

    private final ResourceLocation spriteLocation;
    private final int spriteSize;
    private final int x;
    private final int y;

    private IconLocation(int spriteSize, int x, int y, String resourceLocation) {
        this.spriteLocation = ResourceLocation.create("minecraft", resourceLocation);
        this.spriteSize = spriteSize;
        this.x = x;
        this.y = y;
    }

    private ResourceLocation resourceLocation() {
        return Laby.labyAPI().minecraft().textures().iconsTexture();
    }

    private TextureAtlas atlas() {
        return Laby.references().atlasRegistry().getAtlas(resourceLocation());
    }

    public Icon icon() {
        int protocolVersion = Laby.labyAPI().minecraft().getProtocolVersion();
        if(protocolVersion >= 764) { //>= 1.20.2
            return Icon.sprite(atlas(), atlas().findSprite(spriteLocation));
        }
        return Icon.sprite(resourceLocation(), x, y, spriteSize, spriteSize, 256, 256);
    }

}
