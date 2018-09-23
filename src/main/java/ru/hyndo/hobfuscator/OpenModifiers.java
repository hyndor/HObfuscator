package ru.hyndo.hobfuscator;

import java.lang.reflect.Modifier;

public class OpenModifiers extends Modifier {

    public static final int BRIDGE = 0x00000040;
    public static final int VARARGS = 0x00000080;
    public static final int SYNTHETIC = 0x00001000;
    public static final int ANNOTATION = 0x00002000;
    public static final int ENUM = 0x00004000;
    public static final int MANDATED = 0x00008000;

}
