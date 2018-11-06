package ru.hyndo.hobfuscator;

import org.objectweb.asm.*;
import ru.hyndo.hobfuscator.analyzed.Clazz;
import ru.hyndo.hobfuscator.analyzed.ClazzField;
import ru.hyndo.hobfuscator.analyzed.ClazzMethod;
import ru.hyndo.hobfuscator.feature.ManyDelegatingFeature;
import ru.hyndo.hobfuscator.insn.FieldInsn;
import ru.hyndo.hobfuscator.rename.RenamingStrategy;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;
import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;

public class ClazzWriter implements Opcodes {

    private RenamingStrategy renamingStrategy;

    public byte[] writeClazz(Clazz clazz, ClassReader classReader, RenamingStrategy renamingStrategy, ManyDelegatingFeature delegatingFeature) {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(classReader);
        Objects.requireNonNull(classReader);
        Objects.requireNonNull(delegatingFeature);
        this.renamingStrategy = renamingStrategy;
        ClassWriter cw = new ClassWriter(COMPUTE_FRAMES | COMPUTE_MAXS);
        ModifierClassWriter mcw = new ModifierClassWriter(ASM5, cw, clazz, renamingStrategy);
        delegatingFeature.editClassWriter(cw);
        classReader.accept(mcw, 0);
        return cw.toByteArray();
    }

    public static class ModifierClassWriter extends ClassVisitor {

        private final Clazz clazz;
        private final RenamingStrategy renamingStrategy;

        public ModifierClassWriter(int api, ClassWriter cw, Clazz clazz, RenamingStrategy renamingStrategy) {
            super(api, cw);
            this.clazz = clazz;
            this.renamingStrategy = renamingStrategy;
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            super.visit(version, access, clazz.getRemappedName(), signature, superName, interfaces);
        }

        @Override
        public void visitSource(String source, String debug) {
            super.visitSource("DVE ZVEZDI XUINA", "DVE ZVEZDI XUINA");
        }

        @Override
        public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
            ClazzField field = clazz.getField(name);
            return super.visitField(field.getModifiers(), field.getRemappedName(), desc, signature, value);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            Type descType = Type.getMethodType(desc);
            ClazzMethod method = clazz.getMethod(new Clazz.MethodIdentifier(name, descType));
            MethodVisitor mv = super.visitMethod(method.getAccess(), method.getRemappedName(), desc, null, exceptions); // null signature - remove generics
            return new ModifierMethodWriter(api, mv, clazz, method, renamingStrategy);
        }

    }

    public static class ModifierMethodWriter extends MethodVisitor {

        private final Clazz clazz;
        private final ClazzMethod clazzMethod;
        private String methodName;
        private final RenamingStrategy renamingStrategy;
        private Set<Label> collectedLabels = new HashSet<>();

        public ModifierMethodWriter(int api, MethodVisitor mv, Clazz clazz, ClazzMethod clazzMethod, RenamingStrategy renamingStrategy) {
            super(api, mv);
            this.clazz = clazz;
            this.clazzMethod = clazzMethod;
            this.methodName = clazzMethod.getInternalName();
            this.renamingStrategy = renamingStrategy;
            collectedLabels.add(new Label());
        }

        @Override
        public void visitTypeInsn(int opcode, String type) {
            Clazz clazz = this.clazz.getClazzPool().getClazz(type);
            if(clazz != null) {
                type = clazz.getRemappedName();
            }
            super.visitTypeInsn(opcode, type);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
            Clazz clazzOwner = this.clazz.getClazzPool().getClazz(owner);
            if (clazzOwner != null) {
                owner = clazzOwner.getRemappedName();
                ClazzMethod method = clazzOwner.getMethod(new Clazz.MethodIdentifier(name, Type.getMethodType(desc)));
                if(method == null) {
                    System.out.println("Method " + name + " was not initialized in clazz " + this.clazz.getInternalName() + " in method " + methodName + " with real owner " + owner + " loaded methods " + clazzOwner.getMethods());
                } else {
                    if(method.getMethodAccessorsInfo() != null) {
                        FieldInsn fieldInsn = method.getMethodAccessorsInfo().getFieldInsn();
                        visitFieldInsn(fieldInsn.getOpcode(), fieldInsn.getOwner(), fieldInsn.getName(), fieldInsn.getDesc());
                        return;
                    }
                    name = method.getRemappedName();
                }
            }
            super.visitMethodInsn(opcode, owner, name, desc, itf);
        }

        @Override
        public void visitLabel(Label label) {
            collectedLabels.add(label);
        }

        @Override
        public void visitLineNumber(int line, Label start) {
            super.visitLineNumber(ThreadLocalRandom.current().nextInt(50000), new Label());
        }

        @Override
        public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
            super.visitLocalVariable(renamingStrategy.getName(null), desc, signature, start, end, index);
        }

        @Override
        public void visitFieldInsn(int opcode, String owner, String name, String desc) {
            Clazz clazz = this.clazz.getClazzPool().getClazz(owner);
            if (clazz != null) {
                ClazzField field = clazz.getField(name);
                if (field == null) {
                    System.out.println("Field " + name + " was not initialized in clazz " + this.clazz.getInternalName() + " in method " + methodName);
                } else {
                    name = field.getRemappedName();
                    Objects.requireNonNull(name);
                }
                owner = clazz.getRemappedName();
            }
            System.out.println("Visiting field insn " + name + " with owner " + owner + " in class " + this.clazz.getInternalName());
            super.visitFieldInsn(opcode, owner, name, desc);
        }
    }

}
