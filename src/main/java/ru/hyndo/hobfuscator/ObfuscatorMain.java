package ru.hyndo.hobfuscator;


import ru.hyndo.hobfuscator.analyzed.ClazzPool;
import ru.hyndo.hobfuscator.feature.*;
import ru.hyndo.hobfuscator.rename.OneWordRenamingStrategy;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class ObfuscatorMain {

    public static void main(String[] args) throws IOException {
        Scanner scn = new Scanner(System.in);
        System.out.println("Write absolute path to file:");
        String fileName = scn.next();
        System.out.println("Write jar's main class");
        String mainClassName = scn.next();
        File sourceFile = new File(fileName);
        ClazzPool clazzPool = new ClazzPoolInitializer().getClazzPool(sourceFile);
        OneWordRenamingStrategy renamingStrategy = new OneWordRenamingStrategy("hobfuscator");
        ManyDelegatingFeature delegatingFeature = new ManyDelegatingFeature();
        delegatingFeature.addFeature(new GenerateBeanMethodsFeature(renamingStrategy));
        new RemapFieldsFeature(renamingStrategy).mapPool(clazzPool);
        new RemapMethodsFeature(renamingStrategy, Collections.emptySet()).mapPool(clazzPool);
        new RemapClassNameFeature(renamingStrategy, Collections.singletonList(mainClassName)).mapPool(clazzPool);
        new ChangeFieldModifiersFeature().mapPool(clazzPool);
        new ChangeMethodModifiersFeature().mapPool(clazzPool);
        new ClazzPoolWriter().writeClazzPool(clazzPool, sourceFile, renamingStrategy, delegatingFeature);

    }

}
