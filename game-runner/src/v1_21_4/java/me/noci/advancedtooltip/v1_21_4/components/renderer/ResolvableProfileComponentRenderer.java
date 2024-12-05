package me.noci.advancedtooltip.v1_21_4.components.renderer;

import com.mojang.authlib.properties.Property;
import me.noci.advancedtooltip.core.component.ComponentPrinter;
import me.noci.advancedtooltip.core.component.ComponentRenderer;
import net.minecraft.world.item.component.ResolvableProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResolvableProfileComponentRenderer implements ComponentRenderer<ResolvableProfile> {
    @Override
    public ComponentPrinter parse(ResolvableProfile resolvableProfile) {
        List<ComponentPrinter> components = new ArrayList<>();

        resolvableProfile.name().ifPresent(name -> components.add(ComponentPrinter.value("fullName", name)));
        resolvableProfile.id().ifPresent(uuid -> components.add(ComponentPrinter.value("uuid", uuid.toString())));

        List<ComponentPrinter> properties = new ArrayList<>();
        for (Map.Entry<String, Property> entry : resolvableProfile.properties().entries()) {
            List<ComponentPrinter> propertyValues = new ArrayList<>();
            Property property = entry.getValue();

            propertyValues.add(ComponentPrinter.value("fullName", property.name()));
            propertyValues.add(ComponentPrinter.value("value", property.value()));
            if (property.hasSignature()) {
                propertyValues.add(ComponentPrinter.value("signature", property.signature()));
            }

            properties.add(ComponentPrinter.list(entry.getKey(), propertyValues).handler(ComponentPrinter::print));
        }

        components.add(ComponentPrinter.list("properties", properties).handler(ComponentPrinter::print));

        return ComponentPrinter.object(components);
    }
}
