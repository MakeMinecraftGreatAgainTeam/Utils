package org.mmga.utils.interfaces;

import org.bukkit.plugin.Plugin;

/**
 * @author wzp
 * @version 1.0
 * @since 2023/6/2 23:08:54
 */
public interface MmgaClassTypeHandler{
    Class<?> getType();
    void handler(Plugin plugin, Class<?> aClass);
}
