package ru.hyndo.hobfuscator.analyzed;


import org.objectweb.asm.Type;

public class ClazzMethod implements NamedType {

    private final String internalName;
    private int access;
    private final Type desc;
    private String remappedName;
    private MethodAccessorsInfo methodAccessorsInfo;

    public ClazzMethod(String internalName, int access, Type desc) {
        this.internalName = internalName;
        this.remappedName = internalName;
        this.access = access;
        this.desc = desc;
    }

    public MethodAccessorsInfo getMethodAccessorsInfo() {
        return methodAccessorsInfo;
    }

    public void setMethodAccessorsInfo(MethodAccessorsInfo methodAccessorsInfo) {
        this.methodAccessorsInfo = methodAccessorsInfo;
    }

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    public Type getDesc() {
        return desc;
    }

    public String getRemappedName() {
        return remappedName;
    }

    public void setRemappedName(String remappedName) {
        this.remappedName = remappedName;
    }

    public Clazz.MethodIdentifier getIdentifier() {
        return new Clazz.MethodIdentifier(internalName, desc);
    }

    @Override
    public String getInternalName() {
        return internalName;
    }

    @Override
    public String toString() {
        return "ClazzMethod{" +
                "internalName='" + internalName + '\'' +
                '}';
    }
}

