package ru.hyndo.hobfuscator.rename;

import ru.hyndo.hobfuscator.analyzed.NamedType;

public interface RenamingStrategy {

    String getName(NamedType type);

}
