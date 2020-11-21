package site.liangbai.configmapper.adapter;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;

public abstract class AbstractAdapter {
    public abstract void applyAdapter(Plugin plugin, String name, Object object, Field field, ConfigurationSection section);
}
