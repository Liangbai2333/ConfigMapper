package site.liangbai.configmapper.adapter.util;

import org.bukkit.configuration.ConfigurationSection;
import site.liangbai.configmapper.adapter.AbstractAdapter;
import site.liangbai.configmapper.adapter.impl.CommonAdapter;
import site.liangbai.configmapper.adapter.impl.InnerClassAdapter;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class AdapterHelper {
    public static void autoMapToField(ClassLoader loader, Object object, Field field, ConfigurationSection section) {
        boolean old = field.isAccessible();

        try {
            field.setAccessible(true);

            Field modifierField = Field.class.getDeclaredField("modifiers");
            modifierField.setAccessible(true);
            modifierField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

            AbstractAdapter adapter = getAdapter(field.getName(), section);

            adapter.applyAdapter(loader, field.getName(), object, field, section);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            field.setAccessible(old);
        }
    }

    protected static AbstractAdapter getAdapter(String name, ConfigurationSection section) {
        if (section.isConfigurationSection(name)) {
            return new InnerClassAdapter();
        }

        return new CommonAdapter();
    }
}
