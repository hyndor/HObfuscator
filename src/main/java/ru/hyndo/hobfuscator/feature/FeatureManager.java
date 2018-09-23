package ru.hyndo.hobfuscator.feature;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FeatureManager {

    private final Map<String, Feature> featureMap = new HashMap<>();

    public FeatureManager() {

    }

    public void registerFeature(Feature feature) {
        featureMap.put(feature.getName(), feature);
    }

    public Collection<Feature> getFeatures() {
        return featureMap.values();
    }

    public Feature getFeature(String name) {
        return featureMap.get(name);
    }

}
