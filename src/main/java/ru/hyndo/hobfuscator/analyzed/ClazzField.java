package ru.hyndo.hobfuscator.analyzed;

import java.util.Objects;
import java.util.Set;

public class ClazzField implements NamedType {

    private int modifiers;
    private final String internalName;
    private String remappedName;

    public ClazzField(String internalName, int modifiers) {
        this.internalName = internalName;
        this.remappedName = internalName;
        this.modifiers = modifiers;
    }

    public int getModifiers() {
        return modifiers;
    }

    public void setModifiers(int modifiers) {
        this.modifiers = modifiers;
    }

    @Override
    public String getInternalName() {
        return internalName;
    }

    public String getRemappedName() {
        return remappedName;
    }

    public void setRemappedName(String remappedName) {
        Objects.requireNonNull(remappedName);
        this.remappedName = remappedName;
    }

    @Override
    public String toString() {
        return "ClazzField{" +
                "internalName='" + internalName + '\'' +
                ", remappedName='" + remappedName + '\'' +
                '}';
    }
}
