package ru.hyndo.hobfuscator;


import ru.hyndo.hobfuscator.analyzed.ClazzPool;
import ru.hyndo.hobfuscator.feature.*;
import ru.hyndo.hobfuscator.rename.OneWordRenamingStrategy;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;

public class ObfuscatorMain {

    public static void main(String[] args) throws IOException {
        File sourceFile = new File("testproject-1.0-SNAPSHOT.jar");
        ClazzPool clazzPool = new ClazzPoolInitializer().getClazzPool(sourceFile);
        OneWordRenamingStrategy renamingStrategy = new OneWordRenamingStrategy("aregpidor");
        ManyDelegatingFeature delegatingFeature = new ManyDelegatingFeature();
        delegatingFeature.addFeature(new GenerateBeanMethodsFeature(renamingStrategy));
        new RemapFieldsFeature(renamingStrategy).mapPool(clazzPool);
        new RemapMethodsFeature(renamingStrategy, Collections.emptySet()).mapPool(clazzPool);
        new RemapClassNameFeature(renamingStrategy, Arrays.asList("TestMain")).mapPool(clazzPool);
        new ChangeFieldModifiersFeature().mapPool(clazzPool);
        new ChangeMethodModifiersFeature().mapPool(clazzPool);
        new ClazzPoolWriter().writeClazzPool(clazzPool, sourceFile, renamingStrategy, delegatingFeature);

    }

}
