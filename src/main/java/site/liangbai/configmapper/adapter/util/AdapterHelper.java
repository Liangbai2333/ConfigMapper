package site.liangbai.configmapper.adapter.util;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import site.liangbai.configmapper.adapter.AbstractAdapter;
import site.liangbai.configmapper.adapter.impl.CommonAdapter;
import site.liangbai.configmapper.adapter.impl.InnerClassAdapter;
import site.liangbai.configmapper.adapter.impl.MapAdapter;
import site.liangbai.configmapper.annotation.ConfigOption;

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

            String name = field.getName();
            ConfigurationSection configurationSection = section;

            ConfigOption configOption = field.getAnnotation(ConfigOption.class);

            if (configOption != null) {
                name = configOption.value();

                configurationSection = getConfigurationSection(configOption.section(), section);
            }

            AbstractAdapter adapter = getAdapter(field, configurationSection);

            adapter.applyAdapter(plugin, name, object, field, configurationSection);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            field.setAccessible(old);
        }
    }

    protected static ConfigurationSection getConfigurationSection(String s, ConfigurationSection section) {
        if (s == null || s.equals("")) return section;

        return section.contains(s) ? (section.isConfigurationSection(s) ? section.getConfigurationSection(s) : section) : section;
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
