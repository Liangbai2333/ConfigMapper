package site.liangbai.configmapper;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import site.liangbai.configmapper.adapter.impl.MapAdapter;
import site.liangbai.configmapper.adapter.util.AdapterHelper;
import site.liangbai.configmapper.reflect.ReflectSecurityManager;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.logging.Level;

/**
 * 配置文件映射工具的映射帮助类.
 * You can use {@link ConfigMapper#mapToBean(Class, String)} to map your yaml file.
 * You can use {@link ConfigMapper#mapToBean(Class, ConfigurationSection)} to map your configuration section.
 * @author Liangbai
 */
public final class ConfigMapper {
    private static final ReflectSecurityManager reflectSecurityManager = new ReflectSecurityManager();

    public static <T> T mapToBean(Class<T> type, String fileName) {
        Class<?> stack = reflectSecurityManager.getClassContext()[1];
        ClassLoader classLoader = stack.getClassLoader();

        Plugin plugin = getPluginByClassLoader(classLoader);

        if (plugin == null) return null;

        File file = new File(fileName);

        if (file.exists()) return mapToBean(plugin, type, YamlConfiguration.loadConfiguration(file));

        FileConfiguration configuration = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), fileName));

        return mapToBean(plugin, type, configuration);
    }

    public static <T> T mapToBean(Class<T> type, ConfigurationSection section) {
        Class<?> stack = reflectSecurityManager.getClassContext()[1];
        ClassLoader classLoader = stack.getClassLoader();

        return mapToBean(classLoader, type, section);
    }

    public static <T> T mapToBean(ClassLoader classLoader, Class<T> type, ConfigurationSection section) {
        Plugin plugin = getPluginByClassLoader(classLoader);

        if (plugin == null) return null;

        return mapToBean(plugin, type, section);
    }

    @SuppressWarnings("unchecked")
    public static <T> T mapToBean(Plugin plugin, Class<T> type, ConfigurationSection section) {
        if (Map.class.isAssignableFrom(type)) {
            return (T) MapAdapter.configurationMapToMap(section);
        }

        Constructor<T> constructor = null;
        boolean old = false;

        try {
            constructor = type.getConstructor();

            old = constructor.isAccessible();

            constructor.setAccessible(true);

            T object = constructor.newInstance();

            for (Field declaredField : object.getClass().getDeclaredFields()) {
                if (!section.contains(declaredField.getName())) continue;

                AdapterHelper.autoMapToField(plugin, object, declaredField, section);
            }

            return object;
        } catch (NoSuchMethodException e) {
            Bukkit.getLogger().log(Level.SEVERE, "can not find a empty constructor for java bean", e);
            return null;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (constructor != null) {
                constructor.setAccessible(old);
            }
        }
    }

    protected static Plugin getPluginByClassLoader(ClassLoader classLoader) {
        Field pluginField = null;

        boolean old = false;

        try {
            pluginField = classLoader.getClass().getDeclaredField("plugin");

            old = pluginField.isAccessible();

            pluginField.setAccessible(true);

            return (Plugin) pluginField.get(classLoader);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            Bukkit.getLogger().log(Level.SEVERE, "can not pass pluginField", e);
            return null;
        } finally {
            if (pluginField != null) {
                pluginField.setAccessible(old);
            }
        }
    }
}
