package ru.hyndo.hobfuscator.analyzed;

public class FieldReference {

    private Clazz clazz;
    private ClazzMethod method;

    public FieldReference(Clazz clazz, ClazzMethod method) {
        this.clazz = clazz;
        this.method = method;
    }

    public Clazz getClazz() {
        return clazz;
    }

    public ClazzMethod getMethod() {
        return method;
    }
}
