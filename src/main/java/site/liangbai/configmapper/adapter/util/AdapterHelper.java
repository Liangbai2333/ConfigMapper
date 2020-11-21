package site.liangbai.configmapper.adapter.util;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import site.liangbai.configmapper.adapter.AbstractAdapter;
import site.liangbai.configmapper.adapter.impl.CommonAdapter;
import site.liangbai.configmapper.adapter.impl.InnerClassAdapter;
import site.liangbai.configmapper.adapter.impl.MapAdapter;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

public final class AdapterHelper {
    public static void autoMapToField(Plugin plugin, Object object, Field field, ConfigurationSection section) {
        boolean old = field.isAccessible();

        try {
            field.setAccessible(true);

            Field modifierField = Field.class.getDeclaredField("modifiers");
            modifierField.setAccessible(true);
            modifierField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

            AbstractAdapter adapter = getAdapter(field, section);

            adapter.applyAdapter(plugin, field.getName(), object, field, section);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            field.setAccessible(old);
        }
    }

    protected static AbstractAdapter getAdapter(Field field, ConfigurationSection section) {
        if (Map.class.isAssignableFrom(field.getType())) {
            return new MapAdapter();
        }

        return getAdapter(field.getName(), section);
    }

    protected static AbstractAdapter getAdapter(String name, ConfigurationSection section) {
        if (section.isConfigurationSection(name)) {
            return new InnerClassAdapter();
        }

        return new CommonAdapter();
    }
}
