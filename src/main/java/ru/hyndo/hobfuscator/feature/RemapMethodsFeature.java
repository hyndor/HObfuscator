package ru.hyndo.hobfuscator.feature;

import ru.hyndo.hobfuscator.analyzed.ClazzPool;
import ru.hyndo.hobfuscator.rename.RenamingStrategy;

import java.util.HashSet;
import java.util.Set;

public class RemapMethodsFeature extends AbstractFeature {

    private RenamingStrategy renamingStrategy;
    private Set<String> exclude;

    public RemapMethodsFeature(RenamingStrategy renamingStrategy, Set<String> exclude) {
        super("remap-methods");
        this.renamingStrategy = renamingStrategy;
        this.exclude = new HashSet<>(exclude);
        this.exclude.add("main");
        this.exclude.add("<init>");
    }

    @Override
    public void mapPool(ClazzPool pool) {
        pool.getClasses()
                .stream()
                .flatMap(clazz -> clazz.getMethods().stream())
                .filter(clazzMethod -> !exclude.contains(clazzMethod.getInternalName()))
                .forEach(clazzMethod -> clazzMethod.setRemappedName(renamingStrategy.getName(clazzMethod)));
    }

}
