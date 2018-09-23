package ru.hyndo.hobfuscator.analyzed;

import ru.hyndo.hobfuscator.insn.FieldInsn;

public class MethodAccessorsInfo {

    private MethodType methodType;
    private FieldInsn fieldInsn;

    public MethodAccessorsInfo(MethodType methodType, FieldInsn fieldInsn) {
        this.methodType = methodType;
        this.fieldInsn = fieldInsn;
    }

    public MethodType getMethodType() {
        return methodType;
    }

    public FieldInsn getFieldInsn() {
        return fieldInsn;
    }

    public enum MethodType {
        GETTER, SETTER
    }

}
