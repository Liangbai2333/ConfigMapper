package site.liangbai.configmapper.adapter.impl;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import site.liangbai.configmapper.adapter.AbstractAdapter;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public final class MapAdapter extends AbstractAdapter {
    @Override
    public void applyAdapter(Plugin plugin, String name, Object object, Field field, ConfigurationSection section) {
        try {
            Map<Object, Object> map = new HashMap<>();
            field.set(object, map);

            if (section.isConfigurationSection(name)) {
                map.put(name, configurationMapToMap(section.getConfigurationSection(name)));
            } else {
                map.put(name, section.get(name));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Map<?, ?> configurationMapToMap(ConfigurationSection section) {
        Map<String, Object> map = new HashMap<>();

        section.getKeys(false).forEach(it -> {
            if (section.isConfigurationSection(it)) {
                map.put(it, configurationMapToMap(section.getConfigurationSection(it)));
            } else {
                map.put(it, section.get(it));
            }
        });

        return map;
    }
}
