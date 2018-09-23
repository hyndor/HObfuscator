package ru.hyndo.hobfuscator.feature;

import ru.hyndo.hobfuscator.analyzed.ClazzPool;
import ru.hyndo.hobfuscator.rename.RenamingStrategy;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

public class RemapClassNameFeature extends AbstractFeature {

    private RenamingStrategy renamingStrategy;
    private Collection<String> exclude;

    public RemapClassNameFeature(RenamingStrategy renamingStrategy, Collection<String> exclude) {
        super("remap-classname-feature");
        this.renamingStrategy = renamingStrategy;
        this.exclude = exclude;
    }

    @Override
    public void mapPool(ClazzPool pool) {
        pool.getClasses()
                .stream()
                .filter(clazz -> {
                    String internalName = clazz.getInternalName();
                    String[] split = internalName.split("/");
                    String realName = split[split.length - 1];
                    return !exclude.contains(realName);
                })
                .forEach(clazz -> {
            String newName = renamingStrategy.getName(clazz);
            String[] split = clazz.getInternalName().split("/");
            String packageName = String.join("/", Arrays.copyOf(split, split.length - 1));
            clazz.setRemappedName(packageName + "/" + newName);
            System.out.println(clazz.getRemappedName());
        });
    }
}
