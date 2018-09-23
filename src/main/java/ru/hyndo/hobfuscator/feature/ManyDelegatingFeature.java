package ru.hyndo.hobfuscator.feature;

import org.objectweb.asm.ClassWriter;
import ru.hyndo.hobfuscator.analyzed.ClazzPool;

import java.util.HashSet;
import java.util.Set;

public class ManyDelegatingFeature implements Feature {

    private Set<Feature> featureSet = new HashSet<>();

    @Override
    public String getName() {
        return "delegating-feature";
    }

    @Override
    public void mapPool(ClazzPool pool) {
        featureSet.forEach(feature -> feature.mapPool(pool));
    }

    public Set<Feature> getFeatureSet() {
        return featureSet;
    }

    public void addFeature(Feature feature) {
        featureSet.add(feature);
    }

    @Override
    public ClassWriter editClassWriter(ClassWriter cw) {
        for (Feature feature : featureSet) {
            cw = feature.editClassWriter(cw);
        }
        return cw;
    }
}
