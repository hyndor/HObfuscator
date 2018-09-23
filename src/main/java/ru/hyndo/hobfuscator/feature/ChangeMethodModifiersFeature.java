package ru.hyndo.hobfuscator.feature;

import ru.hyndo.hobfuscator.analyzed.ClazzPool;

import java.lang.reflect.Modifier;

import static java.lang.reflect.Modifier.STATIC;
import static ru.hyndo.hobfuscator.OpenModifiers.SYNTHETIC;

public class ChangeMethodModifiersFeature extends AbstractFeature {
    public ChangeMethodModifiersFeature() {
        super("change-method-modifiers");
    }

    @Override
    public void mapPool(ClazzPool pool) {
        pool.getClasses().stream().flatMap(clazz -> clazz.getMethods().stream())
                .forEach(clazzMethod -> {
                    if(Modifier.isStatic(clazzMethod.getAccess())) {
                        clazzMethod.setAccess(Modifier.PUBLIC | SYNTHETIC | STATIC);
                    } else {
                        clazzMethod.setAccess(Modifier.PUBLIC | SYNTHETIC);
                    }
                });
    }
}
