package ru.hyndo.hobfuscator.analyzed;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class ClazzPool {

    private Map<String, Clazz> classes;

    public ClazzPool(Map<String, Clazz> classes) {
        this.classes = classes;
        classes.values().forEach(clazz -> clazz.setClazzPool(this));
    }

    public Clazz getClazz(String name) {
        Clazz clazz = classes.get(name);
        if(clazz == null) {
//            System.out.println("Trying to get clazz " + name);
            return classes.values().stream().filter(filtering -> filtering.getRemappedName().equals(name)).findAny().orElse(null);
        }
        return clazz;
    }

    public Collection<Clazz> getClasses() {
        return classes.values();
    }
}
