package ru.hyndo.hobfuscator.feature;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import ru.hyndo.hobfuscator.analyzed.ClazzPool;
import ru.hyndo.hobfuscator.rename.RenamingStrategy;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static ru.hyndo.hobfuscator.OpenModifiers.*;

public class GenerateBeanMethodsFeature extends AbstractFeature implements Opcodes {

    private static final List<String> exceptions = Arrays.asList(
            FileNotFoundException.class.getName(),
            java.io.CharConversionException.class.getName(),
            java.io.NotActiveException.class.getName(),
            java.io.InvalidClassException.class.getName(),
            java.io.InvalidObjectException.class.getName(),
            java.io.NotSerializableException.class.getName(),
            java.io.OptionalDataException.class.getName(),
            java.io.ObjectStreamException.class.getName()
    );

    private RenamingStrategy renamingStrategy;

    public GenerateBeanMethodsFeature(RenamingStrategy renamingStrategy) {
        super("generate-bean-methods");
        this.renamingStrategy = renamingStrategy;
    }

    @Override
    public ClassWriter editClassWriter(ClassWriter cw) {
        for (int i = 0; i < 5; i++) {
            MethodVisitor  mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC,
                    renamingStrategy.getName(null), "(IIIIIIIIIIIIIIII)V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ILOAD, ThreadLocalRandom.current().nextInt(15));
            mv.visitVarInsn(ILOAD, ThreadLocalRandom.current().nextInt(15));
            mv.visitInsn(IAND);
            mv.visitVarInsn(ILOAD, ThreadLocalRandom.current().nextInt(15));
            mv.visitInsn(IXOR);
            mv.visitVarInsn(ILOAD, ThreadLocalRandom.current().nextInt(15));
            mv.visitVarInsn(ILOAD, ThreadLocalRandom.current().nextInt(15));
            mv.visitInsn(ICONST_M1);
            mv.visitInsn(IXOR);
            mv.visitInsn(IAND);
            mv.visitInsn(IOR);
            mv.visitInsn(RETURN);
            mv.visitMaxs(40, 40);
            mv.visitEnd();
        }
        return cw;
    }

    @Override
    public void mapPool(ClazzPool pool) {
    }
}
