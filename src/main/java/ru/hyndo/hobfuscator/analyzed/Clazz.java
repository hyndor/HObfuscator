package ru.hyndo.hobfuscator.analyzed;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import sun.management.MethodInfo;

import java.io.InputStream;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Clazz implements NamedType {

    private ClazzPool clazzPool;
    private String internalName;
    private String remappedName;
    private Map<MethodIdentifier, ClazzMethod> methods;
    private Map<String, ClazzField> fields;
    private Supplier<ClassReader> inputStreamSupplier;

    public Clazz(String internalName, Set<ClazzMethod> methods, Map<String, ClazzField> fields, Supplier<ClassReader> inputStreamSupplier) {
        this.internalName = internalName;
        this.methods = methods.stream().collect(Collectors.toMap(ClazzMethod::getIdentifier, clazzMethod -> clazzMethod));
        this.fields = fields;
        this.inputStreamSupplier = inputStreamSupplier;
        this.remappedName = internalName;
    }

    public String getRemappedName() {
        return remappedName;
    }

    public void setRemappedName(String remappedName) {
        this.remappedName = remappedName;
    }

    public void setClazzPool(ClazzPool clazzPool) {
        this.clazzPool = clazzPool;
    }

    public ClazzPool getClazzPool() {
        return clazzPool;
    }

    public Collection<ClazzField> getFields() {
        return fields.values();
    }

    public ClazzField getField(String name) {
        return fields.get(name);
    }

    public Supplier<ClassReader> inputStreamSupplier() {
        return inputStreamSupplier;
    }

    @Override
    public String getInternalName() {
        return internalName;
    }

    public Collection<ClazzMethod> getMethods() {
        return methods.values();
    }

    public ClazzMethod getMethod(MethodIdentifier identifier) {
        return methods.get(identifier);
    }

    public static class MethodIdentifier {
        private String name;
        private Type desc;

        public MethodIdentifier(String name, Type desc) {
            this.name = name;
            this.desc = desc;
        }

        public String getName() {
            return name;
        }

        public Type getDesc() {
            return desc;
        }

        @Override
        public String toString() {
            return "MethodIdentifier{" +
                    "name='" + name + '\'' +
                    ", desc=" + desc +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MethodIdentifier that = (MethodIdentifier) o;
            return Objects.equals(getName(), that.getName()) &&
                    Objects.equals(getDesc(), that.getDesc());
        }

        @Override
        public int hashCode() {

            return Objects.hash(getName(), getDesc());
        }
    }

}
