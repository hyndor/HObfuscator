package ru.hyndo.hobfuscator;

import org.objectweb.asm.ClassReader;
import ru.hyndo.hobfuscator.analyzed.Clazz;
import ru.hyndo.hobfuscator.analyzed.ClazzPool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

public class ClazzPoolInitializer {

    public ClazzPool getClazzPool(File file) {
        Map<String, Clazz> clazzSet = new HashMap<>();
        try {
            JarFile jarFile = new JarFile(file);
            Enumeration<JarEntry> enumOfJar = jarFile.entries();
            while (enumOfJar.hasMoreElements()) {
                JarEntry jarEntry = enumOfJar.nextElement();
                if(jarEntry.getName().endsWith(".class")) {
                    InputStream is = jarFile.getInputStream(jarEntry);
                    Clazz clazz = new ClazzInitializer().loadClass(new ClassReader(is));
                    clazzSet.put(clazz.getInternalName(), clazz);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ClazzPool(clazzSet);
    }

}
