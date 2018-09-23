package ru.hyndo.hobfuscator.feature;

public abstract class AbstractFeature implements Feature {

    private String name;

    public AbstractFeature(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
