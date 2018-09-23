package ru.hyndo.hobfuscator.feature;

import ru.hyndo.hobfuscator.analyzed.ClazzPool;
import ru.hyndo.hobfuscator.rename.RenamingStrategy;

public class RemapFieldsFeature extends AbstractFeature {

    private RenamingStrategy renamingStrategy;

    public RemapFieldsFeature(RenamingStrategy renamingStrategy) {
        super("field-remapper");
        this.renamingStrategy = renamingStrategy;
    }

    @Override
    public void mapPool(ClazzPool pool) {
        pool.getClasses()
                .stream()
                .flatMap(clazz -> clazz.getFields().stream())
                .forEach(clazzField -> clazzField.setRemappedName(renamingStrategy.getName(clazzField)));
    }
}
