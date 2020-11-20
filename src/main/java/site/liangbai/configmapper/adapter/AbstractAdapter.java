package site.liangbai.configmapper.adapter;

import org.bukkit.configuration.ConfigurationSection;

import java.lang.reflect.Field;

public abstract class AbstractAdapter {
    public abstract void applyAdapter(ClassLoader loader, String name, Object object, Field field, ConfigurationSection section);
}
