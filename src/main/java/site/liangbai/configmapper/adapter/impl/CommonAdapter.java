package site.liangbai.configmapper.adapter.impl;

import org.bukkit.configuration.ConfigurationSection;
import site.liangbai.configmapper.adapter.AbstractAdapter;

import java.lang.reflect.Field;

public class CommonAdapter extends AbstractAdapter {

    @Override
    public void applyAdapter(ClassLoader loader, String name, Object object, Field field, ConfigurationSection section) {
        try {
            field.set(object, section.get(name));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
