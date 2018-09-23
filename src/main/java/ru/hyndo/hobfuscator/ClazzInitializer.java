package ru.hyndo.hobfuscator;

import org.objectweb.asm.*;
import ru.hyndo.hobfuscator.analyzed.Clazz;
import ru.hyndo.hobfuscator.analyzed.ClazzField;
import ru.hyndo.hobfuscator.analyzed.ClazzMethod;
import ru.hyndo.hobfuscator.analyzed.MethodAccessorsInfo;
import ru.hyndo.hobfuscator.insn.FieldInsn;

import java.util.*;

public class ClazzInitializer implements Opcodes {

    public Clazz loadClass(ClassReader classReader) {
        Map<String, ClazzField> fields = new HashMap<>();
        Set<ClazzMethod> methods = new HashSet<>();
        String clazzName[] = new String[]{""};
        ClassVisitor cv = new ClassVisitor(ASM5) {
            @Override
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                clazzName[0] = name;
            }

            @Override
            public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
                fields.put(name, new ClazzField(name, access));
                return super.visitField(access, name, desc, signature, value);
            }

            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                ClazzMethod clazzMethod = new ClazzMethod(name, access, Type.getMethodType(desc));
                methods.add(clazzMethod);
                MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
                return new GetterSetterMethodAnalyzer(api, mv, clazzMethod);
            }
        };
        classReader.accept(cv, 0);
        return new Clazz(clazzName[0], methods, fields, () -> classReader);
    }

    private static class GetterSetterMethodAnalyzer extends MethodVisitor {

        private final ClazzMethod clazzMethod;
        private boolean notAccessor;
        private MethodAccessorsInfo accessorsInfo;
        private Label currentLabel;
        private List<Map.Entry<Integer, Integer>> varInsns = new LinkedList<>();
        private List<FieldInsn> fieldInsns = new LinkedList<>();
        private boolean dirty;
        private int maxStack;
        private int maxLocals;

        public GetterSetterMethodAnalyzer(int api, MethodVisitor mv, ClazzMethod clazzMethod) {
            super(api, mv);
            this.clazzMethod = clazzMethod;
        }

        @Override
        public void visitLabel(Label label) {
            this.currentLabel = label;
            super.visitLabel(label);
        }

        @Override
        public void visitFieldInsn(int opcode, String owner, String name, String desc) {
            this.fieldInsns.add(new FieldInsn(opcode, owner, name, desc));
            super.visitFieldInsn(opcode, owner, name, desc);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
            if (!name.equalsIgnoreCase("<init>")) {
                dirty = true;
            }
            super.visitMethodInsn(opcode, owner, name, desc, itf);
        }

        @Override
        public void visitVarInsn(int opcode, int var) {
            super.visitVarInsn(opcode, var);
            varInsns.add(new AbstractMap.SimpleImmutableEntry<>(opcode, var));
        }

        @Override
        public void visitMaxs(int maxStack, int maxLocals) {
            this.maxStack = maxStack;
            this.maxLocals = maxLocals;
            if (maxLocals == 1 && maxStack == 1) return;
            if (maxLocals == 2 && maxStack == 2) return;
            dirty = true;
        }

        @Override
        public void visitEnd() {
            if (dirty) {
                super.visitEnd();
                return;
            }
            int argsLength = clazzMethod.getDesc().getArgumentTypes().length;
            if (argsLength == 1 || argsLength == 0) {
                MethodAccessorsInfo.MethodType methodType = null;
                if (varInsns.size() == 2 || varInsns.size() == 1) {
                    int count = 0;
                    for (Map.Entry<Integer, Integer> varInsn : varInsns) {
                        int opcode = varInsn.getKey();
                        int var = varInsn.getValue();
                        if (count++ != var) {
                            super.visitEnd();
                            return;
                        }
                    }
                    if (fieldInsns.size() == 1) {
                        FieldInsn fieldInsn = fieldInsns.iterator().next();
                        if (fieldInsn.getOpcode() == PUTFIELD) {
                            methodType = MethodAccessorsInfo.MethodType.SETTER;
                        } else if (fieldInsn.getOpcode() == GETFIELD) {
                            methodType = MethodAccessorsInfo.MethodType.GETTER;
                        }
                    }
                    if (methodType != null) {
                        System.out.println("!!!" + clazzMethod.getInternalName() + " found accessor");
                        clazzMethod.setMethodAccessorsInfo(new MethodAccessorsInfo(methodType, fieldInsns.iterator().next()));
                        super.visitEnd();
                        return;
                    }
                }
            }
            super.visitEnd();

        }
    }


}
