package ru.hyndo.hobfuscator.feature;

import ru.hyndo.hobfuscator.analyzed.ClazzPool;

import java.lang.reflect.Modifier;

import static java.lang.reflect.Modifier.STATIC;
import static ru.hyndo.hobfuscator.OpenModifiers.SYNTHETIC;

public class ChangeFieldModifiersFeature extends AbstractFeature {

    public ChangeFieldModifiersFeature() {
        super("change-field-modifiers");
    }

    @Override
    public void mapPool(ClazzPool pool) {
        pool.getClasses().stream().flatMap(clazz -> clazz.getFields().stream())
                .forEach(clazzField -> {
                    if(Modifier.isStatic(clazzField.getModifiers())) {
                        clazzField.setModifiers(Modifier.PUBLIC | SYNTHETIC | STATIC);
                    } else {
                        clazzField.setModifiers(Modifier.PUBLIC | SYNTHETIC);
                    }
                });
    }
}
