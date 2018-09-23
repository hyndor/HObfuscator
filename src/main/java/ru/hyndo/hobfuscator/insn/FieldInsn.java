package ru.hyndo.hobfuscator.insn;

public class FieldInsn {
    private int opcode;
    private String owner;
    private String name;
    private String desc;

    public FieldInsn(int opcode, String owner, String name, String desc) {
        this.opcode = opcode;
        this.owner = owner;
        this.name = name;
        this.desc = desc;
    }

    public int getOpcode() {
        return opcode;
    }

    public String getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}