package ru.hyndo.hobfuscator.feature;

import org.objectweb.asm.ClassWriter;
import ru.hyndo.hobfuscator.analyzed.ClazzPool;

public interface Feature {

    String getName();

    void mapPool(ClazzPool pool);

    default ClassWriter editClassWriter(ClassWriter cw) {
        return cw;
    }

}
