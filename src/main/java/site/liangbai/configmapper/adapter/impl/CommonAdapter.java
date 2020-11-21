package site.liangbai.configmapper.adapter.impl;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import site.liangbai.configmapper.adapter.AbstractAdapter;

import java.lang.reflect.Field;

public final class CommonAdapter extends AbstractAdapter {
    @Override
    public void applyAdapter(Plugin plugin, String name, Object object, Field field, ConfigurationSection section) {
        try {
            field.set(object, section.get(name));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
