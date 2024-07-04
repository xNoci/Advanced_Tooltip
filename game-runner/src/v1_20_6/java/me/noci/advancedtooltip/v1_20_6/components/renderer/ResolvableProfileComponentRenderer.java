package me.noci.advancedtooltip.v1_20_6.components.renderer;

import com.google.common.collect.Lists;
import com.mojang.authlib.properties.Property;
import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.world.item.component.ResolvableProfile;

import java.util.List;

public class ResolvableProfileComponentRenderer implements ComponentRenderer<ResolvableProfile> {
    @Override
    public ComponentPrinter parse(ResolvableProfile resolvableProfile) {
        var nameComponent = resolvableProfile.name().map(name -> ComponentPrinter.value("fullName", name));
        var idComponent = resolvableProfile.id().map(uuid -> ComponentPrinter.value("uuid", uuid.toString()));
        var propertiesComponent = resolvableProfile.properties().entries().stream().map(entry -> {
            String key = entry.getKey();
            Property property = entry.getValue();
            List<ComponentPrinter> components = Lists.newArrayList();
            components.add(ComponentPrinter.value("fullName", property.name()));
            components.add(ComponentPrinter.value("value", property.value()));
            if (property.hasSignature()) {
                components.add(ComponentPrinter.value("signature", property.signature()));
            }

            return ComponentPrinter.list(key, components).handler(ComponentPrinter::print);
        }).toList();

        List<ComponentPrinter> components = Lists.newArrayList();
        nameComponent.ifPresent(components::add);
        idComponent.ifPresent(components::add);
        components.add(ComponentPrinter.list("properties", propertiesComponent).handler(ComponentPrinter::print));

        return ComponentPrinter.object(components);
    }
}
