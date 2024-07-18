package me.noci.advancedtooltip.v1_20_5.utils;

import net.labymod.api.util.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;

public class PotionEffectUtils {

    public static String asString(MobEffectInstance effect) {
        String effectKey = effect.getEffect().getRegisteredName();
        String amplifier = Component.translatable("potion.potency." + effect.getAmplifier()).getString();

        if (effect.getAmplifier() > 0) {
            return I18n.translate("advancedtooltip.components.potion.effect.amplified", effectKey, effect.getDuration(), amplifier);
        }

        return I18n.translate("advancedtooltip.components.potion.effect.not_amplified", effectKey, effect.getDuration());
    }

}
