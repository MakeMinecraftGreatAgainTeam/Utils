package org.mmga.utils.utils.jar;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

/**
 * @author wzp
 * @version 1.0
 * @since 2023/6/2 23:14:59
 */
public class JarUtils {
    public static JarFile getJarFileFromClassLoader(URLClassLoader classLoader){
        URL url = classLoader.getURLs()[0];
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(new File(url.getFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jarFile;
    }
}
