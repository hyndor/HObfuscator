package ru.hyndo.hobfuscator;

import ru.hyndo.hobfuscator.analyzed.Clazz;
import ru.hyndo.hobfuscator.analyzed.ClazzPool;
import ru.hyndo.hobfuscator.feature.ManyDelegatingFeature;
import ru.hyndo.hobfuscator.rename.RenamingStrategy;

import java.io.*;
import java.util.Arrays;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class ClazzPoolWriter {

    public void writeClazzPool(ClazzPool clazzPool, File sourceFile,
                               RenamingStrategy renamingStrategy, ManyDelegatingFeature delegatingFeature) {
        try {
            File outputJar = new File("test.jar");
            if (outputJar.exists()) {
                outputJar.delete();
            }
            JarFile jarFile = new JarFile(sourceFile);
            Manifest manifest = (Manifest) jarFile.getManifest().clone();
            outputJar.createNewFile();
            JarOutputStream jos = new JarOutputStream(new FileOutputStream(outputJar), manifest);
            for (Clazz clazz : clazzPool.getClasses()) {
                byte[] bytes = new ClazzWriter().writeClazz(clazz, clazz.inputStreamSupplier().get(), renamingStrategy, delegatingFeature);
                String[] split = clazz.getRemappedName().split("/");
                String packageName = String.join("/", Arrays.copyOf(split, split.length - 1));
                File output = new File(packageName);
                output.mkdirs();
                String rawClassName = split[split.length - 1];
                File outClazz = new File(output, rawClassName + ".class");
                if (outClazz.exists()) {
                    outClazz.delete();
                }
                DataOutputStream dout = new DataOutputStream(new FileOutputStream(outClazz));
                dout.write(bytes);
                add(outClazz, jos);
            }
            jos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void add(File source, JarOutputStream target) throws IOException {
        BufferedInputStream in = null;
        //noinspection TryFinallyCanBeTryWithResources
        try {
            if (source.isDirectory()) {
                String name = source.getPath().replace("\\", "/");
                if (!name.isEmpty()) {
                    if (!name.endsWith("/"))
                        name += "/";
                    JarEntry entry = new JarEntry(name);
                    entry.setTime(source.lastModified());
                    target.putNextEntry(entry);
                    target.closeEntry();
                }
                //noinspection ConstantConditions
                for (File nestedFile : source.listFiles())
                    add(nestedFile, target);
                return;
            }
            JarEntry entry = new JarEntry(source.getPath().replace("\\", "/"));
            entry.setTime(source.lastModified());
            target.putNextEntry(entry);
            in = new BufferedInputStream(new FileInputStream(source));

            byte[] buffer = new byte[1024];
            while (true) {
                int count = in.read(buffer);
                if (count == -1)
                    break;
                target.write(buffer, 0, count);
            }
            target.closeEntry();
        } finally {
            if (in != null)
                in.close();
        }
    }

}
